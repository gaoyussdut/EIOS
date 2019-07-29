package top.toptimus.dynamicSQLModel.baseModel;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import top.toptimus.constantConfig.Constants;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * 关联表
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ForeignTable {
    private String foreignTableName;    //  连接表名
    private List<String> attributes = new ArrayList<>();    //  主表属性

    /**
     * select
     * id,属性
     * from 表1
     * where id = businessId1_value
     *
     * @return 子表查询sql
     */
    public String buildSelfQuery() {
        HashSet<String> attributesSet = new HashSet<>(attributes);
        return "select \n" +
                "id," + String.join(",", attributesSet) + " \n" +
                "from " + foreignTableName + " \n";
    }

    /**
     * sbss_shall_type_businessId as id,FName
     * from v_sbss_shall_type
     *
     * @return 子表查询sql
     */
    public String buildSelfViewQuery() {
        HashSet<String> attributesSet = new HashSet<>(attributes);
        //  TODO _businessId和id冲突问题解决
        if (attributesSet.contains(Constants.id)) {
            attributesSet.remove(Constants.id);
            attributesSet.add("'" + Constants.jsonData_1 + "'");
        }
        return "select \n" +
//                foreignTableName + "_businessId as id," + String.join(",", attributesSet) + " \n" +
                " id," + String.join(",", attributesSet) + " \n" +
                //"from v_" + foreignTableName + " \n";
                "from " + foreignTableName + " \n";// 原从视图改为从表中取
    }
}