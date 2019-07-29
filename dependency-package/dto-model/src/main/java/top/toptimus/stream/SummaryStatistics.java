package top.toptimus.stream;

import lombok.Getter;
import lombok.NoArgsConstructor;
import top.toptimus.common.enums.SummaryTypeEnum;
import top.toptimus.util.DateTimeUtil;

import java.util.DoubleSummaryStatistics;
import java.util.LongSummaryStatistics;

/**
 * 用来转化DoubleSummaryStatistics和LongSummaryStatistics
 *
 * @author gaoyu
 */
@NoArgsConstructor
@Getter
public class SummaryStatistics {
    private String count;
    private String sum;
    private String min;
    private String max;
    private String average;

    /**
     * 浮点数计算
     *
     * @param doubleSummaryStatistics 统计数据
     * @return 字符统计数据
     */
    public SummaryStatistics build(DoubleSummaryStatistics doubleSummaryStatistics) {
        this.count = String.valueOf(doubleSummaryStatistics.getCount());
        this.sum = String.valueOf(doubleSummaryStatistics.getSum());
        this.min = String.valueOf(doubleSummaryStatistics.getMin());
        this.max = String.valueOf(doubleSummaryStatistics.getMax());
        this.average = String.valueOf(doubleSummaryStatistics.getAverage());
        return this;
    }

    /**
     * 时间戳转时间
     *
     * @param timeStampSummaryStatistics 统计数据
     * @return 字符统计数据
     */
    public SummaryStatistics build(LongSummaryStatistics timeStampSummaryStatistics) {
        this.count = String.valueOf(timeStampSummaryStatistics.getCount());
        this.min = DateTimeUtil.timeStamp2Date(String.valueOf(timeStampSummaryStatistics.getMin()));
        this.max = DateTimeUtil.timeStamp2Date(String.valueOf(timeStampSummaryStatistics.getMax()));
        return this;
    }

    /**
     * 根据SummaryTypeEnum取得指定值
     *
     * @param summaryTypeEnum
     * @return 值
     */
    public String getSummaryValue(SummaryTypeEnum summaryTypeEnum) {
        switch (summaryTypeEnum) {
            case COUNT:
                return getCount();
            case SUM:
                return getSum();
            case MAX:
                return getMax();
            case MIN:
                return getMin();
            case AVERAGR:
                return getAverage();
        }
        return "";
    }
}
