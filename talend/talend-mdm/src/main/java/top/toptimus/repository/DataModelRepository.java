package top.toptimus.repository;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.talend.mdm.commmon.metadata.ComplexTypeMetadata;
import org.talend.mdm.commmon.metadata.FieldMetadata;
import org.talend.mdm.commmon.metadata.MetadataRepository;
import top.toptimus.common.enums.FkeyTypeEnum;
import top.toptimus.meta.TalendMetaInfo;
import top.toptimus.meta.TokenMetaInfoDTO;
import top.toptimus.meta.property.FieldValueType;
import top.toptimus.meta.property.MetaFieldDTO;
import top.toptimus.meta.property.RalValue;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 查询talend主数据，并转换成系统meta
 *
 * @author gaoyu
 */
@Repository
public class DataModelRepository {
//    private static Logger logger = LoggerFactory.getLogger(DataModelRepository.class);

    @Autowired
    @Qualifier("metaJdbcTemplate")
    private JdbcTemplate metaJdbcTemplate;

    /**
     * 根据id取主数据meta列表
     *
     * @param x_pk_x_talend_id talend model id
     * @return K:表名，V:数据视图meta
     */
    @Transactional(readOnly = true)
    public Map<String, TokenMetaInfoDTO> getSchema(String x_pk_x_talend_id) throws IOException {
        try {
            String xsdStr = metaJdbcTemplate.queryForList("select x_schema from data_model_pOJO where x_pk_x_talend_id='" + x_pk_x_talend_id + "';")
                    .get(0)
                    .get("x_schema")
                    .toString();
            return SchemaConvertor.getTokenMetaInfoDTOS(
                    //  主数据meta xsd转换为stream
                    IOUtils.toInputStream(xsdStr, StandardCharsets.UTF_8.name())
            );
        } catch (DataAccessException e) {
            e.printStackTrace();
            throw new RuntimeException("主数据meta没找到");
        }
    }

    /**
     * 根据schema id和tokenMetaId取主数据meta
     *
     * @param x_pk_x_talend_id talend model id
     * @param tokenMetaId      token meta id
     * @return TokenMetaInfoDTO
     */
    @Transactional(readOnly = true)
    public TokenMetaInfoDTO getTokenMetaInfoDTO(String x_pk_x_talend_id, String tokenMetaId) throws IOException {
        try {
            String xsdStr = metaJdbcTemplate.queryForList("select x_schema from data_model_pOJO where x_pk_x_talend_id='" + x_pk_x_talend_id + "';")
                    .get(0)
                    .get("x_schema")
                    .toString();
            return SchemaConvertor.getTokenMetaInfoDTO(
                    //  主数据meta xsd转换为stream
                    IOUtils.toInputStream(xsdStr, StandardCharsets.UTF_8.name())
                    , tokenMetaId
            );
        } catch (DataAccessException e) {
            e.printStackTrace();
            throw new RuntimeException("主数据meta没找到");
        }
    }

    /**
     * 根据schema id和tokenMetaId取主数据meta
     *
     * @param x_pk_x_talend_id talend model id
     * @param tokenMetaId      token meta id
     * @param key              字段
     * @return TalendMetaInfo
     */
    @Transactional(readOnly = true)
    public TalendMetaInfo getTalendMetaInfo(String x_pk_x_talend_id, String tokenMetaId, String key) throws IOException {
        try {
            String xsdStr = metaJdbcTemplate.queryForList("select x_schema from data_model_pOJO where x_pk_x_talend_id='" + x_pk_x_talend_id + "';")
                    .get(0)
                    .get("x_schema")
                    .toString();
            return TalendMetaInfoConvertor.getTalendMetaInfo(
                    //  主数据meta xsd转换为stream
                    IOUtils.toInputStream(xsdStr, StandardCharsets.UTF_8.name())
                    , tokenMetaId
                    , key
            );
        } catch (DataAccessException e) {
            e.printStackTrace();
            throw new RuntimeException("主数据meta没找到");
        }
    }

