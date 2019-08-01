package top.toptimus.indicator;

import com.alibaba.fastjson.JSON;
import top.toptimus.indicator.indicatorBill.dao.IndicatorOuRelDao;
import top.toptimus.indicator.indicatorBill.dto.IndicatorOuRelDto;
import top.toptimus.indicator.ou.base.IndicatorType;
import top.toptimus.indicator.ou.dao.OrgnazitionUnitDao;
import top.toptimus.indicator.ou.dto.OrgnazitionUnitAttribute;
import top.toptimus.indicator.ou.model.OrgnazitionUnitModel;

import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

public class TestOrg {
    private static ThreadLocal<OrgnazitionUnitModel> orgnazitionUnitModelThreadLocal = ThreadLocal.withInitial(OrgnazitionUnitModel::new);   //  单据线程缓存

    public static void main(String[] args) {

        OrgnazitionUnitModel initData = generateOrgnazitionUnitModel();
        //  测试构造函数
        orgnazitionUnitModelThreadLocal.set(
                new OrgnazitionUnitModel(
                        new ArrayList<OrgnazitionUnitDao>() {{
                            initData.getOrgnazitionUnitMap().keySet().forEach(ouId -> add(initData.getOrgnazitionUnitMap().get(ouId)));
                        }}
                )
        );

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
        orgnazitionUnitModelThreadLocal.get().buildIndicatorBillRelModel(
                new ArrayList<IndicatorOuRelDao>() {{
                    add(
                            new IndicatorOuRelDao(
                                    "rel1"
                                    , orgnazitionUnitModelThreadLocal.get().getTopLevelOrgnazitionUnitDao().getOuID()
                                    , orgnazitionUnitModelThreadLocal.get().getChildOrgnazitionUnits(orgnazitionUnitModelThreadLocal.get().getTopLevelOrgnazitionUnitDao().getOuID(), IndicatorType.Finance).get(0).getOuID()
                                    , "meta1"
                                    , "meta2"
                                    , true
                                    , "procedureName1"
                                    , IndicatorType.Finance
                            )
                    );
                    add(
                            new IndicatorOuRelDao(
                                    "rel2"
                                    , orgnazitionUnitModelThreadLocal.get().getTopLevelOrgnazitionUnitDao().getOuID()
                                    , orgnazitionUnitModelThreadLocal.get().getChildOrgnazitionUnits(orgnazitionUnitModelThreadLocal.get().getTopLevelOrgnazitionUnitDao().getOuID(), IndicatorType.Finance).get(1).getOuID()
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
                        , orgnazitionUnitModelThreadLocal.get().getChildOrgnazitionUnits(orgnazitionUnitModelThreadLocal.get().getTopLevelOrgnazitionUnitDao().getOuID(), IndicatorType.Finance).get(0).getOuID()
                        , "meta1"
                        , "meta2"
                        , true
                        , "procedureName11"
                        , IndicatorType.Finance
                )
        );

        System.out.println("根据ou id取得下推分发规则");
        System.out.println(JSON.toJSONString(
                orgnazitionUnitModelThreadLocal.get().getIndicatorBillRelModel().getIndicatorOuRelDaosByOuId(orgnazitionUnitModelThreadLocal.get().getTopLevelOrgnazitionUnitDao().getOuID(), IndicatorType.Finance)
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
                orgnazitionUnitModelThreadLocal.get().getOrgnazitionUnitsByIndicatorType(
                        orgnazitionUnitModelThreadLocal.get().getChildOrgnazitionUnits(orgnazitionUnitModelThreadLocal.get().getTopLevelOrgnazitionUnitDao().getOuID(), IndicatorType.Finance)
                                .get(0)
                                .getOuID()
                        , IndicatorType.Administration
                )
        ));

        System.out.println("按照业务类型找下级组织列表");
        System.out.println(JSON.toJSONString(
                orgnazitionUnitModelThreadLocal.get().getChildOrgnazitionUnits(orgnazitionUnitModelThreadLocal.get().getTopLevelOrgnazitionUnitDao().getOuID(), IndicatorType.Finance)
        ));
//        System.out.println("按照业务类型找上级组织属性——顶层报错");
//        System.out.println(JSON.toJSONString(
//                orgnazitionUnitModel.getParentOrgnazitionUnit(orgnazitionUnitModel.getTopLevelOrgnazitionUnitDao().getOuID(), IndicatorType.Finance)
//        ));
        System.out.println("按照业务类型找上级组织属性不报错");
        System.out.println(JSON.toJSONString(
                orgnazitionUnitModelThreadLocal.get().getParentOrgnazitionUnit(
                        orgnazitionUnitModelThreadLocal.get().getChildOrgnazitionUnits(orgnazitionUnitModelThreadLocal.get().getTopLevelOrgnazitionUnitDao().getOuID(), IndicatorType.Finance).get(0).getOuID()
                        , IndicatorType.Finance
                )
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
//                orgnazitionUnitModel.getChildOrgnazitionUnits("1", IndicatorType.Finance)
//        ));
//        System.out.println("找上级组织属性");
//        System.out.println(JSON.toJSONString(
//                orgnazitionUnitModel.getParentOrgnazitionUnit("2", IndicatorType.Finance)
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
//                orgnazitionUnitModel.getOrgnazitionTreeViewByAttribute(IndicatorType.Finance)
//        ));
//    }

    private static OrgnazitionUnitModel generateOrgnazitionUnitModel() {
        OrgnazitionUnitModel orgnazitionUnitModel = new OrgnazitionUnitModel();

        //  创建三个业务组织
        orgnazitionUnitModel
                .updateOrgnazitionUnit(
                        orgnazitionUnitModel.createOrgnazitionUnit(
                                null
                                , "ouCode1"
                                , "ouName1"
                                , new Date()
                                , "猪"
                        )
                )
                .updateOrgnazitionUnit(
                        orgnazitionUnitModel.createOrgnazitionUnit(
                                orgnazitionUnitModel.getTopLevelOrgnazitionUnitDao().getOuID()
                                , "ouCode2"
                                , "ouName2"
                                , new Date()
                                , "猪"
                        )
                )
                .updateOrgnazitionUnit(
                        orgnazitionUnitModel.createOrgnazitionUnit(
                                orgnazitionUnitModel.getTopLevelOrgnazitionUnitDao().getOuID()
                                , "ouCode3"
                                , "ouName3"
                                , new Date()
                                , "猪"
                        )
                );

        return orgnazitionUnitModel
                .updateOrgnazitionUnitAttributes(
                        orgnazitionUnitModel.getTopLevelOrgnazitionUnitDao().getOuID()
                        , new ArrayList<OrgnazitionUnitAttribute>() {{
                            //  模拟业务组织ouCode1，拥有行政和财务属性
                            add(new OrgnazitionUnitAttribute(IndicatorType.Finance, false));
                            add(new OrgnazitionUnitAttribute(IndicatorType.Administration, false));
                        }}
                )
                .updateOrgnazitionUnitAttributes(
                        orgnazitionUnitModel.getChildOrgnazitionUnits(orgnazitionUnitModel.getTopLevelOrgnazitionUnitDao().getOuID()).get(0).getOuID()  //  ouCode2
                        , new ArrayList<OrgnazitionUnitAttribute>() {{
                            //  模拟业务组织ouCode2，拥有财务属性
                            add(
                                    new OrgnazitionUnitAttribute(
                                            orgnazitionUnitModel.getTopLevelOrgnazitionUnitDao().getOuID()  //  ouCode1
                                            , IndicatorType.Finance
                                            , false)
                            );
                            add(
                                    new OrgnazitionUnitAttribute(
                                            orgnazitionUnitModel.getTopLevelOrgnazitionUnitDao().getOuID()  //  ouCode1
                                            , IndicatorType.Administration
                                            , false)
                            );
                        }}
                )
                .updateOrgnazitionUnitAttributes(
                        orgnazitionUnitModel.getChildOrgnazitionUnits(orgnazitionUnitModel.getTopLevelOrgnazitionUnitDao().getOuID()).get(1).getOuID()  //  ouCode3
                        , new ArrayList<OrgnazitionUnitAttribute>() {{
                            //  模拟业务组织ouCode2，拥有财务属性
                            add(
                                    new OrgnazitionUnitAttribute(
                                            orgnazitionUnitModel.getTopLevelOrgnazitionUnitDao().getOuID()  //  ouCode1
                                            , IndicatorType.Finance
                                            , false)
                            );
                            add(
                                    new OrgnazitionUnitAttribute(
                                            orgnazitionUnitModel.getTopLevelOrgnazitionUnitDao().getOuID()  //  ouCode1
                                            , IndicatorType.Administration
                                            , false)
                            );
                        }}
                );
    }
}
