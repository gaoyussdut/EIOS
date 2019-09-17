package top.toptimus.entity.place;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.toptimus.common.result.Result;
import top.toptimus.common.search.SearchInfo;
import top.toptimus.dao.place.cache.PlaceSession;
import top.toptimus.dao.place.cache.RelCacheDao;
import top.toptimus.entity.TokenDataThreadQueryFacadeEntity;
import top.toptimus.entity.place.amqp.producer.DeletePlaceProducer;
import top.toptimus.entity.place.amqp.producer.SavePlaceProducer;
import top.toptimus.entity.security.query.UserQueryFacadeEntity;
import top.toptimus.exception.TopErrorCode;
import top.toptimus.exception.TopException;
import top.toptimus.merkle.MerkleBasicModel;
import top.toptimus.merkle.MerklePlaceModel;
import top.toptimus.model.place.PlaceSessionModel;
import top.toptimus.place.place_deprecated.BillTokenSaveResultDTO;
import top.toptimus.place.place_deprecated.PlaceDTO;
import top.toptimus.place.place_deprecated.PlaceReduceDTO;
import top.toptimus.relation.MetaTokenRelationDTO;
import top.toptimus.repository.place.RelCacheRepository;
import top.toptimus.resultModel.ResultErrorModel;
import top.toptimus.timer.LogExecuteTime;
import top.toptimus.tokendata.TokenDataDto;
import top.toptimus.tokendata.TokenDataPageableDto;
import top.toptimus.user.UserDTO;

import java.util.*;

/**
 * 库所redis实体
 *
 * @author gaoyu
 */
@Component
public class PlaceRedisEntity {
    private final ThreadLocal<PlaceSessionModel> placeSessionModel = ThreadLocal.withInitial(PlaceSessionModel::new);   //  单据线程缓存
    private static Map<String, MerkleBasicModel> metaMerkleBasicModelMap = new HashMap<>(); // K:metaId V: 基础资料备查账merkleModel
    @Autowired
    private TokenDataThreadQueryFacadeEntity tokenDataThreadQueryFacadeEntity;
    @Autowired
    private SavePlaceProducer savePlaceProducer;
    @Autowired
    private DeletePlaceProducer deletePlaceProducer;
    @Autowired
    private RelCacheRepository relCacheRepository;
    @Autowired
    private UserQueryFacadeEntity userQueryFacadeEntity;

    /**
     * 备查账查询数据
     *
     * @param metaId     备查账MetaId
     * @param searchInfo 检索项目定义
     * @param pageNumber 当前页
     * @param pageSize   当前页大小
     * @return MerkleBasicModel
     */
    public TokenDataPageableDto getMemorandvnData(String metaId, SearchInfo searchInfo, Integer pageNumber, Integer pageSize) {
        boolean metaExisted = PlaceRedisEntity.metaMerkleBasicModelMap.keySet().contains(metaId);   //  判断线程是否存在
        // 如果有缓存的MainTable  使用缓存的MainTable 否则从新构建SQL
        MerkleBasicModel merkleBasicModel = tokenDataThreadQueryFacadeEntity.generateMerkleModel(
                metaId
                , pageNumber
                , pageSize
                , metaExisted
                        ? PlaceRedisEntity.metaMerkleBasicModelMap.get(metaId).getMainTableMap()
                        : new HashMap<>()
                , searchInfo
        );

        if (!metaExisted) {
            //  如果缓存中没有当前的查询merkle  将其缓存
            PlaceRedisEntity.metaMerkleBasicModelMap.put(metaId, merkleBasicModel);
        }
        return merkleBasicModel.getTokenDataPageableDto();
    }

