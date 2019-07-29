package top.toptimus.common.result.base;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 返回值基类
 *
 * @author gaoyu
 * @since 2019-01-09
 */
@NoArgsConstructor
@Data
public abstract class BaseResult implements Serializable {
    private static final long serialVersionUID = -3948389268625625059L;

    protected String msg;
    protected int resultCode;

    /**
     * 构造返回结构   TODO，restful风格结构和RPC的区分
     *
     * @param resultCode 结果码
     * @param msg        message
     */
    public BaseResult(int resultCode, String msg) {
        this.resultCode = resultCode;
        this.msg = msg;
    }
}
