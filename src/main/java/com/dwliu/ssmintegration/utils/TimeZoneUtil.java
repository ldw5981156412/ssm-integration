package com.dwliu.ssmintegration.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.TimeZone;

/**
 * @author dwliu
 */
public class TimeZoneUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(TimeZoneUtil.class);

    private static final TimeZone defaultTimeZone = TimeZone.getDefault();


    /**
     * 设置默认时区，改为手动调用，避免该段代码对其他程序造成的过渡影响
     */
    public static void initGMTZone(){
        TimeZone timeZone = TimeZone.getTimeZone("GMT");
        TimeZone.setDefault(timeZone);
        LOGGER.debug("设置默认时区");
    }

    /**
     * 获取系统默认的时区
     * @return
     */
    public static TimeZone getDefaultTimeZone(){
        return defaultTimeZone;
    }
}