    /**
     * 关闭业务线程
     * 主要用提：
     * 1、在place提交时候调用
     * 2、后台定时作业根据TTL检查超时的时候调用
     *
     * @param merklePlaceModel placeDTO
     * @apiNote mq异步调用
     */
    @LogExecuteTime
    private void closePlaceThread(MerklePlaceModel merklePlaceModel) {
        String authId = userQueryFacadeEntity.findByAccessToken().getId();
        try {
            //  清空redis 数据
            relCacheRepository.deleteAllByBillTokenIdAndAuthId(merklePlaceModel.getPlaceDTO().getBillTokenId(), authId);
        } catch (Exception ignored) {
        }
        //清空placeSessions中的对应的缓存
        PlaceSession placeSession = this.placeSessionModel.get().authIdExist(authId);
        if (null != placeSession) {
            placeSession.getMerklePlaceModels().remove(merklePlaceModel);
        }
    }

    /**
     * 创建表单
     * use case:
     * 1.生成表单的表头token，并根据metaid构造PlaceDTO
     * 2.将生成的PlaceDTO存入缓存中
     * 3.提交生成的tokenDataDTO
     * 4.返回表头tokenDataDTO：result(tokenDataDTO)
     *
     * @param metaId meta id
     * @return Result
     */
    public TokenDataDto createBill(String metaId) {
        //  1.生成表单的表头token，并根据metaid构造PlaceDTO
        TokenDataDto tokenDataDto = new TokenDataDto();
        UserDTO userDTO = userQueryFacadeEntity.findByAccessToken();
        //  2.将生成的PlaceDTO存入缓存中。初始化线程,保存数据到redis
//        this.initPlaceCache(
//                new PlaceDTO(metaId, tokenDataDto).buildUserId(userDTO.getId())   //  创建place
//        );
        //  3.提交生成的tokenDataDTO。保存数据到postgres
        this.saveBillToken(
                tokenDataDto
                , metaId
        );
        //  4.返回表头tokenDataDTO
        return tokenDataDto;

    }


//    /**
//     * 创建关联表单
//     * use case:
//     * 1.将生成的PlaceDTO存入缓存中
//     * 2.将关系存入线程池和redis中
//     * 更新前置单据线程池和redis中的关系数据
//     * 保存当前单据的关系
//     * 3.返回表头tokenDataDTO：result(tokenDataDTO)
//     *
//     * @param preBillTokenId 前置单据表头tokenid
//     * @param preMetaId      前置单据metaId
//     * @param placeDTO       被创建的单据
//     * @return Result(TokenDataDTO)
//     */
//    public Result createBillRel(String preBillTokenId, String preMetaId, PlaceDTO placeDTO) {
//        //  0.构造前置单据信息
//        placeDTO.buildpreMetaTokenRelation(preBillTokenId, preMetaId);
//        //  0.1.用户信息
//        UserDTO userDTO = userQueryFacadeEntity.findByAccessToken();
//
//        //  1.将生成的PlaceDTO存入placeSessions.保存数据和关系到redis
//        this.initPlaceCache(placeDTO.buildUserId(userDTO.getId()));
//
//        //  2.更新前置单据关系
//        this.saveCacheRel(preBillTokenId, userDTO, placeDTO);
//
//        //  4.构造BillTokenSaveResultDTO，并将其发送到消息队列，异步保存数据到PG，保存关系到ES。aop保存关系和数据到PG&ES
//        savePlaceProducer.saveEntryToken(
//                new BillTokenSaveResultDTO(
//                        placeDTO
//                        , userQueryFacadeEntity.findByAccessToken().getId()
//                )
//        );
//
//        try {
//            return Result.success(
//                    placeDTO.getDatas().get(placeDTO.getBillMetaId()).get(0)
//            );
//        } catch (Exception e) {
//            return new ResultErrorModel(e).getResult();
//        }
//    }

//    /**
//     * 从缓存中重新加载数据，并和线程池中的数据做merkle检验
//     *
//     * @param tokenId token id
//     * @param authId  用户id
//     * @return merkle diff,K:meta id,V:token diff id list
//     */
//    public Map<String, List<String>> reloadData(String tokenId, String authId) {
//        //  前置单据
//        MetaTokenRelationDTO metaTokenRelationDTO =
//                this.findPlace(tokenId, authId)
//                        .getPlaceDTO()
//                        .getPreMetaTokenRelationDTO();
//        if (null != metaTokenRelationDTO) {
//            //  当上级单据存在的时候，从缓存中reload数据
//            //  上级单据缓存
//            Optional<RelCacheDao> relCacheDaoOptional = relCacheRepository.findById(metaTokenRelationDTO.getSourceBillTokenId());
//            if (relCacheDaoOptional.isPresent()) {
//                //  检验merkle diff并返回
//                return this.placeSessionModel.get()
//                        .findPlace(metaTokenRelationDTO.getSourceBillTokenId(), authId)
//                        .alterPlaceDatas(    //  检验merkle diff并返回
//                                JSON.parseObject(
//                                        relCacheDaoOptional.get().getDatas()
//                                        , new TypeReference<HashMap<String, List<TokenDataDto>>>() {
//                                        }
//                                )   //  库所中的datas
//                        );
//            }
//        }
//        return new HashMap<>();
//    }

//    public void reloadThreadCache(PlaceDTO placeDTO, String authId) {
//        this.placeSessionModel.get().savePlaceIntoSession(placeDTO, authId);
//    }

