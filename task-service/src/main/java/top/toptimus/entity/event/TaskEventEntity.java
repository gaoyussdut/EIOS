package top.toptimus.entity.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.toptimus.common.enums.taskEnum.TaskTypeEnum;
import top.toptimus.common.enums.taskEnum.status.TaskStatusEnum;
import top.toptimus.entity.tokendata.query.TokenDataSqlRetrieveEntity;
import top.toptimus.entity.tokendata.query.TokenMetaQueryFacadeEntity;
import top.toptimus.repository.TaskDefinitionRepository;
import top.toptimus.repository.TaskRepository;
import top.toptimus.task.TaskDefinitionDTO;
import top.toptimus.task.TaskDto;

import java.util.List;

/**
 * task event
 *
 * @author lzs
 * @since 2019-2-19
 */
@Component
public class TaskEventEntity {

    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private TaskDefinitionRepository taskDefinitionRepository;
    @Autowired
    private TokenMetaQueryFacadeEntity tokenMetaQueryFacadeEntity;
    @Autowired
    private TokenDataSqlRetrieveEntity tokenDataSqlRetrieveEntity;

    /**
     * 新增单据时添加单据任务
     *
     * @param billMetaId  表头meta id
     * @param billTokenId 表头token id
     */
    public void createTaskIns(String billMetaId, String billTokenId) {

        List<TaskDto> taskDtoList = taskRepository.findByBillMetaIdAndBillTokenId(billMetaId, billTokenId);
        if (taskDtoList.isEmpty()) {
            List<TaskDefinitionDTO> taskDefinitionDTOList = taskDefinitionRepository.findByBillMetaId(billMetaId);
            if (taskDefinitionDTOList != null && taskDefinitionDTOList.size() > 0) {
                taskDefinitionDTOList.forEach(taskDefinitionDTO -> {
                    if (taskDefinitionDTO.getParentIndex() == 0) {
                        taskDtoList.add(new TaskDto(billMetaId, billTokenId, taskDefinitionDTO.getTaskId(), taskDefinitionDTO.getTaskName(), taskDefinitionDTO.getParentIndex(), taskDefinitionDTO.getSelfIndex(), taskDefinitionDTO.getTaskTypeEnum(), TaskStatusEnum.STATUS_DRAFT, taskDefinitionDTO.getMetaId(), taskDefinitionDTO.getProcessId(), true));
                    } else {
                        taskDtoList.add(new TaskDto(billMetaId, billTokenId, taskDefinitionDTO.getTaskId(), taskDefinitionDTO.getTaskName(), taskDefinitionDTO.getParentIndex(), taskDefinitionDTO.getSelfIndex(), taskDefinitionDTO.getTaskTypeEnum(), TaskStatusEnum.STATUS_DRAFT, taskDefinitionDTO.getMetaId(), taskDefinitionDTO.getProcessId(), false));
                    }
                });
                taskRepository.saveAll(taskDtoList);
            }
        }
    }

    /**
     * 获取当前任务
     *
     * @param billMetaId  表头meta id
     * @param billTokenId 表头token id
     */
    public TaskDto getCurrentTask(String billMetaId, String billTokenId) {
        return taskRepository.findCurrentTaskByBillMetaIdAndBillTokenId(billMetaId, billTokenId);
    }

    /**
     * 改变当前任务状态
     *
     * @param taskDto 任务dto
     */
    public void updateTaskStatus(TaskDto taskDto) {
        // 如果当前任务为结束状态
        if (taskDto.isCloseStatus()) {
            //判断是否为最后节点
            if (taskRepository.isFinalTask(taskDto)) {
                //  关闭当前任务
                taskRepository.updateTask(taskDto.closeTask());
                // TODO 大任务关闭
                // 修改上级备查帐時間
                try {
                    taskRepository.updateMemorandvnDate(
                            tokenDataSqlRetrieveEntity.getTableName(
                                    tokenMetaQueryFacadeEntity
                                            .getMetaRelationByMetaId(taskDto.getBillMetaId())
                                            .getMasterMemorandvnMetaId()
                            )
                            , taskDto.getBillTokenId());
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            } else {
                //  关闭当前任务
                taskRepository.updateTask(taskDto.closeTask());
                //  生成下一个任务
                taskRepository.updateNextTask(
                        taskDto.getBillTokenId()
                        , taskDto.getTaskId()
                );
            }
        } else {
            taskRepository.updateTask(taskDto);
        }
        // 修改备查帐状态
        try {
            taskRepository.updateMemorandvnStatus(
                    tokenDataSqlRetrieveEntity.getTableName(
                            tokenMetaQueryFacadeEntity
                                    .getMetaRelationByMetaId(taskDto.getBillMetaId())
                                    .getMasterMemorandvnMetaId()
                    )
                    , taskDto.getBillTokenId()
                    , taskDto.getTaskStatusEnum());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

//    /**
//     * 批量汇签时改变任务状态
//     *
//     * @param countersignStatusUpdateDTOS 单据token信息
//     */
//    public void updateCountersignStatus(List<CountersignStatusUpdateDTO> countersignStatusUpdateDTOS) {
//        countersignStatusUpdateDTOS.forEach(countersignStatusUpdateDTO -> updateTaskStatus(countersignStatusUpdateDTO.getBillMetaId(), countersignStatusUpdateDTO.getBillTokenId(), TaskStatusEnum.STATUS_SIGN_PASS));
//    }

    /**
     * 获取当前单据全部任务
     *
     * @param billMetaId  表头meta id
     * @param billTokenId 表头token id
     */
    public List<TaskDto> getAllTasks(String billMetaId, String billTokenId) {
        return taskRepository.findByBillMetaIdAndBillTokenId(billMetaId, billTokenId);
    }

    /**
     * 根据任务类型获取所有任务
     *
     * @param billMetaId
     * @param billTokenId
     * @param taskTypeEnum
     */
    public List<TaskDto> getTaskByTaskType(String billMetaId, String billTokenId, TaskTypeEnum taskTypeEnum) {
        return taskRepository.findByBillMetaIdAndBillTokenIdAndTaskType(billMetaId, billTokenId, taskTypeEnum);
    }
}
