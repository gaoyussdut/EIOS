package top.toptimus.meta;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import top.toptimus.BusinessModelApplication;
import top.toptimus.meta.metaview.MetaInfoDTO;
import top.toptimus.service.domainService.MetaService;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = BusinessModelApplication.class, webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class BuildMetaTest {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private MetaService metaService;


    @Test
    public  void createMasterData() {

        //输入metaId
        String metaId = "chunshan_mianliao_entry";
        String metaName = "面料配套信息";


        System.out.println("----------------创建MetaInfo----------------");

        //保存meta信息
        List<MetaInfoDTO> metaInfoDTOList = this.buildMeta(metaId, metaName);
        metaService.saveMetaInfoDTO(metaInfoDTOList);
    }

    /**
     * metainfomation
     *
     * @return
     */
    private List<MetaInfoDTO> buildMeta(String metaId, String metaName) {
        return new ArrayList<MetaInfoDTO>() {{
            add(new MetaInfoDTO(metaId, metaName, "mianliaomingcheng", "面料名称", "STRING", true, false, true, "", "", "", "", "1", "STRING"));
            add(new MetaInfoDTO(metaId, metaName, "pipeikuanshi", "匹配款式", "STRING", true, false, true, "", "", "", "", "1", "STRING"));
            add(new MetaInfoDTO(metaId, metaName, "gongyingshang", "供应商", "STRING", true, false, true, "", "", "", "", "1", "STRING"));
            add(new MetaInfoDTO(metaId, metaName, "mianliaozhifa", "面料织法", "STRING", true, false, true, "", "", "", "", "1", "STRING"));
            add(new MetaInfoDTO(metaId, metaName, "miaoliaofengge", "面料风格", "STRING", true, false, true, "", "", "", "", "1", "STRING"));
            add(new MetaInfoDTO(metaId, metaName, "mianliaochengfen", "面料成份", "STRING", true, false, true, "", "", "", "", "1", "STRING"));
            add(new MetaInfoDTO(metaId, metaName, "mianliaofukuan", "面料幅宽", "STRING", true, false, true, "", "", "", "", "1", "STRING"));
            add(new MetaInfoDTO(metaId, metaName, "mianliaodanjia", "面料单价", "DECIMAL", true, false, true, "", "", "", "", "1", "CURRENCY"));
            add(new MetaInfoDTO(metaId, metaName, "mianliaoyunfei", "面料运费", "DECIMAL", true, false, true, "", "", "", "", "1", "CURRENCY"));
            add(new MetaInfoDTO(metaId, metaName, "mianliaohoudu", "面料厚度", "DECIMAL", true, false, true, "", "", "", "", "1", "DECIMAL"));
            add(new MetaInfoDTO(metaId, metaName, "mianliaojijie", "面料季节", "STRING", true, false, true, "", "", "", "", "1", "STRING"));
            add(new MetaInfoDTO(metaId, metaName, "mianliaosuolv", "面料缩率", "DECIMAL", true, false, true, "", "", "", "", "1", "DECIMAL"));
            add(new MetaInfoDTO(metaId, metaName, "mianliaozhangli", "面料张力", "STRING", true, false, true, "", "", "", "", "1", "STRING"));
        }};
    }
}
