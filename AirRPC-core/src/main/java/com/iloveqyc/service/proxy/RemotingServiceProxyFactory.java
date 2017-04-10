package com.iloveqyc.service.proxy;

import com.iloveqyc.bean.InvokerParam;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.concurrent.ConcurrentHashMap;

/**
 * User: qiuyongchen Nicolas.David
 * Date: 2017/4/9
 * Time: 下午5:03
 * Usage: 远程服务接口的代理
 */
@Slf4j
public class RemotingServiceProxyFactory {

    private ConcurrentHashMap<InvokerParam, Object> proxys = new ConcurrentHashMap<>();

    /**
     * 获取某个接口的代理类
     * TODO 获取的代理类应当可以根据服务提供者的上下线而自动变化，目前默认仅有一个服务提供者
     */
    public Object getProxy(InvokerParam invokerParam) {
        Object serviceProxy = proxys.get(invokerParam);
        if (serviceProxy != null) {
            return serviceProxy;
        }
        ServiceInvocationHandler invocationHandler = new ServiceInvocationHandler(invokerParam);
        serviceProxy = Proxy.newProxyInstance(invokerParam.getClass().getClassLoader(),
                new Class<?>[]{invokerParam.getServiceClass()},invocationHandler);
        proxys.put(invokerParam, serviceProxy);
        return serviceProxy;
    }
}
