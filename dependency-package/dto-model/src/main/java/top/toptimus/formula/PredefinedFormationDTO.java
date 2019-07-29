package top.toptimus.formula;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Created by JiangHao on 2018/9/10.
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class PredefinedFormationDTO implements Serializable {

    private static final long serialVersionUID = -6205430700593058848L;

    private Integer id;
    private String formula;
    private String describe;
    private String formulaType;
}
