package top.toptimus.formula.util;

/**
 * 命令行变量集
 * <p>
 * <p>我们可以把命令行封装为变量集,命令行的格式必须为:</p>
 * ~~~~~~~~~~~~~~~~~~~~~{.shell}
 * [var1]=[value1] [var2]=[value2]
 * ~~~~~~~~~~~~~~~~~~~~~
 *
 * @author gaoyu
 * @version 1.6.0.4 [20141114 gaoyu] <br>
 * - 如果参数值为空，设置为true <br>
 */
public class CommandLine extends DefaultProperties {

    /**
     * 构造函数
     *
     * @param _cmd   命令行数组
     * @param parent 父节点
     */
    public CommandLine(String[] _cmd, Properties parent) {
        super("Default", parent);

        parseCmdLine(_cmd);
    }

    /**
     * 构造函数
     *
     * @param _cmd 命令行
     * @see #CommandLine(String[], Properties)
     */
    public CommandLine(String[] _cmd) {
        this(_cmd, null);
    }

    /**
     * 解析命令行
     *
     * @param _cmd 命令行
     */
    protected void parseCmdLine(String[] _cmd) {
        for (int i = 0; i < _cmd.length; i++) {
            String __cmd = _cmd[i];
            int __index = __cmd.indexOf("=");
            if (__index >= 0) {
                String __name = __cmd.substring(0, __index);
                String __value = __cmd.substring(__index + 1, __cmd.length());
                if (__name != null && __name.length() > 0) {
                    if (__value != null && __value.length() > 0) {
                        SetValue(__name, __value);
                    } else {
                        SetValue(__name, "true");
                    }
                }
            } else {
                SetValue(__cmd, "true");
            }
        }
    }
}
