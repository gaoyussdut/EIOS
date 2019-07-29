package top.toptimus.repository;

import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import top.toptimus.rule.RuleDefinitionDTO;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by lzs on 2019/2/19.
 */
@Repository
public class RuleDefinitionRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 获取全部规则
     *
     * @return
     */
    public List<RuleDefinitionDTO> findAll() {
        String sql = "SELECT rule_id,rule_name,business_code,rule_type,meta_id from t_rule_definition";
        return jdbcTemplate.query(sql, new RuleDefinitionDTORowMapper());
    }

    /**
     * 获取规则属性信息
     *
     * @return
     */
    public List<RuleDefinitionDTO> findByBusinessCode(String businessCode) {
        String sql = "SELECT rule_id,rule_name,business_code,rule_type,meta_id from t_rule_definition where business_code = '" + businessCode + "'";
        return jdbcTemplate.query(sql, new RuleDefinitionDTORowMapper());
    }

    /**
     * 保存规则属性信息
     *
     * @param ruleDefinitionDTOList
     */
    public void saveAll(List<RuleDefinitionDTO> ruleDefinitionDTOList) {
        String sql = "INSERT INTO t_rule_definition(rule_id,rule_name,business_code,rule_type,meta_id)"
                + "VALUES(?,?,?,?,?)";
        try {
            jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
                @Override
                public int getBatchSize() {
                    return ruleDefinitionDTOList.size();
                }

                @SuppressWarnings("NullableProblems")
                @Override
                public void setValues(PreparedStatement ps, int i) throws SQLException {
                    ps.setString(1, ruleDefinitionDTOList.get(i).getRuleId());
                    ps.setString(2, ruleDefinitionDTOList.get(i).getRuleName());
                    ps.setString(3, ruleDefinitionDTOList.get(i).getBusinessCode());
                    ps.setString(4, ruleDefinitionDTOList.get(i).getRuleType());
                    ps.setString(5, ruleDefinitionDTOList.get(i).getMetaId());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("保存RuleDefinitionDTO失败");
        }
    }

    /**
     * 根据业务码删除
     *
     * @param businessCode
     */
    public void deleteById(String businessCode) {
        String sql = "DELETE FROM t_rule_definition WHERE business_code = '" + businessCode + "'";
        jdbcTemplate.execute(sql);
    }

    public RuleDefinitionDTO findBySourceMetaAndTargetMeta(String sourceMetaId, String targetMetaId) {
        String sql = "SELECT business_code,rule_name,origin_meta,target_meta,rule_type,status from t_rule_definition where origin_meta = '" + sourceMetaId + "' AND target_meta = '" + targetMetaId + "'";
        return jdbcTemplate.queryForObject(sql, new RuleDefinitionDTORowMapper());
    }

    class RuleDefinitionDTORowMapper implements RowMapper<RuleDefinitionDTO> {
        @SuppressWarnings("NullableProblems")
        @Override
        public RuleDefinitionDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new RuleDefinitionDTO(rs.getString("rule_id"), rs.getString("rule_name"), rs.getString("business_code")
                    , rs.getString("rule_type"), rs.getString("meta_id"));
        }
    }
}
