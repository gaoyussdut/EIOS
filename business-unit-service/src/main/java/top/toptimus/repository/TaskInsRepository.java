package top.toptimus.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import top.toptimus.businessUnit.TaskInsDTO;
import top.toptimus.common.enums.TaskStatusEnum;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lzs on 2019/4/14.
 */
@Repository
public class TaskInsRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;


    /**
     * 保存
     *
     * @param taskInsDTO
     */
    public void save(TaskInsDTO taskInsDTO) {
        String sql = "INSERT INTO t_task_ins(business_unit_code,process_ins_id,task_meta_id,task_token_id,status)"
                + "VALUES('" + taskInsDTO.getBusinessUnitCode() + "','"
                + taskInsDTO.getProcessInsId() + "','"
                + taskInsDTO.getTaskMetaId() + "','"
                + taskInsDTO.getTaskTokenId() + "','"
                + taskInsDTO.getStatus().name() + "')";
        try {
            jdbcTemplate.execute(sql);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("保存TaskInsDTO失败");
        }
    }

    /**
     * 根据业务单元和任务meta查找taskIns
     *
     * @param businessUnitCode
     * @param metaId
     * @return
     */
    public List<TaskInsDTO> findByTaskMetaId(String businessUnitCode, String metaId) {
        String sql = "SELECT business_unit_code,process_ins_id,task_meta_id,task_token_id,status FROM t_task_ins " +
                "WHERE business_unit_code = '" + businessUnitCode + "' AND task_meta_id = '" + metaId + "'";
        try {
            return jdbcTemplate.query(sql, new TaskInsDTOMapper());
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    /**
     * 根据任务meta和任务token获取taskIns
     *
     * @param businessUnitCode
     * @param metaId
     * @param tokenId
     * @return
     */
    public TaskInsDTO findByTaskMetaIdAndTaskTokenId(String businessUnitCode, String metaId, String tokenId) {
        String sql = "SELECT business_unit_code,process_ins_id,task_meta_id,task_token_id,status FROM t_task_ins " +
                "WHERE business_unit_code = '" + businessUnitCode + "' AND task_meta_id = '" + metaId + "' AND task_token_id = '" + tokenId + "'";
        try {
            return jdbcTemplate.queryForObject(sql, new TaskInsDTOMapper());
        } catch (Exception e) {
            return new TaskInsDTO();
        }
    }

    /**
     * 根据meta和processins获取taskins
     *
     * @param businessUnitCode
     * @param processInsId
     * @param metaId
     * @return
     */
    public List<TaskInsDTO> getTaskInsByTaskMetaIdAndProcessInsId(String businessUnitCode, String processInsId, String metaId) {
        String sql = "SELECT business_unit_code,process_ins_id,task_meta_id,task_token_id,status FROM t_task_ins " +
                "WHERE business_unit_code = '" + businessUnitCode + "' AND task_meta_id = '" + metaId + "' AND process_ins_id = '" + processInsId + "'";
        try {
            return jdbcTemplate.query(sql, new TaskInsDTOMapper());
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    /**
     * 根据tokenid获取任务实例
     *
     * @param businessUnitCode
     * @param tokenId
     * @return
     */
    public TaskInsDTO getTaskByTokenId(String businessUnitCode, String tokenId) {
        String sql = "SELECT business_unit_code,process_ins_id,task_meta_id,task_token_id,status FROM t_task_ins " +
                "WHERE business_unit_code = '" + businessUnitCode + "' AND task_token_id = '" + tokenId + "'";
        try {
            return jdbcTemplate.queryForObject(sql, new TaskInsDTOMapper());
        } catch (Exception e) {
            return new TaskInsDTO();
        }
    }

    class TaskInsDTOMapper implements RowMapper<TaskInsDTO> {
        @SuppressWarnings("NullableProblems")
        @Override
        public TaskInsDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new TaskInsDTO(
                    rs.getString("business_unit_code")
                    , rs.getString("process_ins_id")
                    , rs.getString("task_meta_id")
                    , rs.getString("task_token_id")
                    , TaskStatusEnum.valueOf(rs.getString("status"))
            );
        }
    }
}
