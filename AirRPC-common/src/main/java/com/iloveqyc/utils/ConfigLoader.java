package com.iloveqyc.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

/**
 * User: qiuyongchen Nicolas.David
 * Date: 2017/4/12
 * Time: 下午8:12
 * Usage: 读取本地的配置
 */
@Slf4j
public class ConfigLoader {

    private static final String APPENV = "/data/webapps/appenv";

    private static Map<String, String> properties = new ConcurrentHashMap<>();

    static {
        Properties pro = getPropertyFromFile(APPENV);
        for (Object o : pro.keySet()) {
            String key = o.toString();
            properties.put(key, pro.getProperty(key));
        }
    }

    private ConfigLoader() {
    }

    public static String loadPropertyByKey(String key) {
        return properties.get(key);
    }

    private static Properties getPropertyFromFile(String fileName) {
        InputStream is = null;
        try {
            is = new FileInputStream(fileName);
        } catch (FileNotFoundException e) {
            log.error("", e);
        }
        Properties properties = new Properties();
        BufferedReader br = null;
        if (is != null) {
            try {
                br = new BufferedReader(new InputStreamReader(is, "utf-8"));
                String line = null;
                while ((line = br.readLine()) != null) {
                    int idx = line.indexOf("=");
                    if (idx != -1) {
                        String key = line.substring(0, idx);
                        String value = line.substring(idx + 1);
                        properties.put(key.trim(), value.trim());
                    }
                }
            } catch (Throwable e) {
                e.printStackTrace();
            } finally {
                if (br != null) {
                    try {
                        br.close();
                    } catch (IOException e) {
                    }
                }
            }
        }
        return properties;
    }
}
