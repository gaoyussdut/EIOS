package top.toptimus.formula;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Created by lzs on 2018/11/1.
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class FormulaDefinitionDTO implements Serializable {

    private static final long serialVersionUID = -5314221077616320930L;

    private Integer id;
    private String formulaId;    //公式ID
    private String formula;      //公式
    private String formulaName;  //公式名称

}