    /**
     * 取schema中所有关联定义
     *
     * @param x_pk_x_talend_id        talend model id
     * @param referenceEntityTypeName 联表名
     * @return 关联定义
     */
    @Transactional(readOnly = true)
    public List<TalendMetaInfo> getTalendMetaInfo(String x_pk_x_talend_id, String referenceEntityTypeName) throws IOException {
        try {
            String xsdStr = metaJdbcTemplate.queryForList("select x_schema from data_model_pOJO where x_pk_x_talend_id='" + x_pk_x_talend_id + "';")
                    .get(0)
                    .get("x_schema")
                    .toString();
            return TalendReferenceConvertor.getTalendMetaInfo(
                    //  主数据meta xsd转换为stream
                    IOUtils.toInputStream(xsdStr, StandardCharsets.UTF_8.name())
                    , referenceEntityTypeName
            );
        } catch (DataAccessException e) {
            e.printStackTrace();
            throw new RuntimeException("主数据meta没找到");
        }
    }

    /**
     * 分页查询所有xsd
     *
     * @param pageNo   页码
     * @param pageSize 分页长度
     * @return 查询结果集
     */
    @Transactional(readOnly = true)
    public List<Map<String, Object>> getAllSchema(int pageNo, int pageSize) {
        try {
            return metaJdbcTemplate.queryForList(
                    "select x_pk_x_talend_id,x_name from data_model_pOJO limit " + (pageNo - 1) * pageSize + "," + pageSize + ";"
            );
        } catch (DataAccessException e) {
            e.printStackTrace();
            return null;
        }
    }

}

/**
 * 配置用私有类
 */
class SchemaConvertor {
    /**
     * 主数据meta xsd生成数据视图meta
     *
     * @param stream 主数据meta xsd
     * @return K:表名，V:数据视图meta
     */
    static public Map<String, TokenMetaInfoDTO> getTokenMetaInfoDTOS(InputStream stream) {
        MetadataRepository repository = new MetadataRepository();
        repository.load(stream);

        return new HashMap<String, TokenMetaInfoDTO>() {{
            repository.getUserComplexTypes().forEach(complexTypeMetadata -> {
//                logger.info("+++++++++++++++++++++++++");
//                logger.info("表名：" + complexTypeMetadata.getName());

                put(
                        complexTypeMetadata.getName()   //  tokenMetaId，也就是talend表名
                        , new TokenMetaInfoDTO().buildTokenMetaInfoDTO(
                                complexTypeMetadata.getName()   //  tokenMetaId，也就是talend表名
                                , SchemaConvertor.getMetaFields(complexTypeMetadata)    //  主数据meta转换为业务视图meta
                        )
                );
            });
        }};
    }

    /**
     * 根据xsd和表名返回token meta
     *
     * @param stream      主数据meta xsd
     * @param tokenMetaId 表名
     * @return 数据视图meta
     */
    public static TokenMetaInfoDTO getTokenMetaInfoDTO(InputStream stream, String tokenMetaId) {
        MetadataRepository repository = new MetadataRepository();
        repository.load(stream);
        ComplexTypeMetadata complexTypeMetadata = repository.getComplexType(tokenMetaId);

        return new TokenMetaInfoDTO().buildTokenMetaInfoDTO(
                complexTypeMetadata.getName()   //  tokenMetaId，也就是talend表名
                , SchemaConvertor.getMetaFields(complexTypeMetadata)    //  主数据meta转换为业务视图meta
        );
    }

    /**
     * 主数据meta转换为业务视图meta
     *
     * @param complexTypeMetadata 主数据meta
     * @return 业务视图meta
     */
    private static List<MetaFieldDTO> getMetaFields(ComplexTypeMetadata complexTypeMetadata) {
        return new ArrayList<MetaFieldDTO>() {{
            complexTypeMetadata.getFields().forEach(fieldMetadata -> {
                //  log
//                logger.info(
//                        "表名：" + fieldMetadata.getEntityTypeName()
//                                + "，字段名：" + fieldMetadata.getPath()
//                                + ",字段类型：" + fieldMetadata.getType().getName()
//                );

                //  增加字段
                add(
                        new MetaFieldDTO().createBaseMetaField(
                                fieldMetadata.getPath() //  key定义
                                , SchemaConvertor.getBasicFieldValueType(fieldMetadata)    //  字段定义
                        )
                );
            });
        }};
    }

