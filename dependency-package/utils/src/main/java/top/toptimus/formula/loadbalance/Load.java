package top.toptimus.formula.loadbalance;

import top.toptimus.formula.util.Reportable;

/**
 * 负载接口
 *
 * @author gaoyu
 * @version 1.5.3 [20141020 gaoyu] <br>
 * - 改造loadbalance模型 <br>
 * @since 1.2.0
 */
public interface Load extends Reportable {
    /**
     * 获取负载的标示ID
     *
     * @return id
     */
    public String getId();

    /**
     * 获取本负载的权重
     *
     * @return weight
     */
    public int getWeight();

    /**
     * 获取本负载的优先级
     *
     * @return priority
     */
    public int getPriority();

    /**
     * 获取Counter
     * <p>
     * <p>create为true时，如果不存在，则创建一个；为false时，如果不存在则返回为空
     *
     * @param create 是否创建
     * @return LoadCounter
     */
    public LoadCounter getCounter(boolean create);

    /**
     * 进行使用计数
     *
     * @param _duration 本次使用的时长
     * @param error     是否有错误发生
     */
    public void count(long _duration, boolean error);

    /**
     * 是否有效
     *
     * @return if is valid
     */
    public boolean isValid();
}
