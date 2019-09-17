package top.toptimus.entity.Memorandvn.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.toptimus.entity.Memorandvn.query.MemorandvnQueryFacadeEntity;
import top.toptimus.model.procedure.ProcedureModel;
import top.toptimus.repository.Memorandvn.BusinessFactRepository;
import top.toptimus.repository.Memorandvn.MemorandvnRepository;

/**
 * 备查账事务处理
 *
 * @author gaoyu
 */
@Component
public class MemorandvnEventEntity {
    // entity
    @Autowired
    private MemorandvnQueryFacadeEntity memorandvnQueryFacadeEntity;
    // repo
    @Autowired
    private MemorandvnRepository memorandvnRepository;
    @Autowired
    private BusinessFactRepository businessFactRepository;

    /**
     * 调用存储过程生成凭证
     *
     * @param billTokenId 表头token id
     * @param lotNo       批号
     * @param userTaskId  user task id
     * @return isSuccess
     */
    public boolean saveCertificate(String billTokenId, String lotNo, String userTaskId) {
        try {
            return businessFactRepository.excuteStoredProcedure(
                    new ProcedureModel(
                            billTokenId
                            , lotNo
                            , memorandvnQueryFacadeEntity.getStoredProcedures(userTaskId)
                    )  //  判断存储过程是否为空，为空报错
            ).isSuccess();
        } catch (Exception e) {
            return false;
        }

    }


}
