package top.toptimus.businessUnit;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 交接定义
 *
 * @author lizongsheng
 * @since 2019-04-14
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class HandoverDefinitionDTO {

    private String fromOrgId;
    private String fromBusinessUnitCode;
    private String toOrgId;
    private String toBusinessUnitCode;
    private String certificateMetaId;
    private String storedProcedure;
    private String fromMetaId;
    private String toMetaId;
}
