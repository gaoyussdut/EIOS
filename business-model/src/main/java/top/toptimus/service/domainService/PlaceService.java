package top.toptimus.service.domainService;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.toptimus.amqp.producer.TransitionEntity;
import top.toptimus.businessUnit.CertificateDefinitionDTO;
import top.toptimus.businessUnit.TaskInsDTO;
import top.toptimus.common.WordTemplate;
import top.toptimus.common.result.Result;
import top.toptimus.common.search.SearchInfo;
import top.toptimus.constantConfig.Constants;
import top.toptimus.entity.event.BusinessUnitEventEntity;
import top.toptimus.entity.event.TaskEventEntity;
import top.toptimus.entity.meta.query.ConfigQueryEntity;
import top.toptimus.entity.meta.query.MetaQueryFacadeEntity;
import top.toptimus.entity.place.PlaceRedisEntity;
import top.toptimus.entity.query.BusinessUnitFacadeQueryEntity;
import top.toptimus.entity.security.query.UserQueryFacadeEntity;
import top.toptimus.entity.tokendata.event.TokenEventEntity;
import top.toptimus.entity.tokendata.query.TokenMetaQueryFacadeEntity;
import top.toptimus.entity.tokendata.query.TokenQueryFacadeEntity;
import top.toptimus.exception.TopErrorCode;
import top.toptimus.exception.TopException;
import top.toptimus.meta.TokenMetaInformationDto;
import top.toptimus.place.BillTokenSaveResultDTO;
import top.toptimus.resultModel.ResultErrorModel;
import top.toptimus.service.BusinessUnitService;
import top.toptimus.tokendata.TokenDataDto;
import top.toptimus.tokendata.field.FkeyField;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 表单服务
 *
 * @author gaoyu
 * @since 2019-01-22
 */
@Service
public class PlaceService {

    public static Logger logger = LoggerFactory.getLogger(PlaceService.class);

    @Autowired
    private PlaceRedisEntity placeRedisEntity;
    @Autowired
    private TokenQueryFacadeEntity tokenQueryFacadeEntity;
    @Autowired
    private TokenEventEntity tokenEventEntity;
    @Autowired
    private TokenMetaQueryFacadeEntity tokenMetaQueryFacadeEntity;
    @Autowired
    private ConfigQueryEntity configQueryEntity;
    @Autowired
    private UserQueryFacadeEntity userQueryFacadeEntity;
    @Autowired
    private TransitionEntity transitionEntity;
    @Autowired
    private TaskEventEntity taskEventEntity;
    @Autowired
    private BusinessUnitEventEntity businessUnitEventEntity;
    @Autowired
    private MetaQueryFacadeEntity metaQueryFacadeEntity;
    @Autowired
    private BusinessUnitService businessUnitService;
    @Autowired
    private BusinessUnitFacadeQueryEntity businessUnitFacadeQueryEntity;


    /**
     * 新建表单
     *
     * @param billMetaId 表头meta id
     * @return Result
     */
    public Result createBill(String businessUnitCode, String billMetaId) {
        //判断该meta是否可新增
        if (businessUnitService.isCreatable(businessUnitCode, billMetaId)) {
            TokenDataDto tokenDataDto = placeRedisEntity.createBill(billMetaId);
            //创建任务实例
            businessUnitEventEntity.saveTaskIns(new TaskInsDTO(businessUnitCode, billMetaId, tokenDataDto.getTokenId()));
            try {
                return Result.success(
                        tokenDataDto
                );
            } catch (Exception e) {
                return new ResultErrorModel(e).getResult();
            }
        } else {
            return new ResultErrorModel(TopErrorCode.BUSINESSUNIT_FAILED_CREATE).getResult();
        }

    }

