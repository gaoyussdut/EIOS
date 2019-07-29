package top.toptimus.formula.util;

/**
 * 是否过期接口
 *
 * @author gaoyu
 */
public interface Expired {
    /**
     * 是否已经过期
     *
     * @param now 当前时间
     * @return true if expired, or not return false
     */
    public boolean isExpired(long now);
}
