package top.toptimus.formula.formula;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


/**
 * 日期工具类
 *
 * @author gaoyu
 * @version 1.6.4.21 [20151229 gaoyu] <br>
 * - 根据sonar建议优化代码 <br>
 */
public class DateUtil {
    private DateUtil() {

    }


    /**
     * 按照模版解析日期
     * <p>如果_instance为空,则会创建新的日期实例;如果不为空,则将解析结果填入指定的实例.</p>
     *
     * @param value    字符串型日期
     * @param pattern  模版
     * @param instance 日期实例
     * @return 日期实例
     */
    public static Date parseDate(String value, String pattern, Date instance) { // NOSONAR
        if (value.length() < pattern.length())
            return instance;
        int year, month, day, hour, minute, second;

        //to find the year
        int index = pattern.indexOf("yyyy");
        if (index >= 0) {
            //found
            year = Integer.parseInt(value.substring(index, index + 4));
        } else {
            index = pattern.indexOf("yy");
            if (index >= 0) {
                year = Integer.parseInt(value.substring(index, index + 2));
                year += 2000;
            } else {
                return instance;
            }
        }

        //to find the month
        index = pattern.indexOf("MM");
        if (index >= 0) {
            month = Integer.parseInt(value.substring(index, index + 2));
            month -= 1;
        } else {
            return instance;
        }

        //to find the day
        index = pattern.indexOf("dd");
        if (index >= 0) {
            day = Integer.parseInt(value.substring(index, index + 2));
        } else {
            return instance;
        }

        //to find the hour
        index = pattern.indexOf("JJ");
        if (index >= 0) {
            hour = Integer.parseInt(value.substring(index, index + 2));
        } else {
            index = pattern.indexOf("HH");
            if (index >= 0) {
                hour = Integer.parseInt(value.substring(index, index + 2));
            } else {
                hour = 0;
            }
        }

        //to find the minute
        index = pattern.indexOf("mm");
        if (index >= 0) {
            minute = Integer.parseInt(value.substring(index, index + 2));
        } else {
            minute = 0;
        }

        //to find the second
        index = pattern.indexOf("ss");
        if (index >= 0) {
            second = Integer.parseInt(value.substring(index, index + 2));
        } else {
            second = 0;
        }

        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day, hour, minute, second);
        if (instance != null) {
            instance.setTime(calendar.getTimeInMillis());
            return instance;
        }
        return calendar.getTime();
    }

    /**
     * 按照模版解析日期
     *
     * @param value   字符串型的日期
     * @param pattern 模版
     * @return 日期实例
     */
    public static Date parseDate(String value, String pattern) {
        return parseDate(value, pattern, null);
    }

    /**
     * 按照缺省模版解析日期
     * <p>
     * <p>缺省模版为yyyyMMddHHmmss</p>
     *
     * @param value 字符串型的日期
     * @return 日期实例
     */
    public static Date parseDate(String value) {
        return parseDate(value, "yyyyMMddHHmmss");
    }


    /**
     * 按照模板格式化日期
     *
     * @param date    日期的实例
     * @param pattern 模板
     * @return 格式化结果
     */
    public static String formatDate(Date date, String pattern) {
        if (date == null)
            return "";

        SimpleDateFormat formatter = new SimpleDateFormat(pattern);
        return formatter.format(date);
    }

    /**
     * 将制定字符串型日期按照模板转化为基于其他模板的字符串型日期
     *
     * @param value     待转换的字符串型日期
     * @param inFormat  原始的模板
     * @param outFormat 新的模板
     * @return 转化为的结果
     */
    public static String transform(String value, String inFormat, String outFormat) {
        Date date = parseDate(value, inFormat);
        if (date == null)
            return "";
        return formatDate(date, outFormat);
    }

    /**
     * 按指定日期单位计算两个日期间的间隔
     *
     * @param timeInterval
     * @param date1
     * @param date2
     * @return
     */
    public static long dateDiff(String timeInterval, Date date1, Date date2) {
        if (timeInterval.equals("year")) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date1);
            int time = calendar.get(Calendar.YEAR);
            calendar.setTime(date2);
            return time - calendar.get(Calendar.YEAR);
        }

        if (timeInterval.equals("quarter")) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date1);
            int time = calendar.get(Calendar.YEAR) * 4;
            calendar.setTime(date2);
            time -= calendar.get(Calendar.YEAR) * 4;
            calendar.setTime(date1);
            time += calendar.get(Calendar.MONTH) / 4;
            calendar.setTime(date2);
            return time - calendar.get(Calendar.MONTH) / 4;
        }

        if (timeInterval.equals("month")) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date1);
            int time = calendar.get(Calendar.YEAR) * 12;
            calendar.setTime(date2);
            time -= calendar.get(Calendar.YEAR) * 12;
            calendar.setTime(date1);
            time += calendar.get(Calendar.MONTH);
            calendar.setTime(date2);
            return time - calendar.get(Calendar.MONTH);
        }

        if (timeInterval.equals("week")) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date1);
            int time = calendar.get(Calendar.YEAR) * 52;
            calendar.setTime(date2);
            time -= calendar.get(Calendar.YEAR) * 52;
            calendar.setTime(date1);
            time += calendar.get(Calendar.WEEK_OF_YEAR);
            calendar.setTime(date2);
            return time - calendar.get(Calendar.WEEK_OF_YEAR);
        }

        if (timeInterval.equals("day")) {
            long time = date1.getTime() / 1000 / 60 / 60 / 24;
            return time - date2.getTime() / 1000 / 60 / 60 / 24;
        }

        if (timeInterval.equals("hour")) {
            long time = date1.getTime() / 1000 / 60 / 60;
            return time - date2.getTime() / 1000 / 60 / 60;
        }

        if (timeInterval.equals("minute")) {
            long time = date1.getTime() / 1000 / 60;
            return time - date2.getTime() / 1000 / 60;
        }

        if (timeInterval.equals("second")) {
            long time = date1.getTime() / 1000;
            return time - date2.getTime() / 1000;
        }

        return date1.getTime() - date2.getTime();
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
}
