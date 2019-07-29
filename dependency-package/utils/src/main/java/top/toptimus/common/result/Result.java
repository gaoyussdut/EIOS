package top.toptimus.common.result;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import top.toptimus.common.result.base.BaseResult;
import top.toptimus.exception.TopErrorCode;

@NoArgsConstructor
@Getter
@Setter
public class Result extends BaseResult {

    private Object data;

    public Result(int resultCode, String msg) {
        super(resultCode, msg);
    }

    private Result(int resultCode, String msg, Object data) {
        super(resultCode, msg);
        this.data = data;
    }

    /**
     * 成功，待返回数据结构
     *
     * @param data data
     * @return result
     */
    public static Result success(Object data) {
        return new Result(
                TopErrorCode.SUCCESS.getErrcode()
                , TopErrorCode.SUCCESS.getDesc()
                , data
        );
    }

    /**
     * 成功，不带返回数据结构
     *
     * @return result
     */
    public static Result success() {
        return new Result(
                TopErrorCode.SUCCESS.getErrcode()
                , TopErrorCode.SUCCESS.getDesc()
        );
    }

    /**
     * 失败 带错误数据
     * @param topErrorCode  错误码
     * @param data   错误数据
     * @return
     */
    public static Result fail(TopErrorCode topErrorCode, Object data) {
        return new Result(
                topErrorCode.getErrcode()
                , topErrorCode.getDesc()
                , data
        );
    }
}