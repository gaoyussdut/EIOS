package top.toptimus.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 字段效验失败错误返回体
 * Created by JiangHao on 2019/6/12.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FieldVerifyFailDTO implements Serializable {

    private static final long serialVersionUID = 5656275625899073149L;

    private String key;  // 字段效验失败的key
    private String errorMessage;  // key效验失败的提示信息
}
