package top.toptimus.tokendata;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 可分页的只包含key,businessId,jsonData的简化TokenDataDTO
 *
 * @author root
 */
@NoArgsConstructor
@Getter
public class TokenDataPageableDto implements Serializable {

    private static final long serialVersionUID = 4642279876693498074L;

    protected int pageSize;
    protected int pageNo;
    protected int total;
    protected List<TokenDataDto> tokenDataDtoList;

    public TokenDataPageableDto(int pageSize, int pageNo, List<TokenDataDto> tokenDataDtoList) {
        this.pageSize = pageSize;
        this.pageNo = pageNo;
        this.total = tokenDataDtoList.size();
        this.tokenDataDtoList = tokenDataDtoList;
    }

    public TokenDataPageableDto(int pageSize, int pageNo, int total, List<TokenDataDto> tokenDataDtoList) {
        this.pageSize = pageSize;
        this.pageNo = pageNo;
        this.total = total;
        this.tokenDataDtoList = tokenDataDtoList;
    }

    public TokenDataPageableDto build(int pageSize, int pageNo, int total) {
        this.pageSize = pageSize;
        this.pageNo = pageNo;
        this.total = total;
        return this;
    }

    public TokenDataPageableDto build(int pageSize, int pageNo) {
        this.pageSize = pageSize;
        this.pageNo = pageNo;
        return this;
    }

    public TokenDataPageableDto build(List<TokenDataDto> reducedTokenDataDtos) {
        this.tokenDataDtoList = reducedTokenDataDtos;
        return this;
    }

    public TokenDataPageableDto build(int total) {
        this.total = total;
        return this;
    }

}
