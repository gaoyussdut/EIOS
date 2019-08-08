package top.toptimus.csv;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * * CSV操作(读取和写入)
 * * @author lq
 * * @version 2018-04-23
 */
public class CSVUtils {
    /**
     * 写入
     *
     * @param filePath csv文件路径(路径+文件名)，csv文件不存在会自动创建
     * @param dataList 数据
     */
    private static boolean exportCsv(String filePath, List<String> dataList) {
        File file = new File(filePath);
        boolean isSucess;

        FileOutputStream out = null;
        OutputStreamWriter osw = null;
        BufferedWriter bw = null;
        try {
            out = new FileOutputStream(file);
            osw = new OutputStreamWriter(out);
            bw = new BufferedWriter(osw);
            if (dataList != null && !dataList.isEmpty()) {
                for (String data : dataList) {
                    bw.append(data).append("\r");
                }
            }
            isSucess = true;
        } catch (Exception e) {
            isSucess = false;
        } finally {
            if (bw != null) {
                try {
                    bw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (osw != null) {
                try {
                    osw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return isSucess;
    }

    /**
     * 读取
     *
     * @param filePath csv文件(路径+文件)
     */
    public static List<String> importCsv(String filePath) {
        File file = new File(filePath);
        List<String> dataList = new ArrayList<>();

        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(file));
            String line;
            while ((line = br.readLine()) != null) {
                dataList.add(line);
            }
        } catch (Exception ignored) {
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return dataList;
    }


    /**
     * 测试
     */
    public static void main(String[] args) {
        //exportCsv();
        importCsv();
    }


    /**
     * CSV读取测试
     */
    private static void importCsv() {
        List<String> dataList = CSVUtils.importCsv("/Users/gaoyu/Desktop/测试.csv");
        if (dataList != null && !dataList.isEmpty()) {
            for (int i = 0; i < dataList.size(); i++) {
                if (i != 0) {//不读取第一行
                    String s = dataList.get(i);
                    String[] as = s.split(",");
                    System.out.println(as[0]);
                    System.out.println(as[1]);
                    System.out.println(as[2]);
                }
            }
        }
    }

    /**
     * CSV写入测试
     */
    public static void exportCsv() {
        List<String> dataList = new ArrayList<>();
        dataList.add("number,name,sex");
        dataList.add("1,张三,男");
        dataList.add("2,李四,男");
        dataList.add("3,小红,女");
        boolean isSuccess = CSVUtils.exportCsv("/Users/gaoyu/Desktop/测试.csv", dataList);
        System.out.println(isSuccess);
    }
}
