package top.toptimus.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 组织
 * Created by JiangHao on 2018/12/26.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrganizationDTO implements Serializable {

    private static final long serialVersionUID = -4211049701850301304L;

    private String id;  // 组织名称id
    private String organizationName;  // 组织名称
    private String organizationDescription;  // 组织描述
    private int selfId;  //自己的id
    private int parentId;  // 父id

}
