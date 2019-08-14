package top.toptimus.entity.query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.toptimus.businessUnit.*;
import top.toptimus.repository.*;
import top.toptimus.repository.meta.CertificateRepository;
import java.util.List;

/**
 * businessunit query
 *
 * @author lzs
 * @since 2019-4-14
 */
@Component
public class BusinessUnitFacadeQueryEntity {

    @Autowired
    private TaskInsRepository taskInsRepository;
    @Autowired
    private CertificateRepository certificateRepository;
    @Autowired
    private BusinessUnitEdgeRepository businessUnitEdgeRepository;
    @Autowired
    private BusinessUnitDefinitionRepository businessUnitDefinitionRepository;
    @Autowired
    private BusinessUnitTaskDefinitionRepository businessUnitTaskDefinitionRepository;
    @Autowired
    private StatusRuleRepository statusRuleRepository;
    @Autowired
    private HandoverDefinitionRepository handoverDefinitionRepository;
    @Autowired
    private BusinessUnitCertificateDefinitionRepository certificateDefinitionRepository;

    /**
     * 根据下级meta找业务单元流程弧
     *
     * @param metaId
     */
    public BusinessUnitEdgeDTO findEdgeByToMetaId(String businessUnitCode, String metaId) {
        return businessUnitEdgeRepository.findEdgeByToMetaId(businessUnitCode, metaId);
    }

    /**
     * 根据业务单元和任务meta查找taskIns
     *
     * @param businessUnitCode
     * @param metaId
     * @return
     */
    public List<TaskInsDTO> getTaskInsByTaskMetaId(String businessUnitCode, String metaId) {
        return taskInsRepository.findByTaskMetaId(businessUnitCode, metaId);

    }

    /**
     * 根据上级meta获取业务单元流程弧
     *
     * @param businessUnitCode 业务单元Id
     * @return Result
     */
    public List<BusinessUnitEdgeDTO> findNextTaskMetaByBusinessUnitCodeAndFromMetaId(String businessUnitCode, String metaId) {
        return businessUnitEdgeRepository.findNextTaskMetaByBusinessUnitCodeAndFromMetaId(businessUnitCode, metaId);
    }

    /**
     * 根据任务meta和任务token获取taskIns
     *
     * @param businessUnitCode
     * @param metaId
     * @param tokenId
     * @return
     */
    public TaskInsDTO getTaskInsByTaskMetaIdAndTaskTokenId(String businessUnitCode, String metaId, String tokenId) {
        return taskInsRepository.findByTaskMetaIdAndTaskTokenId(businessUnitCode, metaId, tokenId);
    }

    /**
     * 根据meta和processins获取taskins
     *
     * @param businessUnitCode
     * @param processInsId
     * @param metaId
     * @return
     */
    public List<TaskInsDTO> getTaskInsByTaskMetaIdAndProcessInsId(String businessUnitCode, String processInsId, String metaId) {
        return taskInsRepository.getTaskInsByTaskMetaIdAndProcessInsId(businessUnitCode, processInsId, metaId);
    }

    /**
     * 根据上下级节点获取弧
     *
     * @param businessUnitCode
     * @param preMetaId
     * @param metaId
     * @param edgeType
     * @return
     */
    public BusinessUnitEdgeDTO findEdgeByFromMetaIdAndToMetaId(String businessUnitCode, String preMetaId, String metaId, String edgeType) {
        return businessUnitEdgeRepository.findEdgeByFromMetaIdAndToMetaId(businessUnitCode, preMetaId, metaId, edgeType);
    }

    /**
     * 根据组织架构Id取得其下的业务单元一览
     *
     * @param orgId 组织架构Id
     * @return List<BusinessUnitDTO>
     */
    public List<BusinessUnitDTO> findBusinessUnitByOrgId(String orgId) {
        return businessUnitDefinitionRepository.findBusinessUnitByOrgId(orgId);
    }

    /**
     * 根据metaId获取节点信息
     *
     * @param metaId
     * @return
     */
    public BusinessUnitTaskDefinitionDTO findBusinessUnitTaskByMetaId(String metaId) {
        return businessUnitTaskDefinitionRepository.findBusinessUnitTaskByMetaId(metaId);
    }

    /**
     * 根据tokenid获取任务实例
     *
     * @param businessUnitCode
     * @param tokenId
     * @return
     */
    public TaskInsDTO getTaskByTokenId(String businessUnitCode, String tokenId) {
        return taskInsRepository.getTaskByTokenId(businessUnitCode, tokenId);
    }

    /**
     * 根据状态规则找状态
     *
     * @return
     */
    public StatusRuleDTO findStatusRuleById(String ruleId) {
        return statusRuleRepository.findStatusRuleById(ruleId);
    }

    /**
     * 查找交接定义
     *
     * @param businessUnitCode
     * @param certificateMetaId
     * @param metaId
     */
    public List<HandoverDefinitionDTO> findHandoverByFromBUAndCertificateMetaId(String businessUnitCode, String certificateMetaId, String metaId) {
        return handoverDefinitionRepository.findHandoverByFromBUAndCertificateMetaId(businessUnitCode, certificateMetaId, metaId);
    }

    /**
     * 根据metaId获取反写凭证
     *
     * @param metaId
     */
    public List<CertificateDefinitionDTO> findCertificateByMetaId(String metaId) {
        return certificateDefinitionRepository.findCertificateByMetaId(metaId);
    }

    /**
     * 根据metaid获取提交存储过程
     * @param metaId
     * @return
     */
    public String getStoredProcedureByMetaId(String metaId) {
        return certificateDefinitionRepository.findStoredProcedureByMetaId(metaId);
    }
    
}
