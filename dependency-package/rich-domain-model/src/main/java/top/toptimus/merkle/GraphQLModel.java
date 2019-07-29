package top.toptimus.merkle;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import graphql.GraphQL;
import graphql.schema.GraphQLSchema;
import graphql.schema.StaticDataFetcher;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.toptimus.formula.formula.Parser;
import top.toptimus.formula.pushDownProperty.DataProperty;
import top.toptimus.formula.util.DefaultProperties;
import top.toptimus.rule.RuleModel;
import top.toptimus.tokendata.TokenDataDto;
import top.toptimus.tokendata.field.FkeyField;

import java.util.*;

/**
 * 根据主数据id（select）类型抽取维度数据的实体
 *
 * @author gaoyu
 * @since 2019-02-19
 */
@Data
@NoArgsConstructor
public class GraphQLModel {
    private String businessCode;
    private String typeName;
    private String schemaInput; //  schema
    private GraphQLSchema graphQLSchema;
    private TypeDefinitionRegistry typeDefinitionRegistry;  //  注册的schema
    private RuntimeWiring.Builder builder;  //  运行时查询数据
    @Getter
    private TokenDataDto tokenDataDto;  //  根据转换规则生成的token

    Logger logger = LoggerFactory.getLogger(GraphQLModel.class);

    /**
     * 构造函数
     *
     * @param typeName    类型
     * @param schemaInput schema
     */
    public GraphQLModel(String businessCode, String typeName, String schemaInput) {
        this.businessCode = businessCode;
        this.typeName = typeName;
        this.schemaInput = schemaInput;
    }

    /**
     * 构造函数
     *
     * @param ruleModel 规则model类
     */
    public GraphQLModel(RuleModel ruleModel) {
//        this.businessCode =;
        this.typeName = "Query";
        this.schemaInput = "type Query{";
        ruleModel.getFkeyRuleDTOList().forEach(ruleDefinitionDTO -> this.schemaInput += ruleDefinitionDTO.getRuleId() + ": String,");
        this.schemaInput = this.schemaInput.substring(0, this.schemaInput.length() - 1);
        this.schemaInput += "} schema{query: Query}";
    }

    //  token数据处理

    /**
     * 存条件数据到缓存
     *
     * @param tokenDataDtos 数据
     * @param ruleId        规则id
     */
    public void saveDataIntoSchema(String ruleId, List<TokenDataDto> tokenDataDtos) {
        //  生成缓存
        this.saveDataIntoSchema(
                ruleId
                , JSON.toJSONString(
                        this.getResult(ruleId, tokenDataDtos)   //  token数据，如果schema中存在，就合并；如果不存在，新生成
                )
        );
    }

    /**
     * 取token数据
     *
     * @param ruleId  规则id
     * @param tokenId token id
     * @return token数据
     */
    public TokenDataDto getTokenData(String ruleId, String tokenId) {
        return
                JSON.parseObject(
                        (String) this.getDataByRuleId(ruleId)  //  根据单据转换规则查数据
                        , new TypeReference<HashMap<String, TokenDataDto>>() {
                        }
                ).get(tokenId);
    }

    //  存取boolean结果处理

    /**
     * 生成缓存boolean
     *
     * @param ruleId 规则id
     * @param isPass 是否通过
     */
    public void saveBooleanDataIntoSchema(String ruleId, boolean isPass) {
        this.saveDataIntoSchema(ruleId, isPass);    //  生成缓存
    }

    /**
     * 取规则结果数据
     *
     * @param ruleId 规则id
     * @return K:token id，V:token数据
     */
    public boolean getBooleanResult(String ruleId) {
        return (boolean) this.getDataByRuleId(ruleId);  //  根据单据转换规则查数据
    }

    //  普通公式处理

    /**
     * 生成缓存公式
     *
     * @param ruleId   规则id
     * @param function 公式
     */
    public void saveFunctionIntoSchema(String ruleId, String function) {
        this.saveDataIntoSchema(ruleId, function);  //  生成缓存
    }