    /**
     * 取表头数据
     * 同步：将place和关系塞进placeRedisEntity里面
     * 异步：将place和关系存入redis
     *
     * @param billMetaId 表头meta id
     * @param tokenId    表头token id
     * @return Result
     */
    public Result getBillToken(String billMetaId, String tokenId) {
        try {
            return Result.success(tokenQueryFacadeEntity.getTokenData(billMetaId, tokenId));
        } catch (Exception e) {
            return new ResultErrorModel(e).getResult();
        }

        //  从数据库中取出库所，并缓存
//        return placeRedisEntity.initCache(
//                tokenMetaQueryFacadeEntity.buildCachePlace(tokenId, billMetaId)    //  取出库所
//        );

    }

//    /**
//     * 创建关联表单时获取关联表单表头数据
//     *
//     * @param preMetaId  前置单据metaid
//     * @param preTokenId 前置单据表头tokenid
//     * @param metaId     需创建单据的metaid
//     * @return Result
//     */
//    public Result getRelBillToken(String preMetaId, String preTokenId, String metaId) {
//        //下推单据的表头tokenid
////        String billTokenId = tokenMetaQueryFacadeEntity.excuteStoredProcedure(preMetaId, preTokenId, metaId);
//        //TODO 等待下推规则完成
//        String billTokenId = UUID.randomUUID().toString();
//        PlaceDTO placeDTO = new PlaceDTO(metaId, new TokenDataDto().build(billTokenId));
//
//        TransformationDTO transformationDTO = new TransformationDTO(
//                "PUSHDOWN"
//                , metaId
//                , placeRedisEntity.findPlace(preTokenId
//                , userQueryFacadeEntity.findByAccessToken().getId()).getPlaceDTO()
//                , placeDTO
//        );
//
//        transitionEntity.generatePushDownTransition(transformationDTO);
//
//        placeDTO.buildTransition(transformationDTO);
//
//        if (!StringUtils.isEmpty(billTokenId)) {
//            //将place和关系存入placeRedisEntity
//            return placeRedisEntity.createBillRel(
//                    preTokenId
//                    , preMetaId
//                    , placeDTO//tokenDataSqlRetrieveEntity.getPlace(billTokenId, metaId)
//            );
//
//        } else {
//            throw new RuntimeException("下推单据存储过程错误");
//        }
//
//    }

    /**
     * 取分录数据
     *
     * @param billMetaId  表头meta id
     * @param billTokenId 表头token id
     * @param entryMetaId 分录meta id
     * @return Result
     */
    public Result getEntryToken(String billMetaId, String billTokenId, String entryMetaId) {
        return Result.success(
                tokenQueryFacadeEntity.getEntryTokenData(billTokenId, billMetaId, entryMetaId)
        ); //  数据库中取
//        List<TokenDataDto> tokenDataDtos = placeRedisEntity.findPlace(billTokenId, userQueryFacadeEntity.findByAccessToken().getId()).getPlaceDTO().getDatas().get(entryMetaId);
//        if (null != tokenDataDtos)
//            return Result.success(tokenDataDtos);    //  缓存数据
//        else
//            return Result.success(
//                    tokenQueryFacadeEntity.getEntryTokenData(billTokenId, billMetaId, entryMetaId)
//            ); //  数据库中取
    }

//    /**
//     * 提交表头数据
//     * TODO 页面要刷新的token通知前端
//     *
//     * @param tokenDataDto token数据
//     * @param metaId       meta id
//     * @return Result
//     */
//    public Result submitBillToken(TokenDataDto tokenDataDto, String metaId) {
//        Result result = placeRedisEntity.submitToken(tokenDataDto.getTokenId(), tokenDataDto, metaId, true);
//
//        PlaceDTO currentPlace = placeRedisEntity.findPlace(tokenDataDto.getTokenId(), userQueryFacadeEntity.findByAccessToken().getId()).getPlaceDTO();
//        if (currentPlace.getPreMetaTokenRelationDTO() != null) {
//            PlaceDTO prePlace = placeRedisEntity.findPlace(currentPlace.getPreMetaTokenRelationDTO().getSourceBillTokenId(), userQueryFacadeEntity.findByAccessToken().getId()).getPlaceDTO();
//            TransformationDTO transformationDTO = new TransformationDTO(
//                    "REVERSE"
//                    , ""
//                    , prePlace
//                    , currentPlace
//            );
//            //变迁逻辑
//            transitionEntity.generatePushDownTransition(transformationDTO);    //  TODO    返回值 等待反写规则完成
//        }
//        // 执行存储备查帐
////        MetaRelationDTO metaRelationDTO = tokenMetaQueryFacadeEntity.getMetaRelationByMetaId(metaId);
////        if (!StringUtils.isEmpty(metaRelationDTO.getStoredProcedure())) {
////            transitionEntity.generateMemorandvnTransition(metaRelationDTO.getStoredProcedure(), tokenDataDto.getTokenId());
////        }
//        // 初始化任务
//        taskEventEntity.createTaskIns(metaId, tokenDataDto.getTokenId());
//        return result;
//    }
    /**
     * saveBillToken
     */
    /**
     * 保存表头数据
     *
     * @param tokenDataDto tokendata
     * @param metaId       表头meta
     * @return Result
     */
    public Result saveBillToken(TokenDataDto tokenDataDto, String metaId) {
        placeRedisEntity.saveBillToken(tokenDataDto, metaId);
        return Result.success();
    }


//    /**
//     * 提交汇签意见
//     *
//     * @param countersignSubmitDTO  汇签信息提交DTO
//     * @param countersignTaskMetaId 汇签任务metaId
//     * @param countersignMetaId     汇签信息metaId
//     * @return Result
//     */
//    public Result submitCountersignToken(CountersignSubmitDTO countersignSubmitDTO, String countersignTaskMetaId, String countersignMetaId) {
//        try {
//            //保存数据
//            Result result = placeRedisEntity.submitToken(
//                    countersignSubmitDTO.getCountersignTokenData().getTokenId()
//                    , countersignSubmitDTO.getCountersignTokenData()
//                    , countersignMetaId
//                    , true);
//            //保存关系
//            tokenEventEntity.saveCountersignRel(countersignSubmitDTO, countersignTaskMetaId, countersignMetaId);
//            //改状态 改单据状态,汇签通过
////            taskEventEntity.updateCountersignStatus(
////                    tokenMetaQueryFacadeEntity.findBillInfoByTaskTokenId(
////                            countersignSubmitDTO.getCountersignTaskTokenIds()
////                    )
////            );
//            return result;
//        } catch (Exception e) {
//            e.printStackTrace();
//            throw new RuntimeException("提交汇签数据失败");
//        }
//
//    }


