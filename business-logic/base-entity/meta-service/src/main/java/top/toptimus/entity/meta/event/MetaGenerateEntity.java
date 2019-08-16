package top.toptimus.entity.meta.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.toptimus.meta.csv.MetaCSVImportModel;
import top.toptimus.meta.csv.MetaIndexDTO;

/**
 * meta数据导入实体
 *
 * @author gaoyu
 * @since 2019-08-16
 */
@Component
public class MetaGenerateEntity {
    @Autowired
    private MetaEventEntity metaEventEntity;

    /**
     * 批量导入基础meta
     *
     * @param metaIndexFile 基础meta的的索引文件
     * @param csvPath       存csv的文件夹的路径
     */
    public void importBaseMeta(String metaIndexFile, String csvPath) {
        for (MetaIndexDTO metaIndexDTO : MetaCSVImportModel.generateMetaIndexDTO(metaIndexFile)) {
            metaEventEntity.saveMetaInfoDTO(
                    MetaCSVImportModel.generateMetaInfoFromCSV(
                            metaIndexDTO.getMetaId()
                            , metaIndexDTO.getTokenMetaName()
                            , csvPath + "/" + metaIndexDTO.getTokenMetaName() + ".csv"  //  文件名
                    )
            );
        }
    }
}
