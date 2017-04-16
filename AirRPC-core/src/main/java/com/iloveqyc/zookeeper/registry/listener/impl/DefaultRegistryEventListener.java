package com.iloveqyc.zookeeper.registry.listener.impl;

import com.google.common.collect.Lists;
import com.iloveqyc.bean.ServerParam;
import com.iloveqyc.zookeeper.registry.ZookeeperRegistryFactory;
import com.iloveqyc.zookeeper.registry.listener.RegistryEventListener;
import com.iloveqyc.zookeeper.registry.listener.ServiceChangeListener;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;

import java.util.List;

/**
 * User: qiuyongchen Nicolas.David
 * Date: 2017/4/16
 * Time: 下午2:52
 * Usage: xxx
 */
@Slf4j
public class DefaultRegistryEventListener implements RegistryEventListener {

    private static List<ServiceChangeListener> serviceListeners = Lists.newArrayList();

    public static void addServiceListener(ServiceChangeListener listener) {
        if (!serviceListeners.contains(listener)) {
            log.info("add service listener:{}", listener);
            serviceListeners.add(listener);
        }
    }

    /**
     * 某个服务的服务提供者发生了变化（即其提供者数量增多或减少）
     *
     * @param serviceName
     * @param hosts
     */
    @Override
    public void onServiceChanged(String serviceName, List<ServerParam> hosts) {
        List<ServerParam> oldServerHosts = ZookeeperRegistryFactory.getZkRegistry().
                getLocalCacheServiceProvider(serviceName);

        // 新增了服务提供者
        List<ServerParam> toAddHosts = Lists.newArrayList(hosts);
        toAddHosts.removeAll(oldServerHosts);

        // 删除了服务提供者
        List<ServerParam> toDeleteHost = Lists.newArrayList(oldServerHosts);
        toDeleteHost.removeAll(hosts);

        for (ServiceChangeListener listener : serviceListeners) {
            if (CollectionUtils.isNotEmpty(toAddHosts)) {
                for (ServerParam host : toAddHosts) {
                    listener.serviceHostAdd(serviceName, host);
                }
            }
            if (CollectionUtils.isNotEmpty(toDeleteHost)) {
                for (ServerParam host : toDeleteHost) {
                    listener.serviceHostDelete(serviceName, host);
                }
            }
        }

    }
}
