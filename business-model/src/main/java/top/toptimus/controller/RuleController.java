package top.toptimus.controller;

import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import top.toptimus.common.result.Result;
import top.toptimus.rule.RuleDTO;
import top.toptimus.service.RuleService;

/**
 * 规则接口
 *
 * @author lizongsheng
 */
@Api(value = "规则接口", tags = "规则管理")
@RestController
@RequestMapping(value = "/rule")
@Controller
public class RuleController {

    @Autowired
    private RuleService ruleService;


    /**
     * 规则一览
     *
     * @return Result
     */
    @GetMapping(value = "/getRules")
    public Result getRules() {
        return ruleService.getRules();
    }

    /**
     * 获取规则属性信息
     *
     * @return Result
     */
    @GetMapping(value = "/getRule")
    public Result getRule(@RequestParam String businessCode) {
        return ruleService.getRule(businessCode);
    }

    /**
     * 获取字段规则信息
     *
     * @return Result
     */
    @GetMapping(value = "/getFkeyRuleByBusinessCode")
    public Result getFkeyRuleByBusinessCode(@RequestParam(required = false) String businessCode, @RequestParam String metaId) {
        return ruleService.getFkeyRuleByBusinessCode(businessCode, metaId);
    }

    /**
     * 获取来源字段信息
     *
     * @return Result
     */
    @GetMapping(value = "/getCalculateFkeyByBusinessCode")
    public Result getCalculateFkeyByBusinessCode(@RequestParam String metaId) {
        return ruleService.getCalculateFkeyByBusinessCode(metaId);
    }

    /**
     * 保存字段规则信息
     *
     * @return Result
     */
    @PostMapping(value = "/saveRule")
    public Result saveRule(@RequestBody RuleDTO ruleDTO) {
        return ruleService.saveRule(ruleDTO);
    }

    /**
     * 保存字段规则信息
     *
     * @return Result
     */
    @GetMapping(value = "/getBusinessCodeDetail")
    public Result getBusinessCodeDetail(@RequestParam String businessCode) {
        return ruleService.getBusinessCodeDetail(businessCode);
    }

    /**
     * 删除字段规则
     *
     * @return Result
     */
    @GetMapping(value = "/deleteBusinessCode")
    public Result deleteBusinessCode(@RequestParam String businessCode) {
        return ruleService.deleteRule(businessCode);
    }

}
