package top.toptimus.meta.MetaRelation;

import lombok.*;

import java.io.Serializable;

/**
 * Created by lzs on 2019/1/17.
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ViewMetaDTO implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -79126780912672794L;

    private String metaId;
    private String metaName;
    private Boolean isDefault;

}
