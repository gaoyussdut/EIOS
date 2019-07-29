package top.toptimus.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import top.toptimus.common.enums.taskEnum.TaskTypeEnum;
import top.toptimus.task.TaskDefinitionDTO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
public class TaskDefinitionRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 根据事务码和metaid查找字段规则
     *
     * @param billMetaId
     * @return
     */
    public List<TaskDefinitionDTO> findByBillMetaId(String billMetaId) {
        String sql = "SELECT bill_meta_id,task_id,task_name,parent_index,self_index,task_type,meta_id,process_id " +
                "from t_bill_task_definition WHERE bill_meta_id = '" + billMetaId + "'";
        return jdbcTemplate.query(sql, new TaskDefinitionDTORowMapper());
    }

    /**
     * 获取下一节点TaskId
     *
     * @param billMetaId
     * @param taskId
     * @return
     */
    public String findNextTaskId(String billMetaId, String taskId) {
        String sql = "SELECT ttd2.task_id " +
                "from t_bill_task_definition ttd " +
                "LEFT JOIN t_bill_task_definition ttd2 ON ttd.self_index = ttd2.parent_index " +
                "WHERE ttd.bill_meta_id = '" + billMetaId + "' AND ttd2.bill_meta_id = '" + billMetaId + "' AND ttd.task_id = '" + taskId + "'";
        return jdbcTemplate.queryForObject(sql, String.class);
    }

    class TaskDefinitionDTORowMapper implements RowMapper<TaskDefinitionDTO> {
        @SuppressWarnings("NullableProblems")
        @Override
        public TaskDefinitionDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new TaskDefinitionDTO(rs.getString("bill_meta_id"), rs.getString("task_id")
                    , rs.getString("task_name"), rs.getInt("parent_index"), rs.getInt("self_index")
                    , TaskTypeEnum.valueOf(rs.getString("task_type")), rs.getString("meta_id"), rs.getString("process_id"));
        }
    }
}
