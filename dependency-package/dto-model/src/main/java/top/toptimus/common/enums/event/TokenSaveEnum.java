package top.toptimus.common.enums.event;

/**
 * token save的类型
 *
 * @author gaoyu
 */
public enum TokenSaveEnum {
    SAVE_ENTRY,
    SAVE_BILL,
    UPDATE_ENTRY,
    SAVE_AO,
    TASK_SUBMIT,
    UPDATE_AO,
    BPMN_TASK_SUBMIT,   //  任务——待办转为已办
    BPMN_TASK_START,    //  任务——流程启动
    INVENTORY,  //  任务——同步能力库
    SAVE_TASK
}
