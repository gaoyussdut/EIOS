package top.toptimus.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import top.toptimus.targetdto.TargetTypeDTO;
import top.toptimus.targetdto.TargetTypePageableDTO;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by lzs on 2019/2/19.
 */
@Repository
public class TargetTypeRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public TargetTypePageableDTO getTargetTypeGeneralView(Integer pageSize, Integer pageNo) {
        String sql = "SELECT mubiaobianma,beizhu,mubiaoleixing,ou_id FROM t_target_type LIMIT "
                + pageSize + " OFFSET " + (pageNo - 1) * pageSize + "";
        String countSql = "SELECT COUNT(mubiaobianma) FROM t_target_type ";

        TargetTypePageableDTO targetTypePageableDTO = new TargetTypePageableDTO(pageSize, pageNo);
        targetTypePageableDTO.build(jdbcTemplate.queryForObject(countSql, Integer.class));
        targetTypePageableDTO.build(jdbcTemplate.query(sql, new TargetTypeDTOMapper()));
        return targetTypePageableDTO;
    }

    public TargetTypeDTO getTargetTypeDetail(String targetTypeId) {
        try {
            String sql = "SELECT mubiaobianma,beizhu,mubiaoleixing,ou_id FROM t_target_type WHERE mubiaobianma = '" + targetTypeId + "'";
            return jdbcTemplate.queryForObject(sql, new TargetTypeDTOMapper());
        } catch (Exception e) {
            return new TargetTypeDTO();
        }

    }

    public void saveTargetType(TargetTypeDTO targetTypeDTO) {
        String sql = "INSERT INTO t_target_type (mubiaobianma,beizhu,mubiaoleixing,ou_id) VALUES('" + targetTypeDTO.getTargetTypeId()
                + "','" + targetTypeDTO.getRemark() + "','" + targetTypeDTO.getTargetType() + "','" + targetTypeDTO.getOuId() + "')";
        jdbcTemplate.execute(sql);
    }

    public void updateTargetType(TargetTypeDTO targetTypeDTO) {
        String sql = "UPDATE t_target_type" +
                " SET mubiaoleixing = '" + targetTypeDTO.getTargetType() + "'," +
                " beizhu = '" + targetTypeDTO.getRemark() + "'," +
                " ou_id = '" + targetTypeDTO.getOuId() + "'" +
                " WHERE mubiaobianma = '" + targetTypeDTO.getTargetTypeId() + "'";
        jdbcTemplate.execute(sql);
    }

    public void deleteTargetType(String targetTypeId) {
        String sql = "DELETE FROM t_target_type WHERE mubiaobianma = '" + targetTypeId + "'";
        jdbcTemplate.execute(sql);
    }

    class TargetTypeDTOMapper implements RowMapper<TargetTypeDTO> {
        @SuppressWarnings("NullableProblems")
        @Override
        public TargetTypeDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new TargetTypeDTO(
                    rs.getString("mubiaobianma")
                    , rs.getString("mubiaoleixing")
                    , rs.getString("beizhu")
                    , rs.getString("ou_id")
            );
        }
    }
}
