package top.toptimus.meta.signGroup;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
/**
 * 汇签组定义
 */
public class SignGroupDTO implements Serializable {
    private static final long serialVersionUID = 5102516417964517235L;

    private String signGroupId; //汇签组id
    private String signGroupName;//汇签组名
    private String roleId;       //负责人角色id
    private String roleName;     //负责人角色名
    private boolean enabled;     //是否启用
//    private List<SignMember> signMembers; //汇签组成员 TODO
}
