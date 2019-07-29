package top.toptimus.common.search;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import top.toptimus.common.enums.SearchCommonEnum;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 检索项目定义
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class SearchItem implements Serializable {
    private String id; // 唯一id
    private String name; // 检索项目名
    private int index; // 呈现的顺序
    private String selectType; // 枚举 想取得的Tap页:未完成，进行中，已完成
    private String searchSql;  //  searchSql 例如：“ AND gongying.status = '1' AND gongying.user_id = ? ”
    private Map<SearchCommonEnum, String> searchCommonMap;  // 检索共通项目 如果此不为空需要单独处理，把相关参数取出来放进去
}
