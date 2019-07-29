package top.toptimus.common.enums.taskEnum.status;

/**
 * 任务状态枚举
 *
 * @author gaoyu
 * @since 2019-03-06
 */
public enum TaskStatusEnum {
    STATUS_DRAFT,  //  草稿

    /*
        会签状态枚举
     */
    STATUS_SIGN_START,  //  汇签启动
    STATUS_SIGN_PASS,   //  会签通过
    STATUS_SIGN_FAIL,   //  会签未通过
    /*
        单据任务状态枚举
     */
    STATUS_BILL_DONE,   //  单据完成
    STATUS_BILL_ABANDON, //  单据作废
    /*
        审批状态枚举
     */
    STATUS_APPROVE_DONE, //审批通过
    STATUS_APPROVING,    //审批中
    STATUS_APPROVE_FAIL,  //审批未通过
    /*
        单据转换状态枚举
     */
    STATUS_TRANSFORMATION_DONE, //转换完成
    STATUS_TRANSFORMATION_FAIL;  //转换失败

    /**
     * 判断任务状态是不是关闭状态
     *
     * @return 是否关闭
     */
    public boolean isCloseStatus() {
        return this.equals(TaskStatusEnum.STATUS_BILL_DONE)
                || this.equals(TaskStatusEnum.STATUS_APPROVE_DONE)
                || this.equals(TaskStatusEnum.STATUS_SIGN_PASS)
                || this.equals(TaskStatusEnum.STATUS_TRANSFORMATION_DONE);
    }

    /**
     * 判断任务状态是不是作废状态
     *
     * @return 是否关闭
     */
    public boolean isFailStatus() {
        return this.equals(TaskStatusEnum.STATUS_SIGN_FAIL)
                || this.equals(TaskStatusEnum.STATUS_BILL_ABANDON)
                || this.equals(TaskStatusEnum.STATUS_APPROVE_FAIL)
                || this.equals(TaskStatusEnum.STATUS_TRANSFORMATION_FAIL);
    }
}
