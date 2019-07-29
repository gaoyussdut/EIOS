package top.toptimus.common.search;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import top.toptimus.common.enums.SearchCommonEnum;

import java.util.Map;

/**
 * 检索项目定义
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class SearchInfo {

    private SearchItem searchItem; // 检索项目定义

    private Map<SearchCommonEnum, Object> param; // 检索参数参数

    public SearchInfo(SearchItem searchItem) {
        this.searchItem = searchItem;
    }
}