    /**
     * 新增分录
     *
     * @param billTokenId
     * @param billMetaId
     * @param entryMetaId
     * @return
     */
    public Result createEntryToken(String billTokenId, String billMetaId, String entryMetaId) {
        TokenDataDto tokenDataDto = new TokenDataDto();
//        PlaceDTO placeDTO = placeRedisEntity.findPlace(billTokenId, userQueryFacadeEntity.findByAccessToken().getId()).getPlaceDTO();
//        if (placeDTO.getPreMetaTokenRelationDTO() != null) {
//            PlaceDTO prePlaceDTO = placeRedisEntity.findPlace(placeDTO.getPreMetaTokenRelationDTO().getSourceBillTokenId(), userQueryFacadeEntity.findByAccessToken().getId()).getPlaceDTO();
//            TransformationDTO transformationDTO = new TransformationDTO(
//                    "PUSHDOWN"
//                    , metaId
//                    , prePlaceDTO
//                    , placeDTO
//            );
//            transitionEntity.generatePushDownTransition(transformationDTO);
//            if (transformationDTO.getTransformationDatas().keySet().contains(metaId)) {
//                tokenDataDto = transformationDTO.getTransformationDatas().get(metaId).get(0);
//            }
//        }
        return placeRedisEntity.createEntryToken(billTokenId, billMetaId, tokenDataDto, entryMetaId);
    }

    /**
     * 保存分录
     *
     * @param billTokenId
     * @param billMetaId
     * @param tokenDataDto
     * @param entryMetaId
     * @return Result
     */
    public Result saveEntryToken(String billTokenId, String billMetaId, TokenDataDto tokenDataDto, String entryMetaId) {
        return placeRedisEntity.saveEntryToken(billTokenId, billMetaId, tokenDataDto, entryMetaId);
    }

    /**
     * 保存分录 同步
     *
     * @param billTokenId  表头TokenId
     * @param billMetaId   表头MetaId
     * @param tokenDataDto 数据
     * @param entryMetaId  分录MetaId
     * @return Result       结果
     */
    public Result saveEntryTokenSync(String billTokenId, String billMetaId, TokenDataDto tokenDataDto, String entryMetaId) {
        try {
            String authId = userQueryFacadeEntity.findByAccessToken().getId();
            BillTokenSaveResultDTO billTokenSaveResultDTO = new BillTokenSaveResultDTO(
                    billTokenId
                    , billMetaId
                    , tokenDataDto
                    , entryMetaId
                    , authId
            );
            //1.保存tokendata
            tokenEventEntity.saveDatas(tokenDataDto
                    , entryMetaId
            );
            //2.同步关系到pg
            tokenEventEntity.saveRel(billTokenSaveResultDTO);
            // 3.返回result(BillTokenSaveResultDTO)
            return Result.success(billTokenSaveResultDTO);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultErrorModel(e).getResult();
        }
    }

