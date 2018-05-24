package com.dwliu.ssmintegration.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;


/**
 * @author dwliu
 */
public class PropertiesUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(PropertiesUtils.class);

    public final static Map<String, String> resources = new HashMap();
    private static final PropertiesUtils instance = new PropertiesUtils();

    public static PropertiesUtils getInstance() {
        return instance;
    }

    static {
        Properties properties = new Properties();
        InputStream in = null;
        try {
            String path = "resources.properties";
            in = Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
            properties.load(in);
            String templete = properties.getProperty("templete");
            resources.put("templete", templete);
        } catch (IOException e) {
            LOGGER.error("读取配置文件出错：{}",e);
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                LOGGER.error("关闭文件流IO异常", e);
            }
        }
    }


}
