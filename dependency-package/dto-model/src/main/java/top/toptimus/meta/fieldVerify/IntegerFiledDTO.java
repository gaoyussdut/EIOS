package top.toptimus.meta.fieldVerify;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * integer类型通用认证
 * <p>
 * Created by JiangHao on 2019/6/13.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class IntegerFiledDTO implements Serializable {

    private static final long serialVersionUID = -1413290853708359936L;
    
    private Integer length;  //长度限制
    private String range;    //范围
}
