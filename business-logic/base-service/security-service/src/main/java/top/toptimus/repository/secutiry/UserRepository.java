package top.toptimus.repository.secutiry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import top.toptimus.user.UserDTO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by JiangHao on 2018/12/13.
 */
@Repository
public class UserRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 根据账户查找用户信息
     *
     * @param account 账户
     * @return 用户信息
     */
    public UserDTO findByAccount(String account) {
        String sql = "SELECT u.id,u.account,u.password_digest,u.enabled,u.name,u.parent_id,u.created_at,u.updated_at,rru.role_id,u.organization_id FROM users u " +
                "LEFT JOIN r_roles_users rru ON rru.user_id = u.id WHERE u.account = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{account}, new UserMapper());
    }

    /**
     * 根据用户id查询用户信息
     *
     * @param userId 用户id
     * @return 用户信息
     */
    public UserDTO findByUserId(String userId) {
        String sql = "SELECT u.id,u.account,u.password_digest,u.enabled,u.name,u.parent_id,u.created_at,u.updated_at,rru.role_id,u.organization_id FROM users u " +
                "LEFT JOIN r_roles_users rru ON rru.user_id = u.id WHERE u.id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{userId}, new UserMapper());
    }

    /**
     * 根据用户id查询其和下属用户信息(PostgreSQL递归)
     *
     * @param userId 用户id
     * @return 下属用户信息
     */
    public List<UserDTO> findSubordinateByUserId(String userId) {
        String sql = "WITH RECURSIVE r AS (" +
                "  SELECT *,'' AS role_id  FROM users WHERE id =  ? " +
                "  union   ALL " +
                "  SELECT users.*,'' AS role_id  FROM users, r WHERE users.parent_id = r.id  )" +
                " SELECT * FROM r ORDER BY id;";
        return jdbcTemplate.query(sql, new Object[]{userId}, new UserMapper());
    }

    /**
     * 根据用户id 查询用户的基础信息
     *
     * @param userId 用户id
     * @return 用户信息
     */
    public UserDTO findUserBaseInfoById(String userId) {
        String sql = "SELECT id,account,enabled,name,created_at,updated_at,telephone_number,email,gender,address,age FROM users "
                + "WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{userId}, new UserInfoMapper());
    }

    /**
     * 注册新用户
     *
     * @param user 用户信息
     */
    public void saveUserInfo(UserDTO user) {
        String sql = "INSERT INTO users(id,account,password_digest,enabled,name,parent_id,created_at,updated_at) VALUES (?,?,?,?,?,?,?,?)";
        jdbcTemplate.update(sql,
                user.getId(),
                user.getAccount(),
                user.getPasswordDigest(),
                user.getEnabled(),
                user.getName(),
                user.getParentId(),
                user.getCreatedAt(),
                user.getUpdatedAt());
    }

    /**
     * 修改密码
     *
     * @param user 用户信息
     */
    public void updateUserPassword(UserDTO user) {
        String sql = "UPDATE users set password = ? WHERE account = ? ";
        jdbcTemplate.update(sql, user.getPasswordDigest(), user.getAccount());
    }


    @SuppressWarnings("NullableProblems")
    class UserMapper implements RowMapper<UserDTO> {
        @Override
        public UserDTO mapRow(ResultSet rs, int i) throws SQLException {
            return new UserDTO(
                    rs.getString("id")
                    , rs.getString("account")
                    , rs.getString("password_digest")
                    , rs.getBoolean("enabled")
                    , rs.getString("name")
                    , rs.getTimestamp("created_at")
                    , rs.getTimestamp("updated_at")
                    , rs.getString("role_id")
                    , rs.getString("organization_id")
                    , rs.getString("parent_id")
                    , new ArrayList<>()
            );
        }
    }

    @SuppressWarnings("NullableProblems")
    class UserSubRowMapper implements RowMapper<UserDTO> {
        @Override
        public UserDTO mapRow(ResultSet rs, int i) throws SQLException {
            return new UserDTO(
                    rs.getString("id")
                    , rs.getString("account")
                    , rs.getString("password_digest")
                    , rs.getBoolean("enabled")
                    , rs.getString("name")
                    , rs.getTimestamp("created_at")
                    , rs.getTimestamp("updated_at")
                    , rs.getString("role_id")
                    , rs.getString("organization_id")
                    , rs.getString("parent_id")
                    , new ArrayList<>()
            );
        }
    }

    class UserInfoMapper implements RowMapper<UserDTO> {
        @Override
        public UserDTO mapRow(ResultSet rs, int i) throws SQLException {
            return new UserDTO(
                    rs.getString("id")
                    , rs.getString("account")
                    , rs.getBoolean("enabled")
                    , rs.getString("name")
                    , rs.getTimestamp("created_at")
                    , rs.getTimestamp("updated_at")
                    , rs.getString("address")
                    , rs.getString("email")
                    , rs.getInt("age")
                    , rs.getString("gender")
                    , rs.getLong("telephone_number")
            );
        }
    }


}