    /**
     * 缓存中存储单据关系
     *
     * @param preBillTokenId 前置单据
     * @param userDTO        用户信息
     * @param placeDTO       库所
     */
    private void saveCacheRel(String preBillTokenId, UserDTO userDTO, PlaceDTO placeDTO) {
        //  线程池中
        this.placeSessionModel.get().findPlace(preBillTokenId, userDTO.getId())
                .getPlaceDTO()
                .getMetaTokenRelationDTOS()
                .add(new MetaTokenRelationDTO(placeDTO));

        //  redis中
        RelCacheDao relCacheDao =
                this.findAllByBillTokenIdAndAuthId(
                        preBillTokenId
                        , userDTO.getId()
                );
        if (null != relCacheDao) {
            relCacheDao.addRel(placeDTO);
            relCacheRepository.save(relCacheDao);
        } else {
            //  TODO 超时，重新加载redis
            throw new RuntimeException("操他妈redis挂了!");
        }
    }

    /**
     * 取表头数据
     *
     * @param placeDTO 库所
     * @return Result
     */
    private TokenDataDto getBillToken(PlaceDTO placeDTO) {
        return this.placeSessionModel.get() //  PlaceSessionModel
                .findPlace(placeDTO.getBillTokenId(), userQueryFacadeEntity.findByAccessToken().getId())   //  MerklePlaceModel
                .getBillTokenByBillMetaId(placeDTO.getBillMetaId());    //  根据表头meta id取token数据
    }

    /**
     * 新增分录
     *
     * @param billTokenId
     * @param billMetaId
     * @param tokenDataDto
     * @param entryMetaId
     * @return
     */
    public Result createEntryToken(String billTokenId, String billMetaId, TokenDataDto tokenDataDto, String entryMetaId) {
        this.saveEntryToken(billTokenId, billMetaId, tokenDataDto, entryMetaId);
        return Result.success(tokenDataDto);
    }

    /**
     * 保存分录
     *
     * @param billTokenId
     * @param billMetaId
     * @param tokenDataDto
     * @param entryMetaId
     * @return
     */
    public Result saveEntryToken(String billTokenId, String billMetaId, TokenDataDto tokenDataDto, String entryMetaId) {
        try {
            String authId = userQueryFacadeEntity.findByAccessToken().getId();
            BillTokenSaveResultDTO billTokenSaveResultDTO = new BillTokenSaveResultDTO(
                    billTokenId
                    , billMetaId
                    , tokenDataDto
                    , entryMetaId
                    , authId
            );
//            savePlaceProducer.saveEntryToken(billTokenSaveResultDTO);
            //  5.返回result(BillTokenSaveResultDTO)
            return Result.success(billTokenSaveResultDTO);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultErrorModel(e).getResult();
        }

    }

