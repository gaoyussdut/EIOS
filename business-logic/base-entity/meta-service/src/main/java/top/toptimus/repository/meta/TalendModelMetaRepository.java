package top.toptimus.repository.meta;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import top.toptimus.meta.TalendModelMetaDto;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by JiangHao on 2018/8/7.
 */
@Repository
public class TalendModelMetaRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 根据x_pk_x_talend_id 查询所有的metaJson
     *
     * @param x_pk_x_talend_id x_pk_x_talend_id
     * @return List<TalendModelMeta>
     */
    public List<TalendModelMetaDto> findAllByXPkXTalendId(String x_pk_x_talend_id) {
        String sql = "SELECT id,x_pk_x_talend_id,meta_id,meta_json FROM t_talend_model_meta where x_pk_x_talend_id = ?";
        return jdbcTemplate.query(sql, new TalendModelMetaDtoRowMapper(), x_pk_x_talend_id);
    }

    /**
     * 保存全部TalendModelMeta
     *
     * @param talendModelMetaDtos talendModelMetas
     */
    public void saveAll(List<TalendModelMetaDto> talendModelMetaDtos) {
        try {
            String sql = "insert into t_talend_model_meta(x_pk_x_talend_id,meta_id,meta_json)" +
                    " values(?,?,?)";
            jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
                @Override
                public int getBatchSize() {
                    return talendModelMetaDtos.size();
                }

                @SuppressWarnings("NullableProblems")
                @Override
                public void setValues(PreparedStatement ps, int i) throws SQLException {
                    ps.setString(1, talendModelMetaDtos.get(i).getTalendId());
                    ps.setString(2, talendModelMetaDtos.get(i).getMetaId());
                    ps.setString(3, talendModelMetaDtos.get(i).getMetaJson());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("保存TalendModelMeta失败");
        }
    }


    /**
     * 根据talendId 删除数据
     *
     * @param talendId talend id
     */
    public void delectAllByTalendId(String talendId) {
        String sql = "DELETE FROM t_talend_model_meta where x_pk_x_talend_id = ?";
        jdbcTemplate.update(sql, talendId);
    }


}

class TalendModelMetaDtoRowMapper implements RowMapper<TalendModelMetaDto> {
    @Override
    public TalendModelMetaDto mapRow(@SuppressWarnings("NullableProblems") ResultSet rs, int rowNum) throws SQLException {
        return new TalendModelMetaDto(
                rs.getInt("id")
                , rs.getString("x_pk_x_talend_id")
                , rs.getString("meta_id")
                , rs.getString("meta_json")
                , null
        );
    }
}


