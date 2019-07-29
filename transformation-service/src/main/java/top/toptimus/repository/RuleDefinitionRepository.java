package top.toptimus.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import top.toptimus.common.enums.RuleTypeEnum;
import top.toptimus.transformation.RuleDefinitionDTO;

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
        String sql = "SELECT rule_id,origin_meta_id,target_meta_id,rule_type,stored_procedure from t_transformation_rule_definition";
        return jdbcTemplate.query(sql, new RuleDefinitionDTORowMapper());
    }

    /**
     * 获取规则属性信息
     *
     * @return
     */
    public RuleDefinitionDTO findById(String ruleId) {
        String sql = "SELECT rule_id,origin_meta_id,target_meta_id,rule_type,stored_procedure from t_transformation_rule_definition " +
                "where rule_id = '" + ruleId + "'";
        return jdbcTemplate.queryForObject(sql, new RuleDefinitionDTORowMapper());
    }

//    /**
//     * 保存规则属性信息
//     *
//     * @param ruleDefinitionDTOList
//     */
//    public void saveAll(List<RuleDefinitionDTO> ruleDefinitionDTOList) {
//        String sql = "INSERT INTO t_rule_definition(rule_id,rule_name,business_code,rule_type,meta_id)"
//                + "VALUES(?,?,?,?,?)";
//        try {
//            jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
//                @Override
//                public int getBatchSize() {
//                    return ruleDefinitionDTOList.size();
//                }
//
//                @SuppressWarnings("NullableProblems")
//                @Override
//                public void setValues(PreparedStatement ps, int i) throws SQLException {
//                    ps.setString(1, ruleDefinitionDTOList.get(i).getRuleId());
//                    ps.setString(2, ruleDefinitionDTOList.get(i).getRuleName());
//                    ps.setString(3, ruleDefinitionDTOList.get(i).getBusinessCode());
//                    ps.setString(4, ruleDefinitionDTOList.get(i).getRuleType());
//                    ps.setString(5, ruleDefinitionDTOList.get(i).getMetaId());
//                }
//            });
//        } catch (Exception e) {
//            e.printStackTrace();
//            throw new RuntimeException("保存RuleDefinitionDTO失败");
//        }
//    }


    class RuleDefinitionDTORowMapper implements RowMapper<RuleDefinitionDTO> {
        @SuppressWarnings("NullableProblems")
        @Override
        public RuleDefinitionDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new RuleDefinitionDTO(rs.getString("rule_id"), rs.getString("origin_meta_id"), rs.getString("target_meta_id")
                    , RuleTypeEnum.valueOf(rs.getString("rule_type")), rs.getString("stored_procedure"));
        }
    }
}
