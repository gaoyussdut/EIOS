package top.toptimus.indicator;

import com.alibaba.fastjson.JSON;
import top.toptimus.indicator.indicatorBill.dao.IndicatorOuRelDao;
import top.toptimus.indicator.indicatorBill.model.IndicatorBillRelModel;
import top.toptimus.indicator.ou.base.IndicatorType;
import top.toptimus.indicator.ou.dao.OrgnazitionUnitDao;
import top.toptimus.indicator.ou.dto.OrgnazitionUnitAttribute;
import top.toptimus.indicator.ou.dto.OrgnazitionUnitDto;
import top.toptimus.indicator.ou.model.OrgnazitionUnitModel;

import java.util.ArrayList;
import java.util.UUID;

public class TestOrg {
    public static void main(String[] args) {
        testOu();
//        testIndicator();
    }

    private static void testIndicator() {
        IndicatorBillRelModel indicatorBillRelModel = new IndicatorBillRelModel(
                new ArrayList<IndicatorOuRelDao>() {{
                    add(
                            new IndicatorOuRelDao(
                                    "rel1"
                                    , "1"
                                    , "2"
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
                                    , "1"
                                    , "2"
                                    , "meta3"
                                    , "meta4"
                                    , true
                                    , "procedureName2"
                                    , IndicatorType.Administration
                            )
                    );
                }}
        );
        System.out.println(JSON.toJSONString(
                indicatorBillRelModel.getIndicatorOuRelDaosByOuId("1")
        ));

        System.out.println(JSON.toJSONString(
                indicatorBillRelModel.buildIndicatorTokenDefineDao(
                        "rel1"
                        , UUID.randomUUID().toString()
                        , UUID.randomUUID().toString()
                )
        ));
    }

    private static void testOu() {
        OrgnazitionUnitModel initData = generateOrgnazitionUnitModel();

        //  测试构造函数
        OrgnazitionUnitModel orgnazitionUnitModel = new OrgnazitionUnitModel(
                new ArrayList<OrgnazitionUnitDao>() {{
                    initData.getOrgnazitionUnitMap().keySet().forEach(ouId -> add(initData.getOrgnazitionUnitMap().get(ouId)));
                }}
        );
        System.out.println("找下级组织列表");
        System.out.println(JSON.toJSONString(
                orgnazitionUnitModel.getChildOrgnazitionUnits("1", IndicatorType.Finance)
        ));
        System.out.println("找上级组织属性");
        System.out.println(JSON.toJSONString(
                orgnazitionUnitModel.getParentOrgnazitionUnit("2", IndicatorType.Finance)
        ));
        System.out.println("当前组织属性");
        System.out.println(JSON.toJSONString(
                orgnazitionUnitModel.getOrgnazitionUnitDao("1")
        ));
        System.out.println(JSON.toJSONString(
                orgnazitionUnitModel.getOrgnazitionUnitDao("2")
        ));
        System.out.println("当获取某一属性下的业务组织树");
        System.out.println(JSON.toJSONString(
                orgnazitionUnitModel.getOrgnazitionTreeViewByAttribute(IndicatorType.Finance)
        ));
    }

    private static OrgnazitionUnitModel generateOrgnazitionUnitModel() {
        return new OrgnazitionUnitModel()
                .updateOrgnazitionUnit(
                        new OrgnazitionUnitDto(
                                "1"
                                , "ouCode1"
                                , "ouName1"
                                , "猪"
                                , ""
                        ).buildOrgnazitionUnitAttribute(new OrgnazitionUnitAttribute(IndicatorType.Administration, true))
                ).updateOrgnazitionUnit(
                        new OrgnazitionUnitDto(
                                "1"
                                , "ouCode1"
                                , "ouName1"
                                , "猪"
                                , ""
                        ).buildOrgnazitionUnitAttribute(new OrgnazitionUnitAttribute(IndicatorType.Finance, true))
                ).updateOrgnazitionUnit(
                        new OrgnazitionUnitDto(
                                "2"
                                , "ouCode2"
                                , "ouName2"
                                , "猪"
                                , ""
                        ).buildOrgnazitionUnitAttribute(new OrgnazitionUnitAttribute("1", IndicatorType.Finance, false))
                ).updateOrgnazitionUnit(
                        new OrgnazitionUnitDto(
                                "3"
                                , "ouCode3"
                                , "ouName3"
                                , "猪"
                                , ""
                        ).buildOrgnazitionUnitAttribute(new OrgnazitionUnitAttribute("1", IndicatorType.Finance, false))
                );
    }
}
