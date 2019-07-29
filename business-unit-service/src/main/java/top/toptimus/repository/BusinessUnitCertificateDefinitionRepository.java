package top.toptimus.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import top.toptimus.businessUnit.CertificateDefinitionDTO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lzs on 2019/4/14.
 */
@Repository
public class BusinessUnitCertificateDefinitionRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<CertificateDefinitionDTO> findCertificateByMetaId(String metaId) {

        String sql = "SELECT business_unit_code,meta_id,certificate_meta_id,stored_procedure FROM t_certificate_definition " +
                "WHERE meta_id = '" + metaId + "'";
        try {
            return jdbcTemplate.query(sql, new CertificateDefinitionDTOMapper());
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    /**
     * 根据metaid获取提交存储过程
     *
     * @param metaId
     * @return
     */
    public String findStoredProcedureByMetaId(String metaId) {

        String sql = "SELECT stored_procedure FROM t_certificate_definition " +
                "WHERE meta_id = '" + metaId + "'";
        try {
            return jdbcTemplate.queryForObject(sql, String.class);
        } catch (Exception e) {
            return null;
        }

    }

    class CertificateDefinitionDTOMapper implements RowMapper<CertificateDefinitionDTO> {
        @SuppressWarnings("NullableProblems")
        @Override
        public CertificateDefinitionDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new CertificateDefinitionDTO(
                    rs.getString("business_unit_code")
                    , rs.getString("meta_id")
                    , rs.getString("certificate_meta_id")
                    , rs.getString("stored_procedure")
            );
        }
    }
}
