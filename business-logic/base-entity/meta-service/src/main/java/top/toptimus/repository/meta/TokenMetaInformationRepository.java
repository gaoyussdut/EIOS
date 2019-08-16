package top.toptimus.repository.meta;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import top.toptimus.common.CurrentPage;
import top.toptimus.common.PaginationHelper;
import top.toptimus.meta.TokenMetaInformationDto;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class TokenMetaInformationRepository {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Transactional(propagation = Propagation.REQUIRED)
    public void save(TokenMetaInformationDto tokenMetaInforMationDto) {
        String sql = "INSERT INTO t_token_meta_formation(token_meta_id,token_meta_name,meta_type)"
                + "VALUES ('" + tokenMetaInforMationDto.getTokenMetaId() + "','"
                + tokenMetaInforMationDto.getTokenMetaName() + "','"
                + tokenMetaInforMationDto.getMetaType() + "')";
        try {
            jdbcTemplate.execute(sql);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("保存TokenMetaInformationDao失败");
        }
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void saveAll(List<TokenMetaInformationDto> tokenMetaInforMationDtos) {
        logger.info("savaAll");
        try {
            final List<TokenMetaInformationDto> tokenMetaInforMationDtoList = tokenMetaInforMationDtos;
            String sql = "insert into t_token_meta_formation(token_meta_id,token_meta_name,meta_type,meta_data_type)" +
                    " values(?,?,?,?)";
            jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
                @Override
                public int getBatchSize() {
                    return tokenMetaInforMationDtoList.size();
                }

                @Override
                public void setValues(@SuppressWarnings("NullableProblems") PreparedStatement ps, int i) throws SQLException {
                    ps.setString(1, tokenMetaInforMationDtoList.get(i).getTokenMetaId());
                    ps.setString(2, tokenMetaInforMationDtoList.get(i).getTokenMetaName());
                    ps.setString(3, tokenMetaInforMationDtoList.get(i).getMetaType());
                    ps.setString(4, tokenMetaInforMationDtoList.get(i).getMetaDataType().name());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("保存tokenMetaInformationDao失败");
        }

    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteAll(List<TokenMetaInformationDto> tokenMetaInforMationDtos) {
        StringBuilder sql = new StringBuilder("delete from t_token_meta_formation where token_meta_id in (");
        for (TokenMetaInformationDto tokenMetaInforMationDto : tokenMetaInforMationDtos) {
            sql.append("'").append(tokenMetaInforMationDto.getTokenMetaId()).append("',");
        }
        sql = new StringBuilder(sql.substring(0, sql.length() - 1));
        sql.append(");");
        try {
            jdbcTemplate.update(sql.toString());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("删除TokenMetaInformationDao失败");
        }
    }

    @Transactional(readOnly = true)
    public TokenMetaInformationDto findByTokenMetaId(String tokenMetaId) {
        try {
            String sql = "SELECT token_meta_id,token_meta_name,meta_type from t_token_meta_formation WHERE token_meta_id = ?";
            return jdbcTemplate.queryForObject(sql
                    , new Object[]{tokenMetaId}
                    , new TokenMetaInforMationDtoRowMapper());
        } catch (EmptyResultDataAccessException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Transactional(readOnly = true)
    public TokenMetaInformationDto findBillByTokenMetaId(String metaType, List<String> tokenMetaIds) {
        try {
            String tokenMetaIdList = "'" + String.join("','", tokenMetaIds) + "'";
            String sql = "SELECT token_meta_id,token_meta_name,meta_type from t_token_meta_formation WHERE meta_type = ? and token_meta_id IN (" + tokenMetaIdList + ")";
            logger.info(sql);
            return jdbcTemplate.queryForObject(sql
                    , new Object[]{metaType}
                    , new TokenMetaInforMationDtoRowMapper());
        } catch (EmptyResultDataAccessException e) {
            e.printStackTrace();
            return null;
        }

    }

    @Transactional(readOnly = true)
    public String getMetaTypeByMetaId(String metaId) {
        try {
            String sql = "SELECT meta_type from t_token_meta_formation WHERE token_meta_id ='" + metaId + "'";
            logger.info(sql);
            return jdbcTemplate.queryForObject(sql, String.class);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }

    }

    /**
     * 通过meta id列表取得meta信息定义，token名和类型
     *
     * @param tokenMetaIds meta id列表
     * @return meta信息定义，token名和类型
     */
    @Transactional(readOnly = true)
    public List<TokenMetaInformationDto> findByTokenMetaIds(List<String> tokenMetaIds) {
        try {
            String tokenMetaIdList = "'" + String.join("','", tokenMetaIds) + "'";
            String sql = "SELECT token_meta_id,token_meta_name,meta_type from t_token_meta_formation WHERE token_meta_id IN (" + tokenMetaIdList + ")";
            return jdbcTemplate.query(sql
                    , new Object[]{}
                    , new TokenMetaInforMationDtoRowMapper());
        } catch (EmptyResultDataAccessException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 查询所有meta信息
     *
     * @param pageNo   页码
     * @param pageSize 页宽
     * @return meta信息定义，token名和类型
     */
    @Transactional(readOnly = true)
    public CurrentPage<TokenMetaInformationDto> findAllTokenMetaInformation(int pageNo, int pageSize) {
        PaginationHelper<TokenMetaInformationDto> ph = new PaginationHelper<>();
        String strSQL = "SELECT token_meta_id,token_meta_name,meta_type from t_token_meta_formation ";
        String countSql = "SELECT Count(1) from t_token_meta_formation ";
        return ph.fetchPage(jdbcTemplate, countSql, strSQL, new Object[]{}, // 参数
                pageNo, pageSize, new TokenMetaInforMationDtoRowMapper());

    }

    /**
     * 根据metaType查询所有meta信息
     *
     * @param pageNo   页码
     * @param pageSize 页宽
     * @return meta信息定义，token名和类型
     */
    @Transactional(readOnly = true)
    public CurrentPage<TokenMetaInformationDto> findByMetaType(String metaType, int pageNo, int pageSize) {
        PaginationHelper<TokenMetaInformationDto> ph = new PaginationHelper<>();
        String strSQL = "SELECT token_meta_id,token_meta_name,meta_type from t_token_meta_formation where meta_type=? ";
        String countSql = "SELECT Count(1) from t_token_meta_formation where meta_type=? ";
        return ph.fetchPage(jdbcTemplate, countSql, strSQL, new Object[]{metaType}, // 参数
                pageNo, pageSize, new TokenMetaInforMationDtoRowMapper());
    }

    /**
     * 查询所有meta信息
     *
     * @param metaDataType meta数据类型
     * @param pageNo       页码
     * @param pageSize     页宽
     * @return meta信息定义，token名和类型
     */
    @Transactional(readOnly = true)
    public CurrentPage<TokenMetaInformationDto> getAllMasterDataMeta(String metaDataType, int pageNo, int pageSize) {
        PaginationHelper<TokenMetaInformationDto> ph = new PaginationHelper<>();
        String strSQL = "SELECT token_meta_id,token_meta_name,meta_type from t_token_meta_formation WHERE meta_data_type = ?";
        String countSql = "SELECT Count(1) from t_token_meta_formation WHERE meta_data_type = ?";
        return ph.fetchPage(jdbcTemplate, countSql, strSQL, new Object[]{metaDataType}, // 参数
                pageNo, pageSize, new TokenMetaInforMationDtoRowMapper());

    }

    /**
     * 查询所有meta信息
     *
     * @param metaDataType meta数据类型
     * @param pageNo       页码
     * @param pageSize     页宽
     * @return meta信息定义，token名和类型
     */
    public CurrentPage<TokenMetaInformationDto> getAllMetaByMetaType(String metaType, String metaDataType, Integer pageNo, Integer pageSize) {
        PaginationHelper<TokenMetaInformationDto> ph = new PaginationHelper<>();
        String strSQL = "SELECT token_meta_id,token_meta_name,meta_type from t_token_meta_formation WHERE meta_data_type = ? AND meta_type = ?";
        String countSql = "SELECT Count(1) from t_token_meta_formation WHERE meta_data_type = ? AND meta_type = ?";
        return ph.fetchPage(jdbcTemplate, countSql, strSQL, new Object[]{metaDataType, metaType}, // 参数
                pageNo, pageSize, new TokenMetaInforMationDtoRowMapper());
    }

    /**
     * 查询视图meta
     *
     * @param metaId
     * @return
     */
    public List<TokenMetaInformationDto> getViewMetaByMasterDataMeta(String metaId) {
        String strSQL = "SELECT token_meta_id,token_meta_name,meta_type from t_token_meta_formation WHERE token_meta_id IN (SELECT view_meta_id FROM r_master_metaid_view_metaid WHERE master_meta_id = '" + metaId + "')";
        return jdbcTemplate.query(strSQL, new TokenMetaInforMationDtoRowMapper());
    }

    /**
     * 调用存储过程保存下推单据的tokendata并返回下推单据表头的tokenid
     *
     * @param storedProcedureName 存储过程名
     * @param preTokenId        前置单据的tokenid
     * @return String 单据表头tokenid
     */
    public String excuteStoredProcedure(String storedProcedureName, String preTokenId) {
        String strSQL = "SELECT "+storedProcedureName +"('"+preTokenId+"')";
        return jdbcTemplate.queryForObject(strSQL,String.class);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void delete(String metaId) {
        String sql = "DELETE FROM t_token_meta_formation WHERE token_meta_id = '" + metaId + "'";
        try {
            jdbcTemplate.execute(sql);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("保存TokenMetaInformationDao失败");
        }
    }
}


class TokenMetaInforMationDtoRowMapper implements RowMapper<TokenMetaInformationDto> {
    @Override
    public TokenMetaInformationDto mapRow(@SuppressWarnings("NullableProblems") ResultSet rs, int rowNum) throws SQLException {
        return new TokenMetaInformationDto(
                rs.getString("token_meta_id")
                , rs.getString("token_meta_name")
                , rs.getString("meta_type")
        );
    }
}