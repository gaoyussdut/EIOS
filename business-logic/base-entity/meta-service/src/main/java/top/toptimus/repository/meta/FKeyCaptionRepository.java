package top.toptimus.repository.meta;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import top.toptimus.meta.FKeyCaptionDto;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by JiangHao on 2018/8/7.
 */
@Repository
public class FKeyCaptionRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 查找全部的key的默认caption
     *
     * @return List<FKeyCaptionDao>
     */
    public List<FKeyCaptionDto> findAll() {
        String sql = "SELECT key,caption from t_token_meta_fkey_caption";
        return jdbcTemplate.query(sql, new FKeyCaptionDtoRowMapper());
    }

    /**
     * 根据key查找对应的默认caption
     *
     * @return List<FKeyCaptionDao>
     */
    public List<FKeyCaptionDto> findAllByKey(List<String> keys) {
        StringBuilder sb = new StringBuilder();
        for (String key : keys) {
            sb.append("'").append(key).append("'").append(",");
        }
        String sql = "SELECT key,caption from t_token_meta_fkey_caption where  key  in ("
                + sb.toString().substring(0, sb.length() - 1) + ")";
        return jdbcTemplate.query(sql, new FKeyCaptionDtoRowMapper());
    }


}

class FKeyCaptionDtoRowMapper implements RowMapper<FKeyCaptionDto> {
    @SuppressWarnings("NullableProblems")
    @Override
    public FKeyCaptionDto mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new FKeyCaptionDto(
                rs.getString("key")
                , rs.getString("caption")
        );
    }
}
