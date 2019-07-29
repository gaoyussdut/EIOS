package top.toptimus.repository.meta;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import top.toptimus.meta.RWpermissionDto;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class RWpermissionRepositroy {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Transactional(propagation = Propagation.REQUIRED)
    public void save(RWpermissionDto rWpermissionDto) {
        String sql = "INSERT INTO t_token_meta_permissions(token_meta_id,key,visible,readonly,required,validation)"
                + "VALUES('" + rWpermissionDto.getTokenMetaId() + "','"
                + rWpermissionDto.getKey() + "',"
                + rWpermissionDto.getVisible() + ","
                + rWpermissionDto.getReadonly() + ","
                + rWpermissionDto.getRequired() + ","
                + rWpermissionDto.getValidation() + ")";
        try {
            jdbcTemplate.execute(sql);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("保存RWpermissionDao失败");
        }
    }


    @Transactional(propagation = Propagation.REQUIRED)
    public void saveAll(List<RWpermissionDto> rWpermissionDtos) {
        try {
            final List<RWpermissionDto> rWpermissionDtoList = rWpermissionDtos;
            String sql = "insert into t_token_meta_permissions(token_meta_id,key,visible,readonly,required,validation)" +
                    " values(?,?,?,?,?,?)";

            jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
                @Override
                public int getBatchSize() {
                    return rWpermissionDtoList.size();
                }

                @Override
                public void setValues(@SuppressWarnings("NullableProblems") PreparedStatement ps, int i) throws SQLException {
                    ps.setString(1, rWpermissionDtoList.get(i).getTokenMetaId());
                    ps.setString(2, rWpermissionDtoList.get(i).getKey());
                    ps.setBoolean(3, rWpermissionDtoList.get(i).getVisible());
                    ps.setBoolean(4, rWpermissionDtoList.get(i).getReadonly());
                    ps.setBoolean(5, rWpermissionDtoList.get(i).getRequired());
                    ps.setString(6, rWpermissionDtoList.get(i).getValidation());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("保存RWpermissionDao失败");
        }


    }

    @Transactional(readOnly = true)
    public List<RWpermissionDto> findAllByTokenMetaId(String tokenMetaId) {
        String sql = "SELECT id,token_meta_id,key,visible,readonly,required,validation from t_token_meta_permissions WHERE token_meta_id = '"
                + tokenMetaId + "'";
        return jdbcTemplate.query(sql, new RWpermissionDaoRowMapper());

    }

    /**
     * 根据metaId删除数据
     *
     * @param metaId meta id
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteByMetaId(String metaId) {
        String sql = "delete from t_token_meta_permissions where token_meta_id=?";
        jdbcTemplate.update(sql, metaId);
    }
}

@SuppressWarnings("NullableProblems")
class RWpermissionDaoRowMapper implements RowMapper<RWpermissionDto> {

    @Override
    public RWpermissionDto mapRow(ResultSet rs, int rowNumber) throws SQLException {
        return new RWpermissionDto(
                rs.getInt("id")
                , rs.getString("token_meta_id")
                , rs.getString("key")
                , rs.getBoolean("visible")
                , rs.getBoolean("readonly")
                , rs.getBoolean("required")
                , rs.getString("validation")
        );
    }
}
