package top.toptimus.task;

import lombok.Getter;
import top.toptimus.common.enums.taskEnum.TaskTypeEnum;
import top.toptimus.common.enums.taskEnum.status.TaskStatusEnum;

import java.util.UUID;

/**
 * 任务dto，针对单据任务、bpmn任务、会签任务等
 *
 * @author gaoyu
 * @since 2019-03-06
 */
@Getter
public class TaskDto {
    private String billMetaId;
    private String billTokenId;
    private String taskId;  //  任务id
    private String taskName;
    private int parentIndex;
    private int selfIndex;
    private TaskTypeEnum taskTypeEnum;  //  任务类型枚举
    private TaskStatusEnum taskStatusEnum;  //  任务状态枚举
    private String metaId;  //关联单据meta
    private String processId;
    private boolean isCurrentTask = false;


    /**
     * 全参构造函数
     *
     * @param taskId         任务id
     * @param taskTypeEnum   任务类型枚举
     * @param taskStatusEnum 任务状态枚举
     */
    public TaskDto(String billMetaId, String billTokenId, String taskId, String taskName, int parentIndex, int selfIndex, TaskTypeEnum taskTypeEnum, TaskStatusEnum taskStatusEnum
            , String metaId, String processId, boolean isCurrentTask) {
        this.billMetaId = billMetaId;
        this.billTokenId = billTokenId;
        this.taskId = taskId;
        this.taskName = taskName;
        this.parentIndex = parentIndex;
        this.selfIndex = selfIndex;
        this.taskTypeEnum = taskTypeEnum;
        this.taskStatusEnum = taskStatusEnum;
        this.metaId = metaId;
        this.processId = processId;
        this.isCurrentTask = isCurrentTask;
    }

    /**
     * 构造表头meta、表头token、关联单据meta
     *
     * @param billMetaId  表头Meta
     * @param billTokenId 表头Token
     * @param relMetaId   关联单据meta
     */
    public TaskDto build(String billMetaId, String billTokenId, String relMetaId) {
        this.billMetaId = billMetaId;
        this.billTokenId = billTokenId;
        this.metaId = relMetaId;
        return this;
    }

    /**
     * 构造任务状态
     *
     * @param taskStatusEnum 任务状态
     */
    public TaskDto build(TaskStatusEnum taskStatusEnum) {
        this.taskStatusEnum = taskStatusEnum;
        return this;
    }

    /**
     * 构造任务是否当前节点
     *
     * @param isCurrentTask 是否当前节点
     */
    public TaskDto build(boolean isCurrentTask) {
        this.isCurrentTask = isCurrentTask;
        return this;
    }

    /**
     * 关闭任务节点
     */
    public TaskDto closeTask() {
        if (!taskStatusEnum.isCloseStatus())
            throw new RuntimeException("任务状态错误");
        this.isCurrentTask = false;
        return this;
    }

    /**
     * 关闭任务节点
     */
    public boolean isCloseStatus() {
        return this.getTaskStatusEnum().isCloseStatus();
    }

    /**
     * 无参构造函数
     */
    public TaskDto() {
        this.taskId = UUID.randomUUID().toString();
        this.taskTypeEnum = TaskTypeEnum.BILL;
        this.taskStatusEnum = TaskStatusEnum.STATUS_DRAFT;
    }
}
