package top.toptimus.repository.secutiry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import top.toptimus.user.RoleDTO;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class RoleRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Transactional(propagation = Propagation.REQUIRED)
    public void saveAll(List<RoleDTO> roles) {
        try {
            String sql = "insert into roles(name,description,enabled,created_at,updated_at) "
                    + " values(?,?,?,?,?)";
            jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
                @Override
                public int getBatchSize() {
                    return roles.size();
                }

                @SuppressWarnings("NullableProblems")
                @Override
                public void setValues(PreparedStatement ps, int i) throws SQLException {
                    ps.setString(1, roles.get(i).getName());
                    ps.setString(2, roles.get(i).getDescription());
                    ps.setBoolean(3, roles.get(i).isEnabled());
                    ps.setTimestamp(4, roles.get(i).getCreated_at());
                    ps.setTimestamp(5, roles.get(i).getUpdated_at());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("保存UserTaskInsDao失败");
        }
    }

    /**
     * 根据用户id查询角色
     *
     * @param userId 用户id
     * @return 角色
     */
    public List<RoleDTO> findRolesByUserId(String userId) {
        String sql = "SELECT rs.id,rs.name,rs.description,rs.enabled,rs.created_at,rs.updated_at FROM r_roles_users ru " +
                "LEFT JOIN roles rs on ru.role_id = rs.id WHERE ru.user_id = ?";
        return jdbcTemplate.query(sql, new Object[]{userId}, new RoleMapper());
    }

    /**
     * 查询所有可用角色
     *
     * @return 角色
     */
    public List<RoleDTO> findAllRole(){
        String sql = "SELECT rs.id,rs.name,rs.description,rs.enabled,rs.created_at,rs.updated_at FROM roles rs WHERE enabled = true";
        return jdbcTemplate.query(sql, new RoleMapper());
    }

}

@SuppressWarnings("NullableProblems")
class RoleMapper implements RowMapper<RoleDTO> {
    @Override
    public RoleDTO mapRow(ResultSet rs, int i) throws SQLException {
        return new RoleDTO(
                rs.getString("id")
                , rs.getString("name")
                , rs.getString("description")
                , rs.getBoolean("enabled")
                , rs.getTimestamp("created_at")
                , rs.getTimestamp("updated_at")
        );
    }
}