package top.toptimus.service.domainService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import top.toptimus.common.result.Result;
import top.toptimus.entity.tokendata.event.TokenEventEntity;
import top.toptimus.entity.tokendata.query.TokenQueryFacadeEntity;
import top.toptimus.exception.TopErrorCode;
import top.toptimus.exception.TopException;
import top.toptimus.model.tokenData.event.TokenDeleteModel;
import top.toptimus.resultModel.ResultErrorModel;
import top.toptimus.tokendata.TokenDataDto;

import java.util.ArrayList;


@Service
public class TokenService {

    @Autowired
    private TokenEventEntity tokenEventEntity;
    @Autowired
    private TokenQueryFacadeEntity tokenQueryFacadeEntity;


    /**
     * 保存tokenDataDto数据
     *
     * @param tokenDataDto token数据
     * @param metaId       metaId
     * @return Result
     */
    public Result saveDatas(TokenDataDto tokenDataDto, String metaId) {
        try {
            tokenEventEntity.saveDatas(tokenDataDto, metaId);
            return Result.success();
        } catch (TopException e) {
            return new ResultErrorModel(e).getResult();
        } catch (DataAccessException e) {
            e.printStackTrace();
            return new ResultErrorModel(TopErrorCode.INVALID_OBJ, e).getResult();
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultErrorModel(TopErrorCode.GENERAL_ERR, e).getResult();
        }
    }

    /**
     * 根据metaId和tokenId取得数据
     *
     * @param metaId   metaId
     * @param tokenId  数据的tokenId
     * @return Result (tokenDataDto数据)
     */
    public Result getTokenData(String metaId, String tokenId) {
        try {
            return Result.success(tokenQueryFacadeEntity.getTokenData(metaId, tokenId));
        } catch (TopException e) {
            return new ResultErrorModel(e).getResult();
        } catch (DataAccessException e) {
            e.printStackTrace();
            return new ResultErrorModel(TopErrorCode.INVALID_OBJ, e).getResult();
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultErrorModel(TopErrorCode.GENERAL_ERR, e).getResult();
        }
    }

    /**
     * 根据凭证metaId和业务单元Id取凭证tokendata
     *
     * @param certificateMetaId   凭证metaId
     * @param businessUnitId        业务单元id
     * @return Result (List<tokenDataDto></tokenDataDto>数据)
     */
    public Result getCertificateTokenData(String certificateMetaId, String  businessUnitId) {
        try {
            return Result.success(
                    tokenQueryFacadeEntity.getCertificateTokenIData(
                            certificateMetaId, businessUnitId
                    )
            );
        } catch (TopException e) {
            return new ResultErrorModel(e).getResult();
        } catch (DataAccessException e) {
            e.printStackTrace();
            return new ResultErrorModel(TopErrorCode.INVALID_OBJ, e).getResult();
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultErrorModel(TopErrorCode.GENERAL_ERR, e).getResult();
        }
    }

    /**
     * 根据metaId取得一览信息
     *
     * @param metaId   metaId
     * @param pageSize 单页数量
     * @param pageNo   页码
     * @return TokenDataPageableDto
     */
    public Result getTokenDataList(String metaId ,Integer pageSize ,Integer pageNo) {
        try {
            return Result.success(tokenQueryFacadeEntity.getTokenDataList(metaId, pageSize ,pageNo));
        } catch (TopException e) {
            return new ResultErrorModel(e).getResult();
        } catch (DataAccessException e) {
            e.printStackTrace();
            return new ResultErrorModel(TopErrorCode.INVALID_OBJ, e).getResult();
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultErrorModel(TopErrorCode.GENERAL_ERR, e).getResult();
        }
    }

    /**
     * 删除指定tokenId和metaId数据
     *
     * @param metaId  meta id
     * @param tokenId token id
     * @return Result
     */
    public Result delTokenData(String metaId ,String tokenId) {
        try {
            tokenQueryFacadeEntity.deleteTokenDatas(new TokenDeleteModel(metaId, new ArrayList<String>(){{add(tokenId);}}));
            return Result.success();
        } catch (TopException e) {
            return new ResultErrorModel(e).getResult();
        } catch (DataAccessException e) {
            e.printStackTrace();
            return new ResultErrorModel(TopErrorCode.INVALID_OBJ, e).getResult();
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultErrorModel(TopErrorCode.GENERAL_ERR, e).getResult();
        }
    }

}
