package top.toptimus.repository.secutiry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public class AuthRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;


    /**
     * 判定用户Id的UserTask的权限
     *
     * @param userId     user id
     * @param userTaskId user task id
     * @return 判断是否有权限
     */
    @Transactional
    public boolean checkUserTaskIdAuth(String userId, String userTaskId) {
        String sql = "SELECT id FROM r_roles_usertask WHERE user_task_id = ?" +
                " AND role_id IN ( SELECT role_id  FROM r_roles_users WHERE user_id = ? )";
        List<String> ids = jdbcTemplate.queryForList(sql, new Object[]{userTaskId, Integer.valueOf(userId)}, String.class);
        return ids.size() > 0;
    }

}
