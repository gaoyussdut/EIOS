package top.toptimus.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import top.toptimus.formula.PredefinedFormationDTO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by JiangHao on 2018/9/10.
 */
@Repository
public class PredefinedFormationRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 获取全部预定义好的公式
     *
     * @return
     */
    public List<PredefinedFormationDTO> findAll() {
        String sql = "SELECT id,formula,describe,formula_type FROM t_predefined_formation";
        return jdbcTemplate.query(sql, new PredefinedFormationRowsMapper());
    }


    class PredefinedFormationRowsMapper implements RowMapper<PredefinedFormationDTO> {
        @Override
        public PredefinedFormationDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new PredefinedFormationDTO(
                    rs.getInt("id")
                    , rs.getString("formula")
                    , rs.getString("describe")
                    , rs.getString("formula_type")
            );
        }
    }


}
