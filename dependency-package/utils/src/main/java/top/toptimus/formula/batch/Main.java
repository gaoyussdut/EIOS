package top.toptimus.formula.batch;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import top.toptimus.formula.util.*;
import top.toptimus.formula.util.resource.ResourceFactory;

import java.io.File;
import java.io.FilenameFilter;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;


/**
 * 处理入口
 *
 * @author gaoyu
 * @version 1.6.8.10 [20170418 gaoyu] <br>
 * - 在装入xml配置文件时，可从env中获取变量 <br>
 */
public class Main implements CommandHelper, Process {

    /**
     * Help指令
     */
    public static final String CMD_HELP = "help";
    /**
     * a logger of log4j
     */
    protected static final Logger LOG = LoggerFactory.getLogger(Main.class);

    /**
     * a print stream to print help
     */
    protected static PrintStream helpPS = System.out;// NOSONAR
    /**
     * 当前指令
     */
    protected String command = CMD_HELP;

    /**
     * 命令行的变量集
     */
    protected DefaultProperties commandLine = null;

    /**
     * 帮助主题
     */
    protected String helpTopic = "all";

    /**
     * 当前支持的command列表
     */
    protected Hashtable<String, Command> commands = new Hashtable<String, Command>();// NOSONAR

    /**
     * 扩展CLASSPATH列表
     */
    protected List<URL> libs = new ArrayList<URL>();

    /**
     * 当前的资源工厂
     */
    protected ResourceFactory resourceFactory = null;

    protected static void addLibary(List<URL> urls, String path) {
        LOG.info("Searching jar libary in :" + path);
        File dir = new File(path);
        if (!dir.isDirectory()) {
            return;
        }

        File[] jars = dir.listFiles(new FilenameFilter() {
            public boolean accept(File file, String name) {
                if (name.endsWith(".jar") || name.endsWith(".zip"))
                    return true;
                return false;
            }
        });

        if (jars != null) {
            for (File file : jars) {
                try {
                    urls.add(file.toURI().toURL());
                    LOG.info("Add Jar:" + file.toString());
                } catch (MalformedURLException e) {
                }
            }
        }
    }

