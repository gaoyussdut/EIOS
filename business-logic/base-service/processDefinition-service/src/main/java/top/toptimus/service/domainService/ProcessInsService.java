package top.toptimus.service.domainService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.toptimus.businessmodel.process.dto.ProcessDiagramDTO;
import top.toptimus.businessmodel.process.dto.ProcessTaskDTO;
import top.toptimus.common.enums.process.UserTaskStatusEnum;
import top.toptimus.common.result.Result;
import top.toptimus.entity.event.TaskEventEntity;
import top.toptimus.entity.processDefinition.query.ProcessQueryFacadeEntity;
import top.toptimus.entity.processIns.event.ProcessInsEventEntity;
import top.toptimus.entity.processIns.query.ProcessInsQueryFacadeEntity;
import top.toptimus.place.place_deprecated.PlaceDTO;
import top.toptimus.processDefinition.UserTaskInsDTO;
import top.toptimus.resultModel.ResultErrorModel;

import java.util.List;
import java.util.UUID;

/**
 * 流程实例服务
 *
 * @author gaoyu
 */
@Service
public class ProcessInsService {

    @Autowired
    private ProcessInsEventEntity processInsEventEntity;
    @Autowired
    private ProcessInsQueryFacadeEntity processInsQueryFacadeEntity;
    @Autowired
    private ProcessQueryFacadeEntity processQueryFacadeEntity;
    @Autowired
    private TaskEventEntity taskEventEntity;


    /**
     * UserTask 状态变更
     *
     * @param placeDTO 库所
     */
    public void transictionUserTaskInProcess(PlaceDTO placeDTO) {
        processInsEventEntity.transictionUserTaskInProcess(placeDTO);
    }

    /**
     * 实例化流程
     *
     * @param processId 流程id
     * @param tokenId   tokenId
     * @return Result(tokenId)
     */
    public Result insertProcessIns(String processId, String tokenId) {
        try {
            return Result.success(processInsEventEntity.insertProcessIns(processId, tokenId, null).getBillTokenId());
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultErrorModel(e).getResult();
        }
    }

    /**
     * 实例化流程并建立流程和业务的关系
     *
     * @param metaId    单据的metaId
     * @param tokenId   单据的TokenId
     * @param taskId    单据下的大任务ID
     * @param processId 单据启动的流程ID
     * @return Result(tokenId)
     */
    public Result insertProcessIns(String metaId, String tokenId, String taskId, String processId) {
        try {
            String processTokenId = UUID.randomUUID().toString();  // 流程中的TokenId
            // 调用大任务修改状态
//            taskEventEntity.updateTaskStatus(metaId,tokenId, TaskStatusEnum.STATUS_APPROVING);
            // 保存单据和流程之间的关系
            processInsEventEntity.saveProcessTask(new ProcessTaskDTO(metaId, tokenId, taskId, processId, processTokenId));
            return Result.success(processInsEventEntity.insertProcessIns(processId, processTokenId, null).getBillTokenId());
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultErrorModel(e).getResult();
        }
    }

    /**
     * 查询节点对应的一览信息
     *
     * @param userTaskId     节点ID
     * @param userTaskStatus 节点状态
     * @return Result
     */
    public Result findUserTaskList(String userTaskId, String userTaskStatus) {
        try {
            return Result.success(processInsQueryFacadeEntity.findUserTaskList(userTaskId, userTaskStatus));
        } catch (Exception e) {
            return new ResultErrorModel(e).getResult();
        }
    }

    /**
     * 根据单据metaId和tokenId查询流程图及审批状态
     *
     * @param metaId  单据metaId
     * @param tokenId 单据tokenId
     * @return Result 流程图及审批状态
     */
    public Result findProcessByMetaAndTokenId(String metaId, String tokenId) {
        // 1.根据单据的metaId和tokenId取得process
        ProcessTaskDTO processTaskDTO = processInsQueryFacadeEntity.findByTokenIdAndMetaId(metaId, tokenId);
        // 2.取得当前流程跑到的节点
        List<UserTaskInsDTO> userTaskInsDTOS = processInsQueryFacadeEntity.findByBillTokenIdAndStatus(processTaskDTO.getProcessTokenId(), UserTaskStatusEnum.RUNNING);
        // 3.返回BPMN的xml和当前节点
        return Result.success(new ProcessDiagramDTO(userTaskInsDTOS != null && userTaskInsDTOS.size() > 0 ? userTaskInsDTOS.get(0).getUserTaskId() : "", processQueryFacadeEntity.getBPMNXmlString(processTaskDTO.getProcessId())));
    }
}
