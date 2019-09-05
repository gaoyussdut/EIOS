package top.toptimus.filter;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import top.toptimus.common.enums.FkeyTypeEnum;
import top.toptimus.common.enums.process.ConditionEnum;

/**
 * 过滤条件DTO
 * Created by lzs on 2019/9/2.
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class FilterDTO {

    private String key; //筛选项key值
    private FkeyTypeEnum fkeyType;    //keytype
    private ConditionEnum conditionEnum;
    private String filterCondition; //过滤条件

//    public

}
