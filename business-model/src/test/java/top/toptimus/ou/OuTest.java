package top.toptimus.ou;

import com.alibaba.fastjson.JSON;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import top.toptimus.BusinessModelApplication;
import top.toptimus.entity.ou.OuEntity;
import top.toptimus.repository.ou.OuRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = BusinessModelApplication.class, webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class OuTest {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private OuEntity ouEntity;
    @Autowired
    private OuRepository ouRepository;

    @Test
    public void testOU() {
        ouEntity.createTopOrgnazitionUnit(
                null
                , "ouCode1"
                , "正泰"
                , null
                , "猪"
        );

        ouEntity.createOrgnazitionUnit(
                ouEntity.getOrgnazitionUnitModelThreadLocal().get().getTopLevelOrgnazitionUnitDao().getOuID()
                , "ouCode2"
                , "市场部"
                , null
                , "猪"
                , true
        );

        ouEntity.createOrgnazitionUnit(
                ouEntity.getOrgnazitionUnitModelThreadLocal().get().getTopLevelOrgnazitionUnitDao().getOuID()
                , "ouCode3"
                , "销售部"
                , null
                , "猪"
                , true
        );
        logger.info(JSON.toJSONString(ouRepository.findAll()));
    }
}
