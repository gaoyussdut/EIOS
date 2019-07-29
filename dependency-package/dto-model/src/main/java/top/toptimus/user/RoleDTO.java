package top.toptimus.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleDTO implements Serializable {

    private static final long serialVersionUID = -8313022030145334672L;

    private String id;            // id
    private String name;          // 名字
    private String description;   // 描述
    private boolean enabled;      // 是否可用
    private Timestamp created_at; // 生成时间
    private Timestamp updated_at; // 更新时间
}
