package top.toptimus.rule;

import lombok.Getter;
import lombok.NoArgsConstructor;
import top.toptimus.transformation.FkeyRuleDTO;
import top.toptimus.transformation.RuleDefinitionDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * 规则model
 *
 * @author lizongsheng
 */
@NoArgsConstructor
@Getter
public class RuleModel {
    private RuleDefinitionDTO ruleDefinitionDTO;
    private List<FkeyRuleDTO> fkeyRuleDTOList = new ArrayList<>();

    public RuleModel build(RuleDefinitionDTO ruleDefinitionDTO, List<FkeyRuleDTO> fkeyRuleDTOList) {
        this.ruleDefinitionDTO = ruleDefinitionDTO;
        this.fkeyRuleDTOList = fkeyRuleDTOList;
        return this;
    }
}
