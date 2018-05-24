package com.dwliu.ssmintegration.utils;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * @author dwliu
 */
public class DateUtils {
    /**
     * 日期转毫
     *
     * @param formatDate
     *            格式比如:yyyy-MM-dd
     * @param date
     * @return long 毫秒
     */
    public static final long stringDate2Long(String formatDate, String date) {
        return string2Date(formatDate, date).getTime();
    }

    /**
     * 将字符串表示的时间，转换成timestamp对象
     *
     * @param formatDate
     *            时间格式
     * @param strDate
     *            字符串表示的时间，该时间的格式必须与formatDate一致
     * @return 转换好的时间戳对象
     */
    public static final Timestamp stringDate2Timestamp(String formatDate, String strDate) {
        Date date = string2Date(formatDate, strDate);
        return new Timestamp(date.getTime());
    }

    /**
     * 日期转毫秒
     *
     * @param formatDate
     *            格式比如:yyyy-MM-dd
     * @param date
     *            需要转换的时间
     * @return Date 日期对象
     */
    public static final Date string2Date(String formatDate, String date) {
        SimpleDateFormat sdf = new SimpleDateFormat(formatDate);
        sdf.setTimeZone(TimeZoneUtil.getDefaultTimeZone());
        try {
            return sdf.parse(date);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 将时间格式化为字符串
     *
     * @param date
     *            被格式化的时间对象
     * @param format
     *            时间格式模板
     * @return 格式后的字符串
     */
    public static final String date2String(Date date, String format) {
        return date2String(date, format, TimeZoneUtil.getDefaultTimeZone());
    }

    /**
     * 将时间格式化为字符串
     *
     * @param date
     *            被格式化的时间对象
     * @param format
     *            标准的JDK提供的格式
     * @param timeZone
     *            时区 ，根据不同的时区，转换成不同时区的时间
     * @return 格式后的字符串
     */
    public static final String date2String(Date date, String format, TimeZone timeZone) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        sdf.setTimeZone(timeZone);
        return sdf.format(date);
    }

    /**
     * 将时间格式化为字符串
     *
     * @param date
     *            被格式化的时间对象
     * @param pattern
     *            时间格式模板
     * @return 格式后的字符串
     */
    public static final String date2String(Date date, DatePattern pattern) {
        return date2String(date, pattern.getValue());
    }

    /**
     * 将时间格式化为字符串
     *
     * @param date
     *            被格式化的时间对象
     * @param pattern
     *            时间格式模板
     * @param timeZone
     *            时区 ，根据不同的时区，转换成不同时区的时间
     * @return 格式后的字符串
     */
    public static final String date2String(Date date, DatePattern pattern, TimeZone timeZone) {
        return date2String(date, pattern.getValue(), timeZone);
    }

    public enum DatePattern {
        /**
         * yyyyMMddHHmmss
         */
        yyyyMMddHHmmss("yyyyMMddHHmmss"),
        /**
         * yyyy年MM月dd日
         */
        ymr("yyyy年MM月dd日"),
        /**
         * yyyy-MM-dd HH:mm:ss
         */
        yyyyMMddHHmmssLine("yyyy-MM-dd HH:mm:ss"),
        /**
         * yyyy-MM-dd
         */
        yyyyMMdd("yyyy-MM-dd"),
        /**
         * yyyy/MM/dd
         */
        ymrSlash("yyyy/MM/dd"),
        /**
         * HH:mm:ss
         */
        hmsSlash("HH:mm:ss"),
        /**
         * yyyy/MM/dd HH:mm:ss
         */
        yyyyMMddHHmmssSlash("yyyy/MM/dd HH:mm:ss"),
        /**
         * yyyy-MM-dd HH
         */
        yyyyMMddHH("yyyy-MM-dd HH"),
        /**
         * yyyy-MM-dd HH:mm
         */
        yyyyMMddHHmm("yyyy-MM-dd HH:mm");

        private final String value;

        DatePattern(String value) {
            this.value = value;
        }

        public String getValue() {
            return this.value;
        }
    }

}
