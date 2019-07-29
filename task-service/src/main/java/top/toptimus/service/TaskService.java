package top.toptimus.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.toptimus.TaskModel;
import top.toptimus.common.enums.taskEnum.TaskTypeEnum;
import top.toptimus.common.enums.taskEnum.action.TaskActionEnum;
import top.toptimus.common.result.Result;
import top.toptimus.entity.event.TaskEventEntity;
import top.toptimus.exception.TopErrorCode;
import top.toptimus.resultModel.ResultErrorModel;
import top.toptimus.task.TaskDto;

/**
 * Created by lzs
 */
@Service
public class TaskService {

    @Autowired
    private TaskEventEntity taskEventEntity;

    /**
     * 获取表单全部任务
     */
    public Result getAllTasks(String billMetaId, String billTokenId) {
        try {
            return Result.success(
                    taskEventEntity.getAllTasks(billMetaId, billTokenId)
            );
        } catch (Exception e) {
            return new ResultErrorModel(e).getResult();
        }
    }

    /**
     * 获取当前任务
     */
    public Result getCurrentTask(String billMetaId, String billTokenId) {
        try {
            return Result.success(
                    taskEventEntity.getCurrentTask(billMetaId, billTokenId)
            );
        } catch (Exception e) {
            return new ResultErrorModel(e).getResult();
        }
    }

    /**
     * 修改状态
     */
    public Result updateTaskStatus(String billMetaId, String billTokenId, TaskActionEnum taskActionEnum) {
        try {

            TaskDto taskDto = taskEventEntity.getCurrentTask(billMetaId, billTokenId);
            if (taskDto != null) {
                TaskModel taskModel = new TaskModel(taskDto, taskActionEnum);
                taskEventEntity.updateTaskStatus(taskModel.getSourceTaskDto());
                return Result.success();
            } else {
                return new ResultErrorModel(TopErrorCode.INVALID_OBJ).getResult();
            }

        } catch (Exception e) {
            return new ResultErrorModel(e).getResult();
        }
    }

    /**
     * 根据任务类型获取所有任务
     *
     * @param billMetaId   表头meta id
     * @param billTokenId  表头token id
     * @param taskTypeEnum 任务类型枚举
     */
    public Result getTaskByTaskType(String billMetaId, String billTokenId, TaskTypeEnum taskTypeEnum) {
        try {
            return Result.success(
                    taskEventEntity.getTaskByTaskType(billMetaId, billTokenId, taskTypeEnum)
            );
        } catch (Exception e) {
            return new ResultErrorModel(e).getResult();
        }
    }
}
