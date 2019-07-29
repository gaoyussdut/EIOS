package top.toptimus.amqp.producer;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import top.toptimus.amqp.consumer.TransformationConsumer;
import top.toptimus.dao.place.cache.RelCacheDao;
import top.toptimus.entity.place.PlaceRedisEntity;
import top.toptimus.entity.security.query.UserQueryFacadeEntity;
import top.toptimus.merkle.MerklePlaceModel;
import top.toptimus.place.PlaceDTO;
import top.toptimus.repository.BusinessCodeRepository;
import top.toptimus.repository.place.RelCacheRepository;
import top.toptimus.tokendata.TokenDataDto;
import top.toptimus.transformation.TransformationDTO;

import java.util.Optional;

/**
 * 变迁服务实体
 *
 * @author gaoyu
 */
@Service
public class TransitionEntity {

    @Autowired
    private PlaceRedisEntity placeRedisEntity;
    @Autowired
    private UserQueryFacadeEntity userQueryFacadeEntity;
    @Autowired
    private RelCacheRepository relCacheRepository;
    @Autowired
    private BusinessCodeRepository businessCodeRepository;
    @Autowired
    private TransformationConsumer transformationConsumer;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 单据提交时单据转换规则
     * TODO 数据变更通知前端
     *
     * @param tokenDataDto token数据
     */
    public void generateTransition(TokenDataDto tokenDataDto) {

        String authId = userQueryFacadeEntity.findByAccessToken().getId();
        /*
          1、调用计算逻辑,计算对上级单据的影响
          2、返回值要通知前端要刷新的数据  TODO    给字段的增量数据还是全部数据
           */
        /*
          库所model，带缓存主键
          在这里是当前单据
           */
        MerklePlaceModel merklePlaceModel = placeRedisEntity.findPlace(tokenDataDto.getTokenId(), authId);

        Optional<RelCacheDao> relCacheDaoOptional = relCacheRepository.findById(merklePlaceModel.getCacheId());
        /*
            当缓存中数据被命中了
         */
        if (relCacheDaoOptional.isPresent()) {
            /*
              当上级单据存在的时候，执行单据转换逻辑，并将上级单据的变更写入缓存
               */
            if (null != merklePlaceModel.getPlaceDTO().getPreMetaTokenRelationDTO()) {
//            /*
//              重新加载上级库所缓存
//               */
//            this.reloadPrePlaceThreadCache(merklePlaceModel, authId);   //  TODO    entity缓存刷新，没有用

                // 将源单存入缓存
                placeRedisEntity.savePlaceIntoCache(
                        this.generateSourcePlaceByTransition(
                                merklePlaceModel   //  缓存id
                                , authId
                        )   //  调用单据转换规则，返回源单
                        , authId
                        , relCacheDaoOptional.get()
                );

//            //  重新加载数据
//            Map<String, List<String>> metaAndTokenDiffs = placeRedisEntity.reloadData(
//                    tokenDataDto.getTokenId()
//                    , authId
//            );  //  TODO    tokenDiffs通知前端刷新
            }
        } else {
            //  新增缓存
            placeRedisEntity.saveRelCache(merklePlaceModel, authId);
        }
    }

    /**
     * 调用单据转换规则，返回源单
     *
     * @param merklePlaceModel 缓存id
     * @param authId           auth id
     * @return 源单库所
     */
    private PlaceDTO generateSourcePlaceByTransition(
            MerklePlaceModel merklePlaceModel
            , String authId
    ) {
        // 缓存的源单
        PlaceDTO prePlaceDTO = placeRedisEntity.findPlace(
                merklePlaceModel.getPlaceDTO().getPreMetaTokenRelationDTO().getSourceBillTokenId()  //  源单token id
                , authId
        ).getPlaceDTO();
        return null;
//        //  根据单据转换结果清洗源单并返回
//        return prePlaceDTO.buildTransition(
//                jmsMessagingTemplate.convertSendAndReceive(
//                        TRANSFORMATION
//                        , ruleQueryFacadeEntity.generateTransformation(
//                                merklePlaceModel.getCacheId()    //  缓存id
//                                , authId    //  auth id
//                                , placeRedisEntity.findPlace(
//                                        merklePlaceModel.getPlaceDTO().getBillTokenId() //  当前token id
//                                        , authId
//                                ).getPlaceDTO()  //  缓存的当前单据
//                                , prePlaceDTO   //  源单
//                        )   //  生成据转换DTO
//                        , TransformationDTO.class
//                )   // 调用MQ（单据转换）
//        );
    }

    /**
     * 构建下推单据转换
     */
    public void generatePushDownTransition(TransformationDTO transformationDTO) {

        // 存在上级单据则存在转换规则
        if (transformationDTO.getPrePlaceDTO() != null) {
            if (!StringUtils.isEmpty(businessCodeRepository.findByOriginMetaAndTargetMeta(transformationDTO.getPrePlaceDTO().getBillMetaId(), transformationDTO.getCurrentPlaceDTO().getBillMetaId()).getBusinessCode())) {
                this.generateTargetPlaceByTransition(
                        transformationDTO
                );   //  调用单据转换规则，返回目标单据
            }
        }
    }

    /**
     * 构建下级单据
     *
     * @param transformationDTO 单据转换DTO
     */
    private void generateTargetPlaceByTransition(TransformationDTO transformationDTO) {
        transformationConsumer.pushDownTransformation(transformationDTO);

//        TransformationDTO retTransformationDTO = jmsMessagingTemplate.convertSendAndReceive(
//                PUSHDOWN
//                , transformationDTO
//                , TransformationDTO.class);
    }

    /**
     * 执行存储过程
     *
     * @param storedProcedure 存储过程
     * @param tokenId         token id
     */
    public void generateMemorandvnTransition(String storedProcedure, String tokenId) {

        String sql = "SELECT " + storedProcedure + "('" + tokenId + "')";
        jdbcTemplate.execute(sql);

    }

    /**
     * 执行存储过程
     *
     * @param storedProcedure 存储过程
     * @param originTokenId         token id
     * @param targetTokenId
     */
    public void excuteStoredProcedure(String storedProcedure, String originTokenId, String targetTokenId) {

        String sql = "SELECT " + storedProcedure + "('" + originTokenId + "','" + targetTokenId + "')";
        jdbcTemplate.execute(sql);

    }

//    /**
//     * 重新加载上级库所缓存
//     *
//     * @param merklePlaceModel 库所model，带缓存主键
//     * @param authId           auth id
//     */
//    private void reloadPrePlaceThreadCache(MerklePlaceModel merklePlaceModel, String authId) {
//        //  上级库所
//        MerklePlaceModel preMerklePlaceModel = placeRedisEntity.findPlace(merklePlaceModel.getPlaceDTO().getPreMetaTokenRelationDTO().getSourceBillTokenId(), authId);
//        if (null == preMerklePlaceModel) {
//            //  从数据库中取前置库所，加载到线程
//            placeRedisEntity.reloadThreadCache(
//                    tokenMetaQueryFacadeEntity.buildCachePlace(merklePlaceModel)    //  创建单据的关联关系，创建place接口，用来放在缓存中
//                    , authId
//            );
//        }
//    }
}
