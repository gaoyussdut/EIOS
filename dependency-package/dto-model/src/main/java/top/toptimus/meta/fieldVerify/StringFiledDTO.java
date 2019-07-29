package top.toptimus.meta.fieldVerify;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Created by JiangHao on 2019/6/12.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StringFiledDTO implements Serializable {

    private static final long serialVersionUID = -9024873290408286982L;

    private Integer length; // 限制字符串长度
}
