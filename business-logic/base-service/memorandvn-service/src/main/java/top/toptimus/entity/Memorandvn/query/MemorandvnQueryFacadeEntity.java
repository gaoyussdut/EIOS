package top.toptimus.entity.Memorandvn.query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;
import top.toptimus.businessmodel.memorandvn.dto.BalanceAccountDTO;
import top.toptimus.businessmodel.memorandvn.dto.CertificateDTO;
import top.toptimus.businessmodel.memorandvn.dto.MemorandvnDTO;
import top.toptimus.businessmodel.process.dto.ProcessDTO;
import top.toptimus.common.CurrentPage;
import top.toptimus.common.enums.process.ProcessEnum;
import top.toptimus.repository.Memorandvn.BusinessFactRepository;
import top.toptimus.repository.Memorandvn.CertificateDefinitionRepository;

import java.util.List;

/**
 * 备查账查询query facade
 *
 * @author gaoyu
 */
@Component
public class MemorandvnQueryFacadeEntity {
    // repo
    @Autowired
    private CertificateDefinitionRepository certificateDefinitionRepository;
    @Autowired
    private BusinessFactRepository businessFactRepository;

    /**
     * 查看全部备查账信息
     *
     * @return 全部备查账信息
     */
    public List<MemorandvnDTO> getAllMemorandvn() {
        return certificateDefinitionRepository.getAllMemorandvn();
    }

    /**
     * 根据metaId查询备查账信息
     *
     * @return MemorandvnDTO
     * @throws DataAccessException DataAccessException
     */
    public MemorandvnDTO getMemorandvnByMetaId(String metaId) throws DataAccessException {
        return certificateDefinitionRepository.findMemorandvnByMetaId(metaId);
    }

    /**
     * 根据备查账ID查可以启动的流程信息
     *
     * @param memorandvnId 备查账ID
     * @return 全部备查账信息
     */
    public List<ProcessDTO> getProcessDTOByMemorandvnId(String memorandvnId, ProcessEnum processEnum, String orgId) {
        return certificateDefinitionRepository.getProcessDTOByMemorandvnId(memorandvnId, processEnum, orgId);
    }

    /**
     * 根据user task id取出存储过程列表
     *
     * @param userTaskId user task id
     * @return 存储过程列表
     */
    public List<String> getStoredProcedures(String userTaskId) {
        // 2.根据任务id找凭证类型和调用的存储过程名
        return businessFactRepository.fingProcedureByTaskCategoryId(
                businessFactRepository.fingTaskCategoryIdByUserTaskId(userTaskId) // 1.根据usertaskId查任务种类id
        );
    }

    /**
     * 根据备查帐meta查找备查帐DTO
     *
     * @param memorandvnMetaIds 备查账meta id列表
     * @return MemorandvnDTO
     */
    public List<MemorandvnDTO> getMemorandvnDTOByMemorandvnMetaId(List<String> memorandvnMetaIds) {
        return certificateDefinitionRepository.getMemorandvnDTOByMemorandvnMetaId(memorandvnMetaIds);
    }

}
