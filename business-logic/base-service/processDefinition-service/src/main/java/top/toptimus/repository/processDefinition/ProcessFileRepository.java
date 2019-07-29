package top.toptimus.repository.processDefinition;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import top.toptimus.processDefinition.ProcessFileDto;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class ProcessFileRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;


    @Transactional(propagation = Propagation.REQUIRED)
    public void saveAll(List<ProcessFileDto> processFileDtos) {
        try {
            String sql = "insert into t_process_file(process_id,process_name,bpmn_file,status) "
                    + " values(?,?,?,?)";
            jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
                @Override
                public int getBatchSize() {
                    return processFileDtos.size();
                }

                @Override
                public void setValues(@SuppressWarnings("NullableProblems") PreparedStatement ps, int i) throws SQLException {
                    ps.setString(1, processFileDtos.get(i).getProcessId());
                    ps.setString(2, processFileDtos.get(i).getProcessName());
                    ps.setString(3, processFileDtos.get(i).getBpmnFile());
                    ps.setString(4, processFileDtos.get(i).getStatus());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("保存ProcessFileDao失败");
        }
    }


    @Transactional(readOnly = true)
    public ProcessFileDto findByProcessId(String processId) {
        try {
            String sql = "select id,process_id,process_name,bpmn_file,status,meta_id from t_process_file where process_id= ? ";
            return jdbcTemplate.queryForObject(sql, new Object[]{processId}, new ProcessFileDtoRowMapper());
        } catch (EmptyResultDataAccessException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Transactional(readOnly = true)
    public List<ProcessFileDto> findAllProcess() {
        String sql = "select DISTINCT process_id,process_name from t_process_file ";
        return jdbcTemplate.query(sql, new ProcessFileRowMapper());
    }

    /**
     * 根据ProcessId删除数据
     *
     * @param processId 流程id
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteByProcessId(String processId) {
        try {
            String sql = "delete from t_process_file where process_id=?";
            jdbcTemplate.update(sql, processId);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("删除ProcessFileDao失败!");
        }
    }
}

class ProcessFileDtoRowMapper implements RowMapper<ProcessFileDto> {
    @Override
    public ProcessFileDto mapRow(@SuppressWarnings("NullableProblems") ResultSet rs, int rowNum) throws SQLException {
        return new ProcessFileDto(
                rs.getString("id")
                , rs.getString("process_id")
                , rs.getString("process_name")
                , rs.getString("bpmn_file")
                , rs.getString("status")
        );
    }
}

class ProcessFileRowMapper implements RowMapper<ProcessFileDto> {
    @Override
    public ProcessFileDto mapRow(@SuppressWarnings("NullableProblems") ResultSet rs, int rowNum) throws SQLException {
        return new ProcessFileDto(
                rs.getString("process_id")
                , rs.getString("process_name")
        );
    }
}
