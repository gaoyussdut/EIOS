package top.toptimus.repository.token.dynamicTokenQuery;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import top.toptimus.baseModel.query.BasePageQueryModel;
import top.toptimus.baseModel.query.BaseQueryModel;
import top.toptimus.meta.property.MetaFieldDTO;
import top.toptimus.model.tokenModel.query.baseQueryFacade.TokenQueryWithPageModel;
import top.toptimus.repository.token.dynamicTokenQuery.model.DynamicTokenQueryModel;
import top.toptimus.tokendata.TokenDataDto;
import top.toptimus.tokendata.TokenDataPageableDto;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 根据meta动态查询token
 */
@Repository
public class DynamicTokenQueryRepository {
    private static final Logger logger = LoggerFactory.getLogger(DynamicTokenQueryRepository.class);

    @Autowired
    protected JdbcTemplate jdbcTemplate;

    /**
     * 数据总条数
     *
     * @param tokenQueryWithPageModel token数据查询model,带分页
     * @return 数据总条数
     */
    @Transactional(readOnly = true)
    public int getTokenDataCount(TokenQueryWithPageModel tokenQueryWithPageModel) {
        // noinspection ConstantConditions
        return jdbcTemplate.queryForObject(tokenQueryWithPageModel.getMainTable().buildViewCountQuery(tokenQueryWithPageModel.getMainTable().getTableName(), tokenQueryWithPageModel.getSearchInfo().getSearchItem()) // 生成sql
                ,tokenQueryWithPageModel.getParam().toArray(), Integer.class);
    }

    /**
     * 查询token数据并封装返回，分页
     *
     * @param basePageQueryModel 基础分页查询model
     * @return TokenDataPageableDto
     */
    @Transactional(readOnly = true)
    public TokenDataPageableDto getReducedTokenDataDtos(BasePageQueryModel basePageQueryModel) {
        return basePageQueryModel.buildTokenDataPageableDto(    //  构建token list返回结构
                new DynamicTokenQueryModel(jdbcTemplate.queryForList(basePageQueryModel.getMainTable().getStrSql() // 生成sql
                        ,basePageQueryModel.getParam().toArray()), basePageQueryModel.getMainTable()).getReducedDtos()  // 查询token数据并封装返回
        );
    }

    /**
     * 查询token数据，不分页
     *
     * @param baseQueryModel 查询基类
     * @return List<TokenDataDto>
     */
    @Transactional(readOnly = true)
    public List<TokenDataDto> getTokenDataDto(BaseQueryModel baseQueryModel) {
        try {
            logger.info("++++++++++++++++++strSql " + baseQueryModel.getMainTable().getStrSql());
            return new DynamicTokenQueryModel(jdbcTemplate.queryForList(baseQueryModel.getMainTable().getStrSql()), baseQueryModel.getMainTable()).getTokenDataDtos();
        } catch (Exception e) {
            logger.info(JSON.toJSONString(e));
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 检查token id是否存在
     *
     * @param tableName 表名
     * @param tokenIds  token id 列表
     * @return 存在的token id 列表
     */
    @Transactional(readOnly = true)
    public List<String> getExistTokenIds(String tableName, List<String> tokenIds) {
        String Sql = new DynamicTokenQueryModel().generateAssertNullSQL(tableName, tokenIds).getAssertSql();
        List<Map<String, Object>> resultSet = jdbcTemplate.queryForList(Sql);
        List<String> stringList = new ArrayList<>();
        resultSet.forEach(stringObjectMap -> stringList.add(String.valueOf(stringObjectMap.get("ID"))));
        return stringList;
    }

    /**
     * 更新token data
     *
     * @param tokenDataDto token数据
     * @param tableName    表名
     * @param metaFields   meta字段列表
     */
    @Transactional
    public void generateUpdate(TokenDataDto tokenDataDto, String tableName, List<MetaFieldDTO> metaFields) {
        String strSQL = new DynamicTokenQueryModel().generateUpdateSql(tokenDataDto, tableName, metaFields).getSaveSql();
        logger.info("strSQL++++:" + JSON.toJSONString(strSQL));
        if (!"".equals(strSQL)) {
            try {
                jdbcTemplate.execute(strSQL);
            } catch (DataAccessException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 保存token data
     *
     * @param datas      token数据
     * @param tableName  表名
     * @param metaFields 字段
     */
    @Transactional
    public void generateSave(List<TokenDataDto> datas, String tableName, List<MetaFieldDTO> metaFields) {
        logger.info("List<TokenDataDto>++++++++++++++：" + JSON.toJSONString(datas));
        jdbcTemplate.execute(new DynamicTokenQueryModel().buildInsertSql(datas, tableName, metaFields)   //  生成插入token的sql
                .getSaveSql());
    }
}
