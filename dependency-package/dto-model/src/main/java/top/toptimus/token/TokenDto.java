package top.toptimus.token;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * token定义
 *
 * @author lizongsheng
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class TokenDto {

    private String tokenId;
    private String tableName;
    private boolean isValid;
    private String status;

    private String column; //字段
    private String value; //值

    public TokenDto(String tokenId) {
        this.tokenId = tokenId;
    }

    public TokenDto build(String tokenId, String tableName, String column, String value) {
        this.tokenId = tokenId;
        this.tableName = tableName;
        this.column = column;
        this.value = value;
        return this;
    }

    public TokenDto build(String tokenId, String tableName, boolean isValid, String status) {
        this.tokenId = tokenId;
        this.tableName = tableName;
        this.isValid = isValid;
        this.status = status;
        return this;
    }

    public TokenDto build(String tableName, String column, String value) {
        this.tableName = tableName;
        this.column = column;
        this.value = value;
        return this;
    }

    public TokenDto build(String tableName, String tokenId) {
        this.tableName = tableName;
        this.tokenId = tokenId;
        return this;
    }

}
