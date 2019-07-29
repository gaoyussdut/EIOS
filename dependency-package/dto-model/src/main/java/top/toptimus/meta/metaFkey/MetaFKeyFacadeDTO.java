package top.toptimus.meta.metaFkey;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 针对top.toptimus.businessmodel.process.dto.meta.dto.metaFkey.MetaFKeyDTO的二次封装
 * controller中使用
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class MetaFKeyFacadeDTO implements Serializable {

    private static final long serialVersionUID = 3189228400448919757L;

    private boolean readOnly;//是否只读
    private String metaId;
    private List<MetaFKeyDTO> metaFKeyDTOs;
}
