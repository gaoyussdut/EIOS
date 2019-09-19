package top.toptimus.service.domainService.deprecated;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.toptimus.common.WordTemplate;
import top.toptimus.common.result.Result;
import top.toptimus.entity.meta.query.MetaQueryFacadeEntity;
import top.toptimus.entity.security.query.UserQueryFacadeEntity;
import top.toptimus.entity.tokendata.event.TokenEventEntity;
import top.toptimus.entity.tokendata.query.TokenQueryFacadeEntity;
import top.toptimus.entity.tokentemplate.query.TokenTemplateQueryFacadeEntity;
import top.toptimus.meta.TokenMetaInformationDto;
import top.toptimus.resultModel.ResultErrorModel;
import top.toptimus.service.BusinessUnitService;
import top.toptimus.service.domainService.TokenService;
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
public class PlaceService_Deprecated {

    public static Logger logger = LoggerFactory.getLogger(PlaceService_Deprecated.class);

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

//    /**
//     * 获取单据预览页面
//     *
//     * @param tokenTemplateId ttid
//     * @param tokenId         token id
//     * @return
//     */
//    public Result getPreview(String tokenTemplateId, String tokenId) {
//        try {
//            return Result.success(businessUnitService.getPreview(tokenTemplateId, tokenId));
//        } catch (Exception e) {
//            return new ResultErrorModel(e).getResult();
//        }
//    }


}
