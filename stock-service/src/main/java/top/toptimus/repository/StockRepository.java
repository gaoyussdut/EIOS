package top.toptimus.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import top.toptimus.processDefinition.UserTaskInsDTO;
import top.toptimus.rule.BusinessCodeDTO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 库存repo
 */
@Repository
public class StockRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 获取规则属性信息
     *
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateStatus(UserTaskInsDTO dao) {
        String sqlStr = "update t_user_task_ins set  status=? " +
                " WHERE token_id=? AND user_task_id=? ";
        try {
            jdbcTemplate.update(sqlStr, dao.getStatus(), dao.getTokenId(), dao.getUserTaskId());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("更新UserTaskInsDao失败！");
        }
    }
}
