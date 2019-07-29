package top.toptimus.repository.processDefinition;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import top.toptimus.common.enums.process.VertexTypeEnum;
import top.toptimus.exception.TopErrorCode;
import top.toptimus.exception.TopException;
import top.toptimus.processDefinition.UserTaskDto;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * 节点定义repo
 */
@SuppressWarnings("NullableProblems")
@Repository
public class UserTaskRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;


    @Transactional(propagation = Propagation.REQUIRED)
    public void saveAll(List<UserTaskDto> userTaskDaos) {
        try {
            String sql = "insert into t_user_task(process_id,user_task_id,user_task_name,meta_id,status) "
                    + " values(?,?,?,?,?)";
            jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
                @Override
                public int getBatchSize() {
                    return userTaskDaos.size();
                }

                @Override
                public void setValues(PreparedStatement ps, int i) throws SQLException {
                    ps.setString(1, userTaskDaos.get(i).getProcessId());
                    ps.setString(2, userTaskDaos.get(i).getUserTaskId());
                    ps.setString(3, userTaskDaos.get(i).getUserTaskName());
                    ps.setString(4, userTaskDaos.get(i).getMetaId());
                    ps.setString(5, userTaskDaos.get(i).getStatus());
                }
            });
        } catch (Exception e) {
            throw new TopException(TopErrorCode.PROC_SAVE_NODE_INFO_ERR);
        }
    }

    /**
     * 通过UserTaskId查找UserTask
     *
     * @param userTaskId user task id
     * @return 节点定义
     */
    @Transactional(readOnly = true)
    public UserTaskDto findByUserTaskId(String userTaskId) {
        try {
            String sql = "select id,process_id,user_task_id,user_task_name,meta_id,status from t_user_task where user_task_id= ? ";
            return jdbcTemplate.queryForObject(sql, new Object[]{userTaskId}, new UserTaskInfoRowMapper());
        } catch (EmptyResultDataAccessException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 根据流程Id和type查找UserTaskDao
     *
     * @param processId      流程id
     * @param vertexTypeEnum 流程的类别
     * @return 节点定义列表
     */
    @Transactional(readOnly = true)
    public List<UserTaskDto> findByProcessIdAndType(String processId, VertexTypeEnum vertexTypeEnum) {
        try {
            String sql = "SELECT id,process_id,user_task_id,user_task_name,meta_id,status FROM t_user_task " +
                    " WHERE user_task_id IN (SELECT user_task_id FROM t_user_task_edge WHERE process_id = ? AND type= ? )" +
                    " AND process_id=? "; // 确保一个流程中唯一
            return jdbcTemplate.query(sql, new Object[]{processId, vertexTypeEnum.name(), processId}, new UserTaskInfoRowMapper());
        } catch (EmptyResultDataAccessException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 通过单独节点查找所有UserTask（其中含有PO信息）
     *
     * @param userTaskId user task id
     * @return 节点定义列表
     */
    @Transactional(readOnly = true)
    public List<UserTaskDto> findAllByUserTaskId(String userTaskId) {
        try {
            String sql = "SELECT tut.id,tut.process_id,tut.user_task_id,tut.user_task_name,tut.meta_id,tut.status FROM  t_user_task tut "
                    + "WHERE tut.process_id IN (SELECT process_id FROM t_user_task WHERE user_task_id = ?);";
            return jdbcTemplate.query(sql, new Object[]{userTaskId}, new UserTaskInfoRowMapper());
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
            String sql = "delete from t_user_task where process_id=?";
            jdbcTemplate.update(sql, processId);
        } catch (Exception e) {
            e.printStackTrace();
            throw new TopException(TopErrorCode.PROC_DELETE_NODE_INFO_ERR);
        }
    }

}

@SuppressWarnings("NullableProblems")
class UserTaskInfoRowMapper implements RowMapper<UserTaskDto> {
    @Override
    public UserTaskDto mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new UserTaskDto(
                rs.getString("id")
                , rs.getString("process_id")
                , rs.getString("user_task_id")
                , rs.getString("user_task_name")
                , rs.getString("meta_id")
                , rs.getString("status")
        );
    }
}

