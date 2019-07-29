package top.toptimus.repository.meta;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import top.toptimus.meta.FKeyDto;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * meta的fkey保存在t_token_meta_fkey中
 * 对t_token_meta_fkey表的保存、查询
 *
 * @author JiangHao
 * @since 2018/5/22.
 */
@Repository
public class FKeyRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 保存fkey到t_token_meta_fkey中
     *
     * @param fKeyDto fkey信息
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void save(FKeyDto fKeyDto) {
        String sql = "INSERT INTO t_token_meta_fkey(meta_id,key,caption,fkey_type) " +
                "VALUES ('" + fKeyDto.getMetaId() + "','"
                + fKeyDto.getKey() + "','"
                + fKeyDto.getCaption() + "','"
                + fKeyDto.getFkeyType() + "')";
        try {
            jdbcTemplate.execute(sql);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("保存FKeyDao失败");
        }

    }

    /**
     * 保存多条fkey到t_token_meta_fkey中
     *
     * @param fKeyDtos fkey信息
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void saveAll(List<FKeyDto> fKeyDtos) {
        try {
            final List<FKeyDto> fKeyDaoList = fKeyDtos;
            String sql = "insert into t_token_meta_fkey(meta_id,key,caption,fkey_type)" +
                    " values(?,?,?,?)";

            jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
                @Override
                public int getBatchSize() {
                    return fKeyDaoList.size();
                }

                @Override
                public void setValues(@SuppressWarnings("NullableProblems") PreparedStatement ps, int i) throws SQLException {
                    ps.setString(1, fKeyDaoList.get(i).getMetaId());
                    ps.setString(2, fKeyDaoList.get(i).getKey());
                    ps.setString(3, fKeyDaoList.get(i).getCaption());
                    ps.setString(4, fKeyDaoList.get(i).getFkeyType());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("保存FKeyDao失败");
        }

    }

    /**
     * 根据meta id查询fkey信息
     *
     * @param metaId meta id
     * @return fkey信息
     */
    @Transactional(readOnly = true)
    public List<FKeyDto> findByMetaId(String metaId) {
        String sql = "SELECT id,meta_id,key,caption,fkey_type from t_token_meta_fkey WHERE meta_id = '"
                + metaId + "'  order by id";
        return jdbcTemplate.query(sql, new FKeyDtoRowMapper());
    }

    /**
     * 根据fkey字段列表查询fkey信息
     *
     * @param fkeys fkey字段列表
     * @return fkey信息
     */
    @Transactional(readOnly = true)
    public List<FKeyDto> findByFkeys(List<String> fkeys) {
        String fkeyList = "'" + String.join("','", fkeys) + "'";
        String strSQL = "select id,meta_id,key,caption,fkey_type from t_token_meta_fkey" +
                " WHERE key IN (" + fkeyList + ")  order by id";

        return jdbcTemplate.query(strSQL, new FKeyDtoRowMapper());  //  FKey列表
    }

    /**
     * 根据metaId删除数据
     *
     * @param metaId meta id
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteByMetaId(String metaId) {
        String sql = "delete from t_token_meta_fkey where meta_id=?";
        jdbcTemplate.update(sql, metaId);
    }

    /**
     * 根据fkey查看Constant
     *
     * @param fkey fkey
     * @return String 常量
     */
    @Transactional(readOnly = true)
    public String getConstantByKey(String fkey) {
        try {
            String sql = "SELECT name from constant where fkey=? AND enabled=TRUE ";
            return jdbcTemplate.queryForObject(sql,new Object[]{fkey},String.class);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }


}

class FKeyDtoRowMapper implements RowMapper<FKeyDto> {

    @Override
    public FKeyDto mapRow(@SuppressWarnings("NullableProblems") ResultSet rs, int rowNum) throws SQLException {
        return new FKeyDto(
                rs.getInt("id")
                , rs.getString("meta_id")
                , rs.getString("key")
                , rs.getString("caption")
                , rs.getString("fkey_type")
        );
    }

}