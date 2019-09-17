package top.toptimus.formula.pushDownProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import top.toptimus.common.enums.FkeyTypeEnum;
import top.toptimus.place.place_deprecated.PlaceDTO;
import top.toptimus.rule.formula.MetaAndFkeyDTO;
import top.toptimus.tokendata.TokenDataDto;
import top.toptimus.transformation.FkeyRuleDTO;

import java.util.HashMap;
import java.util.Map;

/**
 * 单据下推时候使用的公式dto
 *
 * @author gaoyu
 * @since 2019-02-22
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class DataProperty {
    private String fKey;    //  下推的目标单据字段
    private FkeyTypeEnum fkeyTypeEnum;  //  目标单据类型
    private String function;    //  公式
    private String businessId;
    private Map<String, Object> propertyValue;  //  对应公式给的参数，K：公式字段，V:公式的值

    /**
     * 生成下推公式
     *
     * @param fkeyRuleDTO 规则字段规则DTO
     * @param origin      单据转换DTO
     */
    public DataProperty(FkeyRuleDTO fkeyRuleDTO, PlaceDTO origin) {
        this.fKey = fkeyRuleDTO.getKey();
        this.fkeyTypeEnum = Enum.valueOf(FkeyTypeEnum.class, fkeyRuleDTO.getFkeyType());
        this.function = fkeyRuleDTO.getAnalyticalFormula();
        if (fkeyRuleDTO.getFkeyType().equals("SELECT")) {
            origin.getDatas().get(origin.getBillMetaId()).get(0).getFields().forEach(fkeyField -> {
                if (fkeyField.getKey().equals(fkeyRuleDTO.getParams_().get("params1").getKey())) {
                    this.businessId = fkeyField.getBusinessId();
                }
            });
        }
        this.propertyValue = this.buildExplainFormula(fkeyRuleDTO.getParams_(), origin);    //  替换公式中的字段为数值
    }

    /**
     * 替换公式中的字段为数值
     *
     * @param params K:公式字段，fkey, v: 源单meta和fkey
     * @return K:公式字段, V:json data
     */
    public Map<String, Object> buildExplainFormula(
            Map<String, MetaAndFkeyDTO> params
            , PlaceDTO originPlaceDTO
    ) {
        return new HashMap<String, Object>() {{
            params.keySet().forEach(param -> {
                if (originPlaceDTO.getBillMetaId().equals(params.get(param).getMetaId())) {
                    //  表头
                    originPlaceDTO.getDatas().get(params.get(param).getMetaId()).get(0).getFields().forEach(fkeyField -> {
                        if (fkeyField.getKey().equals(params.get(param).getKey())) {
                            put(param, fkeyField.getJsonData());
                        }
                    });
                } else {
                    //  分录
                    for (TokenDataDto tokenDataDto : originPlaceDTO.getDatas().get(params.get(param).getMetaId())) {
                        tokenDataDto.getFields().forEach(fkeyField -> {
                            if (fkeyField.getKey().equals(params.get(param).getKey())) {
                                put(param, fkeyField.getJsonData());
                            }
                        });
                    }
                }
            });
        }};
    }

}
