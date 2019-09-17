package top.toptimus.entity.tokendata.query;

import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.toptimus.common.search.SearchInfo;
import top.toptimus.dao.token.MetaTokenRelationDao;
import top.toptimus.entity.meta.query.MetaQueryFacadeEntity;
import top.toptimus.entity.tokendata.event.TokenEventEntity;
import top.toptimus.exception.TopErrorCode;
import top.toptimus.exception.TopException;
import top.toptimus.meta.MetaRelation.MetaRelationDTO;
import top.toptimus.model.tokenData.base.TokenDataModel;
import top.toptimus.model.tokenModel.query.baseQueryFacade.EntryTokenIdQueryModel;
import top.toptimus.model.tokenModel.query.baseQueryFacade.TokenQueryModel;
import top.toptimus.model.tokenModel.query.baseQueryFacade.TokenQueryWithPageModel;
import top.toptimus.place.place_deprecated.PlaceDTO;
import top.toptimus.repository.token.MetaRelation.MetaTokenRelationRepository;
import top.toptimus.repository.token.TokenRepository;
import top.toptimus.token.relation.TokenRelDTO;
import top.toptimus.tokendata.TokenDataDto;
import top.toptimus.tokendata.TokenDataPageableDto;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * token query facade
 *
 * @author gaoyu
 * @since 2018-6-20
 */
@Component
public class TokenQueryFacadeEntity {
    /**
     * entity
     */
    @Autowired
    private TokenDataSqlRetrieveEntity tokenDataSqlRetrieveEntity;
    @Autowired
    private TokenMetaQueryFacadeEntity tokenMetaQueryFacadeEntity;
    @Autowired
    private MetaQueryFacadeEntity metaQueryFacadeEntity;
    @Autowired
    private TokenEventEntity tokenEventEntity;
    /**
     * repository
     */
    @Autowired
    private TokenRepository tokenRepository;
    @Autowired
    private MetaTokenRelationRepository metaTokenRelationRepository;


    /**
     * 获取分录数据
     *
     * @param billTokenId 表头tokenid
     * @param billMetaId  表头meta
     * @param entryMetaId 分录meta
     * @return List<TokenDataDto>
     */
    public List<TokenDataDto> getEntryTokenData(String billTokenId, String billMetaId, String entryMetaId) {
        return tokenDataSqlRetrieveEntity.generateQuerySql(
                new EntryTokenIdQueryModel(
                        metaQueryFacadeEntity.getMetaInfo(entryMetaId)   //  分录meta
                        , tokenDataSqlRetrieveEntity.getTableName(entryMetaId)  //  分录表名
                ).build(
                        tokenEventEntity.findRel(billTokenId, billMetaId, entryMetaId)   //  查分录tokenids
                )
        ).getTokenDataDtos();
    }

    /**
     * 根据metaId和tokenId取得数据 From DB
     *
     * @param metaId  metaId
     * @param tokenId 数据的tokenId
     * @return tokenDataDto 数据
     */
    public TokenDataDto getTokenData(String metaId, String tokenId) {
        List<TokenDataDto> tokenDataDtos = tokenDataSqlRetrieveEntity.generateQuerySql(new TokenQueryModel(
                Lists.newArrayList(tokenId)
                , tokenDataSqlRetrieveEntity.getTableName(metaId)
                , metaQueryFacadeEntity.getMetaInfo(metaId))).getTokenDataDtos();

        return tokenDataDtos != null && tokenDataDtos.size() > 0 ? tokenDataDtos.get(0) : null;
    }

    /**
     * 根据metaId取得一览信息 From DB
     *
     * @param metaId   metaId
     * @param pageSize 单页数量
     * @param pageNo   页码
     * @return TokenDataPageableDto
     */
    public TokenDataPageableDto getTokenDataList(String metaId, Integer pageSize, Integer pageNo) {
        return tokenDataSqlRetrieveEntity.generateQuerySql(new TokenQueryWithPageModel(
                metaId, new SearchInfo(), pageNo, pageSize)).getTokenDataPageableDto();
    }

