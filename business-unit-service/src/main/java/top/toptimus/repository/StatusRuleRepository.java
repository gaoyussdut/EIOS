package top.toptimus.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import top.toptimus.businessUnit.StatusRuleDTO;

import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
public class StatusRuleRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 根据状态规则找状态
     *
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public StatusRuleDTO findStatusRuleById(String ruleId) {
        String sql = "SELECT status_rule_id,stored_procedure,version,description FROM t_business_unit_task_status_definition " +
                "WHERE status_rule_id = '" + ruleId + "'";
        try {
            return jdbcTemplate.queryForObject(sql, new StatusRuleDTOMapper());
        } catch (Exception e) {
            return new StatusRuleDTO();
        }
    }

    class StatusRuleDTOMapper implements RowMapper<StatusRuleDTO> {
        @SuppressWarnings("NullableProblems")
        @Override
        public StatusRuleDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new StatusRuleDTO(
                    rs.getString("status_rule_id")
                    , rs.getString("stored_procedure")
                    , rs.getString("version")
                    , rs.getString("description")
            );
        }
    }
}
