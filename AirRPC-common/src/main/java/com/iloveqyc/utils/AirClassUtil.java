package com.iloveqyc.utils;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ClassUtils;

/**
 * User: qiuyongchen Nicolas.David
 * Date: 2017/4/9
 * Time: 上午4:01
 * Usage:
 */
@Data
@Slf4j
public class AirClassUtil {

    public static Class<?> getClass(String className) {
        try {
            return ClassUtils.getClass(Thread.currentThread().getContextClassLoader(), className);
        } catch (ClassNotFoundException e) {
            log.error("", e);
        }
        return null;
    }
}
