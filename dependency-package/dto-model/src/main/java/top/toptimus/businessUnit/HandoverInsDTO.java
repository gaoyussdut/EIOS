package top.toptimus.businessUnit;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 交接实例
 *
 * @author lizongsheng
 * @since 2019-04-14
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class HandoverInsDTO {

    private String fromOrgId;
    private String fromBusinessUnitCode;
    private String toOrgId;
    private String toBusinessUnitCode;
    private String certificateMetaId;
    private String certificateTokenId;
    private boolean isRecept = false;
    private String projectId;
    private String fromMetaId;
    private String fromTokenId;
    private String toMetaId;
    private String toTokenId;

    public HandoverInsDTO build(HandoverDefinitionDTO handoverDefinitionDTO, String certificateTokenId, String fromTokenId) {
        this.fromOrgId = handoverDefinitionDTO.getFromOrgId();
        this.fromBusinessUnitCode = handoverDefinitionDTO.getFromBusinessUnitCode();
        this.toOrgId = handoverDefinitionDTO.getToOrgId();
        this.toBusinessUnitCode = handoverDefinitionDTO.getToBusinessUnitCode();
        this.certificateMetaId = handoverDefinitionDTO.getCertificateMetaId();
        this.certificateTokenId = certificateTokenId;
        this.fromMetaId = handoverDefinitionDTO.getFromMetaId();
        this.fromTokenId = fromTokenId;
        this.toMetaId = handoverDefinitionDTO.getToMetaId();
        return this;
    }

}
