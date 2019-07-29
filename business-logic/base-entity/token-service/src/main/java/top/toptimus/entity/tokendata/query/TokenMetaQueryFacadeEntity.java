package top.toptimus.entity.tokendata.query;

import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import top.toptimus.common.CurrentPage;
import top.toptimus.dao.meta.BillMetaStoredProcedureDao;
import top.toptimus.dao.meta.MasterBillMetaRelationDao;
import top.toptimus.dao.meta.MetaRelationDao;
import top.toptimus.dao.token.MetaTokenRelationDao;
import top.toptimus.entity.meta.query.MetaQueryFacadeEntity;
import top.toptimus.entity.security.query.UserQueryFacadeEntity;
import top.toptimus.merkle.MerklePlaceModel;
import top.toptimus.meta.MetaRelation.MasterBillMetaRelationDTO;
import top.toptimus.meta.MetaRelation.MasterMetaInfoDTO;
import top.toptimus.meta.MetaRelation.MetaRelationDTO;
import top.toptimus.meta.SelfDefiningMetaDTO;
import top.toptimus.meta.TokenMetaInformationDto;
import top.toptimus.meta.signGroup.CountersignStatusUpdateDTO;
import top.toptimus.place.PlaceDTO;
import top.toptimus.relation.MetaTokenRelationDTO;
import top.toptimus.relation.PreMetaTokenRelationDTO;
import top.toptimus.repository.meta.CountersignRepository;
import top.toptimus.repository.meta.MetaRelation.BillMetaStoredProcedureRepository;
import top.toptimus.repository.meta.MetaRelation.MasterBillMetaRelationRepository;
import top.toptimus.repository.meta.MetaRelation.MetaRelationRepository;
import top.toptimus.repository.meta.SelfDefiningMetaRepository;
import top.toptimus.repository.meta.TokenMetaInformationRepository;
import top.toptimus.repository.token.MetaRelation.MetaTokenRelationRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * token meta查询
 * 貌似说的是token和meta关系，待整理，乱糟糟
 *
 * @author gaoyu
 */
@Component
public class TokenMetaQueryFacadeEntity {
    @Autowired
    private TokenMetaInformationRepository tokenMetaInformationRepository;
    @Autowired
    private SelfDefiningMetaRepository selfDefiningMetaRepository;
    @Autowired
    private MetaRelationRepository metaRelationRepository;
    @Autowired
    private MetaTokenRelationRepository metaTokenRelationRepository;
    @Autowired
    private MasterBillMetaRelationRepository masterBillMetaRelationRepository;
    @Autowired
    private BillMetaStoredProcedureRepository billMetaStoredProcedureRepository;
    @Autowired
    private CountersignRepository countersignRepository;
    @Autowired
    private TokenQueryFacadeEntity tokenQueryFacadeEntity;
    @Autowired
    private UserQueryFacadeEntity userQueryFacadeEntity;
    @Autowired
    private MetaQueryFacadeEntity metaQueryFacadeEntity;

    @Value("${foreign_key_service.url}")
    private String foreignKeyServiceUrl;  //外键服务的url
    @Value("${foreign_key_service.talend_id}")
    private String foreignKeyServiceTalendId;  //外键服务talendId

    /**
     * 取得meta一览
     *
     * @param pageNo   页码
     * @param pageSize 页宽
     * @return meta信息定义，token名和类型
     */
    public CurrentPage<TokenMetaInformationDto> findAllTokenMetaInformation(int pageNo, int pageSize) {
        return tokenMetaInformationRepository.findAllTokenMetaInformation(pageNo, pageSize);
    }

    /**
     * 获取用户自定义Meta
     *
     * @param userId           user id
     * @param memorandvnMetaId 备查账meta id
     * @return Result
     */
    public SelfDefiningMetaDTO getSelfDefiningMeta(String userId, String memorandvnMetaId) {
        return selfDefiningMetaRepository.getSelfDefiningMeta(userId, memorandvnMetaId);
    }

