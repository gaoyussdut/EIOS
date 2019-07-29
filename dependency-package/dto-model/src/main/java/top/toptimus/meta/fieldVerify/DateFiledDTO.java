package top.toptimus.meta.fieldVerify;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 基础类型DATE的通用效验
 * Created by JiangHao on 2019/6/12.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DateFiledDTO implements Serializable {

    private static final long serialVersionUID = 4205069176825812335L;

    private Boolean enableDefault; // 是否启用默认值
    private String dateFormat;  // 时间格式化格式 例:yyyy-MM-dd hh:mm:ss  、 yyyy-MM-dd

}
