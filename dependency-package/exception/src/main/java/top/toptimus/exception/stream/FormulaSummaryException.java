package top.toptimus.exception.stream;

/**
 * 公式聚合exception
 */
public class FormulaSummaryException extends RuntimeException {
    private static final String MESSAGE = "聚合公式没取到！";

    public FormulaSummaryException() {
        super(MESSAGE);
    }
}