    /**
     * 主数据meta获取单据内meta关系
     *
     * @param metaId 主数据meta
     * @return metaRelationDTO
     */
    public MetaRelationDTO getMasterDataMetaRelation(String metaId) {
        Optional<MetaRelationDao> metaRelationDaoOptional = metaRelationRepository.findById(metaId);
        return metaRelationDaoOptional.map(metaRelationDao -> new MetaRelationDTO(
                metaRelationDao.getMasterMetaId()
                , metaRelationDao.getMasterMetaName()
                , metaRelationDao.getViewMetaDTOList()
                , metaRelationDao.getMasterMemorandvnMetaId()
                , metaRelationDao.getViewMemorandvnDTOList()
                , metaRelationDao.getStoredProcedure()
        )).orElse(null);
    }

    /**
     * 保存主数据meta单据内meta关系(保存)
     *
     * @param metaRelationDTO meta关系dto
     */
    public void saveMasterDataMetaRelation(MetaRelationDTO metaRelationDTO) {
        metaRelationRepository.save(new MetaRelationDao(
                metaRelationDTO.getMasterMetaId(),
                metaRelationDTO.getMasterMetaName(),
                metaRelationDTO.getViewMetaDTOList(),
                metaRelationDTO.getMasterMemorandvnMetaId(),
                metaRelationDTO.getViewMemorandvnDTOList(),
                metaRelationDTO.getStoredProcedure()
        ));
    }


    /**
     * 根据 表头tokenid表头metaid和分录metaid查分录tokenids
     *
     * @param billTokenId 表头token id
     * @param billMetaId  表头meta id
     * @param entryMetaId 分录meta id
     * @return List<String> tokenids
     */
    public List<String> getMetaTokenRelationDTO(String billTokenId, String billMetaId, String entryMetaId) {

        List<MetaTokenRelationDao> metaTokenRelationDaos = metaTokenRelationRepository.findAllByBillTokenIdAndBillMetaIdAndEntryMetaId(billTokenId, billMetaId, entryMetaId);
        return new ArrayList<String>() {
            private static final long serialVersionUID = 8954092241002107066L;

            {
                metaTokenRelationDaos.forEach(metaTokenRelationDao -> add(metaTokenRelationDao.getEntryTokenId()));
            }
        };
    }

    /**
     * 创建单据的关联关系，创建place接口，用来放在缓存中
     *
     * @param merklePlaceModel 所model，带缓存主键
     * @return 库所
     */
    public PlaceDTO buildCachePlace(MerklePlaceModel merklePlaceModel) {
        return this.buildCachePlace(
                merklePlaceModel.getPlaceDTO().getPreMetaTokenRelationDTO().getSourceBillTokenId()
                , merklePlaceModel.getPlaceDTO().getPreMetaTokenRelationDTO().getSourceBillMetaId()
        );
    }


    /**
     * 创建单据的关联关系，创建place接口，用来放在缓存中
     *
     * @param tokenId    表头token id
     * @param billMetaId 表头meta id
     * @return 库所
     */
    public PlaceDTO buildCachePlace(String tokenId, String billMetaId) {
        PlaceDTO placeDTO = tokenQueryFacadeEntity.getEntryTokenDatasMap(tokenId, billMetaId);
        return placeDTO.build(
                //  构造关联token的关系    K：meta id， V：token id 列表
                new ArrayList<MetaTokenRelationDTO>() {
                    private static final long serialVersionUID = 544048363383585490L;

                    {
                        placeDTO.getRelationEntryMetaIds().forEach(metaid ->
                                metaTokenRelationRepository.findAllByBillTokenId(placeDTO.getBillTokenId()).forEach(metaTokenRelationDao -> {
                                            if (metaTokenRelationDao.getEntryMetaId().equals(metaid.getEntryMasterMetaId())) { //  meta相同的时候返回
                                                add(metaTokenRelationDao.build());
                                            }
                                        }
                                )
                        );
                    }
                })
                //构造前置单据信息
                .buildpreMetaTokenRelation( //  TODO

                        new ArrayList<PreMetaTokenRelationDTO>() {
                            private static final long serialVersionUID = -4410460999037590062L;

                            {
                                metaTokenRelationRepository.findAllByEntryMetaIdAndEntryTokenId(billMetaId, tokenId).forEach(
                                        metaTokenRelationDao -> add(metaTokenRelationDao.buildPreMetaTokenRel())
                                );
                            }
                        })
                //用户信息
                .buildUserId(userQueryFacadeEntity.findByAccessToken().getId());
    }

