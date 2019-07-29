package top.toptimus.common.enums;

/**
 * 业务单元弧枚举
 */
public enum BusinessUnitEdgeTypeEnum {
    NEW,            //不唯一新建
    NEW_ONLY,       //唯一新建
    PUSHDOWN_AUTO,  //自动下推
    PUSHDOWN_SS,    //手动下推SS
    PUSHDOWN_FS,    //手动下推FS
    REVERSE         //反写
}
