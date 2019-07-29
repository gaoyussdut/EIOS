package top.toptimus.businessUnit;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 业务单元节点DTO
 *
 * @author lizongsheng
 * @since 2019-04-14
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class BusinessUnitTaskDefinitionDTO {

    private String businessUnitCode;
    private String metaId;
    private String taskId;
    private String statusRuleId;
    private String taskType;

}
