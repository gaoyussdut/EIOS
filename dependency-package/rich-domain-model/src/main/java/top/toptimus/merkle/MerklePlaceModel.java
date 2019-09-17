package top.toptimus.merkle;

import lombok.Getter;
import top.toptimus.merkle.baseModel.MerkleBaseModel;
import top.toptimus.place.place_deprecated.PlaceDTO;
import top.toptimus.tokendata.TokenDataDto;

import java.util.UUID;

/**
 * 库所model，带缓存主键
 *
 * @author gaoyu
 */
public class MerklePlaceModel extends MerkleBaseModel {

    @Getter
    private String cacheId;  //  redis主键
    @Getter
    private PlaceDTO placeDTO;  //  库所


    /**
     * 新增单据
     *
     * @param placeDTO 库所
     */
    public MerklePlaceModel(PlaceDTO placeDTO) {
        super(placeDTO);
        this.cacheId = UUID.randomUUID().toString();
        this.placeDTO = placeDTO;
    }

    /**
     * 更新数据
     *
     * @param placeDTO 库所
     * @return this
     */
    public MerklePlaceModel build(PlaceDTO placeDTO) {
        super.alterPlaceDatas(placeDTO.getDatas());
        this.placeDTO = placeDTO;
        return this;
    }

    /**
     * 根据表头meta id取token数据
     *
     * @param billMetaId 表头meta id
     * @return token数据
     */
    public TokenDataDto getBillTokenByBillMetaId(String billMetaId) {
        try {
            return this.getPlaceDTO()
                    .getDatas()
                    .get(billMetaId)
                    .get(0);
        } catch (Exception e) {
            return null;
        }
    }

//    public MerklePlaceModel buildGraphQLModel(String businessCode, List<String> ruleIds) {
//        this.graphQLModel = new GraphQLModel(
//                businessCode
//                , "Query"
//                , "type Query{" + ""/*TODO split*/ + ": String} schema{query: Query}"
//        );
//        return this;
//    }

//    /**
//     * @param transformationDTO rule
//     * @param ruleDefinitionDTO
//     * @return
//     */
//    public MerklePlaceModel buildPushDownRuleIntoSchema(RuleDefinitionDTO ruleDefinitionDTO, TransformationDTO transformationDTO) {
//        this.graphQLModel.savePushDownRuleIntoSchema(ruleDefinitionDTO, transformationDTO);
//        return this;
//    }
}
