package top.toptimus.common.result;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
public class ResultUserControl {

    private String title; // 标题
    private String userControlEnum; // 控件枚举
    private String metaId; // metaId
    private Map<String, List<ResultLink>> resultLinksMap; // 控件内按钮
    private String self;

    public ResultUserControl(String title, String userControlEnum
            , String metaId, Map<String, List<ResultLink>> resultLinksMap) {
        this.title = title;
        this.userControlEnum = userControlEnum;
        this.metaId = metaId;
        this.resultLinksMap = resultLinksMap;
    }

    public ResultUserControl(String title, String userControlEnum
            , String self) {
        this.title = title;
        this.userControlEnum = userControlEnum;
        this.self = self;
    }
}
