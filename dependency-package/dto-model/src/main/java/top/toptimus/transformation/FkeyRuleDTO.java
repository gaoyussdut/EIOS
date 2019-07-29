package top.toptimus.transformation;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import top.toptimus.rule.formula.MetaAndFkeyDTO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * 单据转换字段规则定义DTO
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class FkeyRuleDTO {
    private String ruleId;
    private String key;
    private String caption;
    private String fkeyType;
    private String formula;
    private String analyticalFormula;
    private Map<String, MetaAndFkeyDTO> params_;  // k:变量名称  v:用于计算的metaId和fkey

    public FkeyRuleDTO(ResultSet rs) throws SQLException {
        this.ruleId = rs.getString("rule_id");
        this.key = rs.getString("key");
        this.caption = rs.getString("caption");
        this.fkeyType = rs.getString("fkey_type");
        this.formula = rs.getString("formula");
        this.analyticalFormula = rs.getString("analytical_formula");
        this.params_ = JSON.parseObject(rs.getString("params_"), new TypeReference<HashMap<String, MetaAndFkeyDTO>>() {
        });
    }
}
