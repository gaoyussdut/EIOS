package top.toptimus.common;

import freemarker.template.Template;
import freemarker.template.Version;
import freemarker.template.Configuration;
import top.toptimus.common.fastdfs.FastDFSClient;
import top.toptimus.common.fastdfs.FastDFSFile;
import top.toptimus.constantConfig.Constants;

import java.io.*;
import java.net.URL;
import java.util.Map;

/**
 * 根据word模板生成文档
 *
 */
public class WordTemplate {

//    private final static String ftlTemplatePath="/home/congchenri/Downloads/DOC/";

    private final static String RESOURCE_FTL = "wordTemplate/";  // 模板存放的位置
    private final static String CONFIGURATION_VERSION = "2.3.0"; // 版本号
    private final static String FILE_SUFFIX_FTL = ".ftl";        // 模板文件尾缀
    private final static String FILE_SUFFIX_DOC = "doc";         // 生成文件尾缀

    /**
     * 根据模板生成文件
     *
     * @param dataMap 数据载体
     * @param name    文档名字
     * @return url 生成的文档的URl
     */
    public static String generateWordDoc(Map<String,Object> dataMap, String name ) {
        try {
            URL url = Thread.currentThread().getContextClassLoader().getResource(RESOURCE_FTL);
            String path = url != null ? url.getPath() : "";
            //1.Configuration 用于读取ftl文件
            Configuration configuration = new Configuration(new Version(CONFIGURATION_VERSION));
            configuration.setDefaultEncoding(Constants.UTF_8);

            //2.指定路径
            configuration.setDirectoryForTemplateLoading(new File(path));

            //3.输出文档路径及名称
            File outFile = new File(path + name + "." + FILE_SUFFIX_DOC);

            //4.以utf-8的编码读取ftl文件
            Template template = configuration.getTemplate(name + FILE_SUFFIX_FTL, Constants.UTF_8);
            Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFile), Constants.UTF_8), 10240);
            template.process(dataMap, out);

            //5.读取输出流并上传到FastDFS 并返回路径
            FileInputStream fileInputStream = new FileInputStream(outFile);
            int len1 = fileInputStream.available();
            byte[] file_buff = new byte[len1];
            int read = fileInputStream.read(file_buff);
            FastDFSFile file = new FastDFSFile(name, file_buff, FILE_SUFFIX_DOC);

            String[] fileAbsolutePath = FastDFSClient.upload(file);
            out.close();
            return FastDFSClient.getTrackerUrl() + fileAbsolutePath[0] + "/" + fileAbsolutePath[1];
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
