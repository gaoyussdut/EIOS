package top.toptimus.stream;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import top.toptimus.formula.formula.FormulaException;
import top.toptimus.formula.formula.Parser;
import top.toptimus.formula.util.DefaultProperties;

import java.io.Serializable;

/**
 * 公式定义
 * Created by JiangHao on 2018/9/12.
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class BusinessItem implements Serializable {

    private static final long serialVersionUID = 8586228007807053839L;

    public String tokenId; //  token id
    public String formula; //  公式
    public String value;   //  公式输出值
    public boolean hasConvertError;    //  公式转换是否错误
    public String key;

    public BusinessItem build(String tokenId, String formula, String key) {
        this.tokenId = tokenId;
        this.formula = formula;
        try {
            /*
                公式执行
                @Link top.toptimus.util.formula.DefaultFunctionHelper 中注册的函数
             */
            this.value = new Parser()
                    .parse(formula)
                    .getValue(new DefaultProperties())
                    .getValue().toString();
            this.hasConvertError = false;
        } catch (FormulaException e) {
            e.printStackTrace();
            this.hasConvertError = true;
        }
        this.key = key;
        return this;
    }
}
