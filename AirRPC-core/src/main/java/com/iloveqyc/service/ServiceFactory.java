package com.iloveqyc.service;

import com.iloveqyc.bean.InvokerParam;
import com.iloveqyc.bean.ProviderParam;
import com.iloveqyc.bean.ServerParam;
import com.iloveqyc.invoker.AirClient;
import com.iloveqyc.invoker.AirClientFactory;
import com.iloveqyc.provider.AirServerFactory;
import com.iloveqyc.service.invoker.ClientManager;
import com.iloveqyc.service.proxy.RemotingServiceProxyFactory;
import com.iloveqyc.zookeeper.registry.ZookeeperRegistry;
import com.iloveqyc.zookeeper.registry.ZookeeperRegistryFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;

import java.util.List;

/**
 * User: qiuyongchen Nicolas.David
 * Date: 2017/4/9
 * Time: 上午4:11
 * Usage: xxx
 */
@Slf4j
public class ServiceFactory {

    private static RemotingServiceProxyFactory remotingServiceProxyFactory;

    static {
        remotingServiceProxyFactory = new RemotingServiceProxyFactory();
    }

    public static Object getService(String iface, String serviceName, Class<?> serviceClass) {
        // 初始化服务提供端
        List<ServerParam> serverParams = ZookeeperRegistryFactory.getZkRegistry().
                getServiceProvider(serviceName);
        if (CollectionUtils.isEmpty(serverParams)) {
            throw new RuntimeException("no provider for service:" + serviceName);
        }
        for (ServerParam serverParam : serverParams) {
            ClientManager.getAirClient(serverParam);
        }

        InvokerParam invokerParam = new InvokerParam();
        invokerParam.setIface(iface);
        invokerParam.setServiceName(serviceName);
        invokerParam.setServiceClass(serviceClass);
        return remotingServiceProxyFactory.getProxy(invokerParam);
    }

    public static void addServices(List<ProviderParam> providerParams, ServerParam serverParam) {
        // 启动netty服务端监听请求
        startServer(providerParams, serverParam);

        // 将服务发布到zookeeper上
        publishServices(providerParams, serverParam);

    }

    private static void startServer(List<ProviderParam> providerParams, ServerParam serverParam) {
        AirServerFactory.getAirServer().active(providerParams, serverParam);
    }

    private static void publishServices(List<ProviderParam> providerParams, ServerParam serverParam) {
        for (ProviderParam providerParam : providerParams) {
            ZookeeperRegistryFactory.getZkRegistry().registerService(
                    providerParam.getServiceName(), serverParam);
        }
    }
}
