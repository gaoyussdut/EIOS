package top.toptimus.meta.MetaRelation;

import lombok.*;

import java.io.Serializable;

/**
 * Created by lzs on 2019/1/17.
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ViewMemorandvnDTO implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -79126780912672794L;

    private String masterMemorandvnMetaId;  //单据内备查帐视图meta
    private String masterMemorandvnName;    //单据内备查帐名称
    private Boolean isDefault;
}
