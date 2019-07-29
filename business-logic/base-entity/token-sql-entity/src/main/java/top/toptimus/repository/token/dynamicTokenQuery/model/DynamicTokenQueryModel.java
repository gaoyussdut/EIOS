package top.toptimus.repository.token.dynamicTokenQuery.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.toptimus.common.enums.FkeyTypeEnum;
import top.toptimus.constantConfig.Constants;
import top.toptimus.dynamicSQLModel.baseModel.MainTable;
import top.toptimus.meta.property.MetaFieldDTO;
import top.toptimus.model.tokenData.query.ReduceTokenModel;
import top.toptimus.tokendata.ReducedTokenDataDto;
import top.toptimus.tokendata.TokenDataDto;
import top.toptimus.tokendata.field.FkeyField;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static top.toptimus.common.enums.FkeyTypeEnum.SELECT_INTERN;

/**
 * 根据meta动态查询token的充血模型
 *
 * @author gaoyu
 * @since 2018-6-29
 */
@Getter
@Setter
@NoArgsConstructor
public class DynamicTokenQueryModel {
    private static final Logger logger = LoggerFactory.getLogger(DynamicTokenQueryModel.class);

    private String saveSql;
    private String assertSql;
    private List<TokenDataDto> tokenDataDtos = new ArrayList<>();
    private List<ReducedTokenDataDto> reducedDtos;

    /**
     * 根据meta动态查询token的清洗
     *
     * @param resultSet 查询结果集
     * @param mainTable 主表结构
     */
    public DynamicTokenQueryModel(List<Map<String, Object>> resultSet, MainTable mainTable) {
        resultSet.forEach(tokenData -> {
            String tokenId = String.valueOf(tokenData.get(mainTable.getTableName() + Constants._businessId));
            this.tokenDataDtos.add(
                    new TokenDataDto(tokenId, mainTable.getTokenMetaInfoDTO(), tokenData)  //  根据查询结果集构造TokenDataDto
            );
        });
        this.reducedDtos = new ReduceTokenModel(tokenDataDtos).getReducedTokenDataDtoList();
    }

    /**
     * 生成插入token的sql的insert部分
     *
     * @param tabname    表名
     * @param pk         主键
     * @param metaFields 字段名
     * @return this
     */
    private static String buildInsertSql(String tabname, String pk, List<MetaFieldDTO> metaFields) {
        StringBuilder sql = new StringBuilder("INSERT INTO " + tabname + "(" + pk + "");
        for (MetaFieldDTO metaField : metaFields) {
            if (!metaField.getType().getType().equals(SELECT_INTERN))
                sql.append(",").append(metaField.getKey());
        }
        sql.append(")	VALUES");
        return sql.toString();
    }

    /**
     * 生成插入token的sql的value部分
     *
     * @param tokenDataDtos token数据
     * @param metaFields    字段
     * @return sql
     */
    private static String buildValueSql(List<TokenDataDto> tokenDataDtos, List<MetaFieldDTO> metaFields) {
        StringBuilder sql = new StringBuilder();
        for (TokenDataDto dataDto : tokenDataDtos) {
            sql.append("('").append(dataDto.getTokenId()).append("'");
            for (MetaFieldDTO metaField : metaFields) {
                if (!metaField.getType().getType().equals(SELECT_INTERN))
                    sql.append(DynamicTokenQueryModel.getFieldSQL(dataDto, metaField));
            }
            sql.append("),");
        }
        sql = new StringBuilder(sql.substring(0, sql.length() - 1) + ";");
        return sql.toString();
    }