    /**
     * 保存表头 TODO
     *
     * @param tokenDataDto
     * @param metaId
     * @return
     */
    public Result saveBillToken(TokenDataDto tokenDataDto, String metaId) {
        try {
            String authId = userQueryFacadeEntity.findByAccessToken().getId();
            savePlaceProducer.saveBillToken(new BillTokenSaveResultDTO(metaId, tokenDataDto, authId));
            return Result.success();
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultErrorModel(e).getResult();
        }
    }

//    /**
//     * 提交表单内token
//     * 创建表单和提交分录时调用
//     * <p>
//     * use case:
//     * 1.根据billTokenId从缓存中查找PlaceDTO
//     * 2.根据tokenDataDTO，metaId更新PlaceDTO并修改PlaceDTO中的userId
//     * 3.将更新后的PlaceDTO存入缓存，更新缓存中的PlaceDTO
//     * 4.构造BillTokenSaveResultDTO，并将其发送到消息队列，异步保存数据到PG，保存关系到ES
//     * 5.返回result(BillTokenSaveResultDTO)
//     *
//     * @param billTokenId  表头tokenid
//     * @param tokenDataDto 被提交的tokendata数据
//     * @param metaId       提交数据对应的meta
//     * @return Result(PlaceSaveResultDTO)  tokenAndRel
//     */
//    private Result submitBillToken(String billTokenId,String billMetaId, TokenDataDto tokenDataDto, String entryMetaId) {
//        try {
//            String authId = userQueryFacadeEntity.findByAccessToken().getId();
//            BillTokenSaveResultDTO billTokenSaveResultDTO = new BillTokenSaveResultDTO(
//                    billTokenId
//                    ,billMetaId
//                    , tokenDataDto
//                    , entryMetaId
//                    , authId
//            );
//            savePlaceProducer.saveEntryToken(billTokenSaveResultDTO);
//            //  5.返回result(BillTokenSaveResultDTO)
//            return Result.success(billTokenSaveResultDTO);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return new ResultErrorModel(e).getResult();
//        }
//    }

    /**
     * 缓存数据
     *
     * @param placeDTO 库所
     * @return Result
     */
    public Result initCache(PlaceDTO placeDTO) {
        //  预览取表头数据
        TokenDataDto tokenDataDto = this.getBillToken(placeDTO);
        //  如果缓存中存在直接返回数据
        if (null != tokenDataDto) {
            return Result.success(tokenDataDto);    //  缓存数据
        }
        //  缓存中不存在则加载缓存
        else {
            //  构造placedto表头数据
            return Result.success(
                    this.initPlaceCache(placeDTO)   //  将place和关系存入placeRedisEntity
            );
        }
    }

    /**
     * 批量缓存数据
     *
     * @param placeDTOs 库所
     * @return Result
     */
    public Result initCache(List<PlaceDTO> placeDTOs) {
        return Result.success(
                new ArrayList<TokenDataDto>() {
                    private static final long serialVersionUID = -6564019519032488538L;

                    {
                        placeDTOs.forEach(placeDTO -> {
                            if (null != getBillToken(placeDTO)) {
                                add(getBillToken(placeDTO));
                            } else {
                                add(initPlaceCache(placeDTO));
                            }
                        });
                    }
                }
        );
    }

    /**
     * 初始化表单缓存
     * 1.创建表单
     * 2.创建关联表单
     * 3.提交表单内token
     * use case:
     * 1.根据输入的参数构造InitPlaceCacheModel
     * 2.将PlaceDTO数据存入线程和redis
     * 3.将关系存入redis和线程中
     *
     * @param placeDTO placeDTO
     * @return token
     */
    @LogExecuteTime
    private TokenDataDto initPlaceCache(PlaceDTO placeDTO) {
        //  1.校验placeDTO中的tokendataDTO,检查tokenId是否存在，不存在报错
        placeDTO.intendedEffectData();  //  校验数据，如果tokenId不存在报错
        try {
            String authId = userQueryFacadeEntity.findByAccessToken().getId();  //  auth id
            this.savePlaceIntoCache(placeDTO, authId);  //  将数据存入缓存
            return Objects.requireNonNull(this.placeSessionModel.get().authIdExist(authId)) //  判断当前操作的会话是否存在，若存在返回对应的PlaceSession，否则返回null
                    .getMerkleByTokenId(placeDTO.getBillTokenId())   //  从session中取库所
                    .getPlaceDTO()
                    .getDatas()
                    .get(placeDTO.getBillMetaId())
                    .get(0);
        } catch (Exception e) {
            e.printStackTrace();
            throw new TopException(TopErrorCode.PLACE_DATA_SAVE_ERR);// 将PlaceDTO存入Redis异常

        }
    }

