package top.toptimus.meta.fieldVerify;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 货币类型的个性化效验  继承Decimal类型的通用效验
 * Created by JiangHao on 2019/6/11.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CurrencyFiledDTO extends DecimalFiledDTO implements Serializable {

    private static final long serialVersionUID = -771688393449500328L;

    private Boolean enableSeparator; //是否使用分隔符
    private Integer separator; // 每几位一个分隔符

    public CurrencyFiledDTO(Integer length
            , Integer precision
            , String range
            , Boolean enableSeparator
            , Integer separator) {
        super(length, precision, range);
        this.enableSeparator = enableSeparator;
        this.separator = separator;
    }
}
