package top.toptimus.service.domainService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.toptimus.common.enums.process.ProcessEnum;
import top.toptimus.common.result.Result;
import top.toptimus.entity.Memorandvn.event.MemorandvnEventEntity;
import top.toptimus.entity.Memorandvn.query.MemorandvnQueryFacadeEntity;
import top.toptimus.place.PlaceDTO;
import top.toptimus.resultModel.ResultErrorModel;

import java.util.List;

/**
 * 备查账服务
 */
@Service
public class MemorandvnService {
    @Autowired
    private MemorandvnEventEntity memorandvnEventEntity;
    @Autowired
    private MemorandvnQueryFacadeEntity memorandvnQueryFacadeEntity;

    /**
     * 根据备查账ID查可以启动的流程信息
     *
     * @param memorandvnId 备查账ID
     * @return Result(List < ProcessDTO >)全部备查账信息
     */
    public Result getProcessDTOByMemorandvnId(String memorandvnId, String processEnum, String orgId) {
        try {
            return Result.success(memorandvnQueryFacadeEntity.getProcessDTOByMemorandvnId(memorandvnId, ProcessEnum.valueOf(processEnum), orgId));
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultErrorModel(e).getResult();
        }
    }

}
