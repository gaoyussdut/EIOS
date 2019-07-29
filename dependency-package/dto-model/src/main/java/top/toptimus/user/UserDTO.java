package top.toptimus.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

/**
 * Created by JiangHao on 2018/12/13.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO implements Serializable {

    private static final long serialVersionUID = -9176359567349618896L;

    private String id;
    private String account; // 账户
    private String passwordDigest;//密码
    private Boolean enabled; // 是否是可用的
    private String name;    // 用户姓名
    private Timestamp createdAt; //创建时间
    private Timestamp updatedAt; //最后修改时间
    private String roleId;  // 角色ID
    private String organizationId; // 组织id
    private String parentId; // 父id：领导的Id
    private List<String> childUserIds; // 子Ids:下属UserIds
    public String address; // 住址
    public String email; // 邮箱
    public Integer age;  // 年龄
    public String gender; // 性别
    public Long telephoneNumber; // 电话号


    public UserDTO(String id, String account, String passwordDigest
            , Boolean enabled, String name, Timestamp createdAt, Timestamp updatedAt
            , String roleId, String organizationId, String parentId, List<String> childUserIds) {
        this.id = id;
        this.account = account;
        this.passwordDigest = passwordDigest;
        this.enabled = enabled;
        this.name = name;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.roleId = roleId;
        this.organizationId = organizationId;
        this.parentId = parentId;
        this.childUserIds = childUserIds;
    }


    public UserDTO(String id, String account, Boolean enabled
            , String name, Timestamp createdAt, Timestamp updatedAt
            , String address, String email, Integer age
            , String gender, Long telephoneNumber) {
        this.id = id;
        this.account = account;
        this.enabled = enabled;
        this.name = name;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.address = address;
        this.email = email;
        this.age = age;
        this.gender = gender;
        this.telephoneNumber = telephoneNumber;
    }
}
