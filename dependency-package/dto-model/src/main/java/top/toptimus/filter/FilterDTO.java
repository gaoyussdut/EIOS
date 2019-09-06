package top.toptimus.filter;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import top.toptimus.common.enums.FkeyTypeEnum;
import top.toptimus.common.enums.process.ConditionEnum;

import java.io.Serializable;

/**
 * 过滤条件DTO
 * Created by lzs on 2019/9/2.
 */

@Data
public class FilterDTO implements Serializable {

    private static final long serialVersionUID = 2636079163602884600L;

    private String key; //筛选项key值
    private FkeyTypeEnum fkeyType;    //keytype
    private ConditionEnum conditionEnum;
    private String filterCondition; //过滤条件

}
