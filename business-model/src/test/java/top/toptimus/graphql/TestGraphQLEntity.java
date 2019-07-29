package top.toptimus.graphql;

import com.alibaba.fastjson.JSON;
import top.toptimus.common.enums.FkeyTypeEnum;
import top.toptimus.formula.pushDownProperty.DataProperty;
import top.toptimus.merkle.GraphQLModel;
import top.toptimus.tokendata.TokenDataDto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TestGraphQLEntity {
    private static String ruleInventoryId = "inventoryId"; //  物料
    private static String ruleFunctionId = "functionId"; //  设备
    private static String ruleIsPass = "passId";
    private static String ruleKeyFunction = "ruleKeyFunction";

    private static TokenDataDto tokenDataDto1 = JSON.parseObject("{\n" +
            "    \"tokenId\":\"1\",\n" +
            "    \"fields\":[\n" +
            "        {\n" +
            "            \"dataType\":\"STRING\",\n" +
            "            \"key\":\"shiyongbumen\",\n" +
            "            \"jsonData\":\"采购部\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"dataType\":\"STRING\",\n" +
            "            \"key\":\"yewuleixing\",\n" +
            "            \"jsonData\":\"外购入库\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"dataType\":\"DATE\",\n" +
            "            \"key\":\"riqi\",\n" +
            "            \"jsonData\":\"2019-02-19 02:01:49.18\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"dataType\":\"STRING\",\n" +
            "            \"key\":\"beizhu\",\n" +
            "            \"jsonData\":\"test1\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"dataType\":\"STRING\",\n" +
            "            \"key\":\"bianhao\",\n" +
            "            \"jsonData\":\"test-001\"\n" +
            "        }\n" +
            "    ]\n" +
            "}", TokenDataDto.class);
    private static TokenDataDto tokenDataDto2 = JSON.parseObject(
            "{\n" +
                    "    \"tokenId\":\"2\",\n" +
                    "    \"fields\":[\n" +
                    "        {\n" +
                    "            \"dataType\":\"STRING\",\n" +
                    "            \"key\":\"shiyongbumen\",\n" +
                    "            \"jsonData\":\"采购部\"\n" +
                    "        },\n" +
                    "        {\n" +
                    "            \"dataType\":\"STRING\",\n" +
                    "            \"key\":\"yewuleixing\",\n" +
                    "            \"jsonData\":\"外购入库\"\n" +
                    "        },\n" +
                    "        {\n" +
                    "            \"dataType\":\"DATE\",\n" +
                    "            \"key\":\"riqi\",\n" +
                    "            \"jsonData\":\"2019-02-19 02:01:49.18\"\n" +
                    "        },\n" +
                    "        {\n" +
                    "            \"dataType\":\"STRING\",\n" +
                    "            \"key\":\"beizhu\",\n" +
                    "            \"jsonData\":\"test1\"\n" +
                    "        },\n" +
                    "        {\n" +
                    "            \"dataType\":\"STRING\",\n" +
                    "            \"key\":\"bianhao\",\n" +
                    "            \"jsonData\":\"test-001\"\n" +
                    "        }\n" +
                    "    ]\n" +
                    "}", TokenDataDto.class);

    private static List<DataProperty> dataProperties = new ArrayList<DataProperty>() {{
        add(
                new DataProperty(
                        "datadiff_key"
                        , FkeyTypeEnum.DATE
                        , "datediff('year',start_time,end_time)"
                        ,""
                        ,
                        new HashMap<String, Object>() {{
                            put("start_time", "2012-1-1 00:00:00");
                            put("end_time", "2017-1-2 00:00:00");
                        }}
                )
        );
        add(
                new DataProperty(
                        "choice_key"
                        , FkeyTypeEnum.STRING
                        , "choice(inputvalue=defaultvalue,id,id1)",
                        "",
                        new HashMap<String, Object>() {{
                            put("inputvalue", "1");
                            put("defaultvalue", "1");
                            put("id", "1");
                            put("id1", "0");
                        }}
                )
        );
    }};

    public static void main(String[] args) {
        //  TODO    所有常量最后都从数据库取出
        GraphQLModel graphQLModel = new GraphQLModel("", "Query", "type Query{inventoryId: String,functionId: String,passId: Boolean,ruleKeyFunction: String} schema{query: Query}");

        //  下推规则测试
        pushDownRuleTest(graphQLModel);
        //  token测试
        tokenTest(graphQLModel);
        //  公式测试
        functionTest(graphQLModel);
        //  boolean测试
        booleanTest(graphQLModel);
        //  全部查询
        testAllQuery(graphQLModel);
    }

    /**
     * 下推规则
     */
    private static void pushDownRuleTest(GraphQLModel graphQLModel) {
        graphQLModel.savePushDownRuleIntoSchema(ruleKeyFunction, dataProperties);
        System.out.println("--------------------------------");
        System.out.println("下推规则测试");
        //  公式存在

        System.out.println(
                JSON.toJSONString(
                        graphQLModel.getTokenDataDto()
                )
        );
        System.out.println("--------------------------------");
    }

    private static void testAllQuery(GraphQLModel graphQLModel) {
        System.out.println("--------------------------------");
        System.out.println("全部查询");
        //  boolean值存在
        System.out.println(graphQLModel.getBooleanResult(ruleIsPass));
        //  公式存在
        System.out.println(graphQLModel.getFunctionResult(ruleFunctionId));
        //  物料1存在
        System.out.println(JSON.toJSONString(graphQLModel.getTokenData(ruleInventoryId, tokenDataDto1.getTokenId())));
        //  物料2存在
        System.out.println(JSON.toJSONString(graphQLModel.getTokenData(ruleInventoryId, tokenDataDto2.getTokenId())));
        System.out.println(
                JSON.toJSONString(
                        graphQLModel.getTokenDataDto()
                )
        );
        System.out.println("--------------------------------");
    }

    private static void functionTest(GraphQLModel graphQLModel) {
        /*
        公式测试
         */
        graphQLModel.saveFunctionIntoSchema(
                ruleFunctionId
                , "datediff('year','2012-1-1 00:00:00','2017-1-2 00:00:00')"
        );
        System.out.println("--------------------------------");
        System.out.println("公式测试");
        //  公式存在
        System.out.println(graphQLModel.getFunctionResult(ruleFunctionId));
        System.out.println("--------------------------------");
    }

    private static void booleanTest(GraphQLModel graphQLModel) {
        /*
        boolean测试
         */
        graphQLModel.saveBooleanDataIntoSchema(ruleIsPass, true);
        System.out.println("--------------------------------");
        System.out.println("boolean测试");
        //  boolean值存在
        System.out.println(graphQLModel.getBooleanResult(ruleIsPass));
        System.out.println("--------------------------------");
    }

    private static void tokenTest(GraphQLModel graphQLModel) {
         /*
        token测试
         */
        graphQLModel.saveDataIntoSchema(
                ruleInventoryId
                , new ArrayList<TokenDataDto>() {{
                    add(tokenDataDto1);
                }}

        );
        System.out.println("--------------------------------");
        System.out.println("token测试");
        //  物料1存在
        System.out.println(JSON.toJSONString(graphQLModel.getTokenData(ruleInventoryId, tokenDataDto1.getTokenId())));
        //  物料2不存在
        System.out.println(JSON.toJSONString(graphQLModel.getTokenData(ruleInventoryId, tokenDataDto2.getTokenId())));
        graphQLModel.saveDataIntoSchema(
                ruleInventoryId
                , new ArrayList<TokenDataDto>() {{
                    add(tokenDataDto2);
                }}
        );
        //  物料1存在
        System.out.println(JSON.toJSONString(graphQLModel.getTokenData(ruleInventoryId, tokenDataDto1.getTokenId())));
        //  物料2存在
        System.out.println(JSON.toJSONString(graphQLModel.getTokenData(ruleInventoryId, tokenDataDto2.getTokenId())));
        System.out.println("--------------------------------");
    }
}
