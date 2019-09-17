package top.toptimus.service.domainService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.toptimus.common.WordTemplate;
import top.toptimus.common.enums.MetaTypeEnum;
import top.toptimus.common.result.Result;
import top.toptimus.entity.meta.query.MetaQueryFacadeEntity;
import top.toptimus.entity.place.PlaceRedisEntity;
import top.toptimus.entity.security.query.UserQueryFacadeEntity;
import top.toptimus.entity.tokendata.event.TokenEventEntity;
import top.toptimus.entity.tokendata.query.TokenQueryFacadeEntity;
import top.toptimus.entity.tokentemplate.query.TokenTemplateQueryFacadeEntity;
import top.toptimus.meta.TokenMetaInformationDto;
import top.toptimus.meta.relation.MetaRelDTO;
import top.toptimus.place.place_deprecated.BillTokenSaveResultDTO;
import top.toptimus.resultModel.ResultErrorModel;
import top.toptimus.schema.BillPreviewDTO;
import top.toptimus.service.BusinessUnitService;
import top.toptimus.tokenTemplate.GeneralViewDTO;
import top.toptimus.tokenTemplate.TokenTemplateDefinitionDTO;
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
    private TokenQueryFacadeEntity tokenQueryFacadeEntity;
    @Autowired
    private TokenEventEntity tokenEventEntity;
    @Autowired
    private UserQueryFacadeEntity userQueryFacadeEntity;
    @Autowired
    private MetaQueryFacadeEntity metaQueryFacadeEntity;
    @Autowired
    private BusinessUnitService businessUnitService;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private TokenTemplateQueryFacadeEntity tokenTemplateQueryFacadeEntity;
    @Autowired
    private PlaceRedisEntity placeRedisEntity;


    /**
     * 获取schemaDTO
     *
     * @param tokenTemplateId ttid
     * @param tokenId         表头token id
     * @return BillPreviewDTO billPreview
     */
    public BillPreviewDTO getBillPreview(String tokenTemplateId, String tokenId) {
        TokenTemplateDefinitionDTO tokenTemplateDefinitionDTO =
                tokenTemplateQueryFacadeEntity.findById(tokenTemplateId);

        return this.placeRedisEntity.findPlace(tokenId, null/*TODO*/)   //  从线程池中取出库所
                .getPlaceDTO()  //  place
                .build( //  构建库所表头分录的ttid信息、token数据关系、meta关系
                        tokenTemplateDefinitionDTO
                        , tokenQueryFacadeEntity.getRelTokenByBillMetaIdAndBillTokenId(
                                tokenTemplateDefinitionDTO.getBillMetaId()
                                , tokenId
                        )
                        , metaQueryFacadeEntity.getRelMetasByTokenTemplateId(tokenTemplateDefinitionDTO.getBillMetaId())
                ).generateBillPreviewDTO();
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

    /**
     * 保存表头数据
     *
     * @param tokenDataDto tokendata
     * @param metaId       表头meta
     * @return Result
     */
    public Result saveBillToken(TokenDataDto tokenDataDto, String metaId) {
        String authId = userQueryFacadeEntity.findByAccessToken().getId();
        BillTokenSaveResultDTO billTokenSaveResultDTO = new BillTokenSaveResultDTO(metaId, tokenDataDto, authId);
        //保存表头数据到PG
        tokenEventEntity.saveDatas(
                billTokenSaveResultDTO
                        .getBillTokenResultBody()
                        .getBillTokenData()
                , billTokenSaveResultDTO
                        .getBillTokenResultBody()
                        .getBillMetaId()
        );
        return Result.success();
    }

    /**
     * 保存分录
     *
     * @param billTokenId  表头token id
     * @param billMetaId   表头meta id
     * @param tokenDataDto 分录数据
     * @param entryMetaId  分录meta id
     * @return Result
     */
    public Result saveEntryToken(String billTokenId, String billMetaId, TokenDataDto tokenDataDto, String entryMetaId, MetaTypeEnum entryType) {

        String authId = userQueryFacadeEntity.findByAccessToken().getId();
        BillTokenSaveResultDTO billTokenSaveResultDTO = new BillTokenSaveResultDTO(
                billTokenId
                , billMetaId
                , tokenDataDto
                , entryMetaId
                , authId
        );
        tokenEventEntity.saveDatas(
                billTokenSaveResultDTO
                        .getBillTokenResultBody()
                        .getEntryTokenData()
                , billTokenSaveResultDTO
                        .getBillTokenResultBody()
                        .getEntryMetaId()
        );
        //2.同步关系到pg
        tokenEventEntity.saveRel(billTokenSaveResultDTO);
        return Result.success(billTokenSaveResultDTO);
    }

    /**
     * 删除分录
     *
     * @param billMetaId   表头meta id
     * @param billTokenId  表头token id
     * @param entryMetaId  分录meta id
     * @param entryTokenId 分录token id
     * @return result
     */
    public Result deleteEntryToken(String billMetaId, String billTokenId, String entryMetaId, String entryTokenId) {

        tokenEventEntity.delRel(billTokenId, entryTokenId);

        List<MetaRelDTO> metaRelDTOS =
                metaQueryFacadeEntity.getRelMetasByTokenTemplateId(billMetaId);

        metaRelDTOS.forEach(metaRelDTO -> {
            if (metaRelDTO.getEntryMetaId().equals(entryMetaId)) {
                if (metaRelDTO.getMetaType().equals(MetaTypeEnum.ENTRY)) {
                    tokenService.delTokenData(entryMetaId, entryTokenId);
                }
            }
        });

        return Result.success();
    }

    /**
     * 删除单据
     *
     * @param tokenTemplateId tokenTemplateId
     * @param tokenId         tokenId
     * @return result
     */
    public Result deleteBillToken(String tokenTemplateId, String tokenId) {
        TokenTemplateDefinitionDTO tokenTemplateDefinitionDTO = tokenTemplateQueryFacadeEntity.findById(tokenTemplateId);
        tokenService.delTokenData(tokenTemplateDefinitionDTO.getBillMetaId(), tokenId);
        return Result.success();
    }

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
     * 获取单据预览页面
     *
     * @param tokenTemplateId ttid
     * @param tokenId         token id
     * @return
     */
    public Result getPreview(String tokenTemplateId, String tokenId) {
        try {
            return Result.success(businessUnitService.getPreview(tokenTemplateId, tokenId));
        } catch (Exception e) {
            return new ResultErrorModel(e).getResult();
        }
    }

    /**
     * 获取单据一览
     *
     * @param tokenTemplateId ttid
     * @return result
     */
    public Result getGeneralView(String tokenTemplateId, Integer pageSize, Integer pageNo) {
        try {
            TokenTemplateDefinitionDTO tokenTemplateDefinitionDTO = tokenTemplateQueryFacadeEntity.findById(tokenTemplateId);
            GeneralViewDTO generalViewDTO = new GeneralViewDTO(tokenTemplateId, tokenTemplateDefinitionDTO.getBillMetaId());
            generalViewDTO.build(tokenQueryFacadeEntity.getTokenDataList(tokenTemplateDefinitionDTO.getBillMetaId(), pageSize, pageNo));
            return Result.success(generalViewDTO);
        } catch (Exception e) {
            return new ResultErrorModel(e).getResult();
        }
    }
}
