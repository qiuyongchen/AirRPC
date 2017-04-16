package com.iloveqyc.zookeeper.registry.listener.impl;

import com.iloveqyc.bean.ServerParam;
import com.iloveqyc.zookeeper.registry.listener.RegistryEventListener;

import java.util.List;

/**
 * User: qiuyongchen Nicolas.David
 * Date: 2017/4/16
 * Time: 下午2:52
 * Usage: xxx
 */
public class DefaultRegistryEventListener implements RegistryEventListener {

    /**
     * 某个服务的服务提供者发生了变化（即其提供者数量增多或减少）
     *
     * @param serviceName
     * @param hosts
     */
    @Override
    public void onServiceChanged(String serviceName, List<ServerParam> hosts) {

    }
}
