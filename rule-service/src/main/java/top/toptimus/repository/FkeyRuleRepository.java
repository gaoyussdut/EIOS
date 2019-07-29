package top.toptimus.repository;

import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import top.toptimus.rule.FkeyRuleDTO;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by lzs on 2019/2/19.
 */
@Repository
public class FkeyRuleRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 根据事务码和metaid查找字段规则
     *
     * @param businessCode
     * @param ruleType
     * @return
     */
    public List<FkeyRuleDTO> findByBusinessCodeAndruleType(String businessCode, String ruleType) {
        String sql = "SELECT business_code,meta_id,key,caption,fkey_type,formula,analytical_formula,editable,order_,calculate_type,rule_type,params " +
                "from t_fkey_rule WHERE business_code = '" + businessCode + "' AND rule_type = '" + ruleType + "'";
        return jdbcTemplate.query(sql, new FkeyRuleDTORowMapper());
    }

    /**
     * 根据事务码和metaid查找字段规则
     *
     * @param businessCode
     * @param metaId
     * @return
     */
    public List<FkeyRuleDTO> findByBusinessCodeAndMetaId(String businessCode, String metaId) {
        String sql = "SELECT tfr.rule_id,tfr.meta_id,tfr.key,tfr.caption,tfr.fkey_type,tfr.formula,tfr.analytical_formula,tfr.editable,tfr.order_,tfr.calculate_type,tfr.params " +
                "from t_fkey_rule tfr " +
                "LEFT JOIN t_rule_definition trd ON trd.rule_id = tfr.rule_id WHERE trd.business_code = '" + businessCode + "' AND tfr.meta_id = '" + metaId + "'";
        return jdbcTemplate.query(sql, new FkeyRuleDTORowMapper());
    }

    /**
     * 根据事务码和metaid查找字段规则
     *
     * @param ruleId
     * @return
     */
    public List<FkeyRuleDTO> findByRuleId(String ruleId) {
        String sql = "SELECT rule_id,meta_id,key,caption,fkey_type,formula,analytical_formula,editable,order_,calculate_type,params " +
                "from t_fkey_rule WHERE rule_id = '" + ruleId + "'";
        return jdbcTemplate.query(sql, new FkeyRuleDTORowMapper());
    }

    /**
     * 保存全部FkeyRuleDTO
     *
     * @param fkeyRuleDTOList fkeyRuleDTOList
     */
    public void saveAll(List<FkeyRuleDTO> fkeyRuleDTOList) {
        try {
            String sql = "insert into t_fkey_rule(rule_id,meta_id,key,caption,fkey_type,formula,analytical_formula,editable,order_,calculate_type,params)" +
                    " values(?,?,?,?,?,?,?,?,?,?,?)";
            jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
                @Override
                public int getBatchSize() {
                    return fkeyRuleDTOList.size();
                }

                @SuppressWarnings("NullableProblems")
                @Override
                public void setValues(PreparedStatement ps, int i) throws SQLException {
                    ps.setString(1, fkeyRuleDTOList.get(i).getRuleId());
                    ps.setString(2, fkeyRuleDTOList.get(i).getMetaId());
                    ps.setString(3, fkeyRuleDTOList.get(i).getKey());
                    ps.setString(4, fkeyRuleDTOList.get(i).getCaption());
                    ps.setString(5, fkeyRuleDTOList.get(i).getFkeyType());
                    ps.setString(6, fkeyRuleDTOList.get(i).getFormula());
                    ps.setString(7, fkeyRuleDTOList.get(i).getAnalyticalFormula());
                    ps.setBoolean(8, fkeyRuleDTOList.get(i).isEditable());
                    ps.setInt(9, fkeyRuleDTOList.get(i).getOrder());
                    ps.setString(10, fkeyRuleDTOList.get(i).getCalculateType());
                    ps.setString(11, JSON.toJSONString(fkeyRuleDTOList.get(i).getParams_()));
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("保存FkeyRuleDTO失败");
        }
    }

    /**
     * 根据事务码删除
     *
     * @param ruleId
     */
    public void deleteByRuleId(String ruleId) {
        String sql = "DELETE FROM t_fkey_rule WHERE rule_id = '" + ruleId + "'";
        jdbcTemplate.execute(sql);
    }

    class FkeyRuleDTORowMapper implements RowMapper<FkeyRuleDTO> {
        @SuppressWarnings("NullableProblems")
        @Override
        public FkeyRuleDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new FkeyRuleDTO(rs);
        }
    }
}
