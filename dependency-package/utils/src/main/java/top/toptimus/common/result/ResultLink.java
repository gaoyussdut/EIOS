package top.toptimus.common.result;

import lombok.AllArgsConstructor;
import lombok.Getter;
import top.toptimus.common.enums.RequestMethodEnum;

import java.util.List;

/**
 * rest接口的url
 */
@AllArgsConstructor
@Getter
public class ResultLink {
    private String title;   //  链接名称
    private String self;   //  链接url
    private List<RequestMethodEnum> requestMethods; // 请求的类型
    private Integer _order; // 接口执行顺序

    public ResultLink(String title, String self
            , List<RequestMethodEnum> requestMethods) {
        this.title = title;
        this.self = self;
        this.requestMethods = requestMethods;
    }

}
