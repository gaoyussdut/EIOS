package top.toptimus.common;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 分页查询
 *
 * @param <E>
 */
@Data
public class CurrentPage<E> implements Serializable {

    private static final long serialVersionUID = -4354920168995602471L;

    private int pageNo;
    private int pageSize;
    private int pagesAvailable;
    private int total;
    private List<E> pageItems = new ArrayList<>();


    /**
     * build方法
     *
     * @param pageNo    页码
     * @param pageSize  页宽
     * @param tokenIds  token id列表
     * @param pageItems 数据
     * @return 分页数据
     */
    public CurrentPage<E> build(int pageNo, int pageSize, CurrentPage<String> tokenIds, List<E> pageItems) {
        this.pageNo = pageNo;
        this.pageSize = pageSize;
        this.total = tokenIds.getTotal();
        this.pageItems = pageItems;
        return this;
    }
}