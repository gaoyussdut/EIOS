package top.toptimus.entity.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.toptimus.repository.BusinessCodeRepository;
import top.toptimus.repository.FkeyRuleRepository;
import top.toptimus.repository.RuleDefinitionRepository;
import top.toptimus.rule.BusinessCodeDTO;
import top.toptimus.rule.FkeyRuleDTO;
import top.toptimus.rule.RuleDefinitionDTO;

import java.util.List;

/**
 * rule event
 *
 * @author lzs
 * @since 2019-2-19
 */
@Component
public class RuleEventEntity {

    @Autowired
    private RuleDefinitionRepository ruleDefinitionRepository;
    @Autowired
    private FkeyRuleRepository fkeyRuleRepository;
    @Autowired
    private BusinessCodeRepository businessCodeRepository;

    /**
     * 保存规则属性信息
     *
     * @param ruleDefinitionDTOList
     */
    public void saveRuleDefinitionDTO(List<RuleDefinitionDTO> ruleDefinitionDTOList) {
        ruleDefinitionRepository.saveAll(ruleDefinitionDTOList);
    }

    /**
     * 保存字段规则
     *
     * @param fkeyRuleDTOList
     */
    public void saveAllFkeyRule(List<FkeyRuleDTO> fkeyRuleDTOList) {
        fkeyRuleRepository.saveAll(fkeyRuleDTOList);
    }

    /**
     * 根据事务码删除规则属性
     *
     * @param businessCode
     */
    public void deleteRuleDefinitionDTO(String businessCode) {
        ruleDefinitionRepository.deleteById(businessCode);
    }

    /**
     * 根据事务码删除字段规则
     *
     * @param ruleId
     */
    public void deleteFkeyRule(String ruleId) {
        fkeyRuleRepository.deleteByRuleId(ruleId);
    }

    /**
     * 根据事务码删除事务码属性
     *
     * @param businessCode
     */
    public void deleteBusinessCodeDefinitionDTO(String businessCode) {
        businessCodeRepository.deleteBusinessCodeDefinitionDTO(businessCode);
    }

    public void saveBusinessCodeDefinitionDTO(BusinessCodeDTO businessCodeDTO) {
        businessCodeRepository.save(businessCodeDTO);
    }
}
