package com.iloveqyc.zookeeper.registry.listener;

import com.iloveqyc.bean.ServerParam;

import java.util.List;

/**
 * User: qiuyongchen Nicolas.David
 * Date: 2017/4/15
 * Time: 下午2:52
 * Usage: 监听服务注册中心的事件
 *        如：新增了服务提供者
 *           注销了服务提供者
 */
public interface RegistryEventListener {

    /**
     * 某个服务的服务提供者发生了变化（即其提供者数量增多或减少）
     * @param serviceName
     * @param hosts
     */
    void onServiceChanged(String serviceName, List<ServerParam> hosts);
}
