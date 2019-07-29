package top.toptimus.meta;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import top.toptimus.common.enums.FkeyTypeEnum;

import java.io.Serializable;

/**
 * Created by jianghao on 2018/5/10.
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class FKeyTypeDto implements Serializable {

    private static final long serialVersionUID = -1045846106807519673L;

    private String key;
    private FkeyTypeEnum Type; // 基础数据类型

}
