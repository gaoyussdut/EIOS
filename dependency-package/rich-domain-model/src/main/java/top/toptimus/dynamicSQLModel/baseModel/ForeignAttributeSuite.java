package top.toptimus.dynamicSQLModel.baseModel;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 取关联表字段别名结构
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ForeignAttributeSuite {
    private String selectId;    //  关联表的selectID，也就是关联表as的名
    private String attribute;   //  关联表字段
    private String fKey;    //  关联表对应的Fkey
    private String foreignTable;
}
