package top.toptimus.formula;

import top.toptimus.formula.formula.*;
import top.toptimus.formula.util.DefaultProperties;

/**
 * 公式测试方法
 *
 * @author gaoyu
 */
public class testFunctionHelper {

    public static void main(String[] args) {
        testFunctionHelper.datediff();
//        testFunctionHelper.choice();
    }

    private static void datediff() {
        Expression expr = new Parser().parse("datediff('year',start_time,end_time)");
        DefaultProperties dataprovider = new DefaultProperties();
//		dataprovider.SetValue("inputvalue", "1");
//		dataprovider.SetValue("defaultvalue", "1");
//		dataprovider.SetValue("id", "1");
//		dataprovider.SetValue("id1", "0");

        dataprovider.SetValue("start_time", "2012-1-1 00:00:00");
        dataprovider.SetValue("end_time", "2017-1-2 00:00:00");
        ExprValue value = expr.getValue(dataprovider);
        System.out.println(value.getValue());
    }

    private static void choice() {
        // String formula =
        // "age() % 10 + (to_long(id) / 20 - 2*20) + 0.1 * 100";
        String formula = "choice(inputvalue=defaultvalue,id,id1)";
        // "choice(to_long(id)<1,1,2)+1212";

//		final Function age = new Function("age") {
//			public void checkArgument(Expression arg) throws FormulaException {
//				if (getArgumentCount() > 0) {
//					throw nw FormulaException(
//							"age function does not support any arguments.");
//				}
//			}
//
//			public ExprValue getValue(DataProvider provider)
//					throws FormulaException {
//				return new ExprValue(23);
//			}
//		};

        FunctionHelper myFunctionHelper = funcName -> {
//				if (funcName.equals("age")) {
//					return age;
//				}
            return null;
        };

        Parser parser = new Parser(new DefaultFunctionHelper(myFunctionHelper));
        Expression expr = parser.parse(formula);

        DefaultProperties dataprovider = new DefaultProperties();
        dataprovider.SetValue("inputvalue", "1");
        dataprovider.SetValue("defaultvalue", "1");
        dataprovider.SetValue("id", "1");
        dataprovider.SetValue("id1", "0");

        ExprValue value = expr.getValue(dataprovider);
        System.out.println(value);
    }
}
