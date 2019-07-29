package top.toptimus.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import top.toptimus.rule.BusinessCodeDTO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lzs on 2019/2/19.
 */
@Repository
public class BusinessCodeRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 获取规则属性信息
     *
     * @return
     */
    public BusinessCodeDTO findByOriginMetaAndTargetMeta(String originMetaId, String targetMetaId) {
        String sql = "SELECT business_code,rule_name,origin_meta,target_meta,target_meta,status,type from t_business_code_definition where origin_meta = '" + originMetaId + "' AND target_meta = '" + targetMetaId + "'";
        try {
            return jdbcTemplate.queryForObject(sql, new BusinessCodeDTODTORowMapper());
        } catch (Exception e) {
            return new BusinessCodeDTO();
        }

    }

    /**
     * 获取规则属性信息
     *
     * @return
     */
    public BusinessCodeDTO findById(String businessCode) {
        String sql = "SELECT business_code,rule_name,origin_meta,target_meta,status,type from t_business_code_definition where business_code = '" + businessCode + "'";
        return jdbcTemplate.queryForObject(sql, new BusinessCodeDTODTORowMapper());
    }

    /**
     * 获取规则属性信息
     *
     * @return
     */
    public List<BusinessCodeDTO> findAll() {
        String sql = "SELECT business_code,rule_name,origin_meta,target_meta,status,type from t_business_code_definition";
        return jdbcTemplate.query(sql, new BusinessCodeDTODTORowMapper());
    }

    /**
     * 获取下级单据
     *
     * @return
     */
    public List<BusinessCodeDTO> findByOriginMetaAndTargetMetaAndType(String originMetaId, String type) {
        try {
            String sql = "SELECT business_code,rule_name,origin_meta,target_meta,target_meta,status,type " +
                    "from t_business_code_definition where origin_meta = '" + originMetaId + "' " +
                    " AND type = '" + type + "'";
            return jdbcTemplate.query(sql, new BusinessCodeDTODTORowMapper());
        } catch (Exception e) {
            return new ArrayList<>();
        }

    }

    public void deleteBusinessCodeDefinitionDTO(String businessCode) {
        String sql = "DELETE FROM t_business_code_definition WHERE business_code = '" + businessCode + "'";
        jdbcTemplate.execute(sql);
    }

    /**
     * 保存规则属性信息
     *
     * @param businessCodeDTO
     */
    public void save(BusinessCodeDTO businessCodeDTO) {
        String sql = "INSERT INTO t_business_code_definition(business_code,rule_name,origin_meta,target_meta,status,type)"
                + "VALUES('" + businessCodeDTO.getBusinessCode() + "','"
                + businessCodeDTO.getRuleName() + "','"
                + businessCodeDTO.getOriginMetaId() + "','"
                + businessCodeDTO.getTargetMetaId() + "','"
                + businessCodeDTO.getStatus() + "','"
                + businessCodeDTO.getType() + "')";
        try {
            jdbcTemplate.execute(sql);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("保存BusinessCodeDTO失败");
        }
    }

    class BusinessCodeDTODTORowMapper implements RowMapper<BusinessCodeDTO> {
        @SuppressWarnings("NullableProblems")
        @Override
        public BusinessCodeDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new BusinessCodeDTO(rs.getString("business_code"), rs.getString("rule_name"), rs.getString("origin_meta"), rs.getString("target_meta")
                    , rs.getString("status"), rs.getString("type"));
        }
    }
}
