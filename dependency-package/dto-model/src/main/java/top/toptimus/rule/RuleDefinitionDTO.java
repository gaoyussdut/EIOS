package top.toptimus.rule;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 规则属性DTO
 * Created by lzs
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class RuleDefinitionDTO implements Serializable {

    private static final long serialVersionUID = -761494940066113918L;

    private String ruleId;      //规则Id
    private String ruleName;    //规则名称
    private String businessCode;    //事务码
    private String ruleType;    //规则类型
    private String metaId;      //metaId
    private List<FkeyRuleDTO> fkeyRuleDTOList = new ArrayList<>();   //公式
    private List<EndPointDTO> endPointDTOList = new ArrayList<>();   //endpoint
    private List<StoredProcedureDTO> storedProcedureDTOList = new ArrayList<>();  //存储过程

    public RuleDefinitionDTO(String ruleId, String ruleName, String businessCode, String ruleType, String metaId) {
        this.ruleId = ruleId;
        this.ruleName = ruleName;
        this.businessCode = businessCode;
        this.ruleType = ruleType;
        this.metaId = metaId;
    }

    public RuleDefinitionDTO build(List<FkeyRuleDTO> fkeyRuleDTOList) {
        this.fkeyRuleDTOList = fkeyRuleDTOList;
        return this;
    }

}
