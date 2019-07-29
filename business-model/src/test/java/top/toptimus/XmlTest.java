//package top.toptimus;
//
//import org.json.JSONObject;
//import org.json.XML;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//
//import java.io.*;
//
///**
// * Created by JiangHao on 2018/9/29.
// */
//@RunWith(SpringJUnit4ClassRunner.class)
//@SpringBootTest(classes = BusinessModelApplication.class, webEnvironment = SpringBootTest.WebEnvironment.MOCK)
//public class XmlTest {
//
//    Logger logger = LoggerFactory.getLogger(this.getClass());
//
//    @Test
//    public void xml() throws IOException {
//
//        File file = new File("/Users/jianghao/Desktop/metaxml.xml");
//        InputStream inputStream = new FileInputStream(file);
//        byte[] b = new byte[inputStream.available()];
//        inputStream.read(b);
//
//        String xml = new String(b);
//        JSONObject xmlJSONObj = XML.toJSONObject(xml);
//
//
//        logger.info("xml数据为:" + xml);
//        logger.info("json数据为:" + xmlJSONObj.toString());
//
//    }
//
//
//}
