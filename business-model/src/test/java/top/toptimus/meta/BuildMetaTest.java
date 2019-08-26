package top.toptimus.meta;

import com.alibaba.fastjson.JSON;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import top.toptimus.BusinessModelApplication;
import top.toptimus.common.enums.MetaDataTypeEnum;
import top.toptimus.common.enums.TokenTemplateTypeEnum;
import top.toptimus.entity.meta.event.MetaGenerateEntity;
import top.toptimus.meta.csv.MetaCSVImportModel;
import top.toptimus.meta.metaview.MetaInfoDTO;
import top.toptimus.model.meta.event.SaveMetaInfoModel;
import top.toptimus.repository.meta.ProcessTableRepository;
import top.toptimus.service.domainService.MetaService;
import top.toptimus.tokenTemplate.TokenTemplateDefinitionDTO;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = BusinessModelApplication.class, webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class BuildMetaTest {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private MetaService metaService;
    @Autowired
    private ProcessTableRepository processTableRepository;
    @Autowired
    private MetaGenerateEntity metaGenerateEntity;

    @Test
    public void testImportBaseMeta(){
        metaGenerateEntity.importBaseMeta(
                "/Users/gaoyu/Desktop/testData/metaIndex.csv"
                ,"/Users/gaoyu/Desktop/testData"
        );
    }

    @Test
    public void createMasterData() {

        //输入metaId
        String metaId = "bo_xiaoshouhetong";
        String metaName = "销售合同字典";
        String metaType = "BILL";
        MetaDataTypeEnum metaDataTypeEnum = MetaDataTypeEnum.MASTERDATA;
        //TTID配置
        String tokenTemplateId = "xiaoshouhetong";
        String tokenTemplateName = "销售合同字典";
        TokenTemplateTypeEnum tokenTemplateTypeEnum = TokenTemplateTypeEnum.BO;
//        logger.info(
//                JSON.toJSONString(this.buildMeta(metaId, metaName, "/Users/gaoyu/Desktop/测试.csv"))
//        );

//        System.out.println("----------------创建MetaInfo----------------");
//
        //保存meta信息
        TokenMetaInformationDto tokenMetaInformationDto = new TokenMetaInformationDto(metaId,metaName,metaType,MetaDataTypeEnum.MASTERDATA);
        List<MetaInfoDTO> metaInfoDTOList = this.buildMeta(metaId, metaName);
        //ttid信息
        TokenTemplateDefinitionDTO tokenTemplateDefinitionDTO = new TokenTemplateDefinitionDTO(tokenTemplateId,tokenTemplateName,metaId,tokenTemplateTypeEnum);
        //保存
        SaveMetaInfoModel saveMetaInfoModel = new SaveMetaInfoModel(metaInfoDTOList).build(tokenMetaInformationDto).build(tokenTemplateDefinitionDTO);
        metaService.saveMetaInfo(saveMetaInfoModel);

    }

    /**
     * metainfomation
     *
     * @return meta info列表
     */
    private List<MetaInfoDTO> buildMeta(String metaId, String metaName) {
        return new ArrayList<MetaInfoDTO>() {{
            add(new MetaInfoDTO(metaId, metaName, "hetongbianma", "合同编码", "STRING", true, false, true, "", "", "", "", "1", "STRING"));
            add(new MetaInfoDTO(metaId, metaName, "kehumingcheng", "客户名称", "SELECT", true, false, true, "", "", "", "", "2", "SELECT"));
        }};
    }

    private List<MetaInfoDTO> buildMeta(String metaId, String tokenMetaName, String filePath) {
        return MetaCSVImportModel.generateMetaInfoFromCSV(metaId, tokenMetaName, filePath);
    }
}
