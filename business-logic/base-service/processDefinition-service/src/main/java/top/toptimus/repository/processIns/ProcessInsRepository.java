package top.toptimus.repository.processIns;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import top.toptimus.processIns.ProcessInsDto;

import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
public class ProcessInsRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Transactional(readOnly = true)
    public ProcessInsDto findProcessInsByProcessIdAndTokenId(String processId, String tokenId) {
        String sql = " select process_ins_id,process_id,process_ins_name,token_id from t_process_ins where process_id=? AND token_id =?";
        try {
            return jdbcTemplate.queryForObject(sql, new Object[]{processId, tokenId}, new ProcessInsDtoRowMapper());
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    /**
     * 保存processInsDao
     *
     * @param processInsDto 项目实例
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void save(ProcessInsDto processInsDto) {
        String strSql = "INSERT INTO t_process_ins(process_ins_id,process_id,process_ins_name,token_id) VALUES('"
                + processInsDto.getProcessInsId() + "', '" + processInsDto.getProcessId()
                + "', '" + processInsDto.getProcessInsName() + "', '" + processInsDto.getTokenId() + "')";
        try {
            jdbcTemplate.execute(strSql);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("保存ProcessInsDao失败");
        }
    }


}

class ProcessInsDtoRowMapper implements RowMapper<ProcessInsDto> {
    @Override
    public ProcessInsDto mapRow(@SuppressWarnings("NullableProblems") ResultSet rs, int rowNum) throws SQLException {
        return new ProcessInsDto(
                rs.getString("process_id")
                , rs.getString("process_ins_id")
                , rs.getString("process_ins_name")
                , rs.getString("token_id")
        );
    }
}