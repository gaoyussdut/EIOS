package top.toptimus.formula;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import top.toptimus.stream.BusinessItem;
import top.toptimus.tokendata.TokenDataDto;
import top.toptimus.tokendata.field.FkeyField;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 公式model
 *
 * @author gaoyu
 */
@NoArgsConstructor
@Getter
public class FormulaModel {
    private List<FormulaDTO> formulaDTOS = new ArrayList<>();  // 业务纬度下配置的公式DTO
    private List<PredefinedFormationDTO> predefinedFormationDTOS = new ArrayList<>(); // 预定义好的公式DTO
    private List<BusinessItem> businessItems = new ArrayList<>();  // 公式计算后的结果

    /**
     * 构造函数，formulaDaos转formulaDTOS
     *
     * @param formulaDTOS 公式dao
     */
    public FormulaModel(List<FormulaDTO> formulaDTOS) {
        this.formulaDTOS.addAll(formulaDTOS);
    }

    /**
     * 根据纬度id取公式计算成果物数据
     *
     * @param formulaDTOS   纬度id下的所有公式
     * @param tokenDataDtos 成果物数据
     */
    public FormulaModel(List<FormulaDTO> formulaDTOS, List<TokenDataDto> tokenDataDtos) {
        if (null != formulaDTOS && !formulaDTOS.isEmpty()) {
            // K:tokenid  V:公式
            Map<String, List<FormulaKeyAndValueDTO>> tokenIdAndFormulaMap = new HashMap<>();

            for (FormulaDTO formulaDao : formulaDTOS) {
                for (TokenDataDto tokenDataDto : tokenDataDtos) {
                    // 是否正确拼出公式 正确拼出公式进行计算 否则不添加到集合
                    boolean isput = true;
                    String formula = formulaDao.getFormula();
                    for (FkeyField fkeyField : tokenDataDto.getFields()) {
                        String formulatest = formula;
                        formula = formula.replace("@" + fkeyField.getKey() + "@", fkeyField.getJsonData());
                        if (!formulatest.equals(formula) && !StringUtils.isNotEmpty(fkeyField.getJsonData())) {
                            isput = false;
                            break;
                        }
                    }
                    if (!isput) {
                        continue;
                    }

                    List<FormulaKeyAndValueDTO> formulaKeyAndValueDTOS = new ArrayList<>();
                    formulaKeyAndValueDTOS.add(new FormulaKeyAndValueDTO(formulaDao.getValueKey(), formula, ""));
                    tokenIdAndFormulaMap.put(tokenDataDto.getTokenId(), formulaKeyAndValueDTOS);
                }
            }
            //  得到公式计算后的结果
            this.build(tokenIdAndFormulaMap);
        }
    }

    /**
     * k:token id，v：公式
     * 给出公式计算后的结果
     *
     * @param tokenFunctionMap k：token id，V:公式
     */
    public FormulaModel build(Map<String, List<FormulaKeyAndValueDTO>> tokenFunctionMap) {
        this.businessItems = new ArrayList<BusinessItem>() {
            private static final long serialVersionUID = 6772330727436323978L;

            {
                for (String tokenId : tokenFunctionMap.keySet()) {
                    for (FormulaKeyAndValueDTO formulaKeyAndValueDTO : tokenFunctionMap.get(tokenId)) {
                        add(new BusinessItem().build(tokenId
                                , formulaKeyAndValueDTO.getFormula()
                                , formulaKeyAndValueDTO.getKey()));
                    }
                }
            }
        };
        return this;
    }
}
