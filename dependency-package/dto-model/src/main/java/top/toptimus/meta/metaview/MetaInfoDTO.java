package top.toptimus.meta.metaview;

import lombok.Data;
import lombok.NoArgsConstructor;
import top.toptimus.common.enums.FkeyTypeEnum;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * v_meta_edit中的信息
 *
 * @author gaoyu
 * @since 2018-07-05
 */
@NoArgsConstructor
@Data
public class MetaInfoDTO implements Serializable {

    private static final long serialVersionUID = 4165483683136760563L;

    private String metaId;
    private String tokenMetaName;
    private String key;
    private String caption;  //显示字段
    private String fkeytype; //fkey对应的数据的类型（包含select）
    private boolean visible; //是否可见
    private boolean readOnly;//是否只读
    private boolean required;//是否必填
    private String validation;    // Json 根据fkeytype查找的个性checkDTO
    private String metaName; //关联meta的meta名
    private String fKey;     //关联的key
    private String metaKey;  //关联meta的ID
    private String order_;   // 顺序

    private String fkeyBaseType;      // 基础类型

    /**
     * 构造函数
     *
     * @param rs 查询结果集
     * @throws SQLException SQLException
     */
    public MetaInfoDTO(ResultSet rs) throws SQLException {
        this.metaId = rs.getString("meta_id");
        this.tokenMetaName = rs.getString("token_meta_name");
        this.key = rs.getString("key");
        this.caption = rs.getString("caption");
        this.fkeytype = rs.getString("fkeytypeenum");
        this.visible = rs.getBoolean("visible");
        this.readOnly = rs.getBoolean("readonly");
        this.required = rs.getBoolean("required");
        this.validation = rs.getString("validation");
        this.metaName = rs.getString("meta_name");
        this.fKey = rs.getString("fkey");
        this.metaKey = rs.getString("meta_key");
        this.order_ = rs.getString("order_");
        this.fkeyBaseType =
                FkeyTypeEnum
                        .valueOf(rs.getString("fkeytypeenum"))
                        .getBaseFkey()
                        .name();
    }

    /**
     * 校验是否基本类型
     *
     * @return 是否基本类型
     */
    public boolean isBaseFkeyType() {
        return FkeyTypeEnum.valueOf(this.getFkeytype()).isBaseFkeyType();
    }

    public MetaInfoDTO build(String metaId) {
        this.metaId = metaId;
        return this;
    }

}