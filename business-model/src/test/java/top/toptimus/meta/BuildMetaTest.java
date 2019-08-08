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
    public void createMasterData() {

        //输入metaId
        String metaId = "chunshan_dingdan_bill";
        String metaName = "订单信息";


        System.out.println("----------------创建MetaInfo----------------");
        //保存meta信息
        List<MetaInfoDTO> metaInfoDTOList = this.buildMeta(metaId, metaName);
        metaService.saveMetaInfoDTO(metaInfoDTOList);
//        //创建表结构
//        String sql = "CREATE TABLE " + metaId + "( id character varying NOT NULL, ";
//        for(MetaInfoDTO metaInfoDTO : metaInfoDTOList){
//            switch (metaInfoDTO.getFkeytype()){
//                case "STRING":
//                    sql += metaInfoDTO.getKey() + " character varying,";
//                case "DECIMAL":
//                    sql += metaInfoDTO.getKey() + " numeric(15,4),";
//                case "DATE":
//                    sql += metaInfoDTO.getKey() + " timestap without time zone,";
//            }
//        }
//        sql += "CONSTRAINT " + metaId + "_pkey PRIMARY KEY (id)) WITH (OIDS=FALSE);ALTER TABLE " + metaId + " OWNER TO postgres";
//        jdbcTemplate.execute(sql);
//
//
//        List<FormulaKeyAndValueDTO> formulaKeyAndValueDTOS = new ArrayList<>();
//        formulaKeyAndValueDTOS.add(
//                new FormulaKeyAndValueDTO(
//                        "fvalue", "choice(to_long(datediff('year','2017-1-1 00:00:00','2012-1-2 00:00:00'))>5,1,2)"
//                        , ""));
//        BuildMetaTest.formula(new HashMap<String, List<FormulaKeyAndValueDTO>>() {{
////            put(UUID.randomUUID().toString(), "datediff('year','2012-1-1 00:00:00','2017-1-2 00:00:00')");
//            put(UUID.randomUUID().toString()
//                    , formulaKeyAndValueDTOS);
////            put(UUID.randomUUID().toString(), "1+2*3/4");
//        }});
    }

    /**
     * metainfomation
     *
     * @return
     */
    private List<MetaInfoDTO> buildMeta(String metaId, String metaName) {
        return new ArrayList<MetaInfoDTO>() {{
            add(new MetaInfoDTO(metaId, metaName, "dingdanriqi", "订单日期", "DATE", true, false, true, "", "", "", "", "1", "DATE"));
            add(new MetaInfoDTO(metaId, metaName, "dianpu", "店铺", "STRING", true, false, true, "", "", "", "", "1", "STRING"));
            add(new MetaInfoDTO(metaId, metaName, "zhuozhuangguwen", "着装顾问", "STRING", true, false, true, "", "", "", "", "1", "STRING"));
            add(new MetaInfoDTO(metaId, metaName, "kehuxingming", "客户姓名", "STRING", true, false, true, "", "", "", "", "1", "STRING"));
            add(new MetaInfoDTO(metaId, metaName, "kehudizhi", "客户地址", "STRING", true, false, true, "", "", "", "", "1", "STRING"));
            add(new MetaInfoDTO(metaId, metaName, "dianhuahaoma", "电话号码", "DECIMAL", true, false, true, "", "", "", "", "1", "DECIMAL"));
            add(new MetaInfoDTO(metaId, metaName, "weixin", "微信", "STRING", true, false, true, "", "", "", "", "1", "STRING"));
        }};
    }
}
