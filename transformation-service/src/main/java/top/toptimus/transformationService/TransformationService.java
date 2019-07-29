package top.toptimus.transformationService;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.toptimus.amqp.producer.TransitionEntity;
import top.toptimus.common.enums.RuleTypeEnum;
import top.toptimus.common.result.Result;
import top.toptimus.entity.event.TaskEventEntity;
import top.toptimus.entity.place.PlaceRedisEntity;
import top.toptimus.entity.query.TransformationFacadeQueryEntity;
import top.toptimus.entity.security.query.UserQueryFacadeEntity;
import top.toptimus.entity.tokendata.query.TokenDataSqlRetrieveEntity;
import top.toptimus.entity.tokendata.query.TokenMetaQueryFacadeEntity;
import top.toptimus.entity.tokendata.query.TokenQueryFacadeEntity;
import top.toptimus.meta.MetaRelation.MetaRelationDTO;
import top.toptimus.place.PlaceDTO;
import top.toptimus.repository.TaskRepository;
import top.toptimus.resultModel.ResultErrorModel;
import top.toptimus.tokendata.TokenDataDto;
import top.toptimus.transformation.FkeyRuleDTO;
import top.toptimus.transformation.RuleDefinitionDTO;
import top.toptimus.transformation.TransformationDTO;
import top.toptimus.transformation.TransformationModel;

import java.util.List;
import java.util.UUID;

/**
 * Created by lzs
 */
@Service
public class TransformationService {

    @Autowired
    private TransitionEntity transitionEntity;
    @Autowired
    private TokenDataSqlRetrieveEntity tokenDataSqlRetrieveEntity;
    @Autowired
    private TokenQueryFacadeEntity tokenQueryFacadeEntity;
    @Autowired
    private TransformationFacadeQueryEntity transformationFacadeQueryEntity;
    @Autowired
    private PlaceRedisEntity placeRedisEntity;

    public TokenDataDto transformation(
            String originMetaId
            , String originTokenId
            , String targetMetaId
            , String targetTokenId
            , String ruleId
            , RuleTypeEnum ruleType
    ) {


        RuleDefinitionDTO ruleDefinitionDTO = transformationFacadeQueryEntity.findRuleById(ruleId);

        if (!StringUtils.isEmpty(ruleDefinitionDTO.getStoredProcedure())) {
            transitionEntity.excuteStoredProcedure(ruleDefinitionDTO.getStoredProcedure(), originTokenId, targetTokenId);
            return tokenQueryFacadeEntity.getTokenData(targetMetaId, targetTokenId);
        } else {
            switch (ruleType) {
                case PUSHDOWN:
                    TransformationModel transformationModel = new TransformationModel(ruleDefinitionDTO
                            , transformationFacadeQueryEntity.findFkeyRuleById(ruleId)
                            , tokenDataSqlRetrieveEntity.getPlace(originTokenId, originMetaId)
                            , new PlaceDTO(targetMetaId, new TokenDataDto().build(targetTokenId))
                            , ruleType
                    ).transformation();
                    placeRedisEntity.saveBillToken(transformationModel.getTokenDataDto(), targetMetaId);
                    return transformationModel.getTokenDataDto();
                case REVERSE:
                    TransformationModel transformationModel2 = new TransformationModel(ruleDefinitionDTO
                            , transformationFacadeQueryEntity.findFkeyRuleById(ruleId)
                            , tokenDataSqlRetrieveEntity.getPlace(originTokenId, originMetaId)
                            , tokenDataSqlRetrieveEntity.getPlace(targetMetaId, targetTokenId)
                            , ruleType
                    ).transformation();
                    placeRedisEntity.saveBillToken(transformationModel2.getTokenDataDto(), targetMetaId);
                    return transformationModel2.getTokenDataDto();
            }

        }
        return null;

    }

}
