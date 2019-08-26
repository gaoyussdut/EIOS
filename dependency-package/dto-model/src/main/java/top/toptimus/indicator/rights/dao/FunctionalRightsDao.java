package top.toptimus.indicator.rights.dao;

import lombok.Getter;
import lombok.NoArgsConstructor;
import top.toptimus.indicator.rights.base.BaseFunctionalRights;

/**
 * 功能权限dao
 *
 * @author gaoyu
 * @since 2019-08-21
 */
@Getter
@NoArgsConstructor
public class FunctionalRightsDao extends BaseFunctionalRights {
    private String id;  //  权限的id
    private String pId; //  权限的父id

    /**
     * 构造函数
     *
     * @param id                权限的id
     * @param pId               权限的父id
     * @param rightName         权限名
     * @param level             层级
     * @param isFunctionalEntry 是否权限项目
     */
    public FunctionalRightsDao(String id, String pId, String rightName, int level, boolean isFunctionalEntry) {
        super(rightName, level, isFunctionalEntry);
        this.id = id;
        this.pId = pId;
    }
}
