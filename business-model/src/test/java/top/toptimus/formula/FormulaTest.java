package top.toptimus.formula;

import com.alibaba.fastjson.JSON;
import top.toptimus.common.enums.FkeyTypeEnum;
import top.toptimus.stream.BusinessItemSummaryStatisticsModel;
import top.toptimus.tokendata.TokenDataDto;
import top.toptimus.tokendata.field.FkeyField;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 聚合函数和scalar公式测试
 */
public class FormulaTest {

    public static void main(String[] args) {
//        SteamingTest.steamingCount();
//        SteamingTest.steamingCountSorting();
//        SteamingTest.steamingSum();
        FormulaTest.currencySummary();
        FormulaTest.dateTimeSummary();
        System.out.println("----------------公式----------------");


        List<FormulaKeyAndValueDTO> formulaKeyAndValueDTOS = new ArrayList<>();
        formulaKeyAndValueDTOS.add(
                new FormulaKeyAndValueDTO(
                        "fvalue", "choice(to_long(datediff('year','2017-1-1 00:00:00','2012-1-2 00:00:00'))>5,1,2)"
                        , ""));
        FormulaTest.formula(new HashMap<String, List<FormulaKeyAndValueDTO>>() {{
//            put(UUID.randomUUID().toString(), "datediff('year','2012-1-1 00:00:00','2017-1-2 00:00:00')");
            put(UUID.randomUUID().toString()
                    , formulaKeyAndValueDTOS);
//            put(UUID.randomUUID().toString(), "1+2*3/4");
        }});
    }

    /**
     * 公式
     *
     * @param tokenFunctionMap
     */
    private static void formula(Map<String, List<FormulaKeyAndValueDTO>> tokenFunctionMap) {
        System.out.println(JSON.toJSONString(new FormulaModel().build(tokenFunctionMap).getBusinessItems()));
    }

    /**
     * 时间聚合
     */
    private static void dateTimeSummary() {
        System.out.println("----------------时间聚合----------------");
        List<TokenDataDto> tokenDataDtos = new ArrayList<TokenDataDto>() {{
            add(new TokenDataDto(UUID.randomUUID().toString()
                    , Arrays.asList(
                    new FkeyField().createSelectFkeyField(FkeyTypeEnum.SELECT, "物料ID", "猪", "")
                    , new FkeyField().createPlainFkeyField(FkeyTypeEnum.DECIMAL, "日期", "2017-01-01 00:00:00"))
            ));
            add(new TokenDataDto(UUID.randomUUID().toString()
                    , Arrays.asList(
                    new FkeyField().createSelectFkeyField(FkeyTypeEnum.SELECT, "物料ID", "狗", "")
                    , new FkeyField().createPlainFkeyField(FkeyTypeEnum.DECIMAL, "日期", "2017-01-02 00:00:00"))
            ));
            add(new TokenDataDto(UUID.randomUUID().toString()
                    , Arrays.asList(
                    new FkeyField().createSelectFkeyField(FkeyTypeEnum.SELECT, "物料ID", "驴", "")
                    , new FkeyField().createPlainFkeyField(FkeyTypeEnum.DECIMAL, "日期", "2017-01-03 00:00:00"))
            ));
            add(new TokenDataDto(UUID.randomUUID().toString()
                    , Arrays.asList(
                    new FkeyField().createSelectFkeyField(FkeyTypeEnum.SELECT, "物料ID", "马猴额", "")
                    , new FkeyField().createPlainFkeyField(FkeyTypeEnum.DECIMAL, "日期", "2017-01-04 00:00:00"))
            ));
            add(new TokenDataDto(UUID.randomUUID().toString()
                    , Arrays.asList(
                    new FkeyField().createSelectFkeyField(FkeyTypeEnum.SELECT, "物料ID", "豁朗额", "")
                    , new FkeyField().createPlainFkeyField(FkeyTypeEnum.DECIMAL, "日期", "2017-01-05 00:00:00"))
            ));
            add(new TokenDataDto(UUID.randomUUID().toString()
                    , Arrays.asList(
                    new FkeyField().createSelectFkeyField(FkeyTypeEnum.SELECT, "物料ID", "猪", "")
                    , new FkeyField().createPlainFkeyField(FkeyTypeEnum.DECIMAL, "日期", "2017-01-06 00:00:00"))
            ));
            add(new TokenDataDto(UUID.randomUUID().toString()
                    , Arrays.asList(
                    new FkeyField().createSelectFkeyField(FkeyTypeEnum.SELECT, "物料ID", "狗", "")
                    , new FkeyField().createPlainFkeyField(FkeyTypeEnum.DECIMAL, "日期", "2017-01-07 00:00:00"))
            ));
            add(new TokenDataDto(UUID.randomUUID().toString()
                    , Arrays.asList(
                    new FkeyField().createSelectFkeyField(FkeyTypeEnum.SELECT, "物料ID", "猪", "")
                    , new FkeyField().createPlainFkeyField(FkeyTypeEnum.DECIMAL, "日期", "2017-01-11 00:00:00"))
            ));

        }};
        BusinessItemSummaryStatisticsModel businessItemSummaryStatistics = new BusinessItemSummaryStatisticsModel(tokenDataDtos, "物料ID", "日期", true);

        System.out.println("每种畜生的summarize：" + JSON.toJSONString(businessItemSummaryStatistics.getSummaryStatisticsMap()));

        System.out.println("到货时间最晚的记录：" + JSON.toJSONString(
                businessItemSummaryStatistics.getMaxQuantityItem()
        ));
        System.out.println("到货时间最早的记录：" + JSON.toJSONString(
                businessItemSummaryStatistics.getMinQuantityItem()
        ));
    }

