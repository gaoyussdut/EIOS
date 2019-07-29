package top.toptimus.meta.fieldVerify;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * String子类型 邮箱认证
 * <p>
 * Created by JiangHao on 2019/6/13.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmailFiledDTO extends StringFiledDTO implements Serializable {

    private static final long serialVersionUID = 158428726439458990L;

    private Boolean closeVerify; // 是否不验证邮箱格式 （不配置默认关闭 正常效验格式是否正确）

}
