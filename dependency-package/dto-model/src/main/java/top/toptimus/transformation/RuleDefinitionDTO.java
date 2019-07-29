package top.toptimus.transformation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import top.toptimus.common.enums.RuleTypeEnum;

/**
 * 单据转换规则定义DTO
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class RuleDefinitionDTO {
    private String ruleId;
    private String originMetaId;
    private String targetMetaId;
    private RuleTypeEnum ruleType;
    private String storedProcedure;
}