    /**
     * 浮点数聚合
     */
    private static void currencySummary() {
        System.out.println("----------------浮点数聚合----------------");
        List<TokenDataDto> tokenDataDtos = new ArrayList<TokenDataDto>() {{
            add(new TokenDataDto(UUID.randomUUID().toString()
                    , Arrays.asList(
                    new FkeyField().createSelectFkeyField(FkeyTypeEnum.SELECT, "物料ID", "猪", "")
                    , new FkeyField().createPlainFkeyField(FkeyTypeEnum.DECIMAL, "数量", "2.99"))
            ));
            add(new TokenDataDto(UUID.randomUUID().toString()
                    , Arrays.asList(
                    new FkeyField().createSelectFkeyField(FkeyTypeEnum.SELECT, "物料ID", "狗", "")
                    , new FkeyField().createPlainFkeyField(FkeyTypeEnum.DECIMAL, "数量", "19.99"))
            ));
            add(new TokenDataDto(UUID.randomUUID().toString()
                    , Arrays.asList(
                    new FkeyField().createSelectFkeyField(FkeyTypeEnum.SELECT, "物料ID", "驴", "")
                    , new FkeyField().createPlainFkeyField(FkeyTypeEnum.DECIMAL, "数量", "29.99"))
            ));
            add(new TokenDataDto(UUID.randomUUID().toString()
                    , Arrays.asList(
                    new FkeyField().createSelectFkeyField(FkeyTypeEnum.SELECT, "物料ID", "马猴额", "")
                    , new FkeyField().createPlainFkeyField(FkeyTypeEnum.DECIMAL, "数量", "29.99"))
            ));
            add(new TokenDataDto(UUID.randomUUID().toString()
                    , Arrays.asList(
                    new FkeyField().createSelectFkeyField(FkeyTypeEnum.SELECT, "物料ID", "豁朗额", "")
                    , new FkeyField().createPlainFkeyField(FkeyTypeEnum.DECIMAL, "数量", "9.99"))
            ));
            add(new TokenDataDto(UUID.randomUUID().toString()
                    , Arrays.asList(
                    new FkeyField().createSelectFkeyField(FkeyTypeEnum.SELECT, "物料ID", "猪", "")
                    , new FkeyField().createPlainFkeyField(FkeyTypeEnum.DECIMAL, "数量", "3.99"))
            ));
            add(new TokenDataDto(UUID.randomUUID().toString()
                    , Arrays.asList(
                    new FkeyField().createSelectFkeyField(FkeyTypeEnum.SELECT, "物料ID", "狗", "")
                    , new FkeyField().createPlainFkeyField(FkeyTypeEnum.DECIMAL, "数量", "19.99"))
            ));
            add(new TokenDataDto(UUID.randomUUID().toString()
                    , Arrays.asList(
                    new FkeyField().createSelectFkeyField(FkeyTypeEnum.SELECT, "物料ID", "猪", "")
                    , new FkeyField().createPlainFkeyField(FkeyTypeEnum.DECIMAL, "数量", "9.99"))
            ));

        }};
        BusinessItemSummaryStatisticsModel businessItemSummaryStatistics = new BusinessItemSummaryStatisticsModel(tokenDataDtos, "物料ID", "数量", false);

        System.out.println("每种畜生的summarize：" + JSON.toJSONString(businessItemSummaryStatistics.getSummaryStatisticsMap()));

        System.out.println("价格最高的记录：" + JSON.toJSONString(
                businessItemSummaryStatistics.getMaxQuantityItem()
        ));
        System.out.println("价格最低的记录：" + JSON.toJSONString(
                businessItemSummaryStatistics.getMinQuantityItem()
        ));
    }