    /**
     * 根据表头token id和authid取得库所
     * 这里维护的是基础库所信息，如果需要其他信息，重载
     * 如果缓存中不存在中不存在 返回空
     *
     * @param billTokenId 表头token id
     * @return 库所
     */
    public MerklePlaceModel findPlace(String billTokenId, String authId) {
        return this.placeSessionModel.get().findPlace(billTokenId, authId);
    }

    /**
     * 将数据存入缓存
     *
     * @param placeDTO 库所
     * @param authId   auth id
     */
    private void savePlaceIntoCache(PlaceDTO placeDTO, String authId) {
        MerklePlaceModel merklePlaceModel = this.placeSessionModel.get().savePlaceIntoSession(placeDTO, authId); //  将库所存入session
        /*
            存redis
         */
        //  判断redis中是否存在旧的RelCacheDao
        Optional<RelCacheDao> relCacheDaoOptional = relCacheRepository.findById(merklePlaceModel.getCacheId());
        if (relCacheDaoOptional.isPresent()) {
            //  变更
            relCacheRepository.save(relCacheDaoOptional.get().buildDatas(merklePlaceModel));
        } else {
            //  新增
            relCacheRepository.save(
                    new RelCacheDao(
                            merklePlaceModel
                            , this.placeSessionModel.get().authIdExist(authId)    //  判断当前操作的会话是否存在，若存在返回对应的PlaceSession，否则返回null
                    )
            );
        }
    }

    /**
     * 变更，将数据存入缓存
     *
     * @param placeDTO 库所
     * @param authId   auth id
     */
    public void savePlaceIntoCache(PlaceDTO placeDTO, String authId, RelCacheDao relCacheDao) {
        /*
            变更，存redis
         */
        relCacheRepository.save(
                relCacheDao.buildDatas(
                        this.placeSessionModel.get()
                                .savePlaceIntoSession(placeDTO, authId) //  库所model，带缓存主键
                )   //  表单token提交时更新RelCacheDao 的 datas
        );
    }

    /**
     * 新增，将数据存入缓存
     *
     * @param merklePlaceModel 库所
     * @param authId           auth id
     */
    public void saveRelCache(MerklePlaceModel merklePlaceModel, String authId) {
        relCacheRepository.save(
                new RelCacheDao(
                        merklePlaceModel
                        , this.placeSessionModel.get().authIdExist(authId)    //  判断当前操作的会话是否存在，若存在返回对应的PlaceSession，否则返回null
                )
        );
    }

    /**
     * 删除分录信息
     *
     * @param billTokenId  表头数据
     * @param entryMetaId  分录meta
     * @param entryTokenId 分录TokenId
     * @return Result
     */
    public Result deleteEntryToken(String billTokenId, String entryMetaId, String entryTokenId) {
//        try {
//            // 1.取得库所关联结构
//            String authId = userQueryFacadeEntity.findByAccessToken().getId();
//            RelCacheDao relCacheDao = this.findAllByBillTokenIdAndAuthId(billTokenId, authId);
//            if (null != relCacheDao) {
//                // 2.删除redis中的分录的数据
//                Map<String, List<TokenDataDto>> datas = relCacheDao.cleanDatasByEntryTokenId(entryMetaId, entryTokenId);
//                relCacheRepository.save(relCacheDao);
//
//                // 3.更新MerklePlaceModel
//                this.placeSessionModel.get()    //  PlaceSessionModel
//                        .authIdExist(authId)   //   取出PlaceSession
//                        .updateDatasByBillTokenId(billTokenId, datas);   //  更新datas
//            }
//
//            // 4.消息队列删除ES关系和数据库中的数据
//            deletePlaceProducer.deleteEntryData(new BillTokenDelResultDTO().buildSuccessBody(billTokenId, entryTokenId, entryMetaId, authId));
//            return Result.success();
//        } catch (Exception e) {
//            e.printStackTrace();
//            throw new TopException(TopErrorCode.GENERAL_ERR);
//        }
        return null;//TODO
    }

