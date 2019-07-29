package top.toptimus.formula;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Created by JiangHao on 2018/9/7.
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class FormulaDTO implements Serializable {

    private static final long serialVersionUID = -1878298644317900035L;

    private Integer id;
    private String businessAspectId; //业务纬度id
    private String businessAspectName; //业务纬度名称
    private String formula;  // 预定义好的公式
    private String keyList;  // 公式中的key
    private String valueKey;  // 结果存放的key

}