    /**
     * 删除分录
     *
     * @param billTokenId  表头token id
     * @param entryMetaId  分录meta id
     * @param entryTokenId 分录token id
     * @return result
     */
    public Result deleteEntryToken(String billTokenId, String entryMetaId, String entryTokenId) {
        return placeRedisEntity.deleteEntryToken(billTokenId, entryMetaId, entryTokenId);
    }

    /**
     * 删除单据
     *
     * @param billTokenId 表头tokenId
     * @return result
     */
    public Result deleteBillToken(String billTokenId) {
        return placeRedisEntity.deleteBillToken(billTokenId);
    }

    /**
     * 查询单据相关的指定备查账数据
     *
     * @param billTokenId      表头tokenId
     * @param billMetaId       表头metaId
     * @param memorandvnMetaId 备查帐的metaId
     * @return Result
     */
    public Result getMemorandvnTokenDatas(String billTokenId, String billMetaId, String memorandvnMetaId) {
        try {
            return Result.success(tokenQueryFacadeEntity.getMemorandvnTokenDatas(billTokenId, billMetaId, memorandvnMetaId)); // 当前查询的数据
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("查询备查账一览失败");
        }
    }

    /**
     * 单据引用备查账
     *
     * @param billMetaId       单据主数据meta
     * @param billTokenId      单据表头tokenid
     * @param memorandvnMetaId 引用备查账的metaid
     * @param tokenIds         引用备查账的tokenids
     * @return Result
     */
    public Result createBillQuoteMemorandvn(String billMetaId, String billTokenId, String memorandvnMetaId, List<String> tokenIds) {
        try {
            tokenQueryFacadeEntity.createBillQuoteMemorandvn(billMetaId, billTokenId, memorandvnMetaId, tokenIds);
            return Result.success();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("单据引用备查账失败");// TODO 返回错误结构
        }
    }

    /**
     * 解除单据对备查账的引用关系
     *
     * @param billMetaId       单据主数据meta
     * @param billTokenId      单据表头tokenid
     * @param memorandvnMetaId 引用备查账的metaid
     * @param tokenId          引用备查账的tokenid
     */
    public Result deleteBillQuoteMemorandvn(String billMetaId, String billTokenId, String memorandvnMetaId, String tokenId) {
        try {
            tokenQueryFacadeEntity.deleteBillQuoteMemorandvn(billMetaId, billTokenId, memorandvnMetaId, tokenId);
            return Result.success();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("解除单据对备查账的引用关系失败");// TODO 返回错误结构
        }
    }

    /**
     * 一览
     * 备查账查询数据(带有link信息)
     *
     * @param metaId     当前的基础资料备查账的metaId
     * @param selectType 当前选择的type页
     * @param pageNumber 当前页
     * @param pageSize   当前页大小
     * @return ResultContext
     */
    public Result getMemorandvnData(
            String metaId
            , String selectType
            , Integer pageNumber
            , Integer pageSize
    ) {

        try {
            return Result.success(
                    placeRedisEntity.getMemorandvnData(
                            metaId
                            , new SearchInfo(configQueryEntity.findMetaSearchConfigById(metaId, selectType))
                            , pageNumber
                            , pageSize
                    )); // 当前查询的数据
        } catch (Exception e) {
            logger.error("查询备查账一览", e);
            if (e instanceof TopException) {
                TopException topException = (TopException) e;
                return new ResultErrorModel(topException.getErrorCode(), e).getResult();
            }
            e.printStackTrace();
            return new ResultErrorModel(e).getResult();
        }
    }

