package top.toptimus.repository.meta;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import top.toptimus.meta.RalValueDto;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class RalvalueRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Transactional(propagation = Propagation.REQUIRED)
    public void save(RalValueDto ralValueDto) {
        String sql = "INSERT INTO t_token_meta_ralvalue(token_meta_id,key,meta_name,fkey,meta_key)"
                + "VALUES('" + ralValueDto.getTokenMetaId() + "','"
                + ralValueDto.getKey() + "','"
                + ralValueDto.getMetaName() + "','"
                + ralValueDto.getFkey() + "','"
                + ralValueDto.getMetaKey() + "')";
        try {
            jdbcTemplate.execute(sql);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("保存RalValueDao失败");
        }
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void saveAll(List<RalValueDto> ralValueDtos) {
        try {
            final List<RalValueDto> ralValueDtoList = ralValueDtos;
            String sql = "insert into t_token_meta_ralvalue(token_meta_id,key,meta_name,fkey,meta_key)" +
                    " values(?,?,?,?,?)";
            jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
                @Override
                public int getBatchSize() {
                    return ralValueDtoList.size();
                }

                @SuppressWarnings("NullableProblems")
                @Override
                public void setValues(PreparedStatement ps, int i) throws SQLException {

                    ps.setString(1, ralValueDtoList.get(i).getTokenMetaId());
                    ps.setString(2, ralValueDtoList.get(i).getKey());
                    ps.setString(3, ralValueDtoList.get(i).getMetaName());
                    ps.setString(4, ralValueDtoList.get(i).getFkey());
                    ps.setString(5, ralValueDtoList.get(i).getMetaKey());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("保存RalValueDao失败");
        }

    }

    @Transactional(readOnly = true)
    public List<RalValueDto> findAllByTokenMetaId(String tokenMetaId) {
        String sql = "SELECT id,token_meta_id,key,meta_name,fkey,meta_key from t_token_meta_ralvalue WHERE token_meta_id = '"
                + tokenMetaId + "'";
        return jdbcTemplate.query(sql, new RalValueDaoRowMapper());
    }

    /**
     * 根据metaId删除数据
     *
     * @param metaId meta id
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteByMetaId(String metaId) {
        String sql = "delete from t_token_meta_ralvalue where token_meta_id=?";
        jdbcTemplate.update(sql, metaId);
    }


}

class RalValueDaoRowMapper implements RowMapper<RalValueDto> {

    @Override
    public RalValueDto mapRow(@SuppressWarnings("NullableProblems") ResultSet rs, int rowNumber) throws SQLException {
        return new RalValueDto(
                rs.getInt("id")
                , rs.getString("token_meta_id")
                , rs.getString("key")
                , rs.getString("meta_name")
                , rs.getString("fkey")
                , rs.getString("meta_key")
        );
    }
}
