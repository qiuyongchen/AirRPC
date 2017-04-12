package com.iloveqyc.zookeeper.client;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.api.CuratorEvent;
import org.apache.curator.framework.api.CuratorListener;

/**
 * User: qiuyongchen Nicolas.David
 * Date: 2017/4/12
 * Time: 下午9:03
 * Usage: 监听zk上的数据变化（比如服务新增/删除）
 */
public class ZookeeperListener implements CuratorListener {
    @Override
    public void eventReceived(CuratorFramework curatorFramework, CuratorEvent curatorEvent) throws Exception {

    }
}