    /**
     * 取公式数据
     *
     * @param ruleId 规则id
     * @return K:token id，V:token数据
     */
    public Object getFunctionResult(String ruleId) {
        return new Parser().parse(
                (String) this.getDataByRuleId(ruleId)   //  根据单据转换规则查数据
        ).getValue(new DefaultProperties()).getValue();
    }

    //  token规则下推

    /**
     * 存下推数据    TODO    废弃
     *
     * @param ruleId         规则id
     * @param dataProperties 公式
     */
    @Deprecated
    public void savePushDownRuleIntoSchema(String ruleId, List<DataProperty> dataProperties) {
        //  生成缓存
        this.saveDataIntoSchema(
                ruleId
                , JSON.toJSONString(dataProperties)
        );
        //  生成token
        this.generatePushDownRuleResult(ruleId, UUID.randomUUID().toString());  //  TODO    测试方法
    }

    /**
     * 根据下推数据生成token数据
     */
    @Deprecated
    private void generatePushDownRuleResult(String ruleId, String tokenId) {
        List<DataProperty> dataProperties =
                JSON.parseArray(
                        (String) this.getDataByRuleId(ruleId)   //  根据单据转换规则查数据
                        , DataProperty.class
                );

        this.tokenDataDto = new TokenDataDto(
                tokenId, //  下推的token id
                //  字段
                new ArrayList<FkeyField>() {{
                    for (DataProperty dataProperty : dataProperties) {
                        add(
                                // Fkey
                                new FkeyField().createPlainFkeyField(
                                        dataProperty.getFkeyTypeEnum()  //  字段类型
                                        , dataProperty.getFKey()    //  字段fkey
                                        , String.valueOf(
                                                new Parser()
                                                        .parse(dataProperty.getFunction())  //  公式
                                                        .getValue(new DefaultProperties(dataProperty.getPropertyValue())) //  传参
                                                        .getValue() //  返回值
                                        )
                                )
                        );
                    }
                }}
        );
    }


//    /**
//     * 存下推数据
//     *
//     * @param transformationDTO 单据转换DTO
//     */
//    public GraphQLModel savePushDownRuleIntoSchema(TransformationModel transformationModel) {
//        List<DataProperty> dataPropertyList = new ArrayList<>();
//        transformationModel.getRuleModel().getFkeyRuleDTOList().forEach(fkeyRuleDTO -> {
//            dataPropertyList.add(new DataProperty(fkeyRuleDTO, transformationModel.getOriginPlaceDTO()));
//        });
//
//        //  生成缓存
//        this.saveDataIntoSchema(
//                transformationModel.getRuleId(),    //  TODO    rule id
//                JSON.toJSONString(dataPropertyList)
//        );
//        //  生成token TODO    ？business code不能用在这，传入的是rule id
//        this.generatePushDownRuleResult(transformationModel);
//        return this;
//    }
//
//    /**
//     * 根据下推数据生成token数据
//     *
//     * @param transformationDTO 单据转换DTO
//     */
//    private void generatePushDownRuleResult(TransformationModel transformationModel) {
//        List<DataProperty> dataProperties =
//                JSON.parseArray(
//                        (String) this.getDataByRuleId(ruleDefinitionDTO.getRuleId())   //  根据单据转换规则查数据
//                        , DataProperty.class
//                );
//
//        String tokenId;
//        // 下推
//        if (ruleDefinitionDTO.getMetaId().equals(transformationDTO.getCurrentPlaceDTO().getBillMetaId())) {
//            tokenId = transformationDTO.getCurrentPlaceDTO().getBillTokenId();
//        }
//        // 反写
//        else if (ruleDefinitionDTO.getMetaId().equals(transformationDTO.getPrePlaceDTO().getBillMetaId())) {
//            tokenId = transformationDTO.getPrePlaceDTO().getBillTokenId();
//        }
//        // 分录
//        else {
//            tokenId = UUID.randomUUID().toString();
//        }
//        List<FkeyField> fkeyFieldList = new ArrayList<>();
//        for (DataProperty dataProperty : dataProperties) {
//            if (!dataProperty.getFkeyTypeEnum().equals(FkeyTypeEnum.SELECT)) {
//                fkeyFieldList.add(
//                        // Fkey
//                        new FkeyField().createPlainFkeyField(
//                                dataProperty.getFkeyTypeEnum()  //  字段类型
//                                , dataProperty.getFKey()    //  字段fkey
//                                , String.valueOf(
//                                        new Parser()
//                                                .parse(dataProperty.getFunction())  //  公式
//                                                .getValue(new DefaultProperties(dataProperty.getPropertyValue())) //  传参
//                                                .getValue() //  返回值
//                                )
//                        )
//                );
//            } else {
//                fkeyFieldList.add(
//                        // Fkey
//                        new FkeyField().createSelectFkeyField(
//                                dataProperty.getFkeyTypeEnum()  //  字段类型
//                                , dataProperty.getFKey()    //  字段fkey
//                                , dataProperty.getBusinessId()
//                                , String.valueOf(
//                                        new Parser()
//                                                .parse(dataProperty.getFunction())  //  公式
//                                                .getValue(new DefaultProperties(dataProperty.getPropertyValue())) //  传参
//                                                .getValue() //  返回值
//                                )
//                        )
//                );
//            }
//
//
//        }
//
//        this.tokenDataDto = new TokenDataDto().build(tokenId).build(fkeyFieldList);
//        //  目标单数据
//        transformationDTO.getTransformationDatas().put(ruleDefinitionDTO.getMetaId(), Lists.newArrayList(this.tokenDataDto));
//    }