    /**
     * 获取关联下的备查账数据
     *
     * @param billTokenId            表头tokenid
     * @param billMetaId             表头meta
     * @param masterMetaId           单据主数据meta
     * @param masterMemorandvnMetaId 关联备查账主数据meta
     * @return List<TokenDataDto>
     */
    private List<TokenDataDto> getMemorandvnTokenData(String billTokenId, String billMetaId, String masterMetaId, String masterMemorandvnMetaId) {
        return tokenDataSqlRetrieveEntity.generateQuerySql(
                new EntryTokenIdQueryModel(
                        metaQueryFacadeEntity.getMetaInfo(masterMemorandvnMetaId)          // 备查账meta
                        , tokenDataSqlRetrieveEntity.getTableName(masterMemorandvnMetaId)  // 备查账表名
                ).build(
                        tokenEventEntity.findRel(billTokenId, billMetaId, masterMetaId)    // 根据单据查询备查账tokenids
                )
        ).getTokenDataDtos();
    }

    /**
     * 根据业务单元id和指定的凭证metaId取未接收的凭证tokenIds
     *
     * @param businessUnitId    业务单元id
     * @param certificateMetaId 凭证metaId
     * @return List<String> 凭证tokenIds
     */
    public List<TokenDataDto> getCertificateTokenIData(String certificateMetaId, String businessUnitId) {
        return this.getMetaTokenData
                (
                        certificateMetaId
                        , metaQueryFacadeEntity
                                .getCertificateTokenIds
                                        (
                                                businessUnitId
                                                , certificateMetaId
                                        )
                );
    }

    /**
     * 获取指定的metaId和tokenIds对应的数据
     *
     * @param metaId   metaId
     * @param tokenIds tokenIds
     * @return List<TokenDataDto> 数据
     */
    public List<TokenDataDto> getMetaTokenData(String metaId, List<String> tokenIds) {
        return tokenDataSqlRetrieveEntity.generateQuerySql(new EntryTokenIdQueryModel(
                        metaQueryFacadeEntity.getMetaInfo(metaId)          // meta
                        , tokenDataSqlRetrieveEntity.getTableName(metaId)  // 表名
                ).build(tokenIds)
        ).getTokenDataDtos();
    }


    /**
     * TODO 表头token数据是否已经进去了
     *
     * @param tokenId    token id
     * @param billMetaId 表头meta id
     */
    public PlaceDTO getEntryTokenDatasMap(String tokenId, String billMetaId) {
        PlaceDTO placeDTO = tokenDataSqlRetrieveEntity.getPlace(tokenId, billMetaId);

//        tokenMetaQueryFacadeEntity.getMasterBillMetaRelation(placeDTO.getBillMetaId())  //  取单据表单主数据与关联表单的的关系
//                .getMasterMetaInfoDTOS().forEach(masterMetaInfoDTO -> {
//            if (masterMetaInfoDTO.getMetaRelEnum().equals(MetaRelEnum.ENTRY)) {
//                //  分录
//                placeDTO.getDatas().put(
//                        masterMetaInfoDTO.getEntryMasterMetaId()
//                        , this.getEntryTokenData(placeDTO.getBillTokenId(), placeDTO.getBillMetaId(), masterMetaInfoDTO.getEntryMasterMetaId())
//                );
//            } else {
//                //  关联
//                if (!placeDTO.getRelationEntryMetaIds().contains(masterMetaInfoDTO))
//                    placeDTO.getRelationEntryMetaIds().add(masterMetaInfoDTO);
//            }
//        });
        return placeDTO;
    }

    /**
     * 删除指定的metaId的tokenIds
     *
     * @param tokenDataModel token数据存储的充血模型
     */
    public void deleteTokenDatas(TokenDataModel tokenDataModel) {
        tokenRepository.deleteTokenByIds(tokenDataSqlRetrieveEntity.getTableName(tokenDataModel.getMetaId()), tokenDataModel.getTokenIdModel().getTokenIds());
    }

