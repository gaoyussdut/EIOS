package top.toptimus.meta.csv;

import top.toptimus.common.enums.FkeyTypeEnum;
import top.toptimus.csv.CSVUtils;
import top.toptimus.meta.metaview.MetaInfoDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * 通过csv生成meta
 *
 * @author gaoyu
 * @since 2019-08-08
 */
public class MetaCSVImportModel {

    /**
     * 通过csv生成meta
     *
     * @param metaId        meta id
     * @param tokenMetaName meta名
     * @param filePath      csv文件路径
     * @return meta info list
     */
    public static List<MetaInfoDTO> generateMetaInfoFromCSV(String metaId, String tokenMetaName, String filePath) {
        List<String> dataList = CSVUtils.importCsv(filePath);
        return new ArrayList<MetaInfoDTO>() {{
            if (dataList != null && !dataList.isEmpty()) {
                for (int i = 0; i < dataList.size(); i++) {
                    if (i != 0) {//不读取第一行
                        String s = dataList.get(i);
                        String[] as = s.split(",");
                        if (as[2].equals(FkeyTypeEnum.SELECT.name())) {
                            //  SELECT类型
                            add(new MetaInfoDTO(metaId, tokenMetaName, as[0], as[1], as[2], as[3], as[4]));
                        } else {
                            //  普通类型
                            add(new MetaInfoDTO(metaId, tokenMetaName, as[0], as[1], as[2]));
                        }
                    }
                }
            }
        }};

    }
}
