package top.toptimus.transformation;

import lombok.Data;
import lombok.NoArgsConstructor;
import top.toptimus.common.enums.FkeyTypeEnum;
import top.toptimus.common.enums.RuleTypeEnum;
import top.toptimus.formula.formula.Parser;
import top.toptimus.formula.pushDownProperty.DataProperty;
import top.toptimus.formula.util.DefaultProperties;
import top.toptimus.place.place_deprecated.PlaceDTO;
import top.toptimus.rule.RuleModel;
import top.toptimus.tokendata.TokenDataDto;
import top.toptimus.tokendata.field.FkeyField;

import java.util.ArrayList;
import java.util.List;

/**
 * 单据转换model
 *
 * @author lizongsheng
 */
@NoArgsConstructor
@Data
public class TransformationModel {

    private String ruleId;  //前面必须是字母开头
    private PlaceDTO originPlaceDTO;
    private PlaceDTO targetPlaceDTO;
    private RuleModel ruleModel;
    private RuleTypeEnum ruleType;

    private TokenDataDto tokenDataDto;  //  根据转换规则生成的token
    List<DataProperty> dataPropertyList = new ArrayList<>();


    public TransformationModel(
            RuleDefinitionDTO ruleDefinitionDTO
            , List<FkeyRuleDTO> fkeyRuleDTOList
            , PlaceDTO originPlaceDTO
            , PlaceDTO targetPlaceDTO
            , RuleTypeEnum ruleType
    ) {
        this.ruleId = ruleDefinitionDTO.getRuleId();
        this.originPlaceDTO = originPlaceDTO;
        this.targetPlaceDTO = targetPlaceDTO;
        this.ruleModel = new RuleModel().build(ruleDefinitionDTO, fkeyRuleDTOList);
        this.ruleType = ruleType;
    }


    /**
     * 存下推数据
     */
    public TransformationModel transformation() {
        this.getRuleModel().getFkeyRuleDTOList().forEach(fkeyRuleDTO -> {
            this.dataPropertyList.add(new DataProperty(fkeyRuleDTO, this.getOriginPlaceDTO()));
        });

        //  生成token TODO    ？business code不能用在这，传入的是rule id
        this.generatePushDownRuleResult();
        return this;
    }

    /**
     * 根据下推数据生成token数据
     */
    private void generatePushDownRuleResult() {
        String tokenId = this.getTargetPlaceDTO().getBillTokenId();

        List<FkeyField> fkeyFieldList = new ArrayList<>();
        for (DataProperty dataProperty : dataPropertyList) {
            if (!dataProperty.getFkeyTypeEnum().equals(FkeyTypeEnum.SELECT)) {
                fkeyFieldList.add(
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
            } else {
                fkeyFieldList.add(
                        // Fkey
                        new FkeyField().createSelectFkeyField(
                                dataProperty.getFkeyTypeEnum()  //  字段类型
                                , dataProperty.getFKey()    //  字段fkey
                                , dataProperty.getBusinessId()
                                , String.valueOf(
                                        new Parser()
                                                .parse(dataProperty.getFunction())  //  公式
                                                .getValue(new DefaultProperties(dataProperty.getPropertyValue())) //  传参
                                                .getValue() //  返回值
                                )
                        )
                );
            }
        }
        this.tokenDataDto = new TokenDataDto().build(tokenId).build(fkeyFieldList);
    }

}
