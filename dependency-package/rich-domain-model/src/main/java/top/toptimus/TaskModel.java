package top.toptimus;

import lombok.Getter;
import top.toptimus.common.enums.taskEnum.TaskTypeEnum;
import top.toptimus.common.enums.taskEnum.action.TaskActionEnum;
import top.toptimus.common.enums.taskEnum.status.TaskStatusEnum;
import top.toptimus.task.TaskDto;

import java.util.UUID;

/**
 * 任务处理实体
 *
 * @author gaoyu
 * @since 2019-03-06
 */
public class TaskModel {
    @Getter
    private TaskDto sourceTaskDto;    //  源单任务
    @Getter
    private TaskDto targetTaskDto;   //  目标单任务

    /**
     * 实体入口，单据任务规则定义
     *
     * @param sourceTaskDto  源单，任务dto，针对单据任务、bpmn任务、会签任务等
     * @param taskActionEnum 单据行为枚举
     */
    public TaskModel(TaskDto sourceTaskDto, TaskActionEnum taskActionEnum) {
        switch (taskActionEnum) {
            case BILL_CLOSE:    //  单据关闭
                this.billClose(sourceTaskDto);
                break;
//            case BILL_CLOSE_REVERSE:    //  单据反关闭
//                this.billCloseReverse(sourceTaskDto);
//            case BILL_ABANDON:  //  单据作废
//                this.billAbandon(sourceTaskDto);
//            case BILL_ABANDON_REVERSE:  //  单据反作废
//                this.billAbandonReverse(sourceTaskDto);
            case BILL_CONVERT:  //  单据转换
                this.billConvert(sourceTaskDto);
                break;
            case SIGN_PASS: //  会签成功
                this.signPass(sourceTaskDto);
                break;
            case SIGN_FAIL: //  会签失败
                this.signFail(sourceTaskDto);
                break;
            case SIGN_START: //  会签开始
                this.signStart(sourceTaskDto);
                break;
            default:
                throw new RuntimeException("接口调用错误");
        }
    }

    /**
     * 单据转换
     * 输入：源单任务状态
     * 处理过程:目标单据任务生成
     *
     * @param sourceTaskDto 源单
     */
    private void billConvert(TaskDto sourceTaskDto) {
        TaskModel.taskException(sourceTaskDto, TaskTypeEnum.TRANSFORMATION, TaskStatusEnum.STATUS_DRAFT);
        this.sourceTaskDto = sourceTaskDto.build(TaskStatusEnum.STATUS_TRANSFORMATION_DONE);
    }

    /**
     * 单据关闭
     * 输入：单据任务状态
     * 处理过程：单据任务状态从草稿变为单据完成
     *
     * @param taskDto 任务dto，针对单据任务、bpmn任务、会签任务等
     */
    private void billClose(TaskDto taskDto) {
        TaskModel.taskException(taskDto, TaskTypeEnum.BILL, TaskStatusEnum.STATUS_DRAFT);
        this.sourceTaskDto = taskDto.build(TaskStatusEnum.STATUS_BILL_DONE);
    }

    /**
     * 单据反关闭
     * 输入：单据任务状态
     * 处理过程：单据任务状态从单据完成变成草稿
     *
     * @param taskDto 任务dto，针对单据任务、bpmn任务、会签任务等
     */
    private void billCloseReverse(TaskDto taskDto) {
        TaskModel.taskException(taskDto, TaskTypeEnum.BILL, TaskStatusEnum.STATUS_BILL_DONE);
//        this.sourceTaskDto = new TaskDto(taskDto.getTaskId(), TaskTypeEnum.BILL, TaskStatusEnum.STATUS_DRAFT);
    }

    /**
     * 单据作废
     * 输入：单据任务状态
     * 处理过程：单据任务状态从草稿变为单据作废
     *
     * @param taskDto 任务dto，针对单据任务、bpmn任务、会签任务等
     */
    private void billAbandon(TaskDto taskDto) {
        TaskModel.taskException(taskDto, TaskTypeEnum.BILL, TaskStatusEnum.STATUS_DRAFT);
//        this.sourceTaskDto = new TaskDto(taskDto.getTaskId(), TaskTypeEnum.BILL, TaskStatusEnum.STATUS_BILL_ABANDON);
    }

    /**
     * 单据反作废
     * 输入：单据任务状态
     * 处理过程：单据任务状态从单据作废变为草稿
     *
     * @param taskDto 任务dto，针对单据任务、bpmn任务、会签任务等
     */
    private void billAbandonReverse(TaskDto taskDto) {
        TaskModel.taskException(taskDto, TaskTypeEnum.BILL, TaskStatusEnum.STATUS_BILL_ABANDON);
//        this.sourceTaskDto = new TaskDto(taskDto.getTaskId(), TaskTypeEnum.BILL, TaskStatusEnum.STATUS_DRAFT);
    }

    /**
     * 会签完成
     * 输入：会签任务状态
     * 处理过程：会签任务状态从草稿变为会签成功
     *
     * @param signTaskDto 会签任务dto，针对单据任务、bpmn任务、会签任务等
     */
    private void signPass(TaskDto signTaskDto) {
        TaskModel.taskException(signTaskDto, TaskTypeEnum.SIGN, TaskStatusEnum.STATUS_SIGN_START);
        this.sourceTaskDto = signTaskDto.build(TaskStatusEnum.STATUS_SIGN_PASS);
    }

    /**
     * 会签失败
     * 输入：会签任务状态
     * 处理过程：会签任务状态从草稿变为会签失败
     *
     * @param signTaskDto 会签任务dto，针对单据任务、bpmn任务、会签任务等
     */
    private void signFail(TaskDto signTaskDto) {
        TaskModel.taskException(signTaskDto, TaskTypeEnum.SIGN, TaskStatusEnum.STATUS_SIGN_START);
        this.sourceTaskDto = signTaskDto.build(TaskStatusEnum.STATUS_SIGN_FAIL);
    }

    /**
     * 会签开始
     * 输入：会签任务状态
     * 处理过程：会签任务状态从草稿变为会签开始
     *
     * @param signTaskDto 会签任务dto，针对单据任务、bpmn任务、会签任务等
     */
    private void signStart(TaskDto signTaskDto) {
        TaskModel.taskException(signTaskDto, TaskTypeEnum.SIGN, TaskStatusEnum.STATUS_DRAFT);
        this.sourceTaskDto = signTaskDto.build(TaskStatusEnum.STATUS_SIGN_START);
    }

    /**
     * 异常处理
     *
     * @param taskDto        任务dto，针对单据任务、bpmn任务、会签任务等
     * @param taskTypeEnum   任务类型枚举
     * @param taskStatusEnum 任务状态枚举
     */
    private static void taskException(TaskDto taskDto, TaskTypeEnum taskTypeEnum, TaskStatusEnum taskStatusEnum) {
        if (!taskDto.getTaskTypeEnum().equals(taskTypeEnum)) {
            throw new RuntimeException("任务类型错误");
        }
        if (!taskDto.getTaskStatusEnum().equals(taskStatusEnum)) {
            throw new RuntimeException("单据状态错误");
        }
    }
}
