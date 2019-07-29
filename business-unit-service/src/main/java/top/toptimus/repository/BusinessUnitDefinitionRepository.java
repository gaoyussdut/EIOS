package top.toptimus.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import top.toptimus.businessUnit.BusinessUnitDTO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class BusinessUnitDefinitionRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 根据组织架构Id取得其下的业务单元一览
     *
     * @param orgId 组织架构Id
     * @return Result
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public List<BusinessUnitDTO> findBusinessUnitByOrgId(String orgId) {
        String sql = "SELECT business_unit_code,business_unit_name,bill_meta_id,bpmn_url,exception_id,description,org_id FROM t_business_unit_definition " +
                "WHERE org_id = '" + orgId + "'";
        try {
            return jdbcTemplate.query(sql, new BusinessUnitDTOMapper());
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    class BusinessUnitDTOMapper implements RowMapper<BusinessUnitDTO> {
        @SuppressWarnings("NullableProblems")
        @Override
        public BusinessUnitDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new BusinessUnitDTO(
                    rs.getString("business_unit_code")
                    , rs.getString("business_unit_name")
                    , rs.getString("bill_meta_id")
                    , rs.getString("bpmn_url")
                    , rs.getString("exception_id")
                    , rs.getString("description")
                    , rs.getString("org_id")
            );
        }
    }
}
