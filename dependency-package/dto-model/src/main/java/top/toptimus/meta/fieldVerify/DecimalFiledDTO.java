package top.toptimus.meta.fieldVerify;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 基础类型Decimal的通用效验
 * Created by JiangHao on 2019/6/11.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DecimalFiledDTO implements Serializable {

    private static final long serialVersionUID = -2561795951043522509L;
    
    private Integer length;  //长度限制
    private Integer precision;   //精度
    private String range;    //范围
}