    /**
     * 删除单据关联信息
     * 仅删除和关联源单据的关系不删除数据
     *
     * @param billTokenId 表头tokenId
     * @return Result
     */
    public Result deleteBillToken(String billTokenId) {
        try {
            // 1.取得库所model
            String authId = userQueryFacadeEntity.findByAccessToken().getId();
            PlaceSession placeSession = this.placeSessionModel.get().authIdExist(authId);
            MerklePlaceModel merklePlaceModel = placeSession.getMerkleByTokenId(billTokenId);
            // 2.清理关系
            // 2.1 如果存在父关系 清理父关系
            if (null != merklePlaceModel.getPlaceDTO().getPreMetaTokenRelationDTO()) {
                RelCacheDao relCacheDao = this.findAllByBillTokenIdAndAuthId(merklePlaceModel.getPlaceDTO().getPreMetaTokenRelationDTO().getSourceBillTokenId(), authId);
                if (null != relCacheDao)
                    relCacheRepository.save(relCacheDao.cleanCertificates(billTokenId));
            }
            // 2.2 删除自己本身
//            relCacheRepository.deleteById(merklePlaceModel.getCacheId());

            // 3.删除线程中的placeDTO
//            placeSession.getMerklePlaceModels().remove(merklePlaceModel);

            // 4.消息队列删除ES关系
            deletePlaceProducer.deleteBillData(new PlaceReduceDTO().build(merklePlaceModel.getPlaceDTO()));
            return Result.success();
        } catch (Exception e) {
            e.printStackTrace();
            throw new TopException(TopErrorCode.GENERAL_ERR);
        }

    }

    /**
     * 根据billTokenId和authId查询对应的库所关联结构
     *
     * @param billTokenId 表头TokenId
     * @param authId      权限Id
     * @return RelCacheDao
     */
    private RelCacheDao findAllByBillTokenIdAndAuthId(String billTokenId, String authId) {
        List<RelCacheDao> relCacheDaos = relCacheRepository.findByBillTokenId(billTokenId); // 根据BillTokenId查询
        for (RelCacheDao relCacheDao : relCacheDaos) {
            if (authId.equals(relCacheDao.getAuthId())) {
                return relCacheDao;
            }
        }
        return null;
    }

    /**
     * 将缓存中的旧数据清空 并将新的数据加载到缓存
     *
     * @param placeDTO placeDTO
     */
    public void reloadCache(PlaceDTO placeDTO) {
        // 1.先清空缓存中的旧数据
        this.closePlaceThread(new MerklePlaceModel(placeDTO));
        // 2.将最新的数据重新加载到缓存
        this.initPlaceCache(placeDTO);
    }

//    /**
//     * 批量新增分录
//     *
//     * @param metaId      分录metaid
//     * @param billTokenId 表头tokenid
//     * @return Result(tokenDataDto)
//     */
//    public Result createEntryToken(String metaId, String billTokenId, List<TokenDataDto> tokenDataDtos) {
//        tokenDataDtos.forEach(tokenDataDto -> {
//            this.submitToken(billTokenId, tokenDataDto, metaId, false);
//        });
//        return Result.success(tokenDataDtos);
//    }

//    /**
//     * 批量存分录信息
//     *
//     * @param billTokenId   表头token id
//     * @param tokenDataDtos tokens
//     * @param metaId        表头meta id
//     * @return Result
//     */
//    public Result submitTokens(String billTokenId, List<TokenDataDto> tokenDataDtos, String metaId) {
//        tokenDataDtos.forEach(tokenDataDto -> {
//            this.submitToken(billTokenId, tokenDataDto, metaId, false);
//        });
//        return Result.success();
//    }
}