    /**
     * 生成据转换DTO
     *
     * @param cacheId              缓存id
     * @param authId               auth id
     * @param currentCachePlaceDTO 缓存中的当前单据DTO
     * @param preCachePlaceDTO     缓存中的源单
     * @return 单据转换DTO
     */
//    public TransformationDTO generateTransformation(
//            String cacheId
//            , String authId
//            , PlaceDTO currentCachePlaceDTO
//            , PlaceDTO preCachePlaceDTO
//    ) {
//        return new TransformationDTO(cacheId, authId, currentCachePlaceDTO.getPreMetaTokenRelationDTO(), currentCachePlaceDTO)    // 构造单据转换DTO
//                .build(
//                        this.getMasterBillMetaRelation(currentCachePlaceDTO.getPreMetaTokenRelationDTO().getSourceBillMetaId())
//                        , currentCachePlaceDTO
//                        , preCachePlaceDTO
//                );  //  根据主数据清洗单据转换DTO
//    }

    /**
     * 根据表头主数据metaid查该表单关联的主数据meta
     *
     * @param billMetaId 表头meta id
     * @return MasterBillMetaRelationDTO
     */
    public MasterBillMetaRelationDTO getMasterBillMetaRelation(String billMetaId) {
        List<String> metaIds = new ArrayList<>();
        MasterBillMetaRelationDao masterBillMetaRelationDao = masterBillMetaRelationRepository.findByBillMasterMetaId(billMetaId);
        if (null != masterBillMetaRelationDao) {
            for (MasterMetaInfoDTO masterMetaInfoDTO : masterBillMetaRelationDao.getMasterMetaInfoDTOS()) {
                metaIds.add(masterMetaInfoDTO.getEntryMasterMetaId());
            }
            metaIds.add(masterBillMetaRelationDao.getBillMasterMetaId());
            return new MasterBillMetaRelationDTO(
                    masterBillMetaRelationDao.getBillMasterMetaId()
                    , masterBillMetaRelationDao.getMasterMetaInfoDTOS()
            ).build(
                    metaQueryFacadeEntity.findMetaFormationsByMetaIds(metaIds)
            );
        } else {
            return new MasterBillMetaRelationDTO();
        }
    }

    /**
     * 保存主数据meta的关联关系
     *
     * @param masterBillMetaRelationDTO 单据表单主数据与关联表单的的关系
     */
    public void saveMasterBillMetaRelation(MasterBillMetaRelationDTO masterBillMetaRelationDTO) {
        masterBillMetaRelationRepository.save(new MasterBillMetaRelationDao(
                masterBillMetaRelationDTO.getBillMasterMetaId(),
                masterBillMetaRelationDTO.getMasterMetaInfoDTOS()
        ));
    }

    /**
     * 主数据meta获取单据内meta关系
     *
     * @param billMetaIdList 主数据meta
     * @return metaRelationDTO
     */
    public List<MetaRelationDTO> getAllMasterDataMetaRelation(List<String> billMetaIdList) {
        List<MetaRelationDao> metaRelationDaoList = Lists.newArrayList(metaRelationRepository.findAllById(billMetaIdList));
        List<MetaRelationDTO> metaRelationDTOList = new ArrayList<>();
        metaRelationDaoList.forEach(metaRelationDao -> metaRelationDTOList.add(new MetaRelationDTO(
                metaRelationDao.getMasterMetaId()
                , metaRelationDao.getMasterMetaName()
                , metaRelationDao.getViewMetaDTOList()
                , metaRelationDao.getMasterMemorandvnMetaId()
                , metaRelationDao.getViewMemorandvnDTOList()
                , metaRelationDao.getStoredProcedure()
        )));
        return metaRelationDTOList;
    }

    /**
     * 主数据meta获取单据内meta关系
     *
     * @param billMetaId 主数据meta
     * @return metaRelationDTO
     */
    public MetaRelationDTO getMetaRelationByMetaId(String billMetaId) {
        MetaRelationDao metaRelationDao = metaRelationRepository.findById(billMetaId).get();
        List<MetaRelationDTO> metaRelationDTOList = new ArrayList<>();
        MetaRelationDTO metaRelationDTO = new MetaRelationDTO(
                metaRelationDao.getMasterMetaId()
                , metaRelationDao.getMasterMetaName()
                , metaRelationDao.getViewMetaDTOList()
                , metaRelationDao.getMasterMemorandvnMetaId()
                , metaRelationDao.getViewMemorandvnDTOList()
                , metaRelationDao.getStoredProcedure()
        );
        return metaRelationDTO;
    }

