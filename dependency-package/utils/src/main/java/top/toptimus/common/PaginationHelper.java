package top.toptimus.common;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

/**
 * 分页查询
 *
 * @param <E>
 */
public class PaginationHelper<E> {
    public CurrentPage<E> fetchPage(final JdbcTemplate jt, final String sqlCountRows, String sqlFetchRows,
                                    final Object args[], final int pageNo, final int pageSize, final RowMapper<E> rowMapper) {
        // determine how many rows are available
        // final int rowCount = jt.queryForInt(sqlCountRows, args);
        int rowCount = 0;
        try {
            rowCount = jt.queryForObject(sqlCountRows, args, Integer.class);
        } catch (EmptyResultDataAccessException e) {
            e.printStackTrace();
        }
        // calculate the number of pages
        int pageCount = rowCount / pageSize;
        if (rowCount > pageSize * pageCount) {
            pageCount++;
        }
        // create the page object
        final CurrentPage<E> page = new CurrentPage<E>();
        page.setTotal(rowCount);
        page.setPageNo(pageNo);
        page.setPageSize(pageSize);
        page.setPagesAvailable(pageCount);
        int relPageNo = pageNo - 1;
        sqlFetchRows += " LIMIT " + pageSize + " OFFSET " + relPageNo;
        page.setPageItems(jt.query(sqlFetchRows, args, rowMapper));

        return page;
    }

    public CurrentPage<E> fetchPage(final JdbcTemplate jt, final String sqlCountRows, String sqlFetchRows,
                                    final int pageNo, final int pageSize, final RowMapper<E> rowMapper) {
        // determine how many rows are available
        // final int rowCount = jt.queryForInt(sqlCountRows, args);
        int rowCount = 0;
        try {
            rowCount = jt.queryForObject(sqlCountRows, Integer.class);
        } catch (EmptyResultDataAccessException e) {
            e.printStackTrace();
        }
        // calculate the number of pages
        int pageCount = rowCount / pageSize;
        if (rowCount > pageSize * pageCount) {
            pageCount++;
        }
        // create the page object
        final CurrentPage<E> page = new CurrentPage<E>();
        page.setTotal(rowCount);
        page.setPageNo(pageNo);
        page.setPageSize(pageSize);
        page.setPagesAvailable(pageCount);
        int relPageNo = pageNo - 1;
        sqlFetchRows += " LIMIT " + pageSize + " OFFSET " + relPageNo;
        page.setPageItems(jt.query(sqlFetchRows, rowMapper));
        return page;
    }
}