    /**
     * 根据meta的type清洗生成set语句
     *
     * @param dataDto   数据
     * @param metaField meta
     * @return sql
     */
    private static String getFieldSQL(TokenDataDto dataDto, MetaFieldDTO metaField) {
        for (FkeyField fkeyField : dataDto.getFields()) {
            if (fkeyField.getKey().equals(metaField.getKey())) {
                switch (metaField.getType().getType()) {
                    case SELECT_INTERN:
                        return ",''";
                    case SELECT:
                        if (fkeyField.getBusinessId() != null) {
                            return ",'" + fkeyField.getBusinessId() + "'";
                        } else {
                            return ",'" + 0 + "'";
                        }
                    case INTEGER:
                        Integer intValue = 0;
                        try {
                            intValue = Integer.valueOf(fkeyField.getJsonData());
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }
                        return ",'" + intValue + "'";
                    case CURRENCY:
                    case DECIMAL:
                        Double doubleValue = 0.0;
                        try {
                            doubleValue = Double.valueOf(fkeyField.getJsonData());
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }
                        return ",'" + doubleValue + "'";
                    case DATE:
                        try {
                            Date dateValue = new SimpleDateFormat("yyyy-MM-dd").parse(fkeyField.getJsonData());
                            return ",'" + new SimpleDateFormat("yyyy-MM-dd").format(dateValue) + "'";
                        } catch (ParseException e) {
                            e.printStackTrace();
                            return ", null";
                        }
                    case TIMESTAMP:
                        try {
                            Date dateValue = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(fkeyField.getJsonData());
                            return ",'" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(dateValue) + "'";
                        } catch (ParseException e) {
                            e.printStackTrace();
                            return ", null";
                        }
                    case BOOLEAN:
                        if (Constants.jsonData_true.equals(fkeyField.getJsonData().toLowerCase())) {
                            return ",'1'";
                        } else {
                            return ",'0'";
                        }
                    default:
                        return ",'" + fkeyField.getJsonData() + "'";
                }
            }
        }
        return ",NULL"; // 没有匹配上
    }

    /**
     * 根据meta字段筛选数据
     *
     * @param fieldUDTS  待清洗数据
     * @param metaFields meta字段
     */
    private static void cleanFkeyFieldUDT(List<FkeyField> fieldUDTS, List<MetaFieldDTO> metaFields) {
        List<FkeyField> cleanFieldUDTS = new ArrayList<>();
        for (FkeyField fkeyFieldUDT : fieldUDTS) {
            for (MetaFieldDTO metaField : metaFields) {
                if (fkeyFieldUDT.getKey() != null && fkeyFieldUDT.getKey().equals(metaField.getKey())) {
                    if (fkeyFieldUDT.getJsonData() == null
                            || fkeyFieldUDT.getJsonData().equals(Constants.jsonData_EmptyString)
                            || fkeyFieldUDT.getJsonData().equals(Constants.nullValue)
                            || metaField.getReadonly())
                        break;
                    cleanFieldUDTS.add(fkeyFieldUDT);

                }
            }

        }
        fieldUDTS.clear();
        fieldUDTS.addAll(cleanFieldUDTS);
    }

