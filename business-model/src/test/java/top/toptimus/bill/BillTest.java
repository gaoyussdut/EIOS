//package top.toptimus.bill;
//
//import com.alibaba.fastjson.JSON;
//import org.junit.FixMethodOrder;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.junit.runners.MethodSorters;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//import top.toptimus.BusinessModelApplication;
//import top.toptimus.service.domainService.PlaceService;
//import top.toptimus.tokendata.TokenDataDto;
//
//import java.io.*;
//import java.util.List;
//import java.util.Map;
//import java.util.UUID;
//
//@RunWith(SpringJUnit4ClassRunner.class)
//@SpringBootTest(classes = BusinessModelApplication.class, webEnvironment = SpringBootTest.WebEnvironment.MOCK)
//@FixMethodOrder(MethodSorters.NAME_ASCENDING)//按方法名顺序执行
//public class BillTest {
//    private Logger logger = LoggerFactory.getLogger(this.getClass());
//
//    private static long l = 1000;
//    private static String billTokenId;
//    private static String relBillTokenId;
//    private String billMetaId = "cg_caigoushenqing_bill";
//    @SuppressWarnings("all")
//    private String entryMetaId = "cg_caigoushenqing_entry";
//        private String relBillMetaId = "cg_caigoudingdan_bill";
//    private String relEntryMetaId = "cg_caigoudingdan_entry";
//    private String testDataPath = "/home/gaoshize/IdeaProjects/toptimus1218/business-model/src/test/resources/testData/";
//    //    @Autowired
////    private PlaceRedisEntity placeRedisEntity;
//    @Autowired
//    private PlaceService placeService;
////    @Autowired
////    private MasterBillMetaRelationRepository masterBillMetaRelationRepository;
////    @Autowired
////    private MetaTokenRelationRepository metaTokenRelationRepository;
////    @Autowired
////    private BillMetaStoredProcedureRepository billMetaStoredProcedureRepository;
//
//    /*
//     *
//     * 构造测试单据配置信息
//     */
////    @Test
////    public void buildData() {
////        /**
////         * 构造表单配置信息
////         * yusuanbeicha 下挂有一个分录baoxiaomingxibeicha
////         *              挂有一个关联的单据fukuan_bill
////         *              挂有一个引用单据caigougonggaobeicha
////         */
////
////        masterBillMetaRelationRepository.save(new MasterBillMetaRelationDao(
////                "yusuanbeicha"
////                , new ArrayList<MasterMetaInfoDTO>() {{
////            add(new MasterMetaInfoDTO(
////                    "baoxiaomingxibeicha",
////                    MetaRelEnum.ENTRY
////            ));
////            add(new MasterMetaInfoDTO(
////                    "caigougonggaobeicha",
////                    MetaRelEnum.CERTIFICATE
////            ));
//////            add(new MasterMetaInfoDTO(
//////                    "fukuan_bill",
//////                    MetaRelEnum.BILL
//////            ));
////        }}
////        ));
////
//
////    /**
////     *  构造下推单据存储过程
////     */
////    @Test
////    public void buildStoredProcedure(){
////        billMetaStoredProcedureRepository.save(
////                n                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        ew BillMetaStoredProcedureDao(
////                        UUID.randomUUID().toString()
////                        ,"yusuanbeicha"
////                        ,"caigougonggaobeicha"
////                        ,"test_yusuanbeicha_caigougonggaobeicha"
////                )
////        );
////    }
//
////
////    /**
////     *
////     */
////    @Test
////    public void createRelBill(){
////        //获取表头数据&加载缓存
////        placeService.getBillToken("yusuanbeicha","4ca2d2bf-da13-4ca3-ac5b-9fe8d3fb158a",false);
////        //创建关联表单
////        TokenDataDto tokenDataDto = (TokenDataDto)placeService.getRelBillToken("yusuanbeicha","4ca2d2bf-da13-4ca3-ac5b-9fe8d3fb158a","caigougonggaobeicha")
////                .getData();
////        //提交创建的关联表单
////        placeService.submitBillToken(tokenDataDto,"caigougonggaobeicha");
////
////        //提交分录
////        placeService.submitBillToken("4ca2d2bf-da13-4ca3-ac5b-9fe8d3fb158a",new TokenDataDto().build("test123123"),"baoxiaomingxibeicha");
////        placeService.getBillToken("caigougonggaobeicha","c678950a-c82e-49be-b214-9583db29ba54",false);
////        MerklePlaceModel merklePlaceModel = placeRedisEntity.findPlace("c678950a-c82e-49be-b214-9583db29ba54","3");
////    }
//
//    /**
//     * 创建表单
//     */
//    @Test
//    public void test1CreatBill() throws InterruptedException {
//        //创建表单加载缓存
//        TokenDataDto tokenDataDto = (TokenDataDto) placeService.createBill(billMetaId).getData();
//        billTokenId = tokenDataDto.getTokenId();
//        logger.info("----------------------");
//        logger.info("-------创建表单-------");
//        logger.info("----------------------");
//        logger.info("表头tokenData:" + JSON.toJSON(tokenDataDto).toString());
//        Thread.sleep(l);
//    }
//
//    /**
//     * 编辑表头并提交
//     */
//    @Test
//    public void test2SubmitBill() throws InterruptedException {
//        //构造数据
//        TokenDataDto tokenDataDto = (TokenDataDto) this.readJsonFile(testDataPath + "billToken.json");
//        assert tokenDataDto != null;
//        tokenDataDto.build(billTokenId);
//        //提交表头
//        placeService.submitBillToken(tokenDataDto, billMetaId);
//        logger.info("----------------------");
//        logger.info("-------提交表头-------");
//        logger.info("----------------------");
//        logger.info("表头tokenData:" + JSON.toJSON(tokenDataDto).toString());
//        Thread.sleep(l);
//    }
//
//    /**
//     * 编辑新增分录并保存
//     */
//    @Test
//    public void test3SubmitEntry() throws InterruptedException {
//        placeService.getBillToken(billMetaId, billTokenId);
//        Thread.sleep(l);
//        //新增分录
//        TokenDataDto tokenDataDto = (TokenDataDto) placeService.createEntryToken(entryMetaId, billTokenId).getData();
//        logger.info("----------------------");
//        logger.info("-------新增分录-------");
//        logger.info("----------------------");
//        logger.info("表头tokenData:" + JSON.toJSON(tokenDataDto).toString());
//        Thread.sleep(l);
//
//        //编辑分录
//        //构造数据
//        TokenDataDto tokenDataDtoNew = (TokenDataDto) this.readJsonFile(testDataPath + "entryToken.json");
//        assert tokenDataDtoNew != null;
//        placeService.saveEntryToken(billTokenId, tokenDataDtoNew.build(tokenDataDto.getTokenId()), entryMetaId);
//        logger.info("----------------------");
//        logger.info("-------编辑分录-------");
//        logger.info("----------------------");
//        logger.info("表头tokenData:" + JSON.toJSON(tokenDataDto).toString());
//        Thread.sleep(l);
//
////        //删除分录
////        logger.info("----------------------");
////        logger.info("-------删除分录-------");
////        logger.info("----------------------");
////        logger.info("表头tokenData:" + JSON.toJSON(placeService.deleteEntryToken(billTokenId, entryMetaId, tokenDataDto.getTokenId())).toString());
////        Thread.sleep(l);
//    }
//
//    /**
//     * 创建关联单据
//     */
//    @Test
//    public void test4CreateRelBill() throws InterruptedException {
//
//        placeService.getBillToken(billMetaId, billTokenId);
//        //创建关联表单
//        Map<String,List<TokenDataDto>> tokenDataDto = (Map<String,List<TokenDataDto>>) placeService.getRelBillToken(billMetaId, billTokenId, relBillMetaId)
//                .getData();
//        relBillTokenId = tokenDataDto.get(relBillMetaId).get(0).getTokenId();
//        logger.info("----------------------");
//        logger.info("-------创建关联表单-----");
//        logger.info("----------------------");
//        logger.info("关联表单表头tokenData:" + JSON.toJSON(tokenDataDto).toString());
//        Thread.sleep(l);
//    }
//
//    /**
//     * 提交关联表单
//     */
//    @Test
//    public void test5SubmitRelBill() throws InterruptedException {
//        //构造数据
//        TokenDataDto tokenDataDto = (TokenDataDto) this.readJsonFile(testDataPath + "relBillToken.json");
//        assert tokenDataDto != null;
//        tokenDataDto.build(relBillTokenId);
//        //提交创建的关联表单
//        placeService.submitBillToken(tokenDataDto, relBillMetaId);
//        logger.info("----------------------");
//        logger.info("-------提交关联表单-----");
//        logger.info("----------------------");
//        logger.info("关联表单表头tokenData:" + JSON.toJSON(tokenDataDto).toString());
//        Thread.sleep(l);
//
//
//        TokenDataDto tokenDataDto2 = (TokenDataDto) placeService.createEntryToken(relEntryMetaId, relBillTokenId).getData();
//        logger.info("----------------------");
//        logger.info("-------新增关联单据分录-------");
//        logger.info("----------------------");
//        logger.info("关联单据分录tokenData:" + JSON.toJSON(tokenDataDto2).toString());
//        Thread.sleep(l);
//
//        //编辑分录
//        //构造数据
//        placeService.saveEntryToken(relBillTokenId, tokenDataDto2, relEntryMetaId);
//        logger.info("----------------------");
//        logger.info("-------编辑关联单据分录提交-------");
//        logger.info("----------------------");
//        logger.info("关联单据分录tokenData:" + JSON.toJSON(tokenDataDto2).toString());
//        Thread.sleep(l);
//    }
//
////    /**
////     * 删除单据
////     */
////    @Test
////    public void test6DeleteBill(){
////        logger.info("----------------------");
////        logger.info("-------删除单据-----");
////        logger.info("----------------------");
////        logger.info("删除单据:"+ JSON.toJSON(placeService.deleteBillToken(billTokenId)).toString());
////    }
//
//    /**
//     * 读取json文件
//     *
//     * @param filePath 路径
//     * @return Object
//     */
//    private Object readJsonFile(String filePath) {
//        File file = new File(filePath);
//        InputStream inputStream;
//        byte[] b;
//        String strJson;
//        try {
//            inputStream = new FileInputStream(file);
//            b = new byte[inputStream.available()];
//            logger.info(String.valueOf(inputStream.read(b)));
//            strJson = new String(b);
//            JSON json = JSON.parseObject(strJson);
//            return json.toJavaObject(TokenDataDto.class);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
//}
