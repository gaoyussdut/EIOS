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
//        this.initDataByCode();
        this.initDataByDb();
        /*
            8665e23e-299c-4776-91f4-fddffdbd7d71    正泰
                eef9f15c-ec71-4832-9651-e8a0e0f7767a    市场部
                9ec4393b-a8a8-406b-a3d6-64ea655727e6    销售部
                    482a50de-1a87-41b2-8e47-9c3549d1fb21    销售一部
                    d575fd19-aeed-441e-a303-a526734790ba    销售二部
         */


        logger.info("所有组织属性");
        logger.info(
                JSON.toJSONString(
                        ouRepository.findAllOrgnazitionUnitDao()
                ));

        logger.info("正泰结点属性");
        logger.info(
                JSON.toJSONString(
                        this.ouEntity.getOrgnazitionUnitBaseInfo(
                                this.ouEntity.getOrgnazitionUnitModelThreadLocal().get().getTopLevelOrgnazitionUnitDao().getOuID()
                                //  8665e23e-299c-4776-91f4-fddffdbd7d71  正泰
                        )
                ));
        logger.info("正泰结点下级属性");
        logger.info(
                JSON.toJSONString(
                        this.ouEntity.getChildOrgnazitionUnits(
                                this.ouEntity.getOrgnazitionUnitModelThreadLocal().get().getTopLevelOrgnazitionUnitDao().getOuID()
                                //  8665e23e-299c-4776-91f4-fddffdbd7d71
                        )
                )
        );
        logger.info("取销售部上级节点属性");
        logger.info(
                JSON.toJSONString(
                        this.ouEntity.getParentOrgnazitionUnit(
                                "9ec4393b-a8a8-406b-a3d6-64ea655727e6"  //  销售部
                        )
                )
        );
        logger.info("取销售一部上级节点属性");
        logger.info(
                JSON.toJSONString(
                        this.ouEntity.getParentOrgnazitionUnit(
                                "482a50de-1a87-41b2-8e47-9c3549d1fb21"  //  销售一部
                        )
                )
        );

        logger.info("按照业务类别选择销售二部上级销售组织");
        logger.info(
                JSON.toJSONString(
                        this.ouEntity.getParentOrgnazitionUnitByIndicatorType(
                                "d575fd19-aeed-441e-a303-a526734790ba"  //  销售二部
                                , IndicatorType.Sales
                        )
                )
        );

        logger.info("按照业务类别选择正泰下级销售组织");
        logger.info(
                JSON.toJSONString(
                        this.ouEntity.getChildOrgnazitionUnits(
                                this.ouEntity.getTopLevelOrgnazitionUnitDao().getOuID()
                                //  8665e23e-299c-4776-91f4-fddffdbd7d71  正泰
                                , IndicatorType.Sales
                        )
                )
        );

    }

    private void initDataByDb() {
        jdbcTemplate.execute("delete from orgnazition_unit;");
        jdbcTemplate.execute("insert into orgnazition_unit (ou_id,ou_code,ou_name,create_date,create_user,enable_date,p_ou_id,level,is_disabled,disable_date,disable_user,description,is_entity) values \n" +
                "('482a50de-1a87-41b2-8e47-9c3549d1fb21','ouCode4','销售一部','2019-08-15','猪',null,'9ec4393b-a8a8-406b-a3d6-64ea655727e6',2,False,null,null,null,True)\n" +
                ",('8665e23e-299c-4776-91f4-fddffdbd7d71','ouCode1','正泰','2019-08-15','猪',null,null,0,False,null,null,null,False)\n" +
                ",('9ec4393b-a8a8-406b-a3d6-64ea655727e6','ouCode3','销售部','2019-08-15','猪',null,'8665e23e-299c-4776-91f4-fddffdbd7d71',1,False,null,null,null,True)\n" +
                ",('d575fd19-aeed-441e-a303-a526734790ba','ouCode5','销售二部','2019-08-15','猪',null,'9ec4393b-a8a8-406b-a3d6-64ea655727e6',2,False,null,null,null,True)\n" +
                ",('eef9f15c-ec71-4832-9651-e8a0e0f7767a','ouCode2','市场部','2019-08-15','猪',null,'8665e23e-299c-4776-91f4-fddffdbd7d71',1,False,null,null,null,True);");
        jdbcTemplate.execute("delete from orgnazition_unit_attribute;");
        jdbcTemplate.execute("insert into orgnazition_unit_attribute (ou_id,indicator_type,parent_id,is_cu) values\n" +
                "('482a50de-1a87-41b2-8e47-9c3549d1fb21','Administration','9ec4393b-a8a8-406b-a3d6-64ea655727e6',False)\n" +
                ",('482a50de-1a87-41b2-8e47-9c3549d1fb21','Sales','9ec4393b-a8a8-406b-a3d6-64ea655727e6',False)\n" +
                ",('8665e23e-299c-4776-91f4-fddffdbd7d71','Administration',null,False)\n" +
                ",('8665e23e-299c-4776-91f4-fddffdbd7d71','Sales',null,False)\n" +
                ",('9ec4393b-a8a8-406b-a3d6-64ea655727e6','Administration','8665e23e-299c-4776-91f4-fddffdbd7d71',False)\n" +
                ",('9ec4393b-a8a8-406b-a3d6-64ea655727e6','Sales','8665e23e-299c-4776-91f4-fddffdbd7d71',False)\n" +
                ",('d575fd19-aeed-441e-a303-a526734790ba','Administration','9ec4393b-a8a8-406b-a3d6-64ea655727e6',False)\n" +
                ",('d575fd19-aeed-441e-a303-a526734790ba','Sales','9ec4393b-a8a8-406b-a3d6-64ea655727e6',False)\n" +
                ",('eef9f15c-ec71-4832-9651-e8a0e0f7767a','Administration','8665e23e-299c-4776-91f4-fddffdbd7d71',False)\n" +
                ",('eef9f15c-ec71-4832-9651-e8a0e0f7767a','Sales','8665e23e-299c-4776-91f4-fddffdbd7d71',False)");

        this.ouEntity.initOuData(); //  初始化数据
    }


    /**
     *  之前造测试数据的。
     */
    private void initDataByCode() {

        logger.info(JSON.toJSONString(
                this.ouEntity.createTopOrgnazitionUnit(
                        null
                        , "ouCode1"
                        , "正泰"
                        , null
                        , "猪"
                )
        ));

        logger.info(JSON.toJSONString(
                this.ouEntity.createOrgnazitionUnit(
                        this.ouEntity.getOrgnazitionUnitModelThreadLocal().get().getTopLevelOrgnazitionUnitDao().getOuID()
                        , "ouCode2"
                        , "市场部"
                        , null
                        , "猪"
                        , true
                )));

        logger.info(JSON.toJSONString(
                this.ouEntity.createOrgnazitionUnit(
                        this.ouEntity.getOrgnazitionUnitModelThreadLocal().get().getTopLevelOrgnazitionUnitDao().getOuID()
                        , "ouCode3"
                        , "销售部"
                        , null
                        , "猪"
                        , true
                )));

        /*
            补充销售一部销售二部
         */
        this.ouEntity.createOrgnazitionUnit(
                this.ouEntity.getOrgnazitionUnitModelThreadLocal().get().getChildOrgnazitionUnits(
                        this.ouEntity.getOrgnazitionUnitModelThreadLocal().get().getTopLevelOrgnazitionUnitDao().getOuID()
                ).get(1).getOuID()
                , "ouCode4"
                , "销售一部"
                , new Date()
                , "猪"
                , true
        );
        this.ouEntity.createOrgnazitionUnit(
                this.ouEntity.getOrgnazitionUnitModelThreadLocal().get().getChildOrgnazitionUnits(
                        this.ouEntity.getOrgnazitionUnitModelThreadLocal().get().getTopLevelOrgnazitionUnitDao().getOuID()
                ).get(1).getOuID()
                , "ouCode5"
                , "销售二部"
                , new Date()
                , "猪"
                , true
        );
        this.ouEntity.createOrgnazitionUnitAttributes(
                this.ouEntity.getOrgnazitionUnitModelThreadLocal().get().getChildOrgnazitionUnits(
                        this.ouEntity.getOrgnazitionUnitModelThreadLocal().get().getChildOrgnazitionUnits(this.ouEntity.getOrgnazitionUnitModelThreadLocal().get().getTopLevelOrgnazitionUnitDao().getOuID()).get(1).getOuID()
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
        this.ouEntity.createOrgnazitionUnitAttributes(
                this.ouEntity.getOrgnazitionUnitModelThreadLocal().get().getChildOrgnazitionUnits(
                        this.ouEntity.getOrgnazitionUnitModelThreadLocal().get().getChildOrgnazitionUnits(this.ouEntity.getOrgnazitionUnitModelThreadLocal().get().getTopLevelOrgnazitionUnitDao().getOuID()).get(1).getOuID()
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

        this.ouEntity.createOrgnazitionUnitAttributes(
                this.ouEntity.getOrgnazitionUnitModelThreadLocal().get().getChildOrgnazitionUnits(this.ouEntity.getOrgnazitionUnitModelThreadLocal().get().getTopLevelOrgnazitionUnitDao().getOuID()).get(0).getOuID()  //  二级部门
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

        this.ouEntity.createOrgnazitionUnitAttributes(
                this.ouEntity.getOrgnazitionUnitModelThreadLocal().get().getChildOrgnazitionUnits(this.ouEntity.getOrgnazitionUnitModelThreadLocal().get().getTopLevelOrgnazitionUnitDao().getOuID()).get(1).getOuID()  //  二级部门
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


        this.ouEntity.createOrgnazitionUnitAttributes(
                this.ouEntity.getTopLevelOrgnazitionUnitDao().getOuID()  //  正泰
                , new ArrayList<OrgnazitionUnitAttribute>() {{
                    //  模拟业务组织正泰，拥有财务属性
                    add(
                            new OrgnazitionUnitAttribute(
                                    null  //  无上级
                                    , IndicatorType.Sales
                                    , false)
                    );
                    add(
                            new OrgnazitionUnitAttribute(
                                    null  //  无上级
                                    , IndicatorType.Administration
                                    , false)
                    );
                }}
        );
    }
}
