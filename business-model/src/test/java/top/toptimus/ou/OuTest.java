package top.toptimus.ou;

import com.alibaba.fastjson.JSON;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import top.toptimus.BusinessModelApplication;
import top.toptimus.entity.ou.OuEntity;
import top.toptimus.indicator.ou.base.IndicatorType;
import top.toptimus.indicator.ou.dto.OrgnazitionUnitAttribute;
import top.toptimus.repository.ou.OuRepository;

import java.util.ArrayList;
import java.util.Date;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = BusinessModelApplication.class, webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class OuTest {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private OuEntity ouEntity;
    @Autowired
    private OuRepository ouRepository;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    public void testOU() {
        this.initDataByDb();


        logger.info("所有组织属性");
        logger.info(
                JSON.toJSONString(
                        ouRepository.findAllOrgnazitionUnitDao()
                ));

        logger.info("头结点属性");
        logger.info(
                JSON.toJSONString(
                        ouEntity.getOrgnazitionUnitBaseInfo(
                                ouEntity.getOrgnazitionUnitModelThreadLocal().get().getTopLevelOrgnazitionUnitDao().getOuID()
                        )
                ));
        logger.info("头结点下级属性");
        logger.info(
                JSON.toJSONString(
                        ouEntity.getChildOrgnazitionUnits(
                                ouEntity.getOrgnazitionUnitModelThreadLocal().get().getTopLevelOrgnazitionUnitDao().getOuID()
                        )
                )
        );
        logger.info("取上级节点属性");
        logger.info(
                JSON.toJSONString(
                        ouEntity.getParentOrgnazitionUnit(
                                ouEntity.getChildOrgnazitionUnits(
                                        ouEntity.getOrgnazitionUnitModelThreadLocal().get().getTopLevelOrgnazitionUnitDao().getOuID()
                                ).get(0).getOuID()
                        )
                )
        );
        logger.info("取上级节点属性");
        logger.info(
                JSON.toJSONString(
                        ouEntity.getParentOrgnazitionUnit(
                                ouEntity.getChildOrgnazitionUnits(
                                        ouEntity.getOrgnazitionUnitModelThreadLocal().get().getTopLevelOrgnazitionUnitDao().getOuID()
                                ).get(0).getOuID()
                        )
                )
        );

        logger.info("带业务组织属性的组织信息");
        logger.info(
                JSON.toJSONString(
                        ouEntity.getOrgnazitionUnitModelThreadLocal().get().getOrgnazitionUnitMap()
//                        ouEntity.getOrgnazitionUnitBaseInfo(
//                                "3fd305db-12d3-4fff-aa86-247819f63ffa"
//                        )
                )
        );
    }

    private void initDataByDb() {
        this.ouEntity.initOuData();
        this.ouEntity.initOuAttributesData();
    }

    private void initDataByCode() {
        jdbcTemplate.execute("delete from orgnazition_unit;");

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

        /*
            补充销售一部销售二部
         */
        ouEntity.createOrgnazitionUnit(
                ouEntity.getOrgnazitionUnitModelThreadLocal().get().getChildOrgnazitionUnits(
                        ouEntity.getOrgnazitionUnitModelThreadLocal().get().getTopLevelOrgnazitionUnitDao().getOuID()
                ).get(1).getOuID()
                , "ouCode4"
                , "销售一部"
                , new Date()
                , "猪"
                , true
        );
        ouEntity.createOrgnazitionUnit(
                ouEntity.getOrgnazitionUnitModelThreadLocal().get().getChildOrgnazitionUnits(
                        ouEntity.getOrgnazitionUnitModelThreadLocal().get().getTopLevelOrgnazitionUnitDao().getOuID()
                ).get(1).getOuID()
                , "ouCode5"
                , "销售二部"
                , new Date()
                , "猪"
                , true
        );
        ouEntity.updateOrgnazitionUnitAttributes(
                ouEntity.getOrgnazitionUnitModelThreadLocal().get().getChildOrgnazitionUnits(
                        ouEntity.getOrgnazitionUnitModelThreadLocal().get().getChildOrgnazitionUnits(ouEntity.getOrgnazitionUnitModelThreadLocal().get().getTopLevelOrgnazitionUnitDao().getOuID()).get(1).getOuID()
                ).get(0).getOuID()  //  销售一部
                , new ArrayList<OrgnazitionUnitAttribute>() {{
                    //  模拟业务组织ouCode2，拥有财务属性
                    add(
                            new OrgnazitionUnitAttribute(
                                    ouEntity.getOrgnazitionUnitModelThreadLocal().get().getTopLevelOrgnazitionUnitDao().getOuID()  //  ouCode1
                                    , IndicatorType.Sales
                                    , false)
                    );
                    add(
                            new OrgnazitionUnitAttribute(
                                    ouEntity.getOrgnazitionUnitModelThreadLocal().get().getTopLevelOrgnazitionUnitDao().getOuID()  //  ouCode1
                                    , IndicatorType.Administration
                                    , false)
                    );
                }}
        );
        ouEntity.updateOrgnazitionUnitAttributes(
                ouEntity.getOrgnazitionUnitModelThreadLocal().get().getChildOrgnazitionUnits(
                        ouEntity.getOrgnazitionUnitModelThreadLocal().get().getChildOrgnazitionUnits(ouEntity.getOrgnazitionUnitModelThreadLocal().get().getTopLevelOrgnazitionUnitDao().getOuID()).get(1).getOuID()
                ).get(1).getOuID()  //  销售一部
                , new ArrayList<OrgnazitionUnitAttribute>() {{
                    //  模拟业务组织ouCode2，拥有财务属性
                    add(
                            new OrgnazitionUnitAttribute(
                                    ouEntity.getOrgnazitionUnitModelThreadLocal().get().getTopLevelOrgnazitionUnitDao().getOuID()  //  ouCode1
                                    , IndicatorType.Sales
                                    , false)
                    );
                    add(
                            new OrgnazitionUnitAttribute(
                                    ouEntity.getOrgnazitionUnitModelThreadLocal().get().getTopLevelOrgnazitionUnitDao().getOuID()  //  ouCode1
                                    , IndicatorType.Administration
                                    , false)
                    );
                }}
        );
    }
}
