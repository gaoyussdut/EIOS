package top.toptimus.meta;

import lombok.Getter;
import lombok.NoArgsConstructor;
import top.toptimus.meta.property.FormulaInfo;
import top.toptimus.meta.property.MetaFieldDTO;

import java.io.Serializable;
import java.util.List;

/**
 * token meta <br>
 * 包含基本数据格式信息
 *
 * @author liushikuan
 */
@NoArgsConstructor
@Getter
public class TokenMetaInfoDTO implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = -79126780912672794L;
    private String tokenMetaId; // MetaId
    private String metaType;    //
    private String tokenMetaName; // name
    /**
     * 数据的基础信息，包括各种读写的信息
     */
    private List<MetaFieldDTO> metaFields;

    /**
     * 公式配置信息
     */
    private List<FormulaInfo> formulaInfos;

    public TokenMetaInfoDTO(TokenMetaInformationDto tokenMetaInformationDto, List<MetaFieldDTO> metaFields) {
        this.tokenMetaId = tokenMetaInformationDto.getTokenMetaId();
        this.metaType = tokenMetaInformationDto.getMetaType();
        this.tokenMetaName = tokenMetaInformationDto.getTokenMetaName();
        this.metaFields = metaFields;
    }

    public TokenMetaInfoDTO buildTokenMetaInfoDTO(String tokenMetaId, List<MetaFieldDTO> metaFields) {
        this.tokenMetaId = tokenMetaId;
        this.metaFields = metaFields;
        return this;
    }

    public TokenMetaInfoDTO build(String tokenMetaId, String tokenMetaName) {
        this.tokenMetaId = tokenMetaId;
        this.tokenMetaName = tokenMetaName;
        return this;
    }
}
