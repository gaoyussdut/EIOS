package top.toptimus.exception.threadLocal;

/**
 * thread local资源已释放exception
 *
 * @author gaoyu
 * @since 2018-11-24
 */
public class ThreadReleaseException extends RuntimeException {

    public ThreadReleaseException(String prefix) {
        super("资源" + prefix + "已经释放");
    }
}
