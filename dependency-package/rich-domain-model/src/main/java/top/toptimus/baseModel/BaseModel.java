package top.toptimus.baseModel;

import lombok.Getter;

import java.util.Calendar;

/**
 * model类的的基类
 * 用于返回事件处理结果
 * Model类中如果捕获了异常  将isSuccess属性改为false
 * <p>
 * Created by JiangHao on 2018/9/30.
 */
@Getter
public abstract class BaseModel {

    // 事件是否处理成功  默认是true  出现异常情况改为false
    private boolean isSuccess = true;
    // 如果出现错误的错误信息
    private String errorMessage;
    // 时间戳
    private long date;

    /**
     * 只能在model类内部定义
     *
     * @param isSuccess    事件是否处理成功  默认是true  出现异常情况改为false
     * @param errorMessage 如果出现错误的错误信息
     */
    protected void buildErrorMessage(Boolean isSuccess, String errorMessage) {
        this.isSuccess = isSuccess;
        this.errorMessage = errorMessage;
        this.date = Calendar.getInstance().getTimeInMillis();
    }

}
