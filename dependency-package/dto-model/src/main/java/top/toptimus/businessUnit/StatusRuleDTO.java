package top.toptimus.businessUnit;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 单据状态规则dto
 *
 * @author lizongsheng
 * @since 2019-04-14
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class StatusRuleDTO {
    private String statusRuleId;
    private String storedProcedure;
    private String version;
    private String description;
}
