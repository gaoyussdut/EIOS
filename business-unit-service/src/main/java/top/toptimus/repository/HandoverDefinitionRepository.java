package top.toptimus.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import top.toptimus.businessUnit.HandoverDefinitionDTO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class HandoverDefinitionRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 根据上级businessunit和凭证meta找定义
     *
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public List<HandoverDefinitionDTO> findHandoverByFromBUAndCertificateMetaId(String fromBusinessUnitCode, String certificateMetaId, String fromMetaId) {
        String sql = "SELECT from_org_id,from_business_unit_code,to_org_id,to_business_unit_code,certificate_meta_id" +
                ",stored_procedure,from_meta_id,to_meta_id FROM t_business_handover_definition " +
                "WHERE from_business_unit_code = '" + fromBusinessUnitCode + "' AND certificate_meta_id = '" + certificateMetaId + "'" +
                " AND from_meta_id = '" + fromMetaId + "'";
        try {
            return jdbcTemplate.query(sql, new HandoverDefinitionDTOMapper());
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    class HandoverDefinitionDTOMapper implements RowMapper<HandoverDefinitionDTO> {
        @SuppressWarnings("NullableProblems")
        @Override
        public HandoverDefinitionDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new HandoverDefinitionDTO(
                    rs.getString("from_org_id")
                    , rs.getString("from_business_unit_code")
                    , rs.getString("to_org_id")
                    , rs.getString("to_business_unit_code")
                    , rs.getString("certificate_meta_id")
                    , rs.getString("stored_procedure")
                    , rs.getString("from_meta_id")
                    , rs.getString("to_meta_id")
            );
        }
    }

}
