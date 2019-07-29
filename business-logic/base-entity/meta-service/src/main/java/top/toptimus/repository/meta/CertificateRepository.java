package top.toptimus.repository.meta;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import top.toptimus.businessmodel.memorandvn.dto.CertificateDTO;
import top.toptimus.rule.CertificateReceiveDTO;
import top.toptimus.rule.SourceBillDTO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

@Repository
public class CertificateRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 根据业务单元id取凭证metaId
     * @param businessUnitId  业务单元id
     * @return List<String> 凭证metaId
     */
    @Transactional(readOnly = true)
    public List<CertificateDTO> getCertificateMetaIdByBUID(String businessUnitId) {
        String sql = "SELECT  distinct tbhi.certificate_meta_id ,ttmf.token_meta_name from t_business_handover_ins tbhi "
                + " LEFT JOIN t_token_meta_formation ttmf ON tbhi.certificate_meta_id = ttmf.token_meta_id"
                + " WHERE tbhi.to_business_unit_code = '"
                + businessUnitId + "' ";
        return jdbcTemplate.query(sql, new CertificateDTORowMapper());
    }

    /**
     * 根据业务单元id和指定的凭证metaId取未接收的凭证tokenIds
     * @param businessUnitId        业务单元id
     * @param certificateMetaId     凭证metaId
     * @return List<String> 凭证tokenIds
     */
    @Transactional(readOnly = true)
    public List<String> getCertificateTokenIds(String businessUnitId,String certificateMetaId) {
        String sql = "SELECT  certificate_token_id from t_business_handover_ins "
                + " WHERE to_business_unit_code = '"
                + businessUnitId + "' "
                + " AND certificate_meta_id = '"
                + certificateMetaId + "'"
                + "AND is_recept = false ";
        return jdbcTemplate.queryForList(sql, String.class);
    }

    /**
     * 根据业务单元id和指定的凭证metaId取凭证转换单据时调用的存储过程和转换单据的metaId
     * @param businessUnitId        业务单元id
     * @param certificateMetaId     凭证metaId
     * @param certificateTokenId    凭证tokenId
     * @return CertificateReceiveDTO
     */
    @Transactional(readOnly = true)
    public CertificateReceiveDTO getCertificateReceiveDTO(
            String certificateMetaId
            ,String certificateTokenId
            ,String businessUnitId) {
        String sql = "SELECT distinct to_meta_id,stored_procedure from t_business_handover_definition "
                + " WHERE to_business_unit_code = '"
                + businessUnitId + "' "
                + " AND certificate_meta_id = '"
                + certificateMetaId + "'";
        return Objects.requireNonNull(jdbcTemplate.queryForObject(sql, new CertificateReceiveDTORowMapper())).buildCertificateTokenIdTokenId(certificateTokenId);
    }

    /**
     * 执行存储过程
     *
     * @param certificateReceiveDTO
     */
    public CertificateReceiveDTO excuteStoredProcedure(CertificateReceiveDTO certificateReceiveDTO) {

        String sql = "SELECT " + certificateReceiveDTO.getStoredProcedure() + "('" + certificateReceiveDTO.getCertificateTokenId() + "')";
        return certificateReceiveDTO.buildBillTokenId(jdbcTemplate.queryForObject(sql,String.class));
    }

    /**
     * 根据业务单元id和指定的凭证metaId凭证tokenId获取源单metaId与tokenId
     * @param businessUnitId        业务单元id
     * @param certificateMetaId     凭证metaId
     * @param certificateTokenId    凭证tokenId
     */
    @Transactional(readOnly = true)
    public SourceBillDTO getSourceBill(
            String certificateMetaId
            ,String certificateTokenId
            ,String businessUnitId) {
        String sql = "SELECT distinct from_meta_id,from_token_id from t_business_handover_ins "
                + " WHERE to_business_unit_code = '"
                + businessUnitId + "' "
                + " AND certificate_meta_id = '"
                + certificateMetaId + "'"
                + " AND certificate_token_id = '"
                + certificateTokenId + "'";
        return Objects.requireNonNull(jdbcTemplate.queryForObject(sql, new SourceBillDTORowMapper()));
    }

    class CertificateReceiveDTORowMapper implements RowMapper<CertificateReceiveDTO> {
        @SuppressWarnings("NullableProblems")
        @Override
        public CertificateReceiveDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new CertificateReceiveDTO(
                    rs.getString("to_meta_id")
                    , rs.getString("stored_procedure")
            );
        }
    }

    class SourceBillDTORowMapper implements RowMapper<SourceBillDTO> {
        @SuppressWarnings("NullableProblems")
        @Override
        public SourceBillDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new SourceBillDTO(
                    rs.getString("from_meta_id")
                    , rs.getString("from_token_id")
            );
        }
    }

    class CertificateDTORowMapper implements RowMapper<CertificateDTO> {
        @SuppressWarnings("NullableProblems")
        @Override
        public CertificateDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new CertificateDTO(
                    ""
                    , rs.getString("token_meta_name")     // meta名字
                    , rs.getString("certificate_meta_id") // 凭证ID
            );
        }
    }


}
