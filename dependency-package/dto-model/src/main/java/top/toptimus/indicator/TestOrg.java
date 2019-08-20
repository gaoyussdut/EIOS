package top.toptimus.indicator;

import com.alibaba.fastjson.JSON;
import top.toptimus.indicator.IndicatorItem.dto.IndicatorItemDTO;
import top.toptimus.indicator.indicatorBill.dao.IndicatorOuRelDao;
import top.toptimus.indicator.indicatorBill.dto.IndicatorOuRelDto;
import top.toptimus.indicator.ou.base.IndicatorType;
import top.toptimus.indicator.ou.dao.OrgnazitionUnitDao;
import top.toptimus.indicator.ou.dto.OrgnazitionUnitAttribute;
import top.toptimus.indicator.ou.model.OrgnazitionUnitModel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class TestOrg {
    private static ThreadLocal<OrgnazitionUnitModel> orgnazitionUnitModelThreadLocal = ThreadLocal.withInitial(OrgnazitionUnitModel::new);   //  单据线程缓存
    private static List<IndicatorItemDTO> indicatorItemDTOS = new ArrayList<IndicatorItemDTO>() {{
        add(new IndicatorItemDTO("1", "定制化服务", IndicatorType.Sales));
        add(new IndicatorItemDTO("2", "核心产品", IndicatorType.Sales));
        add(new IndicatorItemDTO("3", "咨询服务", IndicatorType.Sales));
    }};

    public static void main(String[] args) {
        initTestData(); //  测试数据

        testOuBaseInfo();
        testOuWithAttribute();
        testIndicator();
    }

    /**
     * 测试指标数据
     */
    private static void testIndicator() {
        System.out.println();
        System.out.println("----------------------------------");
        System.out.println("测试指标数据");
        System.out.println("----------------------------------");


        System.out.println("根据ou id取得下推分发规则");
        System.out.println(JSON.toJSONString(
                orgnazitionUnitModelThreadLocal.get()
                        .getIndicatorBillRelModel()
                        .getIndicatorOuRelDaosByOuId(
                                orgnazitionUnitModelThreadLocal.get().getTopLevelOrgnazitionUnitDao().getOuID() //  ouCode1
                                , IndicatorType.Sales
                        )
        ));

        System.out.println("根据id取得下推分发规则");
        System.out.println(JSON.toJSONString(
                orgnazitionUnitModelThreadLocal.get().getIndicatorBillRelModel().buildIndicatorTokenDefineDao(
                        "rel1"
                        , UUID.randomUUID().toString()
                        , UUID.randomUUID().toString()
                )
        ));
    }


    /**
     * 测试ou基础信息——不带业务组织类型
     */
    private static void testOuBaseInfo() {
        System.out.println();
        System.out.println("----------------------------------");
        System.out.println("测试ou基础信息——不带业务组织类型");
        System.out.println("----------------------------------");

        System.out.println("取得业务组织顶层节点");
        System.out.println(JSON.toJSONString(
                orgnazitionUnitModelThreadLocal.get().getTopLevelOrgnazitionUnitDao()
        ));

        System.out.println("根据ID取得组织基本信息");
        System.out.println(JSON.toJSONString(
                orgnazitionUnitModelThreadLocal.get().getOrgnazitionUnitDao(orgnazitionUnitModelThreadLocal.get().getTopLevelOrgnazitionUnitDao().getOuID())
        ));
        System.out.println("不按照业务类型找下级组织列表");
        System.out.println(JSON.toJSONString(
                orgnazitionUnitModelThreadLocal.get().getChildOrgnazitionUnits(orgnazitionUnitModelThreadLocal.get().getTopLevelOrgnazitionUnitDao().getOuID())
        ));

        System.out.println("不按照业务类型找上级组织属性不报错");
        System.out.println(JSON.toJSONString(
                orgnazitionUnitModelThreadLocal.get().getParentOrgnazitionUnit(
                        orgnazitionUnitModelThreadLocal.get().getChildOrgnazitionUnits(orgnazitionUnitModelThreadLocal.get().getTopLevelOrgnazitionUnitDao().getOuID())
                                .get(0)
                                .getOuID()
                )
        ));
    }

    /**
     * 测试ou基础信息——带业务组织类型
     */
    private static void testOuWithAttribute() {
        System.out.println();
        System.out.println("----------------------------------");
        System.out.println("测试ou基础信息——带业务组织类型");
        System.out.println("----------------------------------");

        System.out.println("选择上级业务组织一览");
        System.out.println(JSON.toJSONString(
                orgnazitionUnitModelThreadLocal.get().getParentOrgnazitionUnitsByIndicatorType(
                        orgnazitionUnitModelThreadLocal.get().getChildOrgnazitionUnits(orgnazitionUnitModelThreadLocal.get().getTopLevelOrgnazitionUnitDao().getOuID(), IndicatorType.Sales)
                                .get(0)
                                .getOuID()
                        , IndicatorType.Administration
                )
        ));

        System.out.println("按照业务类型找下级组织列表");
        System.out.println(JSON.toJSONString(
                orgnazitionUnitModelThreadLocal.get().getChildOrgnazitionUnits(orgnazitionUnitModelThreadLocal.get().getTopLevelOrgnazitionUnitDao().getOuID(), IndicatorType.Sales)
        ));
//        System.out.println("按照业务类型找上级组织属性——顶层报错");
//        System.out.println(JSON.toJSONString(
//                orgnazitionUnitModel.getParentOrgnazitionUnit(orgnazitionUnitModel.getTopLevelOrgnazitionUnitDao().getOuID(), IndicatorType.Sales)
//        ));
        System.out.println("按照业务类型找上级组织属性不报错");
        System.out.println(JSON.toJSONString(
                orgnazitionUnitModelThreadLocal.get().getParentOrgnazitionUnit(
                        orgnazitionUnitModelThreadLocal.get().getChildOrgnazitionUnits(orgnazitionUnitModelThreadLocal.get().getTopLevelOrgnazitionUnitDao().getOuID(), IndicatorType.Sales).get(0).getOuID()
                        , IndicatorType.Sales
                )
        ));

        System.out.println("按照业务类型找业务组织树形结构");
        System.out.println(JSON.toJSONString(
                orgnazitionUnitModelThreadLocal.get().getOrgnazitionTreeViewByAttribute(IndicatorType.Sales)
        ));

    }

//    private static void testOthers() {
//        OrgnazitionUnitModel initData = generateOrgnazitionUnitModel();
//        //  测试构造函数
//        OrgnazitionUnitModel orgnazitionUnitModel = new OrgnazitionUnitModel(
//                new ArrayList<OrgnazitionUnitDao>() {{
//                    initData.getOrgnazitionUnitMap().keySet().forEach(ouId -> add(initData.getOrgnazitionUnitMap().get(ouId)));
//                }}
//        );
//        System.out.println("找下级组织列表");
//        System.out.println(JSON.toJSONString(
//                orgnazitionUnitModel.getChildOrgnazitionUnits("1", IndicatorType.Sales)
//        ));
//        System.out.println("找上级组织属性");
//        System.out.println(JSON.toJSONString(
//                orgnazitionUnitModel.getParentOrgnazitionUnit("2", IndicatorType.Sales)
//        ));
//        System.out.println("当前组织属性");
//        System.out.println(JSON.toJSONString(
//                orgnazitionUnitModel.getOrgnazitionUnitDao("1")
//        ));
//        System.out.println(JSON.toJSONString(
//                orgnazitionUnitModel.getOrgnazitionUnitDao("2")
//        ));
//        System.out.println("当获取某一属性下的业务组织树");
//        System.out.println(JSON.toJSONString(
//                orgnazitionUnitModel.getOrgnazitionTreeViewByAttribute(IndicatorType.Sales)
//        ));
//    }

    private static OrgnazitionUnitModel initOrgnazitionUnitModel() {
        OrgnazitionUnitModel orgnazitionUnitModel = new OrgnazitionUnitModel();

        //  创建三个业务组织
        orgnazitionUnitModel
                .updateOrgnazitionUnit(
                        //  新增顶层业务组织，必然为虚体，只能负责填写指标，不能分配任务
                        orgnazitionUnitModel.createTopOrgnazitionUnit(
                                null
                                , "ouCode1"
                                , "正泰"
                                , new Date()
                                , "猪"
                                , ""
                        )
                )
                .updateOrgnazitionUnit(
                        orgnazitionUnitModel.createOrgnazitionUnit(
                                orgnazitionUnitModel.getTopLevelOrgnazitionUnitDao().getOuID()
                                , "ouCode2"
                                , "市场部"
                                , new Date()
                                , "猪"
                                , true
                                , ""
                        )
                )
                .updateOrgnazitionUnit(
                        orgnazitionUnitModel.createOrgnazitionUnit(
                                orgnazitionUnitModel.getTopLevelOrgnazitionUnitDao().getOuID()
                                , "ouCode3"
                                , "销售部"
                                , new Date()
                                , "猪"
                                , true
                                , ""
                        )
                );

        return orgnazitionUnitModel
                .updateOrgnazitionUnitAttributes(
                        orgnazitionUnitModel.getTopLevelOrgnazitionUnitDao().getOuID()
                        , new ArrayList<OrgnazitionUnitAttribute>() {{
                            //  模拟业务组织ouCode1，拥有行政和财务属性
                            add(new OrgnazitionUnitAttribute(IndicatorType.Sales, false));
                            add(new OrgnazitionUnitAttribute(IndicatorType.Administration, false));
                        }}
                )
                .updateOrgnazitionUnitAttributes(
                        orgnazitionUnitModel.getChildOrgnazitionUnits(orgnazitionUnitModel.getTopLevelOrgnazitionUnitDao().getOuID()).get(0).getOuID()  //  市场部
                        , new ArrayList<OrgnazitionUnitAttribute>() {{
                            //  模拟业务组织ouCode2，拥有财务属性
                            add(
                                    new OrgnazitionUnitAttribute(
                                            orgnazitionUnitModel.getTopLevelOrgnazitionUnitDao().getOuID()  //  市场部
                                            , IndicatorType.Sales
                                            , false)
                            );
                            add(
                                    new OrgnazitionUnitAttribute(
                                            orgnazitionUnitModel.getTopLevelOrgnazitionUnitDao().getOuID()  //  市场部
                                            , IndicatorType.Administration
                                            , false)
                            );
                        }}
                )
                .updateOrgnazitionUnitAttributes(
                        orgnazitionUnitModel.getChildOrgnazitionUnits(orgnazitionUnitModel.getTopLevelOrgnazitionUnitDao().getOuID()).get(1).getOuID()  //  销售部
                        , new ArrayList<OrgnazitionUnitAttribute>() {{
                            //  模拟业务组织ouCode2，拥有财务属性
                            add(
                                    new OrgnazitionUnitAttribute(
                                            orgnazitionUnitModel.getTopLevelOrgnazitionUnitDao().getOuID()  //  销售部
                                            , IndicatorType.Sales
                                            , false)
                            );
                            add(
                                    new OrgnazitionUnitAttribute(
                                            orgnazitionUnitModel.getTopLevelOrgnazitionUnitDao().getOuID()  //  销售部
                                            , IndicatorType.Administration
                                            , false)
                            );
                        }}
                );
    }

    private static void initTestData() {
        OrgnazitionUnitModel initData = initOrgnazitionUnitModel();

        //  测试构造函数
        orgnazitionUnitModelThreadLocal.set(
                new OrgnazitionUnitModel(
                        new ArrayList<OrgnazitionUnitDao>() {{
                            initData.getOrgnazitionUnitMap().keySet().forEach(ouId -> add(initData.getOrgnazitionUnitMap().get(ouId)));
                        }}
                )
        );

        /*
            补充销售一部销售二部
         */
        orgnazitionUnitModelThreadLocal.get()
                .updateOrgnazitionUnit(
                        orgnazitionUnitModelThreadLocal.get().createOrgnazitionUnit(
                                orgnazitionUnitModelThreadLocal.get().getChildOrgnazitionUnits(
                                        orgnazitionUnitModelThreadLocal.get().getTopLevelOrgnazitionUnitDao().getOuID()
                                ).get(1).getOuID()
                                , "ouCode4"
                                , "销售一部"
                                , new Date()
                                , "猪"
                                , true
                                , ""
                        )
                )
                .updateOrgnazitionUnit(
                        orgnazitionUnitModelThreadLocal.get().createOrgnazitionUnit(
                                orgnazitionUnitModelThreadLocal.get().getChildOrgnazitionUnits(
                                        orgnazitionUnitModelThreadLocal.get().getTopLevelOrgnazitionUnitDao().getOuID()
                                ).get(1).getOuID()
                                , "ouCode5"
                                , "销售二部"
                                , new Date()
                                , "猪"
                                , true
                                , ""
                        )
                );


        orgnazitionUnitModelThreadLocal.get()
                .updateOrgnazitionUnitAttributes(
                        orgnazitionUnitModelThreadLocal.get().getChildOrgnazitionUnits(
                                orgnazitionUnitModelThreadLocal.get().getChildOrgnazitionUnits(orgnazitionUnitModelThreadLocal.get().getTopLevelOrgnazitionUnitDao().getOuID()).get(1).getOuID()
                        ).get(0).getOuID()  //  销售一部
                        , new ArrayList<OrgnazitionUnitAttribute>() {{
                            //  模拟业务组织ouCode2，拥有财务属性
                            add(
                                    new OrgnazitionUnitAttribute(
                                            orgnazitionUnitModelThreadLocal.get().getTopLevelOrgnazitionUnitDao().getOuID()  //  ouCode1
                                            , IndicatorType.Sales
                                            , false)
                            );
                            add(
                                    new OrgnazitionUnitAttribute(
                                            orgnazitionUnitModelThreadLocal.get().getTopLevelOrgnazitionUnitDao().getOuID()  //  ouCode1
                                            , IndicatorType.Administration
                                            , false)
                            );
                        }}
                )
                .updateOrgnazitionUnitAttributes(
                        orgnazitionUnitModelThreadLocal.get().getChildOrgnazitionUnits(
                                orgnazitionUnitModelThreadLocal.get().getChildOrgnazitionUnits(orgnazitionUnitModelThreadLocal.get().getTopLevelOrgnazitionUnitDao().getOuID()).get(1).getOuID()
                        ).get(1).getOuID()  //  销售二部
                        , new ArrayList<OrgnazitionUnitAttribute>() {{
                            //  模拟业务组织ouCode2，拥有财务属性
                            add(
                                    new OrgnazitionUnitAttribute(
                                            orgnazitionUnitModelThreadLocal.get().getTopLevelOrgnazitionUnitDao().getOuID()  //  ouCode1
                                            , IndicatorType.Sales
                                            , false)
                            );
                            add(
                                    new OrgnazitionUnitAttribute(
                                            orgnazitionUnitModelThreadLocal.get().getTopLevelOrgnazitionUnitDao().getOuID()  //  ouCode1
                                            , IndicatorType.Administration
                                            , false)
                            );
                        }}
                );

        /*
            测试的指标数据建立
         */
        orgnazitionUnitModelThreadLocal.get().buildIndicatorBillRelModel(
                new ArrayList<IndicatorOuRelDao>() {{
                    add(
                            new IndicatorOuRelDao(
                                    "rel1"
                                    , orgnazitionUnitModelThreadLocal.get().getTopLevelOrgnazitionUnitDao().getOuID()
                                    , orgnazitionUnitModelThreadLocal.get().getChildOrgnazitionUnits(orgnazitionUnitModelThreadLocal.get().getTopLevelOrgnazitionUnitDao().getOuID(), IndicatorType.Sales).get(0).getOuID()
                                    , "meta1"
                                    , "meta2"
                                    , true
                                    , "procedureName1"
                                    , IndicatorType.Sales
                            )
                    );
                    add(
                            new IndicatorOuRelDao(
                                    "rel2"
                                    , orgnazitionUnitModelThreadLocal.get().getTopLevelOrgnazitionUnitDao().getOuID()
                                    , orgnazitionUnitModelThreadLocal.get().getChildOrgnazitionUnits(orgnazitionUnitModelThreadLocal.get().getTopLevelOrgnazitionUnitDao().getOuID(), IndicatorType.Sales).get(1).getOuID()
                                    , "meta3"
                                    , "meta4"
                                    , true
                                    , "procedureName2"
                                    , IndicatorType.Administration
                            )
                    );
                }}
        );

        orgnazitionUnitModelThreadLocal.get().getIndicatorBillRelModel().buildIndicatorOURel(
                new IndicatorOuRelDto(
                        "rel3"
                        , orgnazitionUnitModelThreadLocal.get().getTopLevelOrgnazitionUnitDao().getOuID()
                        , orgnazitionUnitModelThreadLocal.get().getChildOrgnazitionUnits(orgnazitionUnitModelThreadLocal.get().getTopLevelOrgnazitionUnitDao().getOuID(), IndicatorType.Sales).get(0).getOuID()
                        , "meta1"
                        , "meta2"
                        , true
                        , "procedureName11"
                        , IndicatorType.Sales
                )
        );
    }
}
