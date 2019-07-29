package top.toptimus.processModel;

import lombok.Getter;
import org.camunda.bpm.model.bpmn.Bpmn;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.camunda.bpm.model.bpmn.instance.*;
import org.camunda.bpm.model.bpmn.instance.Process;
import top.toptimus.common.enums.process.VertexTypeEnum;
import top.toptimus.formula.UserTaskFormulaDTO;
import top.toptimus.processDefinition.ProcessFileDto;
import top.toptimus.processDefinition.UserTaskDto;
import top.toptimus.processDefinition.UserTaskEdgeDto;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * BPMN文件model
 *
 * @author gaoyu
 */
@Getter
public class BPMNModel {
    private List<UserTaskDto> userTaskDtos = new ArrayList<>();
    private List<UserTaskEdgeDto> userTaskEdgeDtos = new ArrayList<>();
    private List<ProcessFileDto> processFileDaos = new ArrayList<>();
    private List<UserTaskFormulaDTO> userTaskFormulaDaos = new ArrayList<>();
    private String processId;

    /**
     * BPMN构建数据
     *
     * @param bpmnContent       xml内容
     * @param userTaskIdAndMetaId K：user task id，V: metaId
     */
    public BPMNModel(String bpmnContent, Map<String, String> userTaskIdAndMetaId) {
        // 获取信息
        ByteArrayInputStream stream = new ByteArrayInputStream(bpmnContent.getBytes());
        BpmnModelInstance modelInstance = Bpmn.readModelFromStream(stream);

        // 流程取得
        Process process = modelInstance.getModelElementsByType(Process.class).iterator().next();
        this.processId = process.getAttributeValue("id"); // 流程Id
        String processName = process.getAttributeValue("name"); // 流程Name
        // 获取信息
        Collection<UserTask> userTasks = modelInstance.getModelElementsByType(UserTask.class);
        Collection<StartEvent> startEvents = modelInstance.getModelElementsByType(StartEvent.class);
        Collection<EndEvent> endEvents = modelInstance.getModelElementsByType(EndEvent.class);
        Collection<ExclusiveGateway> exclusiveGateways = modelInstance.getModelElementsByType(ExclusiveGateway.class);
        Collection<ParallelGateway> parallelGateways = modelInstance.getModelElementsByType(ParallelGateway.class);
        Collection<InclusiveGateway> inclusiveGateways = modelInstance.getModelElementsByType(InclusiveGateway.class);
        // start节点
        for (StartEvent startEvent : startEvents) {
            if (userTaskIdAndMetaId.containsKey(startEvent.getId())) {
                this.userTaskDtos.add(new UserTaskDto().build(processId, startEvent.getId(), startEvent.getName(),
                        userTaskIdAndMetaId.get(startEvent.getId()), "OPEN"));
            }
            this.userTaskEdgeDtos.addAll(BPMNModel.createUserTaskEdge(processId, startEvent.getId(),
                    startEvent.getIncoming(), startEvent.getOutgoing(), VertexTypeEnum.STARTEVENT));
        }

        // 遍历userTask
        for (UserTask userTask : userTasks) {
            if (userTaskIdAndMetaId.containsKey(userTask.getId())) {
                this.userTaskDtos.add(new UserTaskDto().build(processId, userTask.getId(), userTask.getName(),
                        userTaskIdAndMetaId.get(userTask.getId()), "OPEN"));
            }
            this.userTaskEdgeDtos.addAll(BPMNModel.createUserTaskEdge(processId, userTask.getId(),
                    userTask.getIncoming(), userTask.getOutgoing(), VertexTypeEnum.USERTASK));
        }

        // end节点
        for (EndEvent endEvent : endEvents) {
            if (userTaskIdAndMetaId.containsKey(endEvent.getId())) {
                this.userTaskDtos.add(new UserTaskDto().build(processId, endEvent.getId(), endEvent.getName(),
                        userTaskIdAndMetaId.get(endEvent.getId()), "OPEN"));
            }
            this.userTaskEdgeDtos.addAll(BPMNModel.createUserTaskEdge(processId, endEvent.getId(),
                    endEvent.getIncoming(), endEvent.getOutgoing(), VertexTypeEnum.ENDEVENT));
        }

        // 遍历exclusiveGateways
        for (ExclusiveGateway exclusiveGateway : exclusiveGateways) {
            this.userTaskEdgeDtos.addAll(BPMNModel.createUserTaskEdge(processId, exclusiveGateway.getId(),
                    exclusiveGateway.getIncoming(), exclusiveGateway.getOutgoing(), VertexTypeEnum.EXCLUSIVEGATEWAY));
            // 设置公式
            if (exclusiveGateway.getOutgoing().size() > 1) {
                this.userTaskFormulaDaos.addAll(BPMNModel.createUserTaskFormula(processId,
                        exclusiveGateway.getIncoming(), exclusiveGateway.getOutgoing(), exclusiveGateway.getDefault()));
            }
        }

        // 遍历parallelGateways
        for (ParallelGateway parallelGateway : parallelGateways) {
            this.userTaskEdgeDtos.addAll(BPMNModel.createUserTaskEdge(processId, parallelGateway.getId(),
                    parallelGateway.getIncoming(), parallelGateway.getOutgoing(), VertexTypeEnum.PARALLELGATEWAY));
        }

        // 遍历inclusiveGateways
        for (InclusiveGateway inclusiveGateway : inclusiveGateways) {
            this.userTaskEdgeDtos.addAll(BPMNModel.createUserTaskEdge(processId, inclusiveGateway.getId(),
                    inclusiveGateway.getIncoming(), inclusiveGateway.getOutgoing(), VertexTypeEnum.INCLUSIVEGATEWAY));

            // 设置公式
            if (inclusiveGateway.getOutgoing().size() > 1) {
                this.userTaskFormulaDaos.addAll(BPMNModel.createUserTaskFormula(processId,
                        inclusiveGateway.getIncoming(), inclusiveGateway.getOutgoing(), inclusiveGateway.getDefault()));
            }
        }

        this.processFileDaos.addAll(new ArrayList<ProcessFileDto>() {
            private static final long serialVersionUID = 8395846872548689972L;

            {
                add(new ProcessFileDto().build(processId, processName, bpmnContent, "OPEN"));
            }
        });
    }

