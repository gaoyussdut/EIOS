package top.toptimus.dynamicSQLModel.queryFacadeModel;

import com.google.common.collect.Lists;
import top.toptimus.dynamicSQLModel.baseModel.ConvertStruct;
import top.toptimus.dynamicSQLModel.baseModel.ForeignAttributeSuite;
import top.toptimus.dynamicSQLModel.baseModel.ForeignTable;
import top.toptimus.dynamicSQLModel.baseModel.MainTable;
import top.toptimus.meta.TokenMetaInfoDTO;

import java.util.*;

public class CreateViewMainTable extends MainTable {

    /**
     * 生成主表结构
     *
     * @param convertStrucDoubletMapp // K:tableName V:中间结构
     * @param mainTableName           // 主表表名
     */
    public CreateViewMainTable(Map<String, Map<String, ConvertStruct>> convertStrucDoubletMapp, String mainTableName) {
        for (String fKey : convertStrucDoubletMapp.keySet()) {
            Map<String, ConvertStruct> convertStructMap = convertStrucDoubletMapp.get(fKey);
            for (String tableName : convertStructMap.keySet()) {
                if (tableName.equals(mainTableName)) {
                    // 主表
                    this.tableName = tableName;
                    Set<String> attributes = new HashSet<>();
                    attributes.addAll(this.attributes);
                    attributes.addAll(convertStructMap.get(tableName).getAttributes());
                    this.attributes = Lists.newArrayList(attributes);
                } else {
                    this.foreignTableMap.put(fKey, new HashMap<String, ForeignTable>() {
                                private static final long serialVersionUID = -6046812488443835844L;

                                {
                                    put(convertStructMap.get(tableName).getSelectFkey(),
                                            new ForeignTable(tableName, convertStructMap.get(tableName).getAttributes()));
                                }
                            }

                    );
                }
            }
        }
    }

    /**
     * select 主表.id,主表.属性 ,表1.id , 表1.属性 ,表2.id，表2.属性 from ( select id,属性
     * ，businessId_colunm1，businessId_colunm2 from 主表 where id = token_id_value ) as
     * 主表 left join ( select id,属性 from 表1 where id = businessId1_value ) on
     * 主表.businessId_colunm1 = 表1.id left join ( select id,属性 from 表2 where id =
     * businessId2_ value ) on 主表.businessId_colunm2 = 表2.id
     * <p>
     *
     * @return 完整的SQL
     */
    public MainTable buildQuery(String tableName, List<ForeignAttributeSuite> foreignAttributeSuites,
                                TokenMetaInfoDTO tokenMetaInfoDTO) {
        this.strSql = this.buildSelect(tableName, foreignAttributeSuites, tokenMetaInfoDTO) + this.buildSelfQuery(tableName)
                + this.buildJoinQuery(tableName);
        return this;
    }

    /**
     * left join ( select id,属性 from 表1 where id = businessId1_value ) as 表1 on
     * 主表.businessId_colunm1 = 表1.id
     *
     * @return 子表left join
     */
    private String buildJoinQuery(String tableName) {
        StringBuilder rtn = new StringBuilder();
        for (String fKey : foreignTableMap.keySet()) {
            Map<String, ForeignTable> innerForeignTableMap = foreignTableMap.get(fKey);
            for (String selectkey : innerForeignTableMap.keySet()) {
                rtn.append("left join (\n").append(innerForeignTableMap.get(selectkey).buildSelfQuery()).append(") as ")
                        .append(fKey).append(" \n").append("on ").append(tableName).append(".").append(selectkey)
                        .append(" = ").append(fKey).append(".id\n");
            }
        }

        return rtn.toString();
    }
}
