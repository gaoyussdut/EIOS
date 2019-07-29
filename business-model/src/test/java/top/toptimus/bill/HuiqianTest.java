//package top.toptimus.bill;
//
//import com.alibaba.fastjson.JSON;
//import org.apache.kafka.common.requests.LeaderAndIsrRequest;
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
//import top.toptimus.controller.CountersignController;
//import top.toptimus.meta.signGroup.CountersignSubmitDTO;
//import top.toptimus.tokendata.TokenDataDto;
//
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.InputStream;
//import java.util.ArrayList;
//import java.util.List;
//
//@RunWith(SpringJUnit4ClassRunner.class)
//@SpringBootTest(classes = BusinessModelApplication.class, webEnvironment = SpringBootTest.WebEnvironment.MOCK)
//@FixMethodOrder(MethodSorters.NAME_ASCENDING)//按方法名顺序执行
//public class HuiqianTest {
//    private Logger logger = LoggerFactory.getLogger(this.getClass());
//
//    private static long l = 1000;
//
//    private String testDataPath = "/home/gaoshize/IdeaProjects/toptimus1218/business-model/src/test/resources/testData/";
//
//    //单据meta
//    private static String metaId = "fyyy_hwlxmysd";
//    //单据token
//    private static String tokenId = "aaasd22323";
//
//    private static String taskTokenId = "";
//
//    private static String countersignTokenId = "";
//    @Autowired
//    private CountersignController countersignController;
//
//    /**
//     * 创建汇签任务
//     */
//    @Test
//    public void test1()throws InterruptedException{
//
//        TokenDataDto tokenDataDto = (TokenDataDto)countersignController.createCountersignTask().getData();
//        Thread.sleep(l);
//        taskTokenId = tokenDataDto.getTokenId();
//    }
//
//    /**
//     * 提交汇签任务单据
//     */
//    @Test
//    public void test2()throws InterruptedException{
//        TokenDataDto tokenDataDto = (TokenDataDto)this.readJsonFile(testDataPath+"huiqian_task_token.json");
//        assert tokenDataDto != null;
//        tokenDataDto.build(taskTokenId);
//        countersignController.submitCountersignTask(metaId,tokenId,tokenDataDto);
//        Thread.sleep(l);
//    }
//
//    /**
//     * 创建汇签表单
//     */
//    @Test
//    public void test3()throws InterruptedException{
//        TokenDataDto tokenDataDto = (TokenDataDto)countersignController.createCountersign().getData();
//        Thread.sleep(l);
//        countersignTokenId = tokenDataDto.getTokenId();
//    }
//
//    /**
//     * 提交汇签意见
//     */
//    @Test
//    public void test4()throws InterruptedException{
//        TokenDataDto tokenDataDto =(TokenDataDto)this.readJsonFile(testDataPath+"huiqian_bill_token.json");
//        tokenDataDto.build(countersignTokenId);
//        List<String> taskTokenIds = new ArrayList<>();
//        taskTokenIds.add(taskTokenId);
//        CountersignSubmitDTO countersignSubmitDTO = new CountersignSubmitDTO(taskTokenIds,tokenDataDto);
//
//        countersignController.submitCountersignToken(countersignSubmitDTO);
//        Thread.sleep(l);
//    }
//
//    /**
//     * 编辑参与者
//     */
//    @Test
//    public void test5()throws InterruptedException{
//        //添加参与者
//        TokenDataDto tokenDataDto = (TokenDataDto)countersignController.createParticipatar(countersignTokenId).getData();
//        Thread.sleep(l);
//        //编辑并提交参与者
//        TokenDataDto tokenDataDto2 = (TokenDataDto)this.readJsonFile(testDataPath+"huiqian_entry_token.json");
//        assert tokenDataDto2 != null;
//        tokenDataDto2.build(tokenDataDto.getTokenId());
//
//        countersignController.submitParticipatar(countersignTokenId,tokenDataDto2);
//        Thread.sleep(l);
//    }
//
//    /**
//     * 查询参与者
//     */
//    @Test
//    public void test6()throws InterruptedException{
//        countersignController.getParticipatarTokens(countersignTokenId);
//        Thread.sleep(l);
//    }
//    /**
//     * 查询所有汇签意见
//     */
//    @Test
//    public void test7()throws InterruptedException{
//
//        logger.info("metaId:"+metaId);
//        logger.info("tokenId:"+tokenId);
//        logger.info("----------------------");
//        logger.info("-------查询所有汇签意见-------");
//        logger.info("----------------------");
//        logger.info("表头tokenData:" + JSON.toJSON(
//                countersignController.getCountersign(metaId,tokenId)
//        ).toString());
//        Thread.sleep(l);
//    }
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
