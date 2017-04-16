package com.iloveqyc.zookeeper.registry.listener.impl;

import com.iloveqyc.bean.ServerParam;
import com.iloveqyc.zookeeper.registry.ZookeeperRegistryFactory;
import com.iloveqyc.zookeeper.registry.listener.ServiceChangeListener;

/**
 * User: qiuyongchen Nicolas.David
 * Date: 2017/4/16
 * Time: 下午4:03
 * Usage: xxx
 */
public class DefaultServiceChangeListener implements ServiceChangeListener {

    /**
     * 某一个服务增加一个服务提供者
     *
     * @param serviceName
     * @param param
     */
    @Override
    public void serviceHostAdd(String serviceName, ServerParam param) {
        ZookeeperRegistryFactory.getZkRegistry().addLocalCacheServiceProvider(serviceName, param);
    }

    /**
     * 某一个服务减少一个服务提供者
     *
     * @param serviceName
     * @param param
     */
    @Override
    public void serviceHostDelete(String serviceName, ServerParam param) {
        ZookeeperRegistryFactory.getZkRegistry().deleteLocalCacheServiceProvider(serviceName, param);
    }
}
