package top.toptimus.exception;

import lombok.Data;

@Data
public class TopSQLException extends RuntimeException {

    private static final long serialVersionUID = 66625L;
    // 操作
    private String operation;
    // table
    private String tables;
    // Sql语句
    private String sql;
    // 描述
    private String desc;

    public TopSQLException(String operation, String tables, String sql, String desc) {
        this.operation = operation;
        this.tables = tables;
        this.sql = sql;
        this.desc = desc;
    }

    public TopSQLException(String sql, String desc) {
        this.sql = sql;
        this.desc = desc;
    }


    public String SQLExceptionInfos() {
        String allStrInfos = "";
        String LB = "\r\n";

        if (operation != null && !operation.equals(""))
            allStrInfos += "operation:" + operation + LB;

        if (tables != null && !tables.equals(""))
            allStrInfos += "table:" + tables + LB;

        if (sql != null && !sql.equals(""))
            allStrInfos += "sql:" + sql + LB;

        if (desc != null && !desc.equals(""))
            allStrInfos += "desc:" + desc + LB;

        return allStrInfos;
    }
}
