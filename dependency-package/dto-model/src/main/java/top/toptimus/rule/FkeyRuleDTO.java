package top.toptimus.rule;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import top.toptimus.rule.formula.MetaAndFkeyDTO;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * 规则字段规则DTO
 * Created by lzs
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class FkeyRuleDTO implements Serializable {

    private static final long serialVersionUID = -7235404407959650460L;
    
    private String ruleId;
    private String metaId;
    private String key;
    private String caption;
    private String fkeyType;
    private String formula;
    private String analyticalFormula;   //  公式
    private Map<String, MetaAndFkeyDTO> params_;  // k:变量名称  v:用于计算的metaId和fkey
    private boolean editable = true;
    private int order;
    private String calculateType;

    public FkeyRuleDTO(String metaId, String key, String caption, String fkeyType) {
        this.metaId = metaId;
        this.key = key;
        this.caption = caption;
        this.fkeyType = fkeyType;
    }

    public FkeyRuleDTO(ResultSet rs) throws SQLException {
        this.ruleId = rs.getString("rule_id");
        this.metaId = rs.getString("meta_id");
        this.key = rs.getString("key");
        this.caption = rs.getString("caption");
        this.fkeyType = rs.getString("fkey_type");
        this.formula = rs.getString("formula");
        this.analyticalFormula = rs.getString("analytical_formula");
        this.params_ = JSON.parseObject(rs.getString("params"), new TypeReference<HashMap<String, MetaAndFkeyDTO>>() {
        });
        this.editable = rs.getBoolean("editable");
        this.order = rs.getInt("order_");
        this.calculateType = rs.getString("calculate_type");
    }
}