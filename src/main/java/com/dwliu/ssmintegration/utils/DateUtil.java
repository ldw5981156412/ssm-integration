package com.dwliu.ssmintegration.utils;

import org.joda.time.DateTime;
import org.springframework.util.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * @author dwliu
 */
public class DateUtil {

    /**
     * 转换时间（long时间根据时区转成固定格式）
     *
     * @param time
     *            秒
     * @param timeZone
     *            时区
     * @param formatType
     *            格式
     * @return
     */
    public static String secondsFormatTime(long time, int timeZone, String formatType) {
        TimeZone timeZoneTool = null;
        if (timeZone < 0) {
            timeZoneTool = TimeZone.getTimeZone("GMT" + timeZone);
        } else {
            timeZoneTool = TimeZone.getTimeZone("GMT+" + timeZone);
        }
        SimpleDateFormat outputFormat = new SimpleDateFormat(formatType);

        outputFormat.setTimeZone(timeZoneTool);

        Date date = new Date(time);

        return outputFormat.format(date);
    }

    /**
     * 指定日期加上天数后的日期
     *
     * @param day
     *            为增加的天数
     * @param SourceDate
     *            创建时间
     * @return
     * @throws ParseException
     */

    public static Date plusDay(Date SourceDate, int day) {
        return new DateTime(SourceDate).plusDays(day).toDate();
    }

    /**
     * 当前日期加上天数后的日期
     *
     * @param num
     *            为增加的天数
     * @return
     */
    public static String plusDay2(int num) {
        Date d = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String currdate = format.format(d);

        Calendar ca = Calendar.getInstance();
        // num为增加的天数，可以改变的
        ca.add(Calendar.DATE, num + 1);
        d = ca.getTime();
        return format.format(d);
    }

    /**
     * date2比date1多的天数
     *
     * @param date1
     * @param date2
     * @return
     */
    public static int differentDays(Date date1, Date date2) {
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);

        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);
        int day1 = cal1.get(Calendar.DAY_OF_YEAR);
        int day2 = cal2.get(Calendar.DAY_OF_YEAR);

        int year1 = cal1.get(Calendar.YEAR);
        int year2 = cal2.get(Calendar.YEAR);
        // 同一年
        if (year1 != year2) {
            int timeDistance = 0;
            for (int i = year1; i < year2; i++) {
                // 闰年
                if (i % 4 == 0 && i % 100 != 0 || i % 400 == 0) {
                    timeDistance += 366;
                } else // 不是闰年
                {
                    timeDistance += 365;
                }
            }

            return timeDistance + (day2 - day1);
        } else
        // 不同年
        {
            System.out.println("判断day2 - day1 : " + (day2 - day1));
            return day2 - day1;
        }
    }

    /**
     * 返回当前日期加上指定天数后的日期，时、分、秒、毫秒设置为0
     * 
     * @param num
     * @return
     */
    public static Date plusDay3(Date date, int num) {
        Calendar ca = Calendar.getInstance();
        ca.setTime(date);
        // num为增加的天数，可以改变的
        ca.add(Calendar.DATE, num);
        ca.set(ca.HOUR_OF_DAY, 0);
        ca.set(ca.MINUTE, 0);
        ca.set(ca.SECOND, 0);
        ca.set(ca.MILLISECOND, 0);
        Date d = ca.getTime();
        return d;
    }

    /**
     * 
     * 功能: 取得当前时区和零时区的偏移量<br/> 
     * 说明: <br/> 
     * 
     * @author yubin
     * @return 偏移量（小时）
     *
     */
    public static int getOffset() {
        TimeZone timeZone = TimeZone.getDefault();
        // 偏移量(毫秒)
        int rawOffset = timeZone.getRawOffset();
        // 偏移量(小时)
        int offset = rawOffset / 1000 / 60 / 60;
        return offset;
    }

    public static boolean isDateString(String datevalue, String dateFormat) {
        if (StringUtils.isEmpty(datevalue)) {
            return false;
        }
        try {
            SimpleDateFormat fmt = new SimpleDateFormat(dateFormat);
            Date dd = fmt.parse(datevalue);
            if (datevalue.equals(fmt.format(dd))) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }
}