    /**
     * 查询单据相关的指定备查账数据
     *
     * @param billTokenId      表头tokenId
     * @param billMetaId       表头MetaId
     * @param memorandvnMetaId 备查账MetaId
     * @return List<TokenDataDto>
     */
    public List<TokenDataDto> getMemorandvnTokenDatas(String billTokenId, String billMetaId, String memorandvnMetaId) {
        // 根据备查账主数据metaId查询备查账对应的单剧meta
        List<MetaRelationDTO> metaRelationDaos = metaQueryFacadeEntity.findByMasterMemorandvnMetaId(memorandvnMetaId);
        if (metaRelationDaos != null && metaRelationDaos.size() > 0) {
            // 取得List<TokenDataDto>数据
            return this.getMemorandvnTokenData(billTokenId, billMetaId, metaRelationDaos.get(0).getMasterMetaId(), memorandvnMetaId);
        } else {
            return null;
        }
    }

    /**
     * 单据引用备查账
     *
     * @param billMetaId       单据主数据meta
     * @param billTokenId      单据表头tokenid
     * @param memorandvnMetaId 引用备查账的metaid
     * @param tokenIds         引用备查账的tokenids
     */
    public void createBillQuoteMemorandvn(String billMetaId, String billTokenId, String memorandvnMetaId, List<String> tokenIds) {
        // 根据备查账主数据metaId查询备查账对应的单剧meta
        List<MetaRelationDTO> metaRelationDaos = metaQueryFacadeEntity.findByMasterMemorandvnMetaId(tokenDataSqlRetrieveEntity.getTableName(memorandvnMetaId));
        if (metaRelationDaos != null && metaRelationDaos.size() > 0) {
            //根据单据主数据meta，单据表头tokenid，引用备查账的metaid查询已经存在的关联关系
            List<String> exitMemorandvnMetaId = new ArrayList<String>() {
                private static final long serialVersionUID = -8888983107130104092L;

                {
                    metaTokenRelationRepository
                            .findAllByBillTokenIdAndBillMetaIdAndEntryMetaId(billMetaId, billTokenId, metaRelationDaos.get(0).getMasterMetaId())
                            .forEach(metaTokenRelationDao -> add(metaTokenRelationDao.getBillTokenId())
                            );
                }
            };
            //清洗已存在的tokenid
            tokenIds.removeAll(exitMemorandvnMetaId);
            //保存新增的关系
            metaTokenRelationRepository.saveAll(
                    new ArrayList<MetaTokenRelationDao>() {
                        private static final long serialVersionUID = -8888983107130104092L;

                        {
                            tokenIds.forEach(tokenId -> add(new MetaTokenRelationDao(
                                            UUID.randomUUID().toString()
                                            , billMetaId
                                            , billTokenId
                                            , metaRelationDaos.get(0).getMasterMetaId()
                                            , tokenId
                                    )
                                    )
                            );
                        }
                    }
            );
        } else {
            throw new TopException(TopErrorCode.GENERAL_ERR);
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
    public void deleteBillQuoteMemorandvn(String billMetaId, String billTokenId, String memorandvnMetaId, String tokenId) {
        // 根据备查账主数据metaId查询备查账对应的单剧meta
        List<MetaRelationDTO> metaRelationDaos = metaQueryFacadeEntity.findByMasterMemorandvnMetaId(tokenDataSqlRetrieveEntity.getTableName(memorandvnMetaId));
        if (metaRelationDaos != null && metaRelationDaos.size() > 0) {
            metaTokenRelationRepository.deleteAllByBillMetaIdAndBillTokenIdAndEntryMetaIdAndEntryTokenId(billMetaId, billTokenId, metaRelationDaos.get(0).getMasterMetaId(), tokenId);
            metaTokenRelationRepository.refresh();
        } else {
            throw new TopException(TopErrorCode.GENERAL_ERR);
        }
    }

    /**
     * 获取关联单据的token
     *
     * @param metaId  meta id
     * @param tokenId token id
     * @return 表头和分录/关联单据的token关系
     */
    public List<TokenRelDTO> getRelTokenByBillMetaIdAndBillTokenId(String metaId, String tokenId) {
        return tokenRepository.getRelTokenByBillMetaIdAndBillTokenId(metaId, tokenId);
    }
}
