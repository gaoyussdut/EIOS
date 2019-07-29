package top.toptimus.stream;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Getter;
import lombok.NoArgsConstructor;
import top.toptimus.tokendata.TokenDataDto;
import top.toptimus.tokendata.field.FkeyField;
import top.toptimus.util.DateTimeUtil;

import java.util.*;
import java.util.stream.Collectors;

/**
 * token数据的统计信息实体类
 */
@NoArgsConstructor
@Getter
public class BusinessItemSummaryStatisticsModel {
    private Map<String, SummaryStatistics> summaryStatisticsMap;    //  获取统计信息，K:基础数据主键,V:包含count、sum、min、max、average
    private BusinessItemQuantityDto maxQuantityItem;    //  取businessQuantity最大的记录
    private BusinessItemQuantityDto minQuantityItem;    //  取businessQuantity最小的记录

    /**
     * 构造函数
     *
     * @param tokenDataDtos token data  列表
     * @param identityKey   基础数据主键
     * @param quantityKey   基础数据量化信息
     * @param isTimeStamp   是否时间戳
     */
    public BusinessItemSummaryStatisticsModel(List<TokenDataDto> tokenDataDtos, String identityKey, String quantityKey, boolean isTimeStamp) {
        //  token数据转换,得到核算信息
        List<BusinessItemQuantityDto> businessItemQuantityDtos = new ArrayList<BusinessItemQuantityDto>() {
            private static final long serialVersionUID = -6256731295490922000L;

            {
                tokenDataDtos.forEach(tokenDataDto -> add(new BusinessItemQuantityDto().build(tokenDataDto, identityKey, quantityKey, isTimeStamp)));
            }
        };
        //  核算信息转为统计信息
        this.build(businessItemQuantityDtos, isTimeStamp);
    }

    /**
     * 计算核算信息的统计信息
     *
     * @param businessItemQuantityDtos 核算信息
     * @param isTimeStamp              是否时间戳
     * @return 统计信息
     */
    private static Map<String, SummaryStatistics> getSummaryStatistics(List<BusinessItemQuantityDto> businessItemQuantityDtos, boolean isTimeStamp) {
        if (isTimeStamp) {
            //  获取时间戳统计信息
            Map<String, LongSummaryStatistics> timeStampSummaryStatisticsMap = BusinessItemSummaryStatisticsModel.getTimeStampSummaryStatistics(businessItemQuantityDtos);
            return new HashMap<String, SummaryStatistics>() {
                private static final long serialVersionUID = 1859887395893189907L;

                {
                    timeStampSummaryStatisticsMap.keySet().forEach(businessId -> put(businessId, new SummaryStatistics().build(timeStampSummaryStatisticsMap.get(businessId))));
                }
            };
        } else {
            //  获取浮点数统计信息
            Map<String, DoubleSummaryStatistics> doubleSummaryStatisticsMap = BusinessItemSummaryStatisticsModel.getDoubleSummaryStatistics(businessItemQuantityDtos);
            return new HashMap<String, SummaryStatistics>() {
                private static final long serialVersionUID = -3244000973808963640L;

                {
                    doubleSummaryStatisticsMap.keySet().forEach(businessId -> put(businessId, new SummaryStatistics().build(doubleSummaryStatisticsMap.get(businessId))));
                }
            };
        }
    }

    /**
     * 浮点数获取统计信息，
     * K:基础数据主键
     * V:包含count、sum、min、max、average
     *
     * @param businessItemQuantityDtos 核算dto列表
     * @return 统计信息
     */
    private static Map<String, LongSummaryStatistics> getTimeStampSummaryStatistics(List<BusinessItemQuantityDto> businessItemQuantityDtos) {
        return businessItemQuantityDtos.stream()
                .collect(
                        Collectors.groupingBy(
                                BusinessItemQuantityDto::getBusinessIdentity, Collectors.summarizingLong(BusinessItemQuantityDto::getTimeStamp)
                        )
                );
    }

    /**
     * 浮点数获取统计信息，
     * K:基础数据主键
     * V:包含count、sum、min、max、average
     *
     * @param businessItemQuantityDtos 核算dto列表
     * @return 统计信息
     */
    private static Map<String, DoubleSummaryStatistics> getDoubleSummaryStatistics(List<BusinessItemQuantityDto> businessItemQuantityDtos) {
        return businessItemQuantityDtos.stream()
                .collect(
                        Collectors.groupingBy(
                                BusinessItemQuantityDto::getBusinessIdentity, Collectors.summarizingDouble(BusinessItemQuantityDto::getBusinessQuantity)
                        )
                );
    }