    public static void main(String[] args) {
        int result = 0;
        try {
            CommandLine cmdLine = new CommandLine(args, new SystemProperties());
            Main main = new Main();
            result = main.init(cmdLine);
            if (result == 0) {
                result = main.run();
            }
        } catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);
            result = -1;
        }
        System.exit(result);
    }

    @Override
    public void printHelp(PrintStream ps) {
        if (!"all".equals(helpTopic)) {
            Command cmd = commands.get(helpTopic);
            if (cmd != null) {
                ps.println("Syntax:");
                ps.println("Main cmd=<command> [<var>=<value>]");
                cmd.printHelp(ps);
            } else {
                printRootHelp(ps);
            }
        } else {
            printRootHelp(ps);
        }
    }

    private void printRootHelp(PrintStream ps) {
        ps.println("Syntax:");
        ps.println("Main cmd=<command> [<var>=<value>]");
        ps.println("Commands supported are listed below:");

        Enumeration<Command> cmds = commands.elements();

        while (cmds.hasMoreElements()) {
            Command cmd = cmds.nextElement();
            ps.println("\t-" + cmd.getId() + "\t:" + cmd.getNote());
        }

        ps.println("\t-help\t:Print this help");
    }

    /**
     * 从本地文件装入配置
     *
     * @param p        变量集
     * @param filename 本地文件名
     */
    private void loadConfigFromLocalFile(DefaultProperties p, String filename) {
        File file = new File(filename);
        if (file.exists() && file.isFile()) {
            try {
                Document doc = XmlTools.loadFromFile(file);
                if (doc != null) {
                    loadConfigFromElement(p, doc.getDocumentElement());
                }
            } catch (Exception e) {
                LOG.error("Can not load xml file,url = " + filename, e);
            }
        } else {
            LOG.error("The config file is not a valid file,url = "
                    + filename);
        }
    }

    private ResourceFactory getResourceFactory(DefaultProperties p) {
        if (resourceFactory == null) {
            // 设置全局的ResourceFactory
            String rf = p.GetValue("resource.factory",
                    "com.anysoft.util.resource.ResourceFactory");
            try {
                LOG.info("Use resource factory:" + rf);
                resourceFactory = (ResourceFactory) Class.forName(rf).newInstance();
            } catch (Exception ex) {
                LOG.error("Can not create instance of :" + rf, ex);
            }
            if (resourceFactory == null) {
                resourceFactory = new ResourceFactory();
                LOG.info("Use default:" + ResourceFactory.class.getName());
            }
        }
        return resourceFactory;
    }

    private void loadConfigFromResource(DefaultProperties p, String fileUrl) {
        ResourceFactory rf = getResourceFactory(p);
        InputStream in = null;
        try {
            in = rf.load(fileUrl, null);
            Document doc = XmlTools.loadFromInputStream(in);
            if (doc != null) {
                loadConfigFromElement(p, doc.getDocumentElement());
            }
        } catch (Exception ex) {
            LOG.error("The config file is not a valid file,url = "
                    + fileUrl);
        } finally {
            IOTools.close(in);
        }
    }

    /**
     * 装入配置信息
     *
     * @param p 环境变量集
     */
    protected void loadConfig(DefaultProperties p) {
        //装入配置文件,从参数conf中读入，缺省为config.xml
        String filename = PropertiesConstants.getString(p, "conf", "");
        if (filename != null && filename.length() > 0) {
            loadConfigFromLocalFile(p, filename);
        } else {
            filename = PropertiesConstants.getString(p, "conf.url", "java:///config.xml");
            loadConfigFromResource(p, filename);
        }
    }

    protected void loadConfig(DefaultProperties p, String link) {
        loadConfigFromResource(p, link);
    }

    private void loadParameterConfigFromElement(DefaultProperties p, Element e) { // NOSONAR
        //首先处理环境变量：settings/parameter
        NodeList nodeList = XmlTools.getNodeListByPath(e, "settings/parameter");
        if (nodeList != null && nodeList.getLength() > 0) {

            for (int i = 0; i < nodeList.getLength(); i++) {
                Node n = nodeList.item(i);

                if (Node.ELEMENT_NODE != n.getNodeType()) {
                    continue;
                }

                Element parameter = (Element) n;

                String id = parameter.getAttribute("id");
                String value = parameter.getAttribute("value");

                boolean fromEnv = XmlTools.getBoolean(e, "fromEnv", false);
                if (fromEnv) {
                    value = System.getenv(value);
                    if (StringUtils.isEmpty(value)) {
                        value = XmlTools.getString(e, "dft", "");
                    }
                } else {
                    boolean fromProperties = XmlTools.getBoolean(e, "fromProperties", false);
                    if (fromProperties) {
                        value = System.getProperty(value);
                        if (StringUtils.isEmpty(value)) {
                            value = XmlTools.getString(e, "dft", "");
                        }
                    }
                }

                boolean system = XmlTools.getBoolean(e, "system", false);
                if (system) {
                    if (id != null && value != null) { // NOSONAR
                        System.setProperty(id, value);
                    }
                } else {
                    // 支持final标示,如果final为true,则不覆盖原有的取值
                    boolean isFinal = XmlTools.getBoolean(e, "final", false);
                    if (isFinal) { // NOSONAR
                        String oldValue = p.GetValue(id, "", false, true);
                        if (oldValue == null || oldValue.length() <= 0) {
                            p.SetValue(id, value);
                        }
                    } else {
                        p.SetValue(id, value);
                    }
                }
            }
        }
    }

    private void loadCommandConfigFromElement(DefaultProperties p, Element e) {
        //处理commands/command
        NodeList nodeList = XmlTools.getNodeListByPath(e, "commands/command");
        if (nodeList != null && nodeList.getLength() > 0) {

            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);

                if (Node.ELEMENT_NODE != node.getNodeType()) {
                    continue;
                }

                Element element = (Element) node;

                Command cmd = new Command();
                cmd.configure(element, p);

                if (cmd.isOk()) {
                    boolean overwrite = BooleanUtils.toBoolean(element.getAttribute("overwrite"));
                    if (!overwrite) {
                        overwrite = !commands.containsKey(cmd.getId());
                    }
                    if (overwrite) {
                        commands.put(cmd.getId(), cmd);
                    }
                }
            }
        }
    }

    private void loadIncludeConfigFromElement(DefaultProperties p, Element e) {
        //处理includes/include
        NodeList nodeList = XmlTools.getNodeListByPath(e, "includes/include");
        if (nodeList != null && nodeList.getLength() > 0) {

            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);

                if (Node.ELEMENT_NODE != node.getNodeType()) {
                    continue;
                }

                Element element = (Element) node;

                String link = element.getAttribute("link");
                if (StringUtils.isNotEmpty(link)) {
                    String loadable = element.getAttribute("loadable");
                    if (StringUtils.isNotEmpty(loadable)) { // NOSONAR
                        String load = p.transform(loadable);
                        if (StringUtils.isNotEmpty(load)) {
                            loadConfig(p, p.transform(link));
                        } else {
                            LOG.info("Find xml link file,but the loadable is null,ignore...");
                        }
                    } else {
                        loadConfig(p, p.transform(link));
                    }
                }
            }
        }
    }

    protected void loadConfigFromElement(DefaultProperties p, Element e) {
        loadParameterConfigFromElement(p, e);
        loadCommandConfigFromElement(p, e);
        loadIncludeConfigFromElement(p, e);
        loadLibaryFromElement(p, e);
    }

    private void loadLibaryFromElement(DefaultProperties p, Element e) {
        //处理libs/path
        NodeList nodeList = XmlTools.getNodeListByPath(e, "libs/lib");
        if (nodeList != null && nodeList.getLength() > 0) {

            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);

                if (Node.ELEMENT_NODE != node.getNodeType()) {
                    continue;
                }

                Element element = (Element) node;
                String path = element.getAttribute("path");
                if (StringUtils.isNotEmpty(path)) {
                    String loadable = element.getAttribute("loadable");
                    if (StringUtils.isNotEmpty(loadable)) { // NOSONAR
                        String load = p.transform(loadable);
                        if (StringUtils.isNotEmpty(load)) {
                            addLibary(libs, p.transform(path));
                        } else {
                            LOG.info("Find xml lib file,but the loadable is null,ignore..");
                        }
                    } else {
                        addLibary(libs, p.transform(path));
                    }

                }
            }
        }
    }

    @Override
    public int init(DefaultProperties p) {
        commandLine = new DefaultProperties("default", p);
        commandLine.addSettings(p);
        //从配置文件中装入
        loadConfig(commandLine);
        command = PropertiesConstants.getString(p, "cmd", CMD_HELP);
        if (command.equals(CMD_HELP)) {
            helpTopic = PropertiesConstants.getString(p, "topic", "all");
        } else {
            boolean helpMode = PropertiesConstants.getBoolean(p, CMD_HELP,
                    false);
            if (helpMode) {
                helpTopic = command;
                command = CMD_HELP;
            }
        }
        return 0;
    }

    protected int checkAndComputeArguments(Command cmd, Properties p) {
        // 检查参数
        List<Argument> arguments = cmd.getArguments();

        for (Argument argu : arguments) {
            String id = argu.getId();
            String value = argu.getValue(p);

            if (value == null || value.length() <= 0) {
                if (!argu.isNullable()) {
                    helpTopic = command;
                    helpPS.println("Can not find argument named " + id);
                    printHelp(helpPS);
                    return -1;
                }
            } else {
                p.SetValue(id, value);
            }
        }

        return 0;
    }

    @Override
    public int run() {
        boolean bless = PropertiesConstants.getBoolean(commandLine, "bless", true);
        if (bless) {
            Copyright.bless(LOG, "\t\t");
        }
        if (command.equals(CMD_HELP)) {
            printHelp(helpPS);
            return -1;
        }
        Command cmd = commands.get(command);
        if (cmd == null) {
            helpTopic = "all";
            helpPS.println("Please give me a valid command...");
            printHelp(helpPS);
            return -1;
        }

        //装入扩展CLASSPATH
        if (libs.size() > 0) {
            ClassLoader parent = Thread.currentThread().getContextClassLoader();
            if (parent == null) {
                parent = Main.class.getClassLoader();
            }
            if (parent == null) {
                parent = ClassLoader.getSystemClassLoader();
            }
            URLClassLoader cl = new URLClassLoader(libs.toArray(new URL[libs.size()]),
                    parent);
            Thread.currentThread().setContextClassLoader(cl);
        }

        //创建Process对象
        String module = cmd.getModule();
        Process process = null;
        try {
            Factory<Process> factory = new Factory<Process>();// NOSONAR
            process = factory.newInstance(module);
        } catch (Exception ex) {
            LOG.error("Can not create process instance,module=" + module, ex);
            return -1;
        }

        DefaultProperties p = commandLine;

        if (checkAndComputeArguments(cmd, p) < 0) {
            return -1;
        }

        int result = process.init(p);
        if (result == 0) {
            result = process.run();
        }
        return result;
    }
}
