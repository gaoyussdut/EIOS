package top.toptimus.repository.meta;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import top.toptimus.common.CurrentPage;
import top.toptimus.common.PaginationHelper;
import top.toptimus.common.enums.FkeyTypeEnum;
import top.toptimus.meta.FKeyTypeDto;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * meta的fkey type保存在t_token_meta_fkey_type中
 * 对t_token_meta_fkey_type表的保存、查询
 *
 * @author gaoyu
 * @since 2018-07-04
 */
@Repository
public class FKeyTypeRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Transactional(propagation = Propagation.REQUIRED)
    public void save(FKeyTypeDto fKeyTypeDao) {
        String sql = "INSERT INTO t_token_meta_fkey_type(key,type) " +
                "VALUES ('" + fKeyTypeDao.getKey() + "','"
                + fKeyTypeDao.getType() + "')";
        try {
            jdbcTemplate.execute(sql);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("保存FKeyTypeDao失败");
        }

    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void saveAll(List<FKeyTypeDto> fKeyTypeDtos) {
        try {
            final List<FKeyTypeDto> fKeyTypeDtoList = fKeyTypeDtos;
            String sql = "insert into t_token_meta_fkey_type(key,type)" +
                    " values(?,?) on conflict(key) Do UPDATE SET type =t_token_meta_fkey_type.TYPE ";
            jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
                @Override
                public int getBatchSize() {
                    return fKeyTypeDtoList.size();
                }

                @Override
                public void setValues(@SuppressWarnings("NullableProblems") PreparedStatement ps, int i)
                        throws SQLException {
                    ps.setString(1, fKeyTypeDtoList.get(i).getKey());
                    ps.setString(2, fKeyTypeDtoList.get(i).getType().name());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("保存FKeyTypeDao失败");
        }
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public int deleteBykey(String key) {
        String sql = "delete from t_token_meta_fkey_type where key=?";
        return jdbcTemplate.update(sql, key);
    }

    @Transactional(readOnly = true)
    public FKeyTypeDto findById(String key) {
        try {
            String sql = "SELECT key,type FROM t_token_meta_fkey_type WHERE key = ?";
            return jdbcTemplate.queryForObject(sql, new Object[]{key}, new FKeyTypeDtoRowMapper());
        } catch (EmptyResultDataAccessException e) {
            e.printStackTrace();
            return null;
        }

    }

    @Transactional(readOnly = true)
    public List<FKeyTypeDto> findAllByFkeys(List<String> fkeys) {
        String fkeyList = "'" + String.join("','", fkeys) + "'";
        String strSQL = "select key,type from t_token_meta_fkey_type" +
                " WHERE key IN (" + fkeyList + ")";
        return jdbcTemplate.query(strSQL, new FKeyTypeDtoRowMapper());
    }


    /**
     * 根据主数据的metaId修改视图的metaId下的指定Fkey的caption
     *
     * @param masterMetaId 主数据的metaId
     * @param fkey         Fkey
     * @param caption      Caption
     * @return 更新条数
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public int updateMetaFkeyCaption(String masterMetaId ,String fkey ,String caption) {
        try {
        String sql = "UPDATE t_token_meta_fkey SET caption = ? WHERE meta_id IN (SELECT view_meta_id FROM r_master_metaid_view_metaid WHERE master_meta_id = ? ) AND key = ?";

        return jdbcTemplate.update(sql ,caption, masterMetaId , fkey);
        } catch (EmptyResultDataAccessException e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 查找所有Fkey
     *
     * @return meta中的key定义
     */
    @Transactional(readOnly = true)
    public CurrentPage<FKeyTypeDto> findAllFkeys(int pageNo, int pageSize) throws DataAccessException {
        PaginationHelper<FKeyTypeDto> ph = new PaginationHelper<>();
        String strSQL = "select key,type from t_token_meta_fkey_type";
        String countSql = "select COUNT(1) from t_token_meta_fkey_type ";
        return ph.fetchPage(jdbcTemplate, countSql, strSQL, new Object[]{}, // 参数
                pageNo, pageSize, new FKeyTypeDtoRowMapper());
    }

    /**
     * 根据type查询Fkey
     *
     * @return meta中的key定义
     */
    @Transactional(readOnly = true)
    public CurrentPage<FKeyTypeDto> findByType(String type, int pageNo, int pageSize) throws DataAccessException {
        PaginationHelper<FKeyTypeDto> ph = new PaginationHelper<>();
        String strSQL = "select key,type from t_token_meta_fkey_type where type=? ";
        String countSql = "select COUNT(1) from t_token_meta_fkey_type where type=? ";
        return ph.fetchPage(jdbcTemplate, countSql, strSQL, new Object[]{type}, // 参数
                pageNo, pageSize, new FKeyTypeDtoRowMapper());
    }


}


class FKeyTypeDtoRowMapper implements RowMapper<FKeyTypeDto> {
    @Override
    public FKeyTypeDto mapRow(@SuppressWarnings("NullableProblems") ResultSet rs, int rowNum) throws SQLException {
        return new FKeyTypeDto(
                rs.getString("key")
                , FkeyTypeEnum.valueOf(rs.getString("type"))
        );
    }
}
