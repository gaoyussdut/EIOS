package top.toptimus.meta.signGroup;

import lombok.Data;
import lombok.NoArgsConstructor;
import top.toptimus.tokendata.TokenDataDto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 汇签信息提交DTO
 */
@NoArgsConstructor
@Data
public class CountersignSubmitDTO implements Serializable {

    private static final long serialVersionUID = -6764588891475496546L;

    private List<String> countersignTaskTokenIds = new ArrayList<>();//汇签任务tokenIds
    private TokenDataDto countersignTokenData;                       //汇签信息表头tokenData

    public CountersignSubmitDTO(List<String> CountersignTaskTokenIds,TokenDataDto countersignTokenData){
        this.getCountersignTaskTokenIds().addAll(CountersignTaskTokenIds);
        this.countersignTokenData = countersignTokenData;
    }
}
