package top.toptimus.dynamicSQLModel.baseModel;

import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import top.toptimus.common.enums.SearchCommonEnum;
import top.toptimus.common.enums.FkeyTypeEnum;
import top.toptimus.common.search.SearchItem;
import top.toptimus.meta.TokenMetaInfoDTO;
import top.toptimus.meta.property.MetaFieldDTO;

import java.util.*;

/**
 * 主表
 * <p>
 * (1) 生成主表结构 记录meta信息 记录主表信息 记录关联表字段别名结构
 * <p>
 * (2) 构建查询语句： 2.1 根据视图查询数据条数 2.2 根据视图查询
 * TODO 待优化,SQL缓存，分组，筛选
 *
 * @author gaoyu
 * @since 2018-07-02
 */
@NoArgsConstructor
public abstract class MainTable {
    protected static final Logger logger = LoggerFactory.getLogger(MainTable.class);
    @Getter
    protected String tableName; // 主表
    protected List<String> attributes = new ArrayList<>(); // 主表属性，普通类型字段
    protected Map<String, Map<String, ForeignTable>> foreignTableMap = new HashMap<>(); // K:表中的Fkey K:SELECT类型字段 V:关联表
    protected List<ForeignAttributeSuite> foreignAttributeSuites = new ArrayList<>(); // 取关联表字段别名结构


    @Getter
    protected TokenMetaInfoDTO tokenMetaInfoDTO; // token meta信息
    @Getter
    protected String strSql;   // 最终执行的Sql
    @Getter
    protected String countSql; // 分页查询时总数据条数Sql
    @Getter
    protected String commonSql; // 共通的sql,从FROM 到 WHERE条件适用strsql和countSql
    @Getter
    protected String cacheSql; // 缓存的共同的sql 只需要再拼入limit的信息  例如：select user.name,user.age FROM user WHERE user.id = ?


