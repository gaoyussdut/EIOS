package top.toptimus.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import top.toptimus.common.enums.taskEnum.TaskTypeEnum;
import top.toptimus.common.enums.taskEnum.status.TaskStatusEnum;
import top.toptimus.task.TaskDto;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 事务处理repo
 */
@Component
public class TaskRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 保存全部TaskDto
     *
     * @param taskDtoList taskDtoList
     */
    public void saveAll(List<TaskDto> taskDtoList) {
        try {
            String sql = "insert into t_task_ins(bill_meta_id,bill_token_id,task_id,task_name,parent_index,self_index,task_type,task_status,meta_id,process_id,is_current_task)" +
                    " values(?,?,?,?,?,?,?,?,?,?,?)";
            jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
                @Override
                public int getBatchSize() {
                    return taskDtoList.size();
                }

                @SuppressWarnings("NullableProblems")
                @Override
                public void setValues(PreparedStatement ps, int i) throws SQLException {
                    ps.setString(1, taskDtoList.get(i).getBillMetaId());
                    ps.setString(2, taskDtoList.get(i).getBillTokenId());
                    ps.setString(3, taskDtoList.get(i).getTaskId());
                    ps.setString(4, taskDtoList.get(i).getTaskName());
                    ps.setInt(5, taskDtoList.get(i).getParentIndex());
                    ps.setInt(6, taskDtoList.get(i).getSelfIndex());
                    ps.setString(7, taskDtoList.get(i).getTaskTypeEnum().name());
                    ps.setString(8, taskDtoList.get(i).getTaskStatusEnum().name());
                    ps.setString(9, taskDtoList.get(i).getMetaId());
                    ps.setString(10, taskDtoList.get(i).getProcessId());
                    ps.setBoolean(11, taskDtoList.get(i).isCurrentTask());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("保存TaskDto失败");
        }
    }

    /**
     * 保存任务
     *
     * @param taskDto 任务dto
     */
    public void save(TaskDto taskDto) {
        String sql = "INSERT INTO t_task_ins(bill_meta_id,bill_token_id,task_id,task_name,task_type,task_status,meta_id,process_id,is_current_task)"
                + "VALUES('" + taskDto.getBillMetaId() + "','"
                + taskDto.getBillTokenId() + "','"
                + taskDto.getTaskId() + "','"
                + taskDto.getTaskName() + "','"
                + taskDto.getTaskTypeEnum().name() + "','"
                + taskDto.getTaskStatusEnum().name() + "','"
                + taskDto.getMetaId() + "','"
                + taskDto.getProcessId() + "','"
                + taskDto.isCurrentTask() + "')";
        try {
            jdbcTemplate.execute(sql);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("保存TaskDto失败");
        }
    }

    /**
     * 根据事务码和metaid查找字段规则
     *
     * @param billMetaId 表头meta id
     * @return 任务dto列表
     */
    public List<TaskDto> findByBillMetaIdAndBillTokenId(String billMetaId, String billTokenId) {
        try {
            String sql = "SELECT bill_meta_id,bill_token_id,task_id,task_name,parent_index,self_index,task_type,task_status,meta_id,process_id,is_current_task " +
                    "from t_task_ins WHERE bill_meta_id = '" + billMetaId + "' AND bill_token_id = '" + billTokenId + "'";
            return jdbcTemplate.query(sql, new TaskDtoRowMapper());
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    /**
     * 根据事务码和metaid查找字段规则
     *
     * @param billMetaId 表头meta id
     * @return 任务dto
     */
    public TaskDto findCurrentTaskByBillMetaIdAndBillTokenId(String billMetaId, String billTokenId) {
        try {
            String sql = "SELECT bill_meta_id,bill_token_id,task_id,task_name,parent_index,self_index,task_type,task_status,meta_id,process_id,is_current_task " +
                    "from t_task_ins WHERE bill_meta_id = '" + billMetaId + "' AND bill_token_id = '" + billTokenId + "' AND is_current_task = true";
            return jdbcTemplate.queryForObject(sql, new TaskDtoRowMapper());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 根据事务码和metaid查找字段规则
     *
     * @param billMetaId  表头meta id
     * @param billTokenId 表头token id
     * @param taskId      task id
     * @return 任务dto
     */
    public TaskDto findByBillMetaIdAndBillTokenIdAndTaskId(String billMetaId, String billTokenId, String taskId) {
        String sql = "SELECT bill_meta_id,bill_token_id,task_id,task_name,parent_index,self_index,task_type,task_status,meta_id,process_id,is_current_task " +
                "from t_task_ins WHERE bill_meta_id = '" + billMetaId + "' AND bill_token_id = '" + billTokenId + "' AND task_id = '" + taskId + "'";
        return jdbcTemplate.queryForObject(sql, new TaskDtoRowMapper());
    }

    /**
     * 更新当前任务状态
     *
     * @param taskDto 任务dto
     */
    public void updateTask(TaskDto taskDto) {
        String sql = "UPDATE t_task_ins SET task_status = '" + taskDto.getTaskStatusEnum().name() + "'," +
                "is_current_task = " + taskDto.isCurrentTask() +
                " WHERE bill_token_id ='" + taskDto.getBillTokenId() + "' AND task_id = '" + taskDto.getTaskId() + "'";
        try {
            jdbcTemplate.execute(sql);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("更新TaskDto失败");
        }
    }

    /**
     * 更新下级任务为当前任务
     *
     * @param tokenId 下级任务单据id
     * @param taskId  task id
     */
    public void updateNextTask(String tokenId, String taskId) {
        String sql = "UPDATE t_task_ins SET is_current_task = true" +
                " WHERE bill_token_id ='" + tokenId + "' AND task_id = (SELECT DISTINCT tti2.task_id FROM t_task_ins tti " +
                "LEFT JOIN t_task_ins tti2 ON tti.self_index = tti2.parent_index WHERE tti.task_id = '" + taskId + "' AND tti.bill_token_id = '" + tokenId + "' AND tti2.bill_token_id = '" + tokenId + "')";
        try {
            jdbcTemplate.execute(sql);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("更新TaskDto失败");
        }
    }

    /**
     * 修改备查帐状态
     *
     * @param tableName      表名
     * @param billTokenId    表头token id
     * @param taskStatusEnum 任务状态枚举
     */
    public void updateMemorandvnStatus(String tableName, String billTokenId, TaskStatusEnum taskStatusEnum) {
        Date currentDate = new Date();
        Timestamp ts = new Timestamp(currentDate.getTime());
        String taskStatus = null;
        switch (taskStatusEnum) {
            case STATUS_DRAFT:
                taskStatus = "草稿";
                break;
            case STATUS_BILL_DONE:
                taskStatus = "单据完成";
                break;
            case STATUS_SIGN_START:
                taskStatus = "会签开始";
                break;
            case STATUS_SIGN_PASS:
                taskStatus = "会签通过";
                break;
            case STATUS_APPROVE_FAIL:
                taskStatus = "审批未通过";
                break;
            case STATUS_TRANSFORMATION_DONE:
                taskStatus = "单据转换成功";
                break;
            case STATUS_TRANSFORMATION_FAIL:
                taskStatus = "单据转换失败";
                break;
            case STATUS_APPROVING:
                taskStatus = "审批中";
                break;
            case STATUS_APPROVE_DONE:
                taskStatus = "审批通过";
                break;
            case STATUS_SIGN_FAIL:
                taskStatus = "会签失败";
                break;
            case STATUS_BILL_ABANDON:
                taskStatus = "单据作废";
                break;
        }
        String sql = "UPDATE " + tableName + " SET task_status = '" + taskStatus + "',update_date = '" + ts + "' WHERE id = '" + billTokenId + "'";
        try {
            jdbcTemplate.execute(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 修改备查帐时间
     *
     * @param tableName   表名
     * @param billTokenId 表头token id
     */
    public void updateMemorandvnDate(String tableName, String billTokenId) {
        Date currentDate = new Date();
        Timestamp ts = new Timestamp(currentDate.getTime());
        String sql = "UPDATE " + tableName + " SET end_date = '" + ts + "',update_date = '" + ts + "' WHERE id = '" + billTokenId + "'";
        try {
            jdbcTemplate.execute(sql);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("更新备查帐状态失败");
        }
    }

    /**
     * 判斷是否为任务尾节点
     *
     * @param taskDto 任务
     * @return 是否末节点
     */
    public boolean isFinalTask(TaskDto taskDto) {
        String sql = "SELECT count(DISTINCT tti2.task_id) FROM t_task_ins tti LEFT JOIN t_task_ins tti2 ON tti.self_index = tti2.parent_index WHERE tti.bill_token_id = '" + taskDto.getBillTokenId() + "' AND tti2.bill_token_id = '" + taskDto.getBillTokenId() + "' AND tti.task_id = '" + taskDto.getTaskId() + "'";
        try {
            return jdbcTemplate.queryForObject(sql, Integer.class).equals(0) ? true : false;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("判斷是否为任务尾节点错误");
        }
    }

    /**
     * 根据任务类型获取任务
     *
     * @param billMetaId
     * @param taskTypeEnum
     * @return
     */
    public List<TaskDto> findByBillMetaIdAndBillTokenIdAndTaskType(String billMetaId, String billTokenId, TaskTypeEnum taskTypeEnum) {
        try {
            String sql = "SELECT bill_meta_id,bill_token_id,task_id,task_name,parent_index,self_index,task_type,task_status,meta_id,process_id,is_current_task " +
                    "from t_task_ins WHERE bill_meta_id = '" + billMetaId + "' AND bill_token_id = '" + billTokenId + "' AND task_type = '" + taskTypeEnum.name() + "'";
            return jdbcTemplate.query(sql, new TaskDtoRowMapper());
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    class TaskDtoRowMapper implements RowMapper<TaskDto> {
        @SuppressWarnings("NullableProblems")
        @Override
        public TaskDto mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new TaskDto(rs.getString("bill_meta_id"), rs.getString("bill_token_id"), rs.getString("task_id")
                    , rs.getString("task_name"), rs.getInt("parent_index"), rs.getInt("self_index"), TaskTypeEnum.valueOf(rs.getString("task_type")), TaskStatusEnum.valueOf(rs.getString("task_status"))
                    , rs.getString("meta_id"), rs.getString("process_id"), rs.getBoolean("is_current_task"));
        }
    }
}
