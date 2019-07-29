package top.toptimus.entity.event.transformation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.toptimus.entity.place.PlaceRedisEntity;
import top.toptimus.entity.tokendata.event.TokenEventEntity;
import top.toptimus.entity.tokendata.query.TokenMetaQueryFacadeEntity;
import top.toptimus.merkle.GraphQLModel;
import top.toptimus.transformation.TransformationDTO;
import top.toptimus.transformation.TransformationModel;

/**
 * 单据转换处理实体类
 *
 * @author gaoyu
 * @since 2018-6-20
 */
@Component
public class TransformationEventEntity {

    @Autowired
    private TokenEventEntity tokenEventEntity;
    @Autowired
    private PlaceRedisEntity placeRedisEntity;
    @Autowired
    private TokenMetaQueryFacadeEntity tokenMetaQueryFacadeEntity;

    /**
     * 下推转换规则
     *
     * @param transformationDTO transformationDTO
     * @param graphQLModel      根据主数据id（select）类型抽取维度数据的实体
     */
//    public void pushDownTansformation(TransformationModel, GraphQLModel graphQLModel) {
//
//        if (graphQLModel.getRuleModel().getBusinessCodeDTO() != null) {
//            graphQLModel.getRuleModel().getRuleDefinitionDTOList().forEach(ruleDefinitionDTO -> {
//
//                switch (transformationDTO.getRuleType()) {
//                    case ("PUSHDOWN"):
//                        // 如果规则匹配 1推1 所以要匹配meta
//                        if (ruleDefinitionDTO.getRuleType().equals("PUSHDOWN") && ruleDefinitionDTO.getMetaId().equals(transformationDTO.getRuleMetaId())) {
//                            graphQLModel.savePushDownRuleIntoSchema(
//                                    ruleDefinitionDTO
//                                    , transformationDTO
//                            );
//                        }
//                        break;
//                    case ("REVERSE"):
//                        // 如果规则匹配 可能1反写多
//                        if (ruleDefinitionDTO.getRuleType().equals("REVERSE")) {
//                            graphQLModel.savePushDownRuleIntoSchema(
//                                    ruleDefinitionDTO
//                                    , transformationDTO
//                            );
//
//                        }
//                }
//            });
//            // 单据持久化数据库
//            tokenEventEntity.saveDatas(transformationDTO.getTransformationDatas());
//            // TODO  为解决反写后 缓存中的数据不是最新的  临时方案  待解决
//            placeRedisEntity.reloadCache(
//                    tokenMetaQueryFacadeEntity.buildCachePlace(
//                            transformationDTO.getPrePlaceDTO().getBillTokenId()
//                            , transformationDTO.getPrePlaceDTO().getBillMetaId())    //  取出库所
//            );
//        }
//    }
}
