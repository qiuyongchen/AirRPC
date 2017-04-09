package com.iloveqyc.service.proxy;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * User: qiuyongchen Nicolas.David
 * Date: 2017/4/9
 * Time: 下午9:42
 * Usage: xxx
 */
@Slf4j
public class ServiceInvocationHandler implements InvocationHandler {

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        log.info("hi");
        return "你调用了" + method.getName() + "方法，其参数为：" + Arrays.toString(args);
    }

}