    /**
     * 查询所有汇签意见
     *
     * @param metaId            单据metaId
     * @param tokenId           单据tokenId
     * @param countersignMetaId 汇签metaId
     * @return Result
     */
    public Result getCountersigns(String metaId, String tokenId, String countersignMetaId) {
        //  从数据库中取出汇签信息，并缓存
        return placeRedisEntity.initCache(
                tokenMetaQueryFacadeEntity.buildCountersigns( //  取出库所
                        metaId
                        , tokenId
                        , countersignMetaId
                )
        );
    }

    /**
     * 根据taskId查询单条汇签意见
     *
     * @param metaId            单据metaId
     * @param tokenId           单据tokenId
     * @param taskId            单据任务Id
     * @param countersignMetaId 汇签metaId
     * @return Result
     */
    public Result getCountersign(String metaId, String tokenId, String taskId, String countersignMetaId) {
        //  从数据库中取出汇签信息，并缓存
        return placeRedisEntity.initCache(
                tokenMetaQueryFacadeEntity.buildCountersign( //  取出库所
                        metaId
                        , tokenId
                        , taskId
                        , countersignMetaId
                )
        );
    }

//    /**
//     * 提交汇签任务单据
//     *
//     * @param tokenDataDto          汇签任务tokendata
//     * @param countersignTaskMetaId 汇签任务metaId
//     * @param metaId                单据metaId
//     * @param tokenId               单据tokenId
//     * @return Result
//     */
//    public Result submitCountersignTaskToken(TokenDataDto tokenDataDto, String countersignTaskMetaId, String metaId, String tokenId) {
//        try {
//            //存数据
//            Result result = placeRedisEntity.submitToken(
//                    tokenDataDto.getTokenId()
//                    , tokenDataDto
//                    , countersignTaskMetaId
//                    , true);
//            //存关系
//            tokenEventEntity.saveCountersignTaskRel(
//                    taskEventEntity.getCurrentTask(metaId, tokenId)
//                    , countersignTaskMetaId
//                    , tokenDataDto.getTokenId()
//            );
//            //改状态
////            taskEventEntity.updateTaskStatus(metaId, tokenId, TaskStatusEnum.STATUS_SIGN_FAIL);
//            return result;
//        } catch (Exception e) {
//            e.printStackTrace();
//            throw new RuntimeException("提交汇签任务单据失败");
//        }
//
//
//    }

//    /**
//     * 批量新增分录
//     *
//     * @param metaId      分录metaid
//     * @param billTokenId 表头tokenid
//     * @return Result
//     */
//    public Result createEntryTokens(String metaId, String billTokenId, int count) {
//        return placeRedisEntity.createEntryToken(metaId
//                , billTokenId
//                , new ArrayList<TokenDataDto>() {
//                    private static final long serialVersionUID = 1742748055317443358L;
//
//                    {
//                        for (int i = 0; i < count; i++) {
//                            add(new TokenDataDto());
//                        }
//                    }
//                });
//    }

//    /**
//     * 批量存分录信息
//     *
//     * @param billTokenId   表头token id
//     * @param tokenDataDtos token数据
//     * @param metaId        meta id
//     * @return Result
//     */
//    public Result saveEntryTokens(String billTokenId, List<TokenDataDto> tokenDataDtos, String metaId) {
//        return placeRedisEntity.submitTokens(billTokenId, tokenDataDtos, metaId);
//    }

    /**
     * 获取分录meta
     *
     * @param billMetaId 表头meta
     */
    public Result getEntryMetas(String billMetaId) {
        try {
            return Result.success(
                    metaQueryFacadeEntity.getEntryMetas(billMetaId)
            );
        } catch (Exception e) {
            return new ResultErrorModel(e).getResult();
        }
    }

    /**
     * 获取业务单元可新增meta及可下推meta
     *
     * @param businessUnitCode 业务单元Id
     * @return Result
     */
    public Result getPushDownMeta(String businessUnitCode, String metaId, String tokenId) {
        try {
            return Result.success(
                    businessUnitService.getPushDownMeta(businessUnitCode, metaId, tokenId)
            );
        } catch (Exception e) {
            return new ResultErrorModel(e).getResult();
        }
    }

    /**
     * 获取业务单元可新增meta及可下推meta
     *
     * @param businessUnitCode 业务单元Id
     * @return Result
     */
    public Result getRelMeta(String businessUnitCode, String metaId) {
        try {
            if (StringUtils.isEmpty(metaId)) {
                metaId = Constants.empty_task_meta;
            }
            return Result.success(
                    businessUnitService.getRelMeta(businessUnitCode, metaId)
            );
        } catch (Exception e) {
            return new ResultErrorModel(e).getResult();
        }
    }

