package top.toptimus.formula.batch;

import top.toptimus.formula.util.DefaultProperties;

/**
 * YARN 处理进程
 *
 * @author gaoyu
 */
public interface Process {
    /**
     * 初始化进程
     *
     * @param p 初始化变量集
     * @return 0表示初始化成功，反之则失败
     */
    public int init(DefaultProperties p);

    /**
     * 运行进程
     *
     * @return 0表示初始化成功，反之则失败
     */
    public int run();
}
