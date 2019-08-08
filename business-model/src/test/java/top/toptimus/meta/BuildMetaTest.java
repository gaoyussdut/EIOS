package top.toptimus.meta;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import top.toptimus.BusinessModelApplication;
import top.toptimus.controller.MetaController;
import top.toptimus.formula.FormulaKeyAndValueDTO;
import top.toptimus.meta.metaview.MetaInfoDTO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = BusinessModelApplication.class, webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class BuildMetaTest {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private MetaController metaController;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    public  void createMasterData() {

        //输入metaId
         String metaId = "chunshan_dingdan_bill";
        String metaName = "订单信息";


        System.out.println("----------------创建MetaInfo----------------");
        //保存meta信息
        List<MetaInfoDTO> metaInfoDTOList = this.buildMeta(metaId,metaName);
        metaController.save(metaInfoDTOList);
        metaController.createTable(metaId,new ArrayList<String>(){{
            add(metaId);
        }});
    }

    /**
     * metainfomation
     * @return
     */
    private List<MetaInfoDTO> buildMeta(String metaId,String metaName){
        List<MetaInfoDTO> metaInfoDTOList = new ArrayList<MetaInfoDTO>(){{
            add(new MetaInfoDTO(metaId,metaName,"dingdanriqi","订单日期","DATE",true,false,true,"","","","","1","DATE"));
            add(new MetaInfoDTO(metaId,metaName,"dianpu","店铺","STRING",true,false,true,"","","","","1","STRING"));
            add(new MetaInfoDTO(metaId,metaName,"zhuozhuangguwen","着装顾问","STRING",true,false,true,"","","","","1","STRING"));
            add(new MetaInfoDTO(metaId,metaName,"kehuxingming","客户姓名","STRING",true,false,true,"","","","","1","STRING"));
            add(new MetaInfoDTO(metaId,metaName,"kehudizhi","客户地址","STRING",true,false,true,"","","","","1","STRING"));
            add(new MetaInfoDTO(metaId,metaName,"dianhuahaoma","电话号码","DECIMAL",true,false,true,"","","","","1","DECIMAL"));
            add(new MetaInfoDTO(metaId,metaName,"weixin","微信","STRING",true,false,true,"","","","","1","STRING"));
        }};
        return metaInfoDTOList;
    }
}
