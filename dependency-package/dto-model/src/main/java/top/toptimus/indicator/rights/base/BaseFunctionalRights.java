package top.toptimus.indicator.rights.base;

import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 功能权限基类
 *
 * @author gaoyu
 * @since 2019-08-21
 */
@Getter
@NoArgsConstructor
public abstract class BaseFunctionalRights {
    protected String rightName; //  权限名
    protected int level;    //  层级
    protected boolean isEnable = true;   //  是否启用
    protected boolean isFunctionalEntry;  //  是否权限项目

    /**
     * 构造函数
     *
     * @param rightName         权限名
     * @param level             层级
     * @param isFunctionalEntry 是否权限项目
     */
    public BaseFunctionalRights(String rightName, int level, boolean isFunctionalEntry) {
        this.rightName = rightName;
        this.level = level;
        this.isFunctionalEntry = isFunctionalEntry;
    }
}