    /**
     * 根据类型返回基本的数据类型
     *
     * @param fieldMetadata talend meta数据
     * @return 字段属性
     */
    private static FieldValueType getBasicFieldValueType(FieldMetadata fieldMetadata) {
        FieldValueType fieldValueType;
        String entityTypeName =
                //  取外键逻辑
                new TalendMetaInfo().build(fieldMetadata).getReferenceEntityTypeName();
        if (null != entityTypeName) {
            //  外键判断
            fieldValueType = new FieldValueType(
                    FkeyTypeEnum.SELECT
                    , new RalValue().build(entityTypeName)
            );
        } else {
            //  非外键，基础类型判断
            // TODO 基础类型缺
            if ("string".equals(fieldMetadata.getType().getName())) {
                fieldValueType = new FieldValueType(FkeyTypeEnum.STRING, null);
            } else if ("int".equals(fieldMetadata.getType().getName())) {
                fieldValueType = new FieldValueType(FkeyTypeEnum.INTEGER, null);
            } else if ("boolean".equals(fieldMetadata.getType().getName())) {
                fieldValueType = new FieldValueType(FkeyTypeEnum.BOOLEAN, null);
            } else if ("float".equals(fieldMetadata.getType().getName())) {
                fieldValueType = new FieldValueType(FkeyTypeEnum.DECIMAL, null);
            } else if ("dateTime".equals(fieldMetadata.getType().getName())) {
                fieldValueType = new FieldValueType(FkeyTypeEnum.DATE, null);
            } else if ("date".equals(fieldMetadata.getType().getName())) {
                fieldValueType = new FieldValueType(FkeyTypeEnum.DATE, null);
            } else if ("memo".equals(fieldMetadata.getType().getName())) {
                fieldValueType = new FieldValueType(FkeyTypeEnum.MEMO, null);
            } else if ("constant".equals(fieldMetadata.getType().getName())) {
                fieldValueType = new FieldValueType(FkeyTypeEnum.CONSTANT, null);
            } else if ("multi_constant".equals(fieldMetadata.getType().getName())) {
                fieldValueType = new FieldValueType(FkeyTypeEnum.MULTI_CONSTANT, null);
            } else {
                fieldValueType = new FieldValueType(FkeyTypeEnum.STRING, null);
            }
        }

        return fieldValueType;
    }

}

/**
 * SELECT类型逻辑私有类
 */
class TalendMetaInfoConvertor {
    /**
     * 根据xsd和表名返回token meta
     *
     * @param stream      主数据meta xsd
     * @param tokenMetaId 表名
     * @return 数据视图meta
     */
    public static TalendMetaInfo getTalendMetaInfo(InputStream stream, String tokenMetaId, String key) {
        MetadataRepository repository = new MetadataRepository();
        repository.load(stream);
        ComplexTypeMetadata complexTypeMetadata = repository.getComplexType(tokenMetaId);

        return TalendMetaInfoConvertor.getTalendMetaInfo(complexTypeMetadata, key);
    }

    /**
     * 主数据meta转换为业务视图meta
     *
     * @param complexTypeMetadata 主数据meta
     * @return 业务视图meta
     */
    private static TalendMetaInfo getTalendMetaInfo(ComplexTypeMetadata complexTypeMetadata, String key) {
        TalendMetaInfo talendMetaInfo = new TalendMetaInfo();
        for (FieldMetadata fieldMetadata : complexTypeMetadata.getFields()) {
            if (key.equals(fieldMetadata.getPath())) {
                talendMetaInfo = new TalendMetaInfo().build(fieldMetadata);
                break;
            }
        }
        return talendMetaInfo;
    }
}

/**
 * 外键关联逻辑私有类
 */
class TalendReferenceConvertor {
    /**
     * 根据xsd和表名返回token meta
     *
     * @param stream                  主数据meta xsd
     * @param referenceEntityTypeName 关联表名
     * @return 数据视图meta
     */
    public static List<TalendMetaInfo> getTalendMetaInfo(InputStream stream, String referenceEntityTypeName) {
        MetadataRepository repository = new MetadataRepository();
        repository.load(stream);
        return TalendMetaInfo.build(
                repository.getUserComplexTypes()
                , referenceEntityTypeName
        );
    }

}