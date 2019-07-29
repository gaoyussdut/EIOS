package top.toptimus;

import com.alibaba.fastjson.JSON;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import top.toptimus.entity.DataModelEntity;

import java.io.IOException;

/**
 * jdbc test demo
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = BusinessModelApplication.class, webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class JdbcTest {

    private Logger logger = LoggerFactory.getLogger(this.getClass());


    @Autowired
    private DataModelEntity dataModelEntity;

    @Test
    public void getTokenMetaInfoDTOS() throws IOException {
        //  4d66990b-ae70-4b07-8cb0-63381c7fcbd3
        logger.info("result:" + JSON.toJSONString(dataModelEntity.getTokenMetaInfoDTOS("4d66990b-ae70-4b07-8cb0-63381c7fcbd3")));
        logger.info("result:" + JSON.toJSONString(dataModelEntity.getTokenMetaInfoDTOS("f96dece2-4a6e-4893-bfad-53e51911dc9b")));
    }

    @Test
    public void getTalendDataModelMeta() {
        //  4d66990b-ae70-4b07-8cb0-63381c7fcbd3
//        logger.info("result:" + JSON.toJSONString(dataModelEntity.getTokenMetaInfoDTOS("4d66990b-ae70-4b07-8cb0-63381c7fcbd3")));
        logger.info("result:" + JSON.toJSONString(dataModelEntity.getTalendDataModelMeta(0, 5)));
    }

    @Test
    public void getMetaInfos() throws IOException {
        logger.info("Customer:" + JSON.toJSONString(dataModelEntity.getTalendData(
                "4d66990b-ae70-4b07-8cb0-63381c7fcbd3",
                "Customer"
                , 0
                , 3
        )));
        logger.info("Movie:" + JSON.toJSONString(dataModelEntity.getTalendData(
                "4d66990b-ae70-4b07-8cb0-63381c7fcbd3"
                , "Movie"
                , 0
                , 3
        )));

        logger.info("t_Supply:" + JSON.toJSONString(dataModelEntity.getTalendData(
                "f96dece2-4a6e-4893-bfad-53e51911dc9b"
                , "t_ship_POOrderEntry"
                , 0
                , 3
        )));
    }

    @Test
    public void getCustomer() throws IOException {
        logger.info("Customer meta：" + JSON.toJSONString(dataModelEntity.getTokenMetaInfoDTO("4d66990b-ae70-4b07-8cb0-63381c7fcbd3", "Customer")));
        logger.info("Customer:" + JSON.toJSONString(dataModelEntity.getTalendData(
                "4d66990b-ae70-4b07-8cb0-63381c7fcbd3"
                , "Customer"
                , 0
                , 3
        )));
    }

    @Test
    public void getMovie() throws IOException {
//        logger.info("Customer meta：" + JSON.toJSONString(dataModelEntity.getTokenMetaInfoDTO("4d66990b-ae70-4b07-8cb0-63381c7fcbd3", "Movie")));
        logger.info("Customer meta：" + JSON.toJSONString(dataModelEntity.getTalendMetaInfo("4d66990b-ae70-4b07-8cb0-63381c7fcbd3", "Customer", "name")));
    }

    @Test
    public void getReference() throws IOException {
//        logger.info("Customer meta：" + JSON.toJSONString(dataModelEntity.getTokenMetaInfoDTO("4d66990b-ae70-4b07-8cb0-63381c7fcbd3", "Movie")));
        logger.info("t_ship_ICItem Reference：" + JSON.toJSONString(dataModelEntity.getTalendReference("f96dece2-4a6e-4893-bfad-53e51911dc9b", "t_ship_ICItem")));
        logger.info("t_ship_ItemRight meta：" + JSON.toJSONString(dataModelEntity.getTokenMetaInfoDTO("f96dece2-4a6e-4893-bfad-53e51911dc9b", "t_ship_ItemRight")));
    }

    @Test
    public void getFieldPath() throws IOException {
        logger.info(JSON.toJSONString(dataModelEntity.getTalendFieldPath("f96dece2-4a6e-4893-bfad-53e51911dc9b", "t_ship_ICItem", "t_ship_ICStockBillEntry")));
    }
}
