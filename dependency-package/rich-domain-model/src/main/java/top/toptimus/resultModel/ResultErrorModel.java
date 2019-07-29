package top.toptimus.resultModel;

import lombok.Getter;
import top.toptimus.baseModel.BaseModel;
import top.toptimus.common.result.Result;
import top.toptimus.exception.TopErrorCode;

/**
 * aop做service层日志，result做service报错的返回值
 *
 * @author gaoyu
 * @since 2019-01-09
 */
public class ResultErrorModel extends BaseModel {
    @Getter
    private Result result;

    /**
     * GENERAL_ERR(9000, "通用错误"),
     *
     * @param e exception
     */
    public ResultErrorModel(Exception e) {
        this.buildErrorMessage(false, e.getMessage());
        this.result = new Result(
                TopErrorCode.GENERAL_ERR.getErrcode()
                , e.getMessage()
        );
    }

    /**
     * 自定义exception
     *
     * @param topErrorCode 错误码
     * @param e            exception
     */
    public ResultErrorModel(TopErrorCode topErrorCode, Exception e) {
        this.buildErrorMessage(false, e.getMessage());
        this.result = new Result(
                topErrorCode.getErrcode()
                , e.getMessage()
        );
    }

    /**
     * 自定义exception
     *
     * @param topErrorCode 错误码
     */
    public ResultErrorModel(TopErrorCode topErrorCode) {
        this.buildErrorMessage(false, topErrorCode.getDesc());
        this.result = new Result(
                topErrorCode.getErrcode()
                , topErrorCode.getDesc()
        );
    }
}
