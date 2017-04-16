package com.iloveqyc.zookeeper.registry.listener;

import com.iloveqyc.bean.ServerParam;

/**
 * User: qiuyongchen Nicolas.David
 * Date: 2017/4/16
 * Time: 下午4:00
 * Usage:
 */
public interface ServiceChangeListener {

    /**
     * 某一个服务增加一个服务提供者
     * @param serviceName
     * @param param
     */
    void serviceHostAdd(String serviceName, ServerParam param);

    /**
     * 某一个服务减少一个服务提供者
     * @param serviceName
     * @param param
     */
    void serviceHostDelete(String serviceName, ServerParam param);

}
