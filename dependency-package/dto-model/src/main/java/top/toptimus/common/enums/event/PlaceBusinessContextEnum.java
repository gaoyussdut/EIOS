package top.toptimus.common.enums.event;

/**
 * place库所应用上下文枚举,
 * 1、独立的业务单据提交
 * 2、基础数据提交
 * 3、流程启动、流程user task提交、判定、归档
 * 4、wbs分解等项目管理的鬼
 *
 * @author gaoyu
 */
public enum PlaceBusinessContextEnum {
    NONE,//空
    START_EVENT_WITH_MEMORANDUM, // 流程--头节点--引用备查帐
    START_EVENT_WITHOUT_MEMORANDUM,// 流程--头节点--非备查帐
    USERTASK_EVENT_COMMIT,// 流程--用户节点提交
    PLACE_DATA_SAVE,// 非流程--表单提交
    BASIC_DATA_SAVE,// 非流程--基础数据提交
    WBS_SAVE_WITH_MEMORANDUM,//WBS--节点提交--引用备查帐
    WBS_SAVE_WITHOUT_MEMORANDUM, //WBS--节点提交--非备查帐
    ABS_SAVE,//ABS--节点提交
    ACTIVITY_SAVE//ACTIVITY--节点提交
}
