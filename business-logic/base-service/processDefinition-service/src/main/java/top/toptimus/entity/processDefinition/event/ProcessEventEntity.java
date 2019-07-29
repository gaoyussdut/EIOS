package top.toptimus.entity.processDefinition.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.toptimus.processModel.BPMNModel;
import top.toptimus.repository.processDefinition.ProcessFileRepository;
import top.toptimus.repository.processDefinition.UserTaskEdgeRepository;
import top.toptimus.repository.processDefinition.UserTaskRepository;

import java.util.Map;

/**
 * 流程实体
 */
@Component
public class ProcessEventEntity {
    /**
     * repository
     */
    @Autowired
    private UserTaskRepository userTaskRepository;
    @Autowired
    private UserTaskEdgeRepository userTaskEdgeRepository;
    @Autowired
    private ProcessFileRepository processFileRepository;


    /**
     * bpmnXml存储以及关系建立  TODO 事务控制 AOP
     *
     * @param bpmnContent       xml内容
     * @param userTaskIdAndMetaId K：user task id，V: metaId
     */
    public void bpmnXmlUpload(String bpmnContent, Map<String, String> userTaskIdAndMetaId) {
        // 构建数据
        BPMNModel bpmnModel = new BPMNModel(bpmnContent, userTaskIdAndMetaId);

        // 删除原有的process相关信息
        userTaskRepository.deleteByProcessId(bpmnModel.getProcessId());
        userTaskEdgeRepository.deleteByProcessId(bpmnModel.getProcessId());
        processFileRepository.deleteByProcessId(bpmnModel.getProcessId());
        // 持久化
        userTaskRepository.saveAll(bpmnModel.getUserTaskDtos());
        userTaskEdgeRepository.saveAll(bpmnModel.getUserTaskEdgeDtos());
        processFileRepository.saveAll(bpmnModel.getProcessFileDaos());
    }
}
