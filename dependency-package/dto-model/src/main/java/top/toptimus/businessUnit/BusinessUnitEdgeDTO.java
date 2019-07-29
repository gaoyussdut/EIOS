package top.toptimus.businessUnit;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import top.toptimus.common.enums.BusinessUnitEdgeTypeEnum;

/**
 * 业务单元流程弧
 *
 * @author lizongsheng
 * @since 2019-04-14
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class BusinessUnitEdgeDTO {

    private String businessUnitCode; //业务单元ID
    private String edgeId; //弧id
    private String fromMetaId;
    private String toMetaId;
    private BusinessUnitEdgeTypeEnum edgeType;
    private String ruleId; // 下推/反写规则

}
