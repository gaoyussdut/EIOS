package top.toptimus.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public enum TopErrorCode {
    /* *********************  0.成功  ************************/
    SUCCESS(0, "成功"),


    /* *********************  1.业务异常  ************************/
    /**
     * meta异常
     */
    META_GET_ERR(1001, "获取meta信息失败"),
    META_NOT_EXIST(1002, "meta信息不存在"),
    META_FKEY_NOT_EXIST(1003, "meta下的Fkey不存在"),
    META_FKEY_PROPERTY_NOT_EXIST(1004, "meta下读写属性不存在"),
    META_TYPE_ERR(1005, "meta类型有问题"),
    META_MODEL_ERR(1006, "没有配置模板的URL"),
    META_GET_FOREIGNKEY_KEY(1007, "获取Meta外键关系失败"),
    META_KEY_TYPE_EMPTY(1008, "meta配置异常 key的类型不能为空"),

    /**
     * token数据异常
     */
    TOKEN_NOT_EXIST(2001, "token信息不存在"),
    TOKEN_EXIST_LOCK(2002, "tokenId存在锁"),
    TOKEN_ENTRY_UPDATE_ERR(2003, "分录数据更新插入失败"),
    TOKEN_ID_NOT_EXIST(2004, "tokenId为空"),
    TOKEN_ID_ABNORRMAL(2005, "根据tokenId清洗数据异常,不正常的tokenId"),
    TOKEN_DATA_IS_EMPTY(2006, "tokenData数据为空"),
    TOKEN_BILL_DATA_IS_EMPTY(2007, "表头的token数据为空"),
    TOKEN_DELETE_META_INCONSEQUE(2008, "meta和tokenId不一致"),

    /**
     * 流程相关异常
     */
    PROC_EXEC_ERR(3001, "启动流程失败"),
    PROC_START_NODE_ERR(3002, "首节点未找到"),
    PROC_NODE_STATUS_ERR(3003, "未找到节点状态"),
    PROC_REPECT_INSTANCE_PROCESS(3004, "此流程已经实例,重复实例流程异常"),
    PROC_NODE_NOT_FOUND(3005, "节点的信息未找到"),
    PROC_SAVE_NODE_INFO_ERR(3006, "保存节点信息失败"),
    PROC_DELETE_NODE_INFO_ERR(3007, "删除节点信息失败"),
    PROC_SAVE_NODE_EDGE_INFO_ERR(3008, "保存节点的边失败"),
    PROC_DELETE_NODE_EDGE_INFO_ERR(3009, "删除节点的边失败"),

    /**
     * place数据异常
     */
    PLACE_BUSINESS_CONTEXT_ERR(4001, "启动流程上下文异常"),
    PLACE_DATA_NOT_EXIST(4002, "未获取到placeDTO"),
    PLACE_DATA_SAVE_ERR(4003, "将PlaceDTO存入Redis异常"),
    PLACE_PRE_BILL_ERR(4004, "单据前置信息异常"),

    /**
     * WBS相关异常
     */
    WBSNODE_RESOURCE_OCCUPATION_ERR(5001, "资源正被修改"),
    WBSNODE_SAVE_ERR(5002, "保存WBSNode失败"),
    WBSNODE_UPDATE_ERR(5003, "更新WBSNode失败"),
    WBSNODE_FIND_ERR(5004, "查找WBSNode异常"),
    WBSNODE_RELATION_ERR(5005, "保存WBS模板和实例关系失败"),

    /**
     * 业务单元相关
     */
    BUSINESSUNIT_FAILED_CREATE(6001, "新增失败"),
    /**
     * 目标管理相关
     */
    TARGET_TYPE_HAS_BEEN_ASSOCIATED(7001, "目标类型已被关联"),


    /* 备查账相关异常 */

    /**********************  2.系统异常  ************************/
    GENERAL_ERR(9000, "通用错误"),
    NULL_OBJ(9001, "对象为空"),
    /**
     * 数据库操作异常
     */
    INVALID_OBJ(9002, "无效的数据"),
    SQL_ERR(9003, "数据库操作失败"),
    ARRAY_INDEX_OUT_BOUNDS(9004, "所查数据下标超过数据条数"),
    INVALID_PAGE_NUMBER(9005, "非法的页码"),

    /**
     * 通用错误
     */
    INVALID_PARAMS(9007, "参数错误"),
    AUTH_ERR(9008, "没有权限"),

    /**
     * 用户相关
     */
    USER_DOES_NOT_EXIST(10001, "用户不存在"),

    /**
     * 字段效验失败
     */
    FIELD_VERIFY_FAIL(11000, "字段效验失败");


    //错误码
    private Integer errcode;
    //错误描述
    private String desc;

    @Override
    public String toString() {
        return "[" + this.errcode + "]" + this.desc;
    }

}
