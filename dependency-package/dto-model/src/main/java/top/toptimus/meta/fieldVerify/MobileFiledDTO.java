package top.toptimus.meta.fieldVerify;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * String子类型 手机号认证
 * <p>
 * Created by JiangHao on 2019/6/13.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MobileFiledDTO extends StringFiledDTO implements Serializable {

    private static final long serialVersionUID = 6595953429031889237L;

    private Boolean closeVerify; // 是否不验证手机号格式 （不配置默认关闭 正常效验格式是否正确）

}