    /**
     * 取得主数据meta一览
     *
     * @param pageNo   页码
     * @param pageSize 页宽
     * @return meta信息定义，token名和类型
     */
    public CurrentPage<TokenMetaInformationDto> getAllMasterDataMeta(String metaDataType, int pageNo, int pageSize) {
        return tokenMetaInformationRepository.getAllMasterDataMeta(metaDataType, pageNo, pageSize);
    }

    /**
     * 取得主数据meta一览
     *
     * @param pageNo   页码
     * @param pageSize 页宽
     * @return meta信息定义，token名和类型
     */
    public CurrentPage<TokenMetaInformationDto> getAllMetaByMetaType(String metaType, String metaDataType, Integer pageNo, Integer pageSize) {
        return tokenMetaInformationRepository.getAllMetaByMetaType(metaType, metaDataType, pageNo, pageSize);
    }

    /**
     * 获取视图meta
     *
     * @param metaId meta id
     * @return meta信息定义，token名和类型 列表
     */
    public List<TokenMetaInformationDto> getViewMetaByMasterDataMeta(String metaId) {
        return tokenMetaInformationRepository.getViewMetaByMasterDataMeta(metaId);
    }

    /**
     * 调用存储过程保存下推单据的tokendata并返回下推单据表头的tokenid
     *
     * @param preMetaId  前置单据metaid
     * @param preTokenId 前置单据表头tokenid
     * @param metaId     下推单据的metaid
     * @return String   下推单据的表头tokenid
     */
    public String excuteStoredProcedure(String preMetaId, String preTokenId, String metaId) {
        //根据前置表单和被创建的表单的metaid查存储过程
        List<BillMetaStoredProcedureDao> billMetaStoredProcedureDaos =
                billMetaStoredProcedureRepository.findAllByBillMasterMetaIdAndEntryMasterMetaId(preMetaId, metaId);
        if (null != billMetaStoredProcedureDaos && billMetaStoredProcedureDaos.size() > 0) {
            String storedProcedureName = billMetaStoredProcedureDaos.get(0).getStoredProcedure();
            //执行存储过程返回表头tokenid
            return tokenMetaInformationRepository.excuteStoredProcedure(storedProcedureName, preTokenId);
        }
        return null;
    }

    /**
     * 查询汇签意见
     *
     * @param metaId            单据metaId
     * @param tokenId           单据tokenId
     * @param countersignMetaId 汇签metaId
     * @return PlaceDTO
     */
    public List<PlaceDTO> buildCountersigns(String metaId, String tokenId, String countersignMetaId) {
        return new ArrayList<PlaceDTO>() {
            private static final long serialVersionUID = -8888983107130104092L;

            {
                countersignRepository.findByMetaIdAndTokenId(metaId, tokenId).forEach(countersignTokenId -> {
                    add(buildCachePlace(
                            countersignTokenId
                            , countersignMetaId
                    ));
                });
            }
        };

    }

    /**
     * 查询汇签意见
     *
     * @param metaId            单据metaId
     * @param tokenId           单据tokenId
     * @param countersignMetaId 汇签metaId
     * @return PlaceDTO
     */
    public PlaceDTO buildCountersign(String metaId, String tokenId, String taskId, String countersignMetaId) {
        return buildCachePlace(
                countersignRepository.findByMetaIdAndTokenId(metaId, tokenId, taskId)
                , countersignMetaId
        );
    }

    /**
     * 查询汇签信息涉及到的单据
     *
     * @param countersignTaskTokenIds 汇签任务tokenIds
     * @return List<CountersignStatusUpdateDTO> 查询汇签任务关联的单据token
     */
    public List<CountersignStatusUpdateDTO> findBillInfoByTaskTokenId(List<String> countersignTaskTokenIds) {
        return countersignRepository.findBillInfoByTaskTokenId(countersignTaskTokenIds);
    }

}
