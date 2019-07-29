package top.toptimus.service.domainService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.toptimus.common.enums.process.VertexTypeEnum;
import top.toptimus.common.result.Result;
import top.toptimus.entity.processDefinition.event.ProcessEventEntity;
import top.toptimus.entity.processDefinition.query.ProcessQueryFacadeEntity;
import top.toptimus.exception.TopException;
import top.toptimus.processDefinition.UserTaskDto;
import top.toptimus.resultModel.ResultErrorModel;

import java.util.Map;

@Service
public class ProcessDefinitionService {

    @Autowired
    private ProcessQueryFacadeEntity processQueryFacadeEntity;
    @Autowired
    private ProcessEventEntity processEventEntity;

    /**
     * 待办详细:表头业务信息 TaskDTO
     *
     * @param tokenId    token id
     * @param userTaskId 流程节点ID
     * @return Result(PlaceDTO)库所
     */
    public Result getProcessUserTaskDTO(String tokenId, String userTaskId) {
        try {
            return Result.success(processQueryFacadeEntity.getProcessUserTaskDTO(tokenId, userTaskId));
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultErrorModel(e).getResult();
        }
    }

    /**
     * bpmnXml存储以及关系建立
     *
     * @param bpmnContent       bpmn文件
     * @param userTaskIdAndMetaId usertask和metaId
     * @return Result
     */
    public Result bpmnXmlUpload(
            String bpmnContent
            , Map<String, String> userTaskIdAndMetaId
    ) {
        try {
            processEventEntity.bpmnXmlUpload(bpmnContent, userTaskIdAndMetaId);
            return Result.success();
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultErrorModel(e).getResult();
        }
    }

    public String getBPMNXmlString(String processId) {
        return processQueryFacadeEntity.getBPMNXmlString(processId);
    }

    /**
     * 取得所有流程Id对应的Name
     *
     * @return Result(k : process id, V : process name)
     */
    public Result findAllProcess() {
        try {
            return Result.success(processQueryFacadeEntity.findAllProcess());
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultErrorModel(e).getResult();
        }
    }

    /**
     * 根据流程ID取得指定节点信息
     *
     * @param processId      流程Id
     * @param vertexTypeEnum STARTEVENT, USERTASK, ENDEVENT
     * @return user task 一览
     */
    public Result findByProcessIdAndType(String processId, VertexTypeEnum vertexTypeEnum) {
        try {
            return Result.success(
                    processQueryFacadeEntity.findByProcessIdAndType(processId, vertexTypeEnum)
            );
        } catch (TopException e) {
            return new ResultErrorModel(e).getResult();
        }
    }

}
