package top.toptimus.formula.batch;

import java.io.PrintStream;


/**
 * 指令帮助
 * <p>
 * <p>负责向PrintStream打印帮助信息。
 *
 * @author gaoyu
 */
public interface CommandHelper {

    /**
     * 向PrintStream打印帮助
     *
     * @param ps 打印流
     */
    public void printHelp(PrintStream ps);
}
