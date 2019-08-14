package top.toptimus.tokenTemplate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import top.toptimus.common.enums.TokenTemplateTypeEnum;

/**
 * tokenTemplate定义
 *
 * @author lzs
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class TokenTemplateDefinitionDTO {

    private String tokenTemplateId;
    private String tokenTemplateName;
    private String billMetaId;
    private TokenTemplateTypeEnum tokenTemplateType;

}
