package top.toptimus.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import top.toptimus.businessUnit.BusinessUnitTaskDefinitionDTO;

import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
public class BusinessUnitTaskDefinitionRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 根据metaId取得节点信息
     *
     * @param metaId metaId
     * @return Result
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public BusinessUnitTaskDefinitionDTO findBusinessUnitTaskByMetaId(String metaId) {
        String sql = "SELECT business_unit_code,meta_id,task_id,status_rule_id,task_type FROM t_business_unit_task_definition " +
                "WHERE meta_id = '" + metaId + "'";
        try {
            return jdbcTemplate.queryForObject(sql, new BusinessUnitTaskDefinitionDTOMapper());
        } catch (Exception e) {
            return new BusinessUnitTaskDefinitionDTO();
        }
    }

    class BusinessUnitTaskDefinitionDTOMapper implements RowMapper<BusinessUnitTaskDefinitionDTO> {
        @SuppressWarnings("NullableProblems")
        @Override
        public BusinessUnitTaskDefinitionDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new BusinessUnitTaskDefinitionDTO(
                    rs.getString("business_unit_code")
                    , rs.getString("meta_id")
                    , rs.getString("task_id")
                    , rs.getString("status_rule_id")
                    , rs.getString("task_type")
            );
        }
    }
}
