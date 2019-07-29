package top.toptimus.common.enums.process;

/**
 * 表单meta关联类型
 */
public enum MetaRelEnum {
    BILL_HEADER, // 表头部分
    ENTRY, // 分录
    /*
    关联单据
    */
    CERTIFICATE,//  原始凭证关联，报价单，入库单等
    BUSINESSRECORD,//业务记录关联，商机、服务支持，成员客户等
    MEMORANDVN  // 引用单据,关联备查账
}
