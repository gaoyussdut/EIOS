package top.toptimus.rule;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 规则配置页面DTO
 * Created by lzs
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class RuleDTO implements Serializable {

    private static final long serialVersionUID = -6320188061283207686L;

    private BusinessCodeDTO businessCodeDTO;
    private List<RuleDefinitionDTO> ruleDefinitionDTOList = new ArrayList<>();

}
