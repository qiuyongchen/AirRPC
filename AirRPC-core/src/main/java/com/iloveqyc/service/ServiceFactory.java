package com.iloveqyc.service;

import com.iloveqyc.bean.InvokerParam;
import com.iloveqyc.bean.ProviderParam;
import com.iloveqyc.bean.ServerParam;
import com.iloveqyc.service.proxy.RemotingServiceProxyFactory;

import java.util.List;

/**
 * User: qiuyongchen Nicolas.David
 * Date: 2017/4/9
 * Time: 上午4:11
 * Usage: xxx
 */
public class ServiceFactory {

    private static RemotingServiceProxyFactory remotingServiceProxyFactory;

    static {
        remotingServiceProxyFactory = new RemotingServiceProxyFactory();
    }

    public static Object getService(String iface, String serviceName, Class<?> serviceClass) {
        InvokerParam invokerParam = new InvokerParam();
        invokerParam.setIface(iface);
        invokerParam.setServiceName(serviceName);
        invokerParam.setServiceClass(serviceClass);
        return remotingServiceProxyFactory.getProxy(invokerParam);
    }

    public static void addServices(List<ProviderParam> providerParams, ServerParam serverParam) {
    }
}
