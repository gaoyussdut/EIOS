package top.toptimus.meta;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import top.toptimus.common.enums.MetaDataTypeEnum;

import java.io.Serializable;


/**
 * meta信息定义，token名和类型
 * Created by jianghao on 2018/5/10.
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class TokenMetaInformationDto implements Serializable {

    private static final long serialVersionUID = -4917070414624721884L;

    private String tokenMetaId;
    private String tokenMetaName;
    private String metaType;
    private MetaDataTypeEnum metaDataType;

    public TokenMetaInformationDto(String tokenMetaId, String tokenMetaName, String metaType) {
        this.tokenMetaId = tokenMetaId;
        this.tokenMetaName = tokenMetaName;
        this.metaType = metaType;
    }
}
