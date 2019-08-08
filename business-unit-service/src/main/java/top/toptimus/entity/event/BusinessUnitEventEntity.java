package top.toptimus.entity.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import top.toptimus.businessUnit.CertificateDefinitionDTO;
import top.toptimus.businessUnit.HandoverInsDTO;
import top.toptimus.businessUnit.TaskInsDTO;
import top.toptimus.repository.BusinessUnitEdgeRepository;
import top.toptimus.repository.HandoverInsRepository;
import top.toptimus.repository.SchemaRepository;
import top.toptimus.repository.TaskInsRepository;
import top.toptimus.repository.meta.CertificateRepository;
import top.toptimus.rule.CertificateReceiveDTO;
import top.toptimus.rule.SourceBillDTO;
import top.toptimus.schema.SchemaDTO;

import java.util.List;

/**
 * businessunit event
 *
 * @author lzs
 * @since 2019-4-14
 */
@Component
public class BusinessUnitEventEntity {

    @Autowired
    private TaskInsRepository taskInsRepository;
    @Autowired
    private CertificateRepository certificateRepository;
    @Autowired
    private BusinessUnitEdgeRepository businessUnitEdgeRepository;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private HandoverInsRepository handoverInsRepository;
    @Autowired
    private SchemaRepository schemaRepository;

    /**
     * 保存任务实例
     *
     * @param taskInsDTO
     */
    public void saveTaskIns(TaskInsDTO taskInsDTO) {
        taskInsRepository.save(taskInsDTO);
    }

    /**
     * 凭证接收,调用存储过程转换单据,返回单据的metaid和tokenId
     *
     * @param certificateMetaId  凭证metaId
     * @param certificateTokenId 业务单元id
     * @param businessUnitId     凭证tokenId
     * @return CertificateReceiveDTO
     */
    public CertificateReceiveDTO receiveCertificate(
            String certificateMetaId
            , String certificateTokenId
            , String businessUnitId) {
        // 执行存储过程
        return certificateRepository
                .excuteStoredProcedure(
                        //查询需要执行的存储过程
                        certificateRepository
                                .getCertificateReceiveDTO(
                                        certificateMetaId
                                        , certificateTokenId
                                        , businessUnitId
                                )
                );
    }

    /**
     * 根据凭证查看原单
     *
     * @param certificateMetaId  凭证metaId
     * @param businessUnitId     业务单元id
     * @param certificateTokenId 凭证tokenId
     */
    public SourceBillDTO getSourceBill(
            String certificateMetaId
            , String certificateTokenId
            , String businessUnitId
    ) {
        return certificateRepository.getSourceBill(
                certificateMetaId
                , certificateTokenId
                , businessUnitId
        );
    }

    /**
     * 执行存储过程
     *
     * @param storedProcedure 存储过程
     * @param tokenId         token id
     */
    public String excuteStoredProcedure(String storedProcedure, String tokenId) {

        String sql = "SELECT " + storedProcedure + "('" + tokenId + "')";
        return jdbcTemplate.queryForObject(sql, String.class);

    }

    /**
     * 保存凭证交接实例
     *
     * @param handoverInsDTOs
     */
    public void saveHandoverIns(List<HandoverInsDTO> handoverInsDTOs) {
        handoverInsRepository.saveAll(handoverInsDTOs);
    }

    /**
     * 保存shcema
     * @param schemaDTO
     */
    public void saveSchema(SchemaDTO schemaDTO) {
        schemaRepository.saveSchema(schemaDTO);
    }

    /**
     * 删除schema
     * @param id
     */
    public void deleteSchema(String id) {
        schemaRepository.deleteSchema(id);
    }
}
