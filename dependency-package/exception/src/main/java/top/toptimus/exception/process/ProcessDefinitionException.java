package top.toptimus.exception.process;

/**
 * 流程异常类
 */
public class ProcessDefinitionException extends RuntimeException {
    private static final String MESSAGE = "";

    public ProcessDefinitionException() {
        super(MESSAGE);
    }

    public ProcessDefinitionException(Exception e) {
        super(MESSAGE, e);
    }

    public ProcessDefinitionException(Throwable throwable) {
        super(MESSAGE, throwable);
    }

    public ProcessDefinitionException(String msg) {
        super(msg);
    }
}