    /**
     * 取businessQuantity最大的记录
     *
     * @param businessItemQuantityDtos 核算dto列表
     * @return 最大的记录
     */
    private static BusinessItemQuantityDto getMaxQuantityItem(List<BusinessItemQuantityDto> businessItemQuantityDtos, boolean isTimeStamp) {
        if (isTimeStamp) {
            return businessItemQuantityDtos
                    .stream().max(Comparator.comparingLong(BusinessItemQuantityDto::getTimeStamp))
                    .get()
                    .build(true);
        } else {
            return businessItemQuantityDtos
                    .stream().max(Comparator.comparingDouble(BusinessItemQuantityDto::getBusinessQuantity))
                    .get()
                    .build(false);
        }
    }

    /**
     * 取businessQuantity最小的记录
     *
     * @param businessItemQuantityDtos 核算dto列表
     * @return 最小的记录
     */
    private static BusinessItemQuantityDto getMinQuantityItem(List<BusinessItemQuantityDto> businessItemQuantityDtos, boolean isTimeStamp) {
        if (isTimeStamp) {
            return businessItemQuantityDtos
                    .stream().min(Comparator.comparingLong(BusinessItemQuantityDto::getTimeStamp))
                    .get()
                    .build(true);
        } else {
            return businessItemQuantityDtos
                    .stream().min(Comparator.comparingDouble(BusinessItemQuantityDto::getBusinessQuantity))
                    .get()
                    .build(false);
        }
    }

    /**
     * 通过核算信息计算统计信息
     *
     * @param businessItemQuantityDtos 核算信息
     * @param isTimeStamp              是否时间戳
     */
    private void build(List<BusinessItemQuantityDto> businessItemQuantityDtos, boolean isTimeStamp) {
        //  统计信息
        this.summaryStatisticsMap = BusinessItemSummaryStatisticsModel.getSummaryStatistics(businessItemQuantityDtos, isTimeStamp);
        //  取businessQuantity最大的记录
        this.maxQuantityItem = BusinessItemSummaryStatisticsModel.getMaxQuantityItem(businessItemQuantityDtos, isTimeStamp);
        //  取businessQuantity最小的记录
        this.minQuantityItem = BusinessItemSummaryStatisticsModel.getMinQuantityItem(businessItemQuantityDtos, isTimeStamp);
    }
}

/**
 * 计算每个token根据基础数据核算数量的DTO
 *
 * @author gaoyu
 */
@NoArgsConstructor
@Getter
class BusinessItemQuantityDto {
    private String tokenId; //  token id
    private String businessIdentity;    //  基础数据主键
    @JSONField(serialize = false)
    private double businessQuantity;    //  基础数据量化信息,包括时间戳,不序列化
    @JSONField(serialize = false)
    private long timeStamp;  //  时间戳，不序列化
    private String businessQuantityStr; //  基础数据量化信息,如果是时间戳，转为时间类型；序列化

    /**
     * token数据转换
     *
     * @param tokenDataDto token data
     * @param identityKey  基础数据主键
     * @param quantityKey  基础数据量化信息
     * @param isTimeStamp  是否时间戳
     * @return 核算dto
     */
    public BusinessItemQuantityDto build(TokenDataDto tokenDataDto, String identityKey, String quantityKey, boolean isTimeStamp) {
        this.tokenId = tokenDataDto.getTokenId();
        boolean businessIdentityMatch = false;
        boolean businessQuantityMatch = false;
        for (FkeyField fkeyField : tokenDataDto.getFields()) {
            if (fkeyField.getKey().equals(identityKey)) {
                this.businessIdentity = fkeyField.getJsonData();  //  基础数据主键匹配
                businessIdentityMatch = true;
            }
            if (fkeyField.getKey().equals(quantityKey)) {
                this.businessQuantityStr = fkeyField.getJsonData();

                if (!isTimeStamp) {
                    /*
                        浮点数处理
                    */
                    try {
                        this.businessQuantity = Double.parseDouble(fkeyField.getJsonData());  //  基础数据量化信息匹配
                    } catch (NumberFormatException ex) {
                        this.businessQuantity = Double.NaN;
                    }
                } else {
                    /*
                        时间戳处理
                    */
                    this.timeStamp = DateTimeUtil.date2TimeStamp(fkeyField.getJsonData());
                }
                businessQuantityMatch = true;
            }
            if (businessIdentityMatch && businessQuantityMatch) {
                break;
            }
        }
        return this;
    }

    /**
     * 当是时间戳的时候，转为时间
     *
     * @param isTimeStamp 是否时间戳
     * @return this
     */
    public BusinessItemQuantityDto build(boolean isTimeStamp) {
        if (isTimeStamp) {
            this.businessQuantityStr = DateTimeUtil.timeStamp2Date(String.valueOf(this.timeStamp));
        }
        return this;
    }

}

