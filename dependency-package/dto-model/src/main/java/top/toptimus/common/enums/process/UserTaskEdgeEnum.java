package top.toptimus.common.enums.process;

/**
 * 有向狐类型
 */
public enum UserTaskEdgeEnum {
    STARTEVENT,
    USERTASK,
    ENDEVENT,
    EXCLUSIVEGATEWAY, // 排他网关
    PARALLELGATEWAY,  // 并行网关
    INCLUSIVEGATEWAY  // 包容网关
}