    /**
     * 生成主表结构 记录meta信息 记录主表信息 记录关联表字段别名结构
     *
     * @param tokenMetaInfoDTO // meta信息
     * @param mainTableName    // 主表表名
     */
    public MainTable(TokenMetaInfoDTO tokenMetaInfoDTO, String mainTableName) {
        // meta信息
        this.tokenMetaInfoDTO = tokenMetaInfoDTO;
        // 主表
        this.tableName = mainTableName;
        // meta数据清洗,K:tableName V:中间结构
        Map<String, Map<String, ConvertStruct>> convertStrucDoubletMapp = this
                .generateConvertStructMap(tokenMetaInfoDTO, tokenMetaInfoDTO.getTokenMetaId());

        for (String fKey : convertStrucDoubletMapp.keySet()) {
            Map<String, ConvertStruct> convertStructMap = convertStrucDoubletMapp.get(fKey);
            for (String tableName : convertStructMap.keySet()) {
                if (tableName.equals(tokenMetaInfoDTO.getTokenMetaId())) {
                    Set<String> attributes = new HashSet<>();
                    attributes.addAll(this.attributes);
                    attributes.addAll(convertStructMap.get(tableName).getAttributes());
                    this.attributes = Lists.newArrayList(attributes);
                } else {
                    this.foreignTableMap.put(fKey, new HashMap<String, ForeignTable>() {
                                private static final long serialVersionUID = -6099972622629659276L;

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
     * 取得字段
     *
     * @param tokenMetaInfoDTO meta信息
     * @param metaKey          meta key
     * @return sql
     */
    private static String getFkey(TokenMetaInfoDTO tokenMetaInfoDTO, String metaKey) {
        for (MetaFieldDTO metaField : tokenMetaInfoDTO.getMetaFields()) {
            if (metaField.getType().getType().name().equals(FkeyTypeEnum.SELECT.name())
                    && metaField.getType().getRalValue().getMetaKey().equals(metaKey)) {
                return metaField.getKey();
            }
        }
        return null;
    }

    /**
     * 取得别名
     *
     * @param selectId               关联键
     * @param foreignTableName       关联表
     * @param attribute              属性
     * @param foreignAttributeSuites 关联表属性
     * @param tokenMetaInfoDTO       meta信息
     * @return sql
     */
    private static String getAlias(String selectId, String foreignTableName, String attribute,
                                   List<ForeignAttributeSuite> foreignAttributeSuites, TokenMetaInfoDTO tokenMetaInfoDTO) {
        for (ForeignAttributeSuite foreignAttributeSuite : foreignAttributeSuites) {
            if (selectId.equals(foreignAttributeSuite.getSelectId())
                    && attribute.equals(foreignAttributeSuite.getAttribute())) {
                return " as " + foreignAttributeSuite.getFKey();
            } else {
                for (MetaFieldDTO metaField : tokenMetaInfoDTO.getMetaFields()) {
                    if (metaField.getType().getType().name().equals(FkeyTypeEnum.SELECT_INTERN.name())) {
                        if (foreignTableName.equals(metaField.getType().getRalValue().getMetaKey()) // 表名
                                && attribute.equals(metaField.getType().getRalValue().getFkey()) // 字段名
                                ) {
                            return " as " + metaField.getKey();
                        }
                    }
                }
            }
        }
        return "";
    }

    /**
     * meta数据清洗
     *
     * @param tokenMetaInfoDTO meta信息
     * @return K:tableName V:中间结构
     */
    private Map<String, Map<String, ConvertStruct>> generateConvertStructMap(TokenMetaInfoDTO tokenMetaInfoDTO,
                                                                             String tableName) {
        Map<String, Map<String, ConvertStruct>> convertStrucDoubletMapp = new HashMap<>(); // K:表中的FKey K:metaId V:中间结构
        for (MetaFieldDTO metaField : tokenMetaInfoDTO.getMetaFields()) {
            if (metaField.getType().getType().name().equals(FkeyTypeEnum.SELECT.name())) {
                this.generateConvertStructMap(convertStrucDoubletMapp, metaField.getKey() // 表中的FKey
                        , metaField.getType().getRalValue().getMetaKey() // 表名
                        , metaField.getType().getRalValue().getFkey() // 字段名
                        , metaField.getKey() // 关联键名
                );
            } else if (metaField.getType().getType().name().equals(FkeyTypeEnum.SELECT_INTERN.name())) {
                String fieldFkey = getFkey(tokenMetaInfoDTO, metaField.getType().getRalValue().getMetaKey()); // 表中的FKey
                if (null != fieldFkey)
                    this.generateConvertStructMap(convertStrucDoubletMapp, fieldFkey // 表中的FKey
                            , metaField.getType().getRalValue().getMetaKey() // 表名
                            , metaField.getType().getRalValue().getFkey() // 字段名
                            , null // 关联键名
                    );
            } else {
                // 没有RAL，原表,meta就是tablename
                this.generateConvertStructMap(convertStrucDoubletMapp, metaField.getKey() // 表中的FKey
                        , tableName // 表名
                        , metaField.getKey() // 字段名
                        , null // 关联键名
                );
            }
        }
        return convertStrucDoubletMapp;
    }

    /**
     * 生成中间数据
     *
     * @param convertStrucDoubletMap K:表中的FKey K:metaId V:中间结构
     * @param fKey                   表中的fKey
     * @param metaId                 meta名
     * @param metaKey                字段名
     * @param selectFkey             关联键名
     */
    private void generateConvertStructMap(Map<String, Map<String, ConvertStruct>> convertStrucDoubletMap, String fKey,
                                          String metaId, String metaKey, String selectFkey) {
        Map<String, ConvertStruct> convertStructMap = new HashMap<>();
        if (convertStrucDoubletMap.containsKey(fKey)) {
            convertStructMap = convertStrucDoubletMap.get(fKey);
        }

        if (convertStructMap.containsKey(metaId)) { // 当表名存在，加入属性
            ConvertStruct convertStruct = convertStructMap.get(metaId);
            List<String> attributes = convertStruct.getAttributes();
            attributes.add(metaKey); // 加入属性
            this.buildForeignAttributeSuites(selectFkey, attributes, fKey, metaId);
            String orgSelectFkey = convertStruct.getSelectFkey();
            convertStructMap.put(metaId,
                    new ConvertStruct().build(attributes, orgSelectFkey == null ? selectFkey : orgSelectFkey));
        } else {
            List<String> attributes = new ArrayList<>();
            attributes.add(metaKey); // 加入属性
            this.buildForeignAttributeSuites(selectFkey, attributes, fKey, metaId);
            convertStructMap.put(metaId, new ConvertStruct().build(attributes, selectFkey));
        }
        convertStrucDoubletMap.put(fKey, convertStructMap);
    }

    /**
     * 记录关联表字段别名结构
     *
     * @param selectId   关联键
     * @param attributes 属性
     * @param fKey       字段
     * @param metaId     meta id
     */
    private void buildForeignAttributeSuites(String selectId, List<String> attributes, String fKey, String metaId) {
        HashSet<String> attributeSets = new HashSet<>(attributes);
        for (String attributeSet : attributeSets) {
            this.foreignAttributeSuites.add(new ForeignAttributeSuite(selectId, attributeSet, fKey, metaId));
        }
    }

    /**
     * 根据视图查询数据条数
     *
     * @param tableName 表名
     * @return sql
     */
    public String buildViewCountQuery(String tableName) {
        this.countSql = "select count(1) " + this.buildSelfQuery(tableName) + this.buildJoinViewQuery(tableName);
        logger.info(this.countSql);
        return this.countSql;
    }

    /**
     * 根据视图查询数据条数
     *
     * @param tableName 表名
     * @return sql
     */
    public String buildViewCountQuery(String tableName, SearchItem searchItem) {
        // 共通的sql赋值
        this.commonSql = this.buildSelfQuery(tableName) + this.buildJoinViewQuery(tableName) + this.buildWhereSQL(searchItem);
        this.countSql = "select count(1) " + this.commonSql;
        logger.info(this.countSql);
        return this.countSql;
    }

    /**
     * left join ( select sbss_shall_type_businessId as id,FName from
     * v_sbss_shall_type ) as FShallType on sbss_output.FShallType = FShallType.id
     *
     * @param tableName 表名
     * @return sql
     */
    protected String buildJoinViewQuery(String tableName) {
        StringBuilder rtn = new StringBuilder();
        for (String fKey : foreignTableMap.keySet()) {
            Map<String, ForeignTable> innerForeignTableMap = foreignTableMap.get(fKey);
            for (String selectkey : innerForeignTableMap.keySet())
                rtn.append("left join (\n").append(innerForeignTableMap.get(selectkey).buildSelfViewQuery())
                        .append(") as ").append(fKey).append(" \n").append("on ").append(tableName).append(".")
                        .append(selectkey).append(" = ").append(fKey).append(".id\n");
        }

        return rtn.toString();
    }

    /**
     * from ( select id,属性 ，businessId_colunm1，businessId_colunm2 from 主表 where id =
     * token_id_value ) as 主表
     *
     * @return 查询主表的SQL
     */
    protected String buildSelfQuery(String tableName) {
        return "from (\n" + "select \n" + "* \n" + "from " + tableName + " \n" + ") as " + tableName + "\n";
    }

    /**
     * select 主表.id,主表.属性 ,表1.id , 表1.属性 ,表2.id，表2.属性
     *
     * @return 最上面的select
     */
    protected String buildSelect(String tableName, List<ForeignAttributeSuite> foreignAttributeSuites,
                                 TokenMetaInfoDTO tokenMetaInfoDTO) {
        StringBuilder rtn = new StringBuilder("select \n" + this.buildTableAndAttribute(tableName, attributes) + "\n");

        for (String fKey : foreignTableMap.keySet()) {
            Map<String, ForeignTable> innerForeignTableMap = foreignTableMap.get(fKey);
            for (String selectkey : innerForeignTableMap.keySet()) {
                String columnStr = this.buildTableAndAttribute(fKey, innerForeignTableMap.get(selectkey),
                        foreignAttributeSuites, tokenMetaInfoDTO);
                if (!rtn.toString().contains(columnStr))
                    rtn.append(",").append(columnStr).append("\n");
            }
        }

        return rtn.toString();
    }

    /**
     * 根据表名和attribute拼sql
     *
     * @param tableName  表名
     * @param attributes 属性列表
     * @return 表名和属性组合sql
     */
    private String buildTableAndAttribute(String tableName, List<String> attributes) {
        StringBuilder rtn = new StringBuilder(tableName + ".id as " + tableName + "_businessId");
        HashSet<String> attributesSet = new HashSet<>(attributes);
        for (String attribute : attributesSet) {
            rtn.append(",").append(tableName).append(".").append(attribute);
        }
        return rtn.toString();
    }

    /**
     * 根据表名和attribute拼sql
     *
     * @param tableName    表名
     * @param foreignTable 关联表
     * @return 表名和属性组合sql
     */
    private String buildTableAndAttribute(String tableName, ForeignTable foreignTable,
                                          List<ForeignAttributeSuite> foreignAttributeSuites, TokenMetaInfoDTO tokenMetaInfoDTO) {
        StringBuilder rtn = new StringBuilder(tableName + ".id as " + tableName + "_businessId");
        HashSet<String> attributesSet = new HashSet<>(foreignTable.getAttributes());
        for (String attribute : attributesSet) {
            rtn.append(",").append(tableName).append(".").append(attribute).append(getAlias(tableName,
                    foreignTable.getForeignTableName(), attribute, foreignAttributeSuites, tokenMetaInfoDTO));
        }
        return rtn.toString();
    }

    /**
     * 根据lotNo查询
     *
     * @param lotNo lot no
     * @return sql
     */
    public String buildAbsViewQuery(String orgId, String lotNo) {
        String strSql = this.buildSelect(this.tableName, this.foreignAttributeSuites, this.tokenMetaInfoDTO)
                + this.buildAbsSelfQuery(this.tableName, orgId, lotNo)
                + this.buildJoinViewQuery(this.tableName);
        logger.info("strSql:" + strSql);
        return strSql;
    }

    /**
     * from ( select id,属性 ，businessId_colunm1，businessId_colunm2 from 主表 where id =
     * token_id_value ) as 主表
     *
     * @return 查询主表的SQL
     */
    private String buildAbsSelfQuery(String tableName, String orgId, String lotNo) {
        if (StringUtils.isEmpty(lotNo)) {
            return "from (\n" + "select \n" + "* \n" + "from " + tableName + " \n" + "where lotno is NULL and orgid = '" + orgId + "' \n" + ") as " + tableName + "\n";
        } else {
            return "from (\n" + "select \n" + "* \n" + "from " + tableName + " \n" + "where lotno = '" + lotNo + "' and orgid = '" + orgId + "'\n" + ") as " + tableName + "\n";
        }
    }

    public void buildStrSQL(String strSQL) {
        this.strSql = strSQL;
    }

    /**
     * 构造where条件
     *
     * @param searchItem 备查帐定义检索信息
     * @return
     */
    public String buildWhereSQL(SearchItem searchItem) {
        StringBuilder rtn = new StringBuilder(" WHERE 1=1 ");
        if (searchItem != null) {
            rtn.append(searchItem.getSearchSql()); // 拼接共通的信息
            if (null != searchItem.getSearchCommonMap() && searchItem.getSearchCommonMap().size() > 0) {
                // 遍历特殊处理
                for (SearchCommonEnum searchCommonEnum : searchItem.getSearchCommonMap().keySet()) {
                    rtn.append(searchItem.getSearchCommonMap().get(searchCommonEnum)); // 拼接sql 例如：“ AND renyuanbeicha.orgid = ?”
                }
            }
        } else {
            return "";
        }
        return rtn.toString();
    }

}