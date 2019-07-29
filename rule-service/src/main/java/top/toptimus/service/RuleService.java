package top.toptimus.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.toptimus.common.result.Result;
import top.toptimus.entity.event.RuleEventEntity;
import top.toptimus.entity.query.RuleQueryFacadeEntity;
import top.toptimus.resultModel.ResultErrorModel;
import top.toptimus.rule.FkeyRuleDTO;
import top.toptimus.rule.RuleDTO;
import top.toptimus.rule.RuleDefinitionDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lzs
 */
@Service
public class RuleService {

    @Autowired
    private RuleQueryFacadeEntity ruleQueryFacadeEntity;
    @Autowired
    private RuleEventEntity ruleEventEntity;

    /**
     * 获取规则一览
     *
     * @return Result
     */
    public Result getRules() {
        try {
            return Result.success(
                    ruleQueryFacadeEntity.getBusinessCodeDefinitionDTOs()
            );
        } catch (Exception e) {
            return new ResultErrorModel(e).getResult();
        }
    }

    /**
     * 获取规则属性信息
     *
     * @return Result
     */
    public Result getRule(String businessCode) {
        try {
            return Result.success(
                    ruleQueryFacadeEntity.getBusinessCodeDefinitionDTO(businessCode)
            );
        } catch (Exception e) {
            return new ResultErrorModel(e).getResult();
        }
    }

    /**
     * 获取字段规则信息
     *
     * @return Result
     */
    public Result getFkeyRuleByBusinessCode(String businessCode, String billMetaId) {
        try {

            return Result.success(
                    ruleQueryFacadeEntity.getMetaRuleDTOByBusinessCode(businessCode, billMetaId)
            );
        } catch (Exception e) {
            return new ResultErrorModel(e).getResult();
        }
    }

    /**
     * 获取来源字段信息(SELECT平铺)
     *
     * @return Result
     */
    public Result getCalculateFkeyByBusinessCode(String billMetaId) {
        try {

            return Result.success(
                    ruleQueryFacadeEntity.getCalculateFkeyByBusinessCode(billMetaId)
            );
        } catch (Exception e) {
            return new ResultErrorModel(e).getResult();
        }
    }

    /**
     * 获取字段规则信息
     *
     * @return Result
     */
    public Result getBusinessCodeDetail(String businessCode) {
        try {

            return Result.success(
                    ruleQueryFacadeEntity.getBusinessCodeDetail(businessCode)
            );
        } catch (Exception e) {
            return new ResultErrorModel(e).getResult();
        }
    }

    /**
     * 保存字段规则
     *
     * @param ruleDTO 规则配置页面DTO
     * @return Result
     */
    public Result saveRule(RuleDTO ruleDTO) {

        try {
            // 保存属性
            ruleEventEntity.deleteBusinessCodeDefinitionDTO(ruleDTO.getBusinessCodeDTO().getBusinessCode());
            ruleEventEntity.saveBusinessCodeDefinitionDTO(ruleDTO.getBusinessCodeDTO());
            ruleEventEntity.deleteRuleDefinitionDTO(ruleDTO.getBusinessCodeDTO().getBusinessCode());
            ruleEventEntity.saveRuleDefinitionDTO(ruleDTO.getRuleDefinitionDTOList());
            ruleDTO.getRuleDefinitionDTOList().forEach(ruleDefinitionDTO -> {
                ruleEventEntity.deleteFkeyRule(ruleDefinitionDTO.getRuleId());
            });
            ruleEventEntity.saveAllFkeyRule(
                    // 保存字段规则
                    new ArrayList<FkeyRuleDTO>() {{
                        ruleDTO.getRuleDefinitionDTOList().forEach(ruleDefinitionDTO -> addAll(ruleDefinitionDTO.getFkeyRuleDTOList()));
                    }}
            );
            return Result.success();
        } catch (Exception e) {
            return new ResultErrorModel(e).getResult();
        }
    }

    /**
     * 删除字段规则
     *
     * @param businessCode businessCode
     * @return Result
     */
    public Result deleteRule(String businessCode) {

        try {
            // 删除属性
            ruleEventEntity.deleteBusinessCodeDefinitionDTO(businessCode);
            List<RuleDefinitionDTO> ruleDefinitionDTOList = ruleQueryFacadeEntity.getRuleDefinitionByBusinessCode(businessCode);
            ruleEventEntity.deleteRuleDefinitionDTO(businessCode);
            ruleDefinitionDTOList.forEach(ruleDefinitionDTO -> {
                ruleEventEntity.deleteFkeyRule(ruleDefinitionDTO.getRuleId());
            });
            return Result.success();
        } catch (Exception e) {
            return new ResultErrorModel(e).getResult();
        }
    }
}