    //  private方法

    /**
     * 生成缓存
     *
     * @param ruleId 规则id
     * @param data   数据
     */
    private void saveDataIntoSchema(String ruleId, Object data) {
        //  预处理
        if (null == this.typeDefinitionRegistry)
            this.typeDefinitionRegistry = new SchemaParser().parse(this.schemaInput);    //  初始化schema
        if (null == this.builder)
            this.builder = RuntimeWiring.newRuntimeWiring()
                    .type(this.typeName, builder -> builder.dataFetcher(ruleId, new StaticDataFetcher(data)));  //  初始化运行时查询
        else
            this.builder.type(this.typeName, builder -> builder.dataFetcher(ruleId, new StaticDataFetcher(data)));  //  新增rule

        //  运行时数据
        this.graphQLSchema =
                new SchemaGenerator().makeExecutableSchema(
                        this.typeDefinitionRegistry
                        , this.builder.build()
                );
    }

    /**
     * 从graphQL中取token数据。如果schema已经初始化了，从schema中取数据，并加入新token。如果报错了，就新生成
     *
     * @param ruleId 规则id
     * @return K:token id，V:token数据
     */
    private Map<String, TokenDataDto> getResult(String ruleId, List<TokenDataDto> tokenDataDtos) {
        try {
            //  schema已经初始化了
            Map<String, TokenDataDto> tokenDataDtoMap = JSON.parseObject(
                    (String) this.getDataByRuleId(ruleId)   //  根据单据转换规则查数据
                    , new TypeReference<HashMap<String, TokenDataDto>>() {
                    }
            );
            tokenDataDtos.forEach(tokenDataDto -> tokenDataDtoMap.put(tokenDataDto.getTokenId(), tokenDataDto));
            return tokenDataDtoMap;
        } catch (Exception e) {
            //  schema没有初始化
            return new HashMap<String, TokenDataDto>() {{
                tokenDataDtos.forEach(tokenDataDto -> put(tokenDataDto.getTokenId(), tokenDataDto));
            }};
        }
    }

    /**
     * 根据单据转换规则查数据
     *
     * @param ruleId 单据转换规则
     * @return 数据
     */
    private Object getDataByRuleId(String ruleId) {
        //noinspection unchecked
        return (
                //  graphQL数据
                (Map<String, Object>) GraphQL.newGraphQL(this.graphQLSchema).build().execute("{" + ruleId + "}").getData()
        ).get(ruleId);
    }
}