    /**
     * 生成更新sql的set部分
     *
     * @param fieldUDTS 字段定义
     * @return sql
     */
    private static String generateSetSql(List<FkeyField> fieldUDTS) {
        StringBuilder strRtn = new StringBuilder();
        if (fieldUDTS == null || fieldUDTS.size() == 0) {
            throw new RuntimeException("fieldUDTS数据为空!");
        }
        for (FkeyField fkeyField : fieldUDTS) {
            if (Constants.id.equals(fkeyField.getKey().toLowerCase())) {
                continue;
            }
            FkeyTypeEnum datatype = fkeyField.getDataType();
            strRtn.append(fkeyField.getKey()).append("=");
            switch (datatype) {
                case SELECT_INTERN:
                    break;
                case SELECT:
                    strRtn.append("'").append(fkeyField.getBusinessId()).append("',");
                    break;
                case INTEGER:
                    Integer intValue = 0;
                    try {
                        intValue = Integer.valueOf(fkeyField.getJsonData());
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                    strRtn.append("'").append(intValue).append("',");
                    break;
                case DECIMAL:
                case CURRENCY:
                    Double doubleValue = 0.0;
                    try {
                        doubleValue = Double.valueOf(fkeyField.getJsonData());
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                    strRtn.append("'").append(doubleValue).append("',");
                    break;
                case DATE: //TODO
                    Date dateValue = null;
                    try {
                        dateValue = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(fkeyField.getJsonData());
                    } catch (ParseException e) {
                        try {
                            dateValue = new SimpleDateFormat("yyyy-MM-dd").parse(fkeyField.getJsonData());
                        } catch (ParseException er) {
                            e.printStackTrace();
                        }
                    }
                    if (dateValue == null) {
                        strRtn.append("null,");
                    } else {
                        strRtn.append("'").append(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(dateValue)).append("',");
                    }
                    break;
                case TIMESTAMP:
                    Date timeValue = null;
                    try {
                        timeValue = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(fkeyField.getJsonData());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    if (timeValue == null) {
                        strRtn.append("null,");
                    } else {
                        strRtn.append("'").append(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(timeValue)).append("',");
                    }
                    break;
                case BOOLEAN:
                    if (Constants.jsonData_true.equals(fkeyField.getJsonData().toLowerCase())) {
                        strRtn.append("'1',");
                        break;
                    } else {
                        strRtn.append("'0',");
                        break;
                    }

                default:
                    strRtn.append("'").append(fkeyField.getJsonData()).append("',");
            }

        }
        return strRtn.substring(0, strRtn.length() - 1);
    }

    /**
     * 生成插入token的sql
     *
     * @param tokenDataDtos token数据
     * @param tableName     表名
     * @param metaFields    字段
     * @return this
     */
    public DynamicTokenQueryModel buildInsertSql(List<TokenDataDto> tokenDataDtos, String tableName,
                                                 List<MetaFieldDTO> metaFields) {
        //  数据清洗
        for (MetaFieldDTO metaField : metaFields) {
            if (Constants.id.equals(metaField.getKey().toLowerCase())) {
                metaFields.remove(metaField);
                break;
            }
        }
        String pk = Constants.id;
        if (tokenDataDtos == null || tokenDataDtos.size() == 0) {
            this.saveSql = "";
        } else {
            this.saveSql = DynamicTokenQueryModel.buildInsertSql(tableName, pk, metaFields);
            this.saveSql += DynamicTokenQueryModel.buildValueSql(tokenDataDtos, metaFields);
            logger.info(" SQL :" + this.saveSql);
        }
        if (StringUtils.isEmpty(this.saveSql)) {
            throw new RuntimeException("保存sql为空");
        }
        return this;
    }

    /**
     * 生成更新sql
     *
     * @param tokenDataDto 数据
     * @param tabname      表名
     * @param metaFields   字段名
     * @return this
     */
    public DynamicTokenQueryModel generateUpdateSql(TokenDataDto tokenDataDto, String tabname,
                                                    List<MetaFieldDTO> metaFields) {
        this.saveSql = "UPDATE " + tabname + " SET ";
        List<FkeyField> fieldUDTS = tokenDataDto.getFields();
        DynamicTokenQueryModel.cleanFkeyFieldUDT(fieldUDTS, metaFields);
        logger.info("generateUpdateSql tokenDataDto:" + tokenDataDto.getFields().size());
        if (null == tokenDataDto.getFields() || tokenDataDto.getFields().size() == 0) {
            logger.warn("FieldUDTS为空！");
            this.saveSql += "id=id" + " WHERE " + Constants.id + " = '" + tokenDataDto.getTokenId() + "' ;";
            return this;
        }
        this.saveSql += DynamicTokenQueryModel.generateSetSql(fieldUDTS);
        this.saveSql += " WHERE " + Constants.id + " = '" + tokenDataDto.getTokenId() + "' ;";
        return this;
    }

    /**
     * 生成根据token id查询数据库中的token的sql
     *
     * @param tableName 表名
     * @param tokenIds  token id列表
     * @return this
     */
    public DynamicTokenQueryModel generateAssertNullSQL(String tableName, List<String> tokenIds) {
        this.assertSql = "SELECT ID FROM " + tableName + " where id in ('" + String.join("','", tokenIds) + "')\n";
        return this;
    }
}
