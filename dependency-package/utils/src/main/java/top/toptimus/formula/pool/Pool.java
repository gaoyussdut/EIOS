package top.toptimus.formula.pool;

import top.toptimus.formula.util.BaseException;
import top.toptimus.formula.util.Properties;
import top.toptimus.formula.util.Reportable;

/**
 * 缓冲池接口
 *
 * @param <pooled> 缓冲池对象
 * @author gaoyu
 * @version 1.3.2 [20140815 gaoyu]
 * - 集成Reportable接口
 * @since 1.1.0
 */
public interface Pool<pooled extends AutoCloseable> extends Reportable {

    /**
     * 创建缓冲池
     *
     * @param props 环境变量
     */
    public void create(Properties props);

    /**
     * 关闭缓冲池
     */
    public void close();

    /**
     * 从缓冲池中借出缓冲对象
     *
     * @param priority 优先级
     * @return pooled
     * @throws BaseException
     */
    public pooled borrowObject(int priority, int timeout) throws BaseException;

    /**
     * 归还缓冲对象
     *
     * @param obj 缓冲对象
     */
    public void returnObject(pooled obj);
}
