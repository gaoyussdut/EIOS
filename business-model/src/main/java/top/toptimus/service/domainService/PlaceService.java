package top.toptimus.service.domainService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.toptimus.common.enums.MetaTypeEnum;
import top.toptimus.common.result.Result;
import top.toptimus.entity.meta.query.MetaQueryFacadeEntity;
import top.toptimus.entity.place.PlaceRedisEntity;
import top.toptimus.entity.security.query.UserQueryFacadeEntity;
import top.toptimus.entity.tokendata.event.TokenEventEntity;
import top.toptimus.entity.tokendata.query.TokenQueryFacadeEntity;
import top.toptimus.entity.tokentemplate.query.TokenTemplateQueryFacadeEntity;
import top.toptimus.place.place_deprecated.BillTokenSaveResultDTO;
import top.toptimus.resultModel.ResultErrorModel;
import top.toptimus.schema.BillPreviewDTO;
import top.toptimus.tokenTemplate.GeneralViewDTO;
import top.toptimus.tokenTemplate.TokenTemplateDefinitionDTO;
import top.toptimus.tokendata.TokenDataDto;

/**
 * 表单服务
 *
 * @author gaoyu
 * @since 2019-09-17
 */
@Service
public class PlaceService {
    @Autowired
    private PlaceRedisEntity placeRedisEntity;
    @Autowired
    private TokenTemplateQueryFacadeEntity tokenTemplateQueryFacadeEntity;
    @Autowired
    private TokenQueryFacadeEntity tokenQueryFacadeEntity;
    @Autowired
    private MetaQueryFacadeEntity metaQueryFacadeEntity;
    @Autowired
    private UserQueryFacadeEntity userQueryFacadeEntity;
    @Autowired
    private TokenEventEntity tokenEventEntity;


    /**
     * 获取schemaDTO
     *
     * @param tokenTemplateId ttid
     * @param tokenId         表头token id
     * @return BillPreviewDTO billPreview
     */
    public BillPreviewDTO getPreview(String tokenTemplateId, String tokenId) {
        TokenTemplateDefinitionDTO tokenTemplateDefinitionDTO =
                tokenTemplateQueryFacadeEntity.findById(tokenTemplateId);

        return this.placeRedisEntity
                .findPlace(
                        tokenId
                        , userQueryFacadeEntity.findByAccessToken().getId()
                )   //  从线程池中取出库所
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

    /*
        以下部分全部 TODO
        集成进placeRedisEntity
     */

    /**
     * 保存表头数据
     *
     * @param tokenDataDto tokendata
     * @param metaId       表头meta
     * @return Result
     */
    public Result saveBillToken(TokenDataDto tokenDataDto, String metaId) {
        return placeRedisEntity.saveBillToken(tokenDataDto, metaId);
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
            return Result.success(
                    new GeneralViewDTO(tokenTemplateId, tokenTemplateDefinitionDTO.getBillMetaId())
                            .build(
                                    tokenQueryFacadeEntity.getTokenDataList(tokenTemplateDefinitionDTO.getBillMetaId(), pageSize, pageNo)
                            )
            );
        } catch (Exception e) {
            return new ResultErrorModel(e).getResult();
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

    /**
     * 保存分录
     *
     * @param billTokenId  表头token id
     * @param billMetaId   表头meta id
     * @param tokenDataDto 分录数据
     * @param entryMetaId  分录meta id
     * @param entryType    保存关联单据用，比较复杂，TODO
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
     * @param billMetaId   表头meta id    TODO
     * @param billTokenId  表头token id
     * @param entryMetaId  分录meta id    TODO
     * @param entryTokenId 分录token id
     * @return result
     */
    public Result deleteEntryToken(String billMetaId, String billTokenId, String entryMetaId, String entryTokenId) {

        tokenEventEntity.delRel(billTokenId, entryTokenId);

//        List<MetaRelDTO> metaRelDTOS =
//                metaQueryFacadeEntity.getRelMetasByTokenTemplateId(billMetaId);

//        metaRelDTOS.forEach(metaRelDTO -> {
//            if (metaRelDTO.getEntryMetaId().equals(entryMetaId)) {
//                if (metaRelDTO.getMetaType().equals(MetaTypeEnum.ENTRY)) {
//                    tokenService.delTokenData(entryMetaId, entryTokenId);
//                }
//            }
//        });

        return Result.success();
    }
}
