package top.toptimus.repository.secutiry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import top.toptimus.user.OrganizationDTO;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by JiangHao on 2018/12/26.
 */
@Repository
public class OrganizationRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 根据组织id查询组织信息
     *
     * @param orgId 组织id
     * @return 组织信息
     */
    public OrganizationDTO findById(String orgId) {
        String sql = "SELECT id,organization_name,organization_description,self_id,parent_id FROM organization where id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{orgId}, new OrganizationRowMapper());
    }

    /**
     * 根据用户id查询组织信息
     *
     * @param userId 用户id
     * @return 组织信息
     */
    public OrganizationDTO findByUserId(String userId) {
        String sql = "SELECT org.id,org.organization_name,org.organization_description,org.self_id,org.parent_id FROM users u" +
                " LEFT JOIN organization org ON  u.organization_id = org.id where u.id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{userId}, new OrganizationRowMapper());
    }


    class OrganizationRowMapper implements RowMapper<OrganizationDTO> {
        @Override
        public OrganizationDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new OrganizationDTO(
                    rs.getString("id")
                    , rs.getString("organization_name")
                    , rs.getString("organization_description")
                    , rs.getInt("self_id")
                    , rs.getInt("parent_id")
            );
        }
    }
}
