package top.toptimus.common.enums.taskEnum.action;

/**
 * 单据行为枚举
 *
 * @author gaoyu
 * @since 2019-03-06
 */
public enum TaskActionEnum {
    BILL_CLOSE, //  单据关闭
    BILL_CLOSE_REVERSE, //  单据反关闭
    BILL_ABANDON,   //  单据作废
    BILL_ABANDON_REVERSE,   //  单据反作废

    BILL_MODIFY,    //  修改
    BILL_CONVERT,   //  单据转换

    SIGN_PASS,  //  会签成功
    SIGN_FAIL,   //  会签失败
    SIGN_START  // 会签开始
}
