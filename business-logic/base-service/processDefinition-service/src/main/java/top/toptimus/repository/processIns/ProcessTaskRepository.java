package top.toptimus.repository.processIns;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import top.toptimus.businessmodel.process.dto.ProcessTaskDTO;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 业务审批Repo
 */
@Repository
public class ProcessTaskRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 根据单据metaId和tokenId查询业务审批
     *
     * @param metaId  单据metaId
     * @param tokenId 单据tokenId
     * @return ProcessTaskDTO 业务审批
     */
    @Transactional(readOnly = true)
    public ProcessTaskDTO findByTokenIdAndMetaId(String metaId,String tokenId ) {
        String sql = " select token_id,meta_id,task_id,process_id,process_token_id from r_bill_process_task where token_id=? AND meta_id =?";
        try {
            return jdbcTemplate.queryForObject(sql, new Object[]{tokenId, metaId}, new ProcessTaskRowMapper());
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    /**
     * 根据流程tokenId查询业务审批
     *
     * @param processTokenId 流程的tokenId
     * @return ProcessTaskDTO 业务审批
     */
    @Transactional(readOnly = true)
    public ProcessTaskDTO findByProcessTokenId(String processTokenId ) {
        String sql = " select token_id,meta_id,task_id,process_id,process_token_id from r_bill_process_task where process_token_id=?";
        try {
            return jdbcTemplate.queryForObject(sql, new Object[]{processTokenId}, new ProcessTaskRowMapper());
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    /**
     * 保存processInsDao
     *
     * @param processTaskDTO 业务任务实例
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void save(ProcessTaskDTO processTaskDTO) {
        String strSql = "INSERT INTO r_bill_process_task(token_id,meta_id,task_id,process_id,process_token_id) VALUES('"
                + processTaskDTO.getTokenId() + "', '" + processTaskDTO.getMetaId()
                + "', '" + processTaskDTO.getTaskId() + "', '" + processTaskDTO.getProcessId() +"', '"
                + processTaskDTO.getProcessTokenId()+ "')";
        try {
            jdbcTemplate.execute(strSql);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("保存ProcessTaskDTO失败!");
        }
    }


}

class ProcessTaskRowMapper implements RowMapper<ProcessTaskDTO> {
    @Override
    public ProcessTaskDTO mapRow(@SuppressWarnings("NullableProblems") ResultSet rs, int rowNum) throws SQLException {
        return new ProcessTaskDTO(
                  rs.getString("meta_id")
                , rs.getString("token_id")
                , rs.getString("task_id")
                , rs.getString("process_id")
                , rs.getString("process_token_id")
        );
    }
}