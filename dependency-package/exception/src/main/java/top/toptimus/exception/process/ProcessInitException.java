package top.toptimus.exception.process;

/**
 * 流程初始化异常
 *
 * @author gaoyu
 * @since 2018-11-23
 */
public class ProcessInitException extends RuntimeException {
    private static final String MESSAGE = "实例化流程或节点跳转失败！";

    public ProcessInitException() {
        super(MESSAGE);
    }
}
