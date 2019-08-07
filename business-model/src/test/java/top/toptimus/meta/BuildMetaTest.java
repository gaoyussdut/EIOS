package top.toptimus.meta;

import top.toptimus.controller.MetaController;
import top.toptimus.formula.FormulaKeyAndValueDTO;
import top.toptimus.meta.metaview.MetaInfoDTO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class BuildMetaTest {

    private MetaController metaController;

    public static void main(String[] args) {



        System.out.println("----------------创建MetaInfo----------------");

//        metaController.
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

    private List<MetaInfoDTO> buildMeta(){
        List<MetaInfoDTO> metaInfoDTOList = new ArrayList<MetaInfoDTO>(){{
            add(new MetaInfoDTO("chunshan_dingdan_bill","订单信息","dingdanriqi","订单日期","",true,false,true,"","","","","1","DATE"));
            add(new MetaInfoDTO("chunshan_dingdan_bill","订单信息","dianpu","店铺","",true,false,true,"","","","","1","STRING"));
            add(new MetaInfoDTO("chunshan_dingdan_bill","订单信息","zhuozhuangguwen","着装顾问","",true,false,true,"","","","","1","STRING"));
            add(new MetaInfoDTO("chunshan_dingdan_bill","订单信息","kehuxingming","客户姓名","",true,false,true,"","","","","1","STRING"));
            add(new MetaInfoDTO("chunshan_dingdan_bill","订单信息","kehudizhi","客户地址","",true,false,true,"","","","","1","STRING"));
            add(new MetaInfoDTO("chunshan_dingdan_bill","订单信息","dianhuahaoma","电话号码","",true,false,true,"","","","","1","STRING"));
            add(new MetaInfoDTO("chunshan_dingdan_bill","订单信息","weixin","微信","",true,false,true,"","","","","1","STRING"));
        }};
        return metaInfoDTOList;
    }
}
