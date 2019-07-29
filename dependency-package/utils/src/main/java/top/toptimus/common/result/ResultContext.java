package top.toptimus.common.result;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import top.toptimus.common.result.base.BaseResult;
import top.toptimus.exception.HttpStatusEnum;

import java.util.List;

/**
 * restful风格的result接口，封装备查账一览
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ResultContext extends BaseResult {

    private static final long serialVersionUID = -415523723723476761L;

    @JSONField(name = "_resource")
    private Object data;

    @JSONField(name = "_links")
    private List<ResultUserControl> resultLinks;   //  全部链接列表

    @JSONField(name = "_self")
    private ResultLink self;    //  当前链接

    @JSONField(name = "_previous")
    private ResultLink previous;    //  前一个链接

    @JSONField(name = "_next")
    private ResultLink next;    //  后一个链接

    @JSONField(name = "_units")
    private List<ResultUnit> resultUnits; // 数据载体
    /**
     * 正确返回
     *
     * @param resultLinks    全部链接列表
     */
    public ResultContext(
            List<ResultUserControl> resultLinks
    ) {
        this.resultLinks = resultLinks;
    }

    /**
     * 错误信息
     *
     * @param resultCode 错误码
     * @param e          exception
     */
    public ResultContext( int resultCode, Exception e) {
        super(resultCode, e.getMessage());
    }
}