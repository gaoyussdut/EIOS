package top.toptimus.entity.query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.toptimus.repository.FkeyRuleRepository;
import top.toptimus.repository.RuleDefinitionRepository;
import top.toptimus.transformation.FkeyRuleDTO;
import top.toptimus.transformation.RuleDefinitionDTO;

import java.util.List;

/**
 * token query facade
 *
 * @author lzs
 * @since 2019-4-17
 */
@Component
public class TransformationFacadeQueryEntity {

    @Autowired
    private RuleDefinitionRepository ruleDefinitionRepository;
    @Autowired
    private FkeyRuleRepository fkeyRuleRepository;

    public RuleDefinitionDTO findRuleById(String ruleId) {
        return ruleDefinitionRepository.findById(ruleId);
    }

    public List<FkeyRuleDTO> findFkeyRuleById(String ruleId) {
        return fkeyRuleRepository.findById(ruleId);
    }
}
