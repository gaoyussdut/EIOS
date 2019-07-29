package top.toptimus.repository.processDefinition;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import top.toptimus.exception.TopErrorCode;
import top.toptimus.exception.TopException;
import top.toptimus.processDefinition.UserTaskEdgeDto;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * 节点弧定义
 */
@SuppressWarnings("ALL")
@Repository
public class UserTaskEdgeRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Transactional(propagation = Propagation.REQUIRED)
    public void saveAll(List<UserTaskEdgeDto> userTaskEdgeDaos) {
        try {
            String sql = "insert into t_user_task_edge(process_id,user_task_id,incoming,outgoing,type) "
                    + " values(?,?,?,?,?)";
            jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
                @Override
                public int getBatchSize() {
                    return userTaskEdgeDaos.size();
                }

                @Override
                public void setValues(@SuppressWarnings("NullableProblems") PreparedStatement ps, int i)
                        throws SQLException {
                    ps.setString(1, userTaskEdgeDaos.get(i).getProcessId());
                    ps.setString(2, userTaskEdgeDaos.get(i).getUserTaskId());
                    ps.setString(3, userTaskEdgeDaos.get(i).getIncoming());
                    ps.setString(4, userTaskEdgeDaos.get(i).getOutgoing());
                    ps.setString(5, userTaskEdgeDaos.get(i).getType());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            throw new TopException(TopErrorCode.PROC_SAVE_NODE_EDGE_INFO_ERR);
        }
    }

    /**
     * 查找当前节点的下个节点
     *
     * @param userTaskId user task id
     * @return 下个节点
     */
    @Transactional(readOnly = true)
    public List<UserTaskEdgeDto> findNextUserTask(String userTaskId) {
        try {
            String sql = "SELECT DISTINCT next.user_task_id,next.type,tut.meta_id,tut.user_task_name "
                    + " FROM t_user_task_edge next FULL JOIN t_user_task_edge now  ON next.incoming = now.outgoing "
                    + " LEFT JOIN t_user_task tut ON tut.user_task_id = next.user_task_id "
                    + " WHERE now.user_task_id = ? AND  now.outgoing !='' ";
            return jdbcTemplate.query(sql, new Object[]{userTaskId}, new UserTaskEdgeDtoRowMapper());
        } catch (EmptyResultDataAccessException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 根据ProcessId删除数据
     *
     * @param processId 流程id
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteByProcessId(String processId) {
        try {
            String sql = "delete from t_user_task_edge where process_id=?";
            jdbcTemplate.update(sql, processId);
        } catch (Exception e) {
            e.printStackTrace();
            throw new TopException(TopErrorCode.PROC_DELETE_NODE_EDGE_INFO_ERR);
        }
    }

}

class UserTaskEdgeDtoRowMapper implements RowMapper<UserTaskEdgeDto> {
    @Override
    public UserTaskEdgeDto mapRow(@SuppressWarnings("NullableProblems") ResultSet rs, int rowNum) throws SQLException {
        return new UserTaskEdgeDto(rs.getString("user_task_id"), rs.getString("meta_id"),
                rs.getString("user_task_name"), rs.getString("type"));
    }
}
