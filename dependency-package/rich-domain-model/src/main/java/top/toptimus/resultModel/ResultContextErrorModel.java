package top.toptimus.resultModel;

import lombok.Getter;
import top.toptimus.baseModel.BaseModel;
import top.toptimus.common.result.ResultContext;
import top.toptimus.exception.HttpStatusEnum;
import top.toptimus.exception.TopErrorCode;

/**
 * aop做service层日志，result做service报错的返回值
 *
 * @author gaoyu
 * @since 2019-01-09
 */
public class ResultContextErrorModel extends BaseModel {
    @Getter
    private ResultContext resultContext;

    /**
     * GENERAL_ERR(9000, "通用错误"),
     *
     * @param e exception
     */
    public ResultContextErrorModel(Exception e) {
        this.buildErrorMessage(false, e.getMessage());
        this.resultContext = new ResultContext(TopErrorCode.GENERAL_ERR.getErrcode() , e );
    }

}
