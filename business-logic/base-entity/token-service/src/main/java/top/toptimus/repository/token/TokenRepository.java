package top.toptimus.repository.token;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import top.toptimus.common.CurrentPage;
import top.toptimus.common.PaginationHelper;
import top.toptimus.exception.TopSQLException;
import top.toptimus.model.tokenData.base.TokenDataModel;
import top.toptimus.model.tokenData.query.EntryTokenQueryModel;
import top.toptimus.token.TokenDto;
import top.toptimus.token.relation.TokenRelDTO;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class TokenRepository {

    private static final Logger logger = LoggerFactory.getLogger(TokenRepository.class);
    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 根据tokenId更新指定表的字段
     *
     * @param tokenDto token数据
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateTokenValueById(TokenDto tokenDto) {
        String sql = " UPDATE " + tokenDto.getTableName() + " SET " + tokenDto.getColumn() + "='" + tokenDto.getValue() + "' WHERE id = '" + tokenDto.getTokenId() + "'";
        try {
            jdbcTemplate.execute(sql);
        } catch (TopSQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据tokenId更新指定表的字段
     *
     * @param tokenDtos token数据
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateTokenValuesById(List<TokenDto> tokenDtos) {
        StringBuilder sql = new StringBuilder();
        for (TokenDto tokenDto : tokenDtos) {
            sql.append(" UPDATE ").append(tokenDto.getTableName()).append(" SET ").append(tokenDto.getColumn()).append("='").append(tokenDto.getValue()).append("' WHERE id = '").append(tokenDto.getTokenId()).append("';");
        }
        try {
            jdbcTemplate.execute(sql.toString());
        } catch (TopSQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 插入指定表中的指定数据
     *
     * @param tokenDto token数据
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void insertTokenValueById(TokenDto tokenDto) {
        String sql = "INSERT INTO " + tokenDto.getTableName() + "(id," + tokenDto.getColumn() + ") VALUES('" + tokenDto.getTokenId() + "','" + tokenDto.getValue() + "')";
        try {
            jdbcTemplate.execute(sql);
        } catch (TopSQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 插入指定表中的指定tokenId
     *
     * @param tokenDto token数据
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void insertTokenId(TokenDto tokenDto) {
        String sql = "INSERT INTO " + tokenDto.getTableName() + "(id) VALUES('" + tokenDto.getTokenId() + "')";
        try {
            jdbcTemplate.execute(sql);
            logger.info("TokenDto sql:" + sql);
        } catch (TopSQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 匹配指定表中指定字段检索tokenId的分页
     *
     * @param tableName            表名
     * @param column               列
     * @param entryTokenQueryModel 分录数据查询model
     * @return id列表
     */
    @Transactional
    public CurrentPage<String> findTokenIdsByTableColumn(String tableName, String column, EntryTokenQueryModel entryTokenQueryModel) {
        PaginationHelper<String> ph = new PaginationHelper<>();
        // 持久化
        String sql = "SELECT id FROM " + tableName + " WHERE " + column + "='" + entryTokenQueryModel.getBillTokenId() + "'";
        String countSql = "SELECT Count(id) FROM " + tableName + " WHERE " + column + "='" + entryTokenQueryModel.getBillTokenId() + "'";
        try {
            return ph.fetchPage(jdbcTemplate, countSql, sql, new Object[]{}, // 参数
                    entryTokenQueryModel.getPageNo(), entryTokenQueryModel.getPageSize(), (rs, rowNum) -> rs.getString("id"));
        } catch (TopSQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 匹配指定表中指定字段检索tokenId（分页）
     *
     * @param tokenDataModel token数据存储的充血模型
     * @return 分页数据
     */
    @Transactional
    public CurrentPage<TokenDto> findTokenIdsByTableColumnPageable(TokenDataModel tokenDataModel) {
        PaginationHelper<TokenDto> ph = new PaginationHelper<>();
        // 持久化
        String sql = "SELECT id FROM " + tokenDataModel.getTableNameList().get(0);
        String countSql = "SELECT Count(id) FROM " + tokenDataModel.getTableNameList().get(0);
        try {
            return ph.fetchPage(jdbcTemplate, countSql, sql, new Object[]{}, // 参数
                    tokenDataModel.getTokenDataPageableDto().getPageNo(), tokenDataModel.getTokenDataPageableDto().getPageSize(), new TokenRowMapper());
        } catch (TopSQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 匹配指定表中指定字段检索tokenId（分页）
     *
     * @param tokenDto token数据
     * @param pageNo   页码
     * @param pageSize 页宽
     * @return 分页数据
     */
    @Transactional
    public CurrentPage<TokenDto> findTokenIdsByTableColumnPageable(TokenDto tokenDto, int pageNo,
                                                                   int pageSize) {
        PaginationHelper<TokenDto> ph = new PaginationHelper<>();
        String whereSql = ""; // WHERE条件 如果column和value都为空的场合 没有Where条件
        if (StringUtils.isNotEmpty(tokenDto.getColumn()) && StringUtils.isNotEmpty(tokenDto.getValue())) {
            whereSql = " WHERE " + tokenDto.getColumn() + "='" + tokenDto.getValue() + "'";
        }
        // 持久化
        String sql = "SELECT id FROM " + tokenDto.getTableName() + whereSql;
        String countSql = "SELECT Count(id) FROM " + tokenDto.getTableName() + whereSql;
        try {
            return ph.fetchPage(jdbcTemplate, countSql, sql, new Object[]{}, // 参数
                    pageNo, pageSize, new TokenRowMapper());
        } catch (TopSQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteTokenByIds(String tableName, List<String> tokenIds) {
        String sql = "DELETE FROM " + tableName + " where id in ('" + String.join("','", tokenIds) + "')\n";
        try {
            jdbcTemplate.execute(sql);
            logger.info("deleteTokenByIds sql:" + sql);
        } catch (TopSQLException e) {
            e.printStackTrace();
        }
    }

    public List<TokenRelDTO> getRelTokenByBillMetaIdAndBillTokenId(String metaId, String tokenId) {
        String sql = "SELECT bill_meta_id,bill_token_id,entry_meta_id,entry_token_id FROM r_bill_entry_token" +
                " WHERE bill_meta_id = '"+metaId+"' AND bill_token_id = '"+tokenId+"'";
        try {
            return jdbcTemplate.query(sql, new TokenRelDTOMapper());
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    class TokenRelDTOMapper implements RowMapper<TokenRelDTO> {
        @Override
        public TokenRelDTO mapRow(@SuppressWarnings("NullableProblems") ResultSet rs, int rowNumber) throws SQLException {
            return new TokenRelDTO(
                    rs.getString("bill_meta_id")
                    ,rs.getString("bill_token_id")
                    ,rs.getString("entry_meta_id")
                    ,rs.getString("entry_token_id")
            );
        }
    }
}

class TokenRowMapper implements RowMapper<TokenDto> {
    @Override
    public TokenDto mapRow(@SuppressWarnings("NullableProblems") ResultSet rs, int rowNumber) throws SQLException {
        return new TokenDto(
                rs.getString("id")
        );
    }
}