    /**
     * 生成userTask弧度
     *
     * @param processId 流程id
     * @param id        user task id
     * @param inComings bpmn入度
     * @param outgoings bpmn出度
     * @param type      定点类型
     * @return 节点弧定义
     */
    private static List<UserTaskEdgeDto> createUserTaskEdge(
            String processId
            , String id
            , Collection<SequenceFlow> inComings
            , Collection<SequenceFlow> outgoings
            , VertexTypeEnum type
    ) {
        return new ArrayList<UserTaskEdgeDto>() {{
            if (type.equals(VertexTypeEnum.STARTEVENT)) {
                // 如果是头节点 没有inComing
                for (SequenceFlow outgoing : outgoings) {
                    add(new UserTaskEdgeDto().build(processId, id, null, outgoing.getId(), type));
                }
            } else {
                // 非头节点
                for (SequenceFlow inComing : inComings) {
                    if (outgoings != null && outgoings.size() != 0) {
                        // userTask
                        for (SequenceFlow outgoing : outgoings) {
                            add(new UserTaskEdgeDto().build(processId, id, inComing.getId(),
                                    outgoing.getId(), type));
                        }
                    } else {
                        // 如果不存在outgoing 尾节点
                        add(new UserTaskEdgeDto().build(processId, id, inComing.getId(), null, type));
                    }
                }
            }
        }};
    }

    /**
     * 生成UserTaskFormula公式
     *
     * @param inComings          bpmn入度
     * @param defaltSequenceFlow bpmn sequence flow
     * @param outGoings          bpmn出度
     * @return 公式定义
     */
    private static List<UserTaskFormulaDTO> createUserTaskFormula(
            String processId
            , Collection<SequenceFlow> inComings
            , Collection<SequenceFlow> outGoings
            , SequenceFlow defaltSequenceFlow
    ) {
        return new ArrayList<UserTaskFormulaDTO>() {{
            for (SequenceFlow incoming : inComings) {
                for (SequenceFlow outgoing : outGoings) {
                    // 为默认路径
                    if (outgoing.equals(defaltSequenceFlow)) {
                        add(new UserTaskFormulaDTO().build(processId, incoming.getSource().getId(),
                                outgoing.getTarget().getId(), outgoing.getConditionExpression().getTextContent(), true));
                    } else {
                        add(new UserTaskFormulaDTO().build(processId, incoming.getSource().getId(),
                                outgoing.getTarget().getId(), outgoing.getConditionExpression().getTextContent(), false));
                    }
                }
            }
        }};
    }
}
