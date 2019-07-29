package top.toptimus.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import top.toptimus.formula.UserTaskFormulaDTO;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class UserTaskFormulaRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 根据from节点id查询UserTaskFormulaDTO
     *
     * @param fromUsertaskId 业务纬度id
     */
    public List<UserTaskFormulaDTO> findAllUserTaskFormulaDTOByFromUsertaskId(String fromUsertaskId) {

        String sql = "SELECT tfu.process_id,tfu.from_usertask,tfu.to_usertask,tfd.formula,tfu.isdefault \n"
                + "FROM t_formula_usertask tfu \n"
                + "LEFT JOIN t_formula_definition tfd ON tfd.formula_id = tfu.formula_id \n"
                + "WHERE tfu.from_usertask = ?";

        return jdbcTemplate.query(sql, new Object[]{fromUsertaskId}, new UserTaskFormulaDTORowMapper());

    }

    /**
     * 根据processId 删除关联的所有公式
     *
     * @param processId 流程ID
     */
    public void delectByProcessId(String processId) {
        String sql = "DELETE FROM t_formula_usertask WHERE process_id = ?";
        jdbcTemplate.update(sql, new Object[]{processId});
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void saveAll(List<UserTaskFormulaDTO> userTaskFormulaDtos) {
        try {
            String sql = "insert into t_formula_usertask(process_id,from_usertask,to_usertask,formula_id,isdefault) "
                    + " values(?,?,?,?,?)";
            jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
                @Override
                public int getBatchSize() {
                    return userTaskFormulaDtos.size();
                }

                @Override
                public void setValues(@SuppressWarnings("NullableProblems") PreparedStatement ps, int i)
                        throws SQLException {
                    ps.setString(1, userTaskFormulaDtos.get(i).getProcessId());
                    ps.setString(2, userTaskFormulaDtos.get(i).getFromUsertaskId());
                    ps.setString(3, userTaskFormulaDtos.get(i).getToUsertaskId());
                    ps.setString(4, userTaskFormulaDtos.get(i).getFormula());
                    ps.setBoolean(5, userTaskFormulaDtos.get(i).isIsdefault());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("保存ProcessFileDao失败");
        }
    }

    class UserTaskFormulaDTORowMapper implements RowMapper<UserTaskFormulaDTO> {
        @Override
        public UserTaskFormulaDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new UserTaskFormulaDTO(rs.getString("process_id"), rs.getString("from_usertask"),
                    rs.getString("to_usertask"), rs.getString("formula"), rs.getBoolean("isdefault"));
        }
    }

}