    private static void steamingCount() {
        System.out.println("steamingCount");
        //3 猪, 2 狗, others 1
        List<String> items =
                Arrays.asList("猪", "猪", "狗",
                        "猪", "驴", "狗", "豁朗额");

        Map<String, Long> result =
                items.stream().collect(
                        Collectors.groupingBy(
                                Function.identity(), Collectors.counting()
                        )
                );

        System.out.println(result);

    }

    private static void steamingCountSorting() {
        System.out.println("steamingCountSorting");
        //3 猪, 2 狗, others 1
        List<String> items =
                Arrays.asList("猪", "猪", "狗",
                        "猪", "驴", "狗", "豁朗额");

        Map<String, Long> result =
                items.stream().collect(
                        Collectors.groupingBy(
                                Function.identity(), Collectors.counting()
                        )
                );

        Map<String, Long> finalMap = new LinkedHashMap<>();

        //Sort a map and add to finalMap
        result.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue()
                        .reversed()).forEachOrdered(e -> finalMap.put(e.getKey(), e.getValue()));

        System.out.println(finalMap);
    }

    private static void steamingSum() {
//        System.out.println("steamingSum");
//        //3 猪, 2 狗, others 1
//        List<BusinessItemQuantityDto> items = Arrays.asList(
//                new BusinessItemQuantityDto(UUID.randomUUID().toString(), "猪", 2.99),
//                new BusinessItemQuantityDto(UUID.randomUUID().toString(), "狗", 19.99),
//                new BusinessItemQuantityDto(UUID.randomUUID().toString(), "驴", 29.99),
//                new BusinessItemQuantityDto(UUID.randomUUID().toString(), "马猴额", 29.99),
//                new BusinessItemQuantityDto(UUID.randomUUID().toString(), "豁朗额", 9.99),
//                new BusinessItemQuantityDto(UUID.randomUUID().toString(), "猪", 3.99),
//                new BusinessItemQuantityDto(UUID.randomUUID().toString(), "狗", 19.99),
//                new BusinessItemQuantityDto(UUID.randomUUID().toString(), "猪", 9.99)
//        );
//
//        System.out.println("畜生出现的记录数：" + JSON.toJSONString(
//                items.stream()
//                        .collect(
//                                Collectors.groupingBy(BusinessItemQuantityDto::getBusinessIdentity, Collectors.counting()
//                                )
//                        )
//        ));
//
//
//        System.out.println("每种畜生的summarize：" + JSON.toJSONString(BusinessItemSummaryStatistics.getSummaryStatistics(items)));
//
//        System.out.println("最贵的畜生：" + JSON.toJSONString(
//                BusinessItemSummaryStatistics.getMaxQuantityItem(items)
//        ));
//        System.out.println("最便宜的畜生：" + JSON.toJSONString(
//                BusinessItemSummaryStatistics.getMinQuantityItem(items)
//        ));

    }

    /**
     * 将长时间格式字符串转换为时间 yyyy-MM-dd HH:mm:ss
     *
     * @param strDate
     * @return
     */
    public static Date strToDateLong(String strDate) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        ParsePosition pos = new ParsePosition(0);
        Date strtodate = formatter.parse(strDate, pos);
        return strtodate;
    }

}

