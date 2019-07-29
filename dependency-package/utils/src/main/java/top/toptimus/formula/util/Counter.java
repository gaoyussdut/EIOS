package top.toptimus.formula.util;


/**
 * 计数接口
 *
 * @author gaoyu
 * @version 1.6.4.31 [20160128 gaoyu] <br>
 * - 增加活跃度和健康度接口 <br>
 * - 增加可配置性 <br>
 * @since 1.5.2
 */
public interface Counter extends Reportable, XMLConfigurable, Configurable {

    /**
     * 获取活跃度
     *
     * @return 活跃度
     */
    public int getActiveScore();

    /**
     * 获取健康度
     *
     * @return 健康度
     */
    public int getHealthScore();

    /**
     * 计数
     *
     * @param duration 耗时
     * @param error    是否错误
     */
    public void count(long duration, boolean error);

    /**
     * 工厂类
     *
     * @author gaoyu
     */
    public static class TheFactory extends Factory<Counter> {
        protected static TheFactory instance;

        static {
            instance = new TheFactory();
        }

        public static Counter getCounter(String module, Properties p) {
            return instance.newInstance(module, p);
        }
    }
}
