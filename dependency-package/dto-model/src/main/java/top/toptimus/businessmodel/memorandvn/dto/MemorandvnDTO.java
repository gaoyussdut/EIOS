package top.toptimus.businessmodel.memorandvn.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import top.toptimus.common.enums.event.MemorandvnEnum;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 备查账簿dto
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class MemorandvnDTO implements Serializable {

    private static final long serialVersionUID = 5643860247526889264L;

    private String memorandvnId;// 备查账id
    private String memorandvnName; // 备查账名称
    private String memorandvnMetaId; // 备查账metaId
    private MemorandvnEnum memorandvnType; // 备查帐类型
    private String metaId;     // 分录metaId
    private String memorandvnFkey;     // 备查帐外键
    private String childMemorandvnFkey;  // 下一节点orgFkey
    private String status;

    public MemorandvnDTO(ResultSet rs) throws SQLException {
        this.memorandvnId = rs.getString("memorandvn_id");
        this.memorandvnName = rs.getString("memorandvn_name");
        this.memorandvnMetaId = rs.getString("memorandvn_meta_id");
        this.memorandvnType = MemorandvnEnum.valueOf(rs.getString("memorandvn_type"));
        this.metaId = rs.getString("meta_id");
    }

    public MemorandvnDTO build(String memorandvnFkey) {
        this.memorandvnFkey = memorandvnFkey;
        return this;
    }

    public MemorandvnDTO build(String memorandvnFkey, String childMemorandvnFkey) {
        this.memorandvnFkey = memorandvnFkey;
        this.childMemorandvnFkey = childMemorandvnFkey;
        return this;
    }

    public MemorandvnDTO build(String memorandvnFkey, String childMemorandvnFkey, MemorandvnEnum memorandvnType) {
        this.memorandvnFkey = memorandvnFkey;
        this.childMemorandvnFkey = childMemorandvnFkey;
        this.memorandvnType = memorandvnType;
        return this;
    }

    public MemorandvnDTO build(String memorandvnFkey, MemorandvnEnum memorandvnType) {
        this.memorandvnFkey = memorandvnFkey;
        this.memorandvnType = memorandvnType;
        return this;
    }

    public MemorandvnDTO buildMemorandvnTable(ResultSet rs) throws SQLException {
        this.memorandvnId = rs.getString("memorandvn_id");
        this.memorandvnName = rs.getString("memorandvn_name");
        this.memorandvnMetaId = rs.getString("memorandvn_meta_id");
        this.memorandvnType = MemorandvnEnum.NONE;
        return this;
    }
}
