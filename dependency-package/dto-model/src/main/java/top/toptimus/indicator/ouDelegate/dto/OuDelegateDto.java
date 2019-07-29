package top.toptimus.indicator.ouDelegate.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 委托业务组织id和业务委托关系主键
 *
 * @author gaoyu
 * @since 2019-07-19
 */
@AllArgsConstructor
@Getter
public class OuDelegateDto {
    private String delegateId;  //  业务委托关系主键
    private String delegateOuId;    //  委托业务组织id
}