    /**
     * 获取业务节点的关联meta下的数据
     *
     * @param businessUnitCode 业务单元Id
     * @param preMetaId        前置metaId
     * @param preTokenId       前置tokenId
     * @param metaId           当前metaId
     * @return Result
     */
    public Result getRelData(String businessUnitCode, String preMetaId, String preTokenId, String metaId) {
        try {
            return Result.success(
                    businessUnitService.getRelData(businessUnitCode, preMetaId, preTokenId, metaId)
            );
        } catch (Exception e) {
            return new ResultErrorModel(e).getResult();
        }
    }

    /**
     * 手动下推
     *
     * @param businessUnitCode 业务单元Id
     * @param preMetaId        前置metaId
     * @param preTokenId       前置tokenId
     * @param metaId           下推到的metaId
     * @return Result
     */
    public Result pushDown(String businessUnitCode, String preMetaId, String preTokenId, String metaId) {
        try {
            TokenDataDto tokenDataDto = businessUnitService.pushDown(businessUnitCode, preMetaId, preTokenId, metaId);
            this.saveBillToken(
                    tokenDataDto
                    , metaId
            );
            return Result.success(tokenDataDto);
        } catch (Exception e) {
            return new ResultErrorModel(e).getResult();
        }
    }

    /**
     * 提交
     *
     * @param tokenDataDto 表头token
     * @param metaId       表头meta id
     * @return result
     */
    public Result submit(String businessUnitCode, TokenDataDto tokenDataDto, String metaId) {
        try {
            this.saveBillToken(tokenDataDto, metaId);
            // 状态规则等操作
            businessUnitService.submit(businessUnitCode, tokenDataDto, metaId);
            return Result.success();
        } catch (Exception e) {
            return new ResultErrorModel(e).getResult();
        }
    }

    /**
     * 根据metaId和tokenId生成模板
     *
     * @param metaId  metaId
     * @param tokenId 数据的tokenId
     * @return tokenDataDto 数据
     */
    public String generateWordDocByMetaToken(String metaId, String tokenId) {
        Map<String, Object> dataMap = new HashMap<>();
        // 1.取得表头数据
        TokenDataDto tokenDataDto = tokenQueryFacadeEntity.getTokenData(metaId, tokenId);
        for (FkeyField fkeyField : tokenDataDto.getFields()) {
            dataMap.put(fkeyField.getKey(), fkeyField.getJsonData());
        }
        // 2.取得分录Meta
        List<TokenMetaInformationDto> tokenMetaInformationDtos = metaQueryFacadeEntity.getEntryMetas(metaId);
        // 循环
        for (TokenMetaInformationDto tokenMetaInformationDto : tokenMetaInformationDtos) {
            // 2.1 取得分录数据
            List<TokenDataDto> tokenDataDtos = tokenQueryFacadeEntity.getEntryTokenData(tokenId, metaId, tokenMetaInformationDto.getTokenMetaId());
            // 2.2 设置数据
            List<Map<String, Object>> tempDatas = new ArrayList<>();
            for (TokenDataDto tokenDataDtoTemp : tokenDataDtos) {
                Map<String, Object> tempMap = new HashMap<>();
                // 数据信息
                for (FkeyField fkeyField : tokenDataDtoTemp.getFields()) {
                    tempMap.put(fkeyField.getKey(), fkeyField.getJsonData());
                }
                tempDatas.add(tempMap);
            }
            dataMap.put(tokenMetaInformationDto.getTokenMetaId(), tempDatas);
        }
        // 取得生成出的Word
        return WordTemplate.generateWordDoc(dataMap, metaId);
    }

    /**
     * 提交后调用存储过程
     *
     * @param metaId
     * @return
     */
    public Result submitStoreProcedure(String metaId, String tokenId) {
        try {
            String storedProcedure = businessUnitFacadeQueryEntity.getStoredProcedureByMetaId(metaId);
            businessUnitEventEntity.excuteStoredProcedure(storedProcedure, tokenId);
            return Result.success();
        } catch (Exception e) {
            return new ResultErrorModel(e).getResult();
        }
    }
}
