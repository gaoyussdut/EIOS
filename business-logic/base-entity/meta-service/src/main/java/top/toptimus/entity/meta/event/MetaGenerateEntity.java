package top.toptimus.entity.meta.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.toptimus.common.enums.MetaDataTypeEnum;
import top.toptimus.common.enums.MetaTypeEnum;
import top.toptimus.common.enums.TokenTemplateTypeEnum;
import top.toptimus.meta.TokenMetaInformationDto;
import top.toptimus.meta.csv.MetaCSVImportModel;
import top.toptimus.meta.csv.MetaIndexDTO;
import top.toptimus.model.meta.event.SaveMetaInfoModel;
import top.toptimus.tokenTemplate.TokenTemplateDefinitionDTO;

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

            if (metaIndexDTO.isModify()) {
                //  修改
                metaEventEntity.alterTableAddColumnsByMetaInfo(
                        metaIndexDTO.getMetaId()
                        , MetaCSVImportModel.generateMetaInfoFromCSV( //  变更后的meta info
                                metaIndexDTO.getMetaId()
                                , metaIndexDTO.getTokenMetaName()
                                , csvPath + "/" + metaIndexDTO.getTokenMetaName() + ".csv"  //  文件名
                        )   //  变更后的meta
                );
            } else {
                TokenMetaInformationDto tokenMetaInformationDto = new TokenMetaInformationDto(metaIndexDTO.getMetaId(),metaIndexDTO.getTokenMetaName(), MetaTypeEnum.BILL.name(), MetaDataTypeEnum.MASTERDATA);
                TokenTemplateDefinitionDTO tokenTemplateDefinitionDTO = new TokenTemplateDefinitionDTO(
                        metaIndexDTO.getMetaId()
                        ,metaIndexDTO.getTokenMetaName()
                        ,metaIndexDTO.getMetaId()
                        ,TokenTemplateTypeEnum.BO);
                SaveMetaInfoModel saveMetaInfoModel = new SaveMetaInfoModel(
                        MetaCSVImportModel.generateMetaInfoFromCSV(
                        metaIndexDTO.getMetaId()
                        , metaIndexDTO.getTokenMetaName()
                        , csvPath + "/" + metaIndexDTO.getTokenMetaName() + ".csv"  //  文件名
                )).build(tokenMetaInformationDto).build(tokenTemplateDefinitionDTO);


                metaEventEntity.saveMetaInfo(saveMetaInfoModel);
                //  新增
//                metaEventEntity.saveMetaInfoDTO(
//                        MetaCSVImportModel.generateMetaInfoFromCSV(
//                                metaIndexDTO.getMetaId()
//                                , metaIndexDTO.getTokenMetaName()
//                                , csvPath + "/" + metaIndexDTO.getTokenMetaName() + ".csv"  //  文件名
//                        )
//                );  //  新建表
            }
        }
    }
}
