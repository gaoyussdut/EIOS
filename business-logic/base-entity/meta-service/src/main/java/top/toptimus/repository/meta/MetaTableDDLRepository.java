package top.toptimus.repository.meta;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import top.toptimus.common.enums.FkeyTypeEnum;
import top.toptimus.dynamicSQLModel.baseModel.ConvertStruct;
import top.toptimus.dynamicSQLModel.baseModel.ForeignAttributeSuite;
import top.toptimus.dynamicSQLModel.queryFacadeModel.CreateViewMainTable;
import top.toptimus.meta.TokenMetaInfoDTO;
import top.toptimus.meta.metaview.MetaInfoDTO;
import top.toptimus.meta.property.MetaFieldDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 */
@Component
public class MetaTableDDLRepository {

    private final static Logger logger = LoggerFactory.getLogger(MetaTableDDLRepository.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

//    /**
//     * 流程中表的创建DDL
//     *
//     * @param processRepoDao
//     * @param tokenMetaInfoDaoList
//     */
//    public void createTableDDL(ProcessRepoDao processRepoDao, List<TokenMetaInfoDTO> tokenMetaInfoDaoList) {
//        Map<String, Map<String, MetaField>> tableMap = new HashMap<>();
//
//
//        // 1、数据整理，将同一个存储表的TokenMetaInfoDao中所有的字段暂存在到一个Map
//        processRepoDao.getProcessTableList().forEach(processTable -> {
//            Map<String, MetaField> metaFieldMap = tableMap.get(processTable.getTableName());
//            if (null == metaFieldMap) {
//                metaFieldMap = new HashMap<>();
//                tableMap.put(processTable.getTableName(), metaFieldMap);
//            }
//
//            for (ProcessTableVertex processTableVertex : processTable.getProcessTableVertexList()) {
//                for (TokenMetaInfoDTO tokenMetaInfoDao : tokenMetaInfoDaoList) {
//                    if (tokenMetaInfoDao.getTokenMetaId().equals(processTableVertex.getMetaId())) {
//
//                        for (MetaField metaField : tokenMetaInfoDao.getMetaFields()) {
//                            if (!metaFieldMap.containsKey(metaField.getKey())) {
//                                metaFieldMap.put(metaField.getKey(), metaField);
//                            }
//                        }
//
//                        break;
//                    }
//                }
//            }
//
//        });
//
//        // 2、创建数据库表
//        tableMap.forEach((tableName, metaFieldList) -> createTable(tableName, metaFieldList));
//    }

    public void createTable(String tableName, Map<String, MetaFieldDTO> metaFieldMap) {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("CREATE TABLE ").append(tableName).append(" (ID VARCHAR(255) NOT NULL PRIMARY KEY");

        List<String> commentList = new ArrayList<>();

        metaFieldMap.forEach((key, metaField) -> {
            if (FkeyTypeEnum.SELECT_INTERN == metaField.getType().getType()) {
                return;
            }
            stringBuilder.append(",");

            stringBuilder.append(key).append(" ");

            stringBuilder.append(convert2ColType(metaField.getType().getType())).append(" ");

            String comment = metaField.getCaption();
            comment = comment.replaceAll("\'", "");
            comment = comment.replaceAll("\"", "");

            commentList.add("COMMENT ON COLUMN " + tableName + "." + key + " IS \'" + comment + "\'");

        });

//        stringBuilder.append(", insert_timestamp timestamp default CURRENT_TIMESTAMP");
        stringBuilder.append(") ");

        this.executeTable(tableName, stringBuilder.toString(), commentList);
    }

    /**
     * 创建表
     *
     * @param tableName   表名
     * @param metaInfoMap K:字段key，V:meta 字段信息
     */
    public void createTableByMetaInfo(String tableName, Map<String, MetaInfoDTO> metaInfoMap) {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("drop table if exists ").append(tableName).append(";");
        stringBuilder.append("CREATE TABLE ").append("\"").append(tableName).append("\"")
                .append("(ID VARCHAR(255) NOT NULL PRIMARY KEY");

        List<String> commentList = new ArrayList<>();

        metaInfoMap.forEach((key, metaInfoDTO) -> {
            if (FkeyTypeEnum.SELECT_INTERN.name().equals(metaInfoDTO.getFkeytype())) {
                return;
            }
            if (metaInfoDTO.getKey().equals("id")) {
                return;
            }
            stringBuilder.append(",");

            stringBuilder.append(key).append(" ");

            stringBuilder.append(convert2ColType(FkeyTypeEnum.valueOf(metaInfoDTO.getFkeytype()))).append(" ");

            String comment = metaInfoDTO.getCaption();
            comment = comment.replaceAll("\'", "");
            comment = comment.replaceAll("\"", "");

            commentList.add("COMMENT ON COLUMN " + tableName + "." + key + " IS \'" + comment + "\'");

        });

//        stringBuilder.append(", insert_timestamp timestamp default CURRENT_TIMESTAMP");
        stringBuilder.append(") ");

        this.executeTable(tableName, stringBuilder.toString(), commentList);
    }

    /**
     * 执行table sql
     *
     * @param tableName   表名
     * @param sql         sql
     * @param commentList comment
     */
    private void executeTable(String tableName, String sql, List<String> commentList) {
        logger.info("删除旧表：{}", tableName);
        jdbcTemplate.execute("DROP TABLE IF EXISTS " + "\"" + tableName + "\"");

        logger.info("创建新表:{}", sql);
        jdbcTemplate.execute(sql);

        for (String commSql : commentList) {
            jdbcTemplate.execute(commSql);
        }
    }


    private String convert2ColType(FkeyTypeEnum typeEnum) {
        switch (typeEnum) {
            case SELECT:
                return "VARCHAR(255)";
            case STRING:
            case EMAIL:
                return "VARCHAR(255) NULL DEFAULT NULL";
            case MOBILE:
                return "VARCHAR(11) NULL DEFAULT NULL";
            case IDCARD:
                return "VARCHAR(18) NULL DEFAULT NULL";
            case DATE:
            case TIMESTAMP:
                return "TIMESTAMP";
            case BOOLEAN:
                return "BOOLEAN";
            case DECIMAL:
            case CURRENCY:
                return "NUMERIC(15, 4) DEFAULT 0.0";
            case INTEGER:
                return "INTEGER DEFAULT 0";
            case CODE:
                return "VARCHAR(255)";
            case FILE:
                return "VARCHAR(2000)";
            case IMAGE:
                return "VARCHAR(2000)";
            case MEMO:
                return "VARCHAR(2000)";
            case CONSTANT:
                return "VARCHAR(255) NULL DEFAULT NULL";
            case MULTI_CONSTANT:
                return "VARCHAR(255) NULL DEFAULT NULL";
            default:
                return null;
        }
    }


    public void createViewDDL(TokenMetaInfoDTO tokenMetaInfoDTO) {
        String viewName = "v_" + tokenMetaInfoDTO.getTokenMetaId();
        List<ForeignAttributeSuite> foreignAttributeSuites = new ArrayList<>(); //  取关联表字段别名结构
        String strSql =
                new CreateViewMainTable
                        (
                                ConvertStruct.generateConvertStructMap(tokenMetaInfoDTO, tokenMetaInfoDTO.getTokenMetaId(), foreignAttributeSuites)   //  meta数据清洗
                                , tokenMetaInfoDTO.getTokenMetaId()    //  meta id
                        )   //  生成主表结构
                        .buildQuery(tokenMetaInfoDTO.getTokenMetaId(), foreignAttributeSuites, tokenMetaInfoDTO)
                        .getStrSql();  //  生成sql

        strSql = "CREATE VIEW " + viewName + " AS " + strSql;

        logger.info("创建新视图:{}", strSql);
        jdbcTemplate.execute(strSql);
    }

    public void dropViewDDL(String metaId, String metaName) {
        String viewName = "v_" + metaId;
        logger.info("删除旧视图：{}:{}", metaName, viewName);
        jdbcTemplate.execute("DROP VIEW IF EXISTS " + viewName);
    }

//    public void dropTableDDL(String metaId, String metaName) {
//        String viewName = "v_" + metaId;
//        logger.info("删除旧业务表：{}:{}", metaName, viewName);
//        jdbcTemplate.execute("DROP TABLE IF EXISTS " + viewName);
//    }

}

