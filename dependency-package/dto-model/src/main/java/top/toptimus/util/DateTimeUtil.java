package top.toptimus.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author gaoyu
 */
public class DateTimeUtil {
    /**
     * 时间戳转换成日期格式字符串
     *
     * @param seconds 精确到秒的字符串
     * @return 时间字符串
     */
    public static String timeStamp2Date(String seconds) {
        if (seconds == null || seconds.isEmpty() || seconds.equals("null")) {
            return "";
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new Date(Long.valueOf(seconds + "000")));
    }

    /**
     * 日期格式字符串转换成时间戳
     *
     * @param date_str 字符串日期
     * @return 时间戳
     */
    public static long date2TimeStamp(String date_str) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return sdf.parse(date_str).getTime() / 1000;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("时间转换异常");
        }
    }

    private static long date2TimeStamp(Date date_str) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return sdf.parse(sdf.format(date_str)).getTime() / 1000;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("时间转换异常");
        }
    }

    public static long currentTimeStamp() {
        return DateTimeUtil.date2TimeStamp(new Date());
    }
}
