package com.iloveqyc.zookeeper.client.listener;

import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.api.CuratorEvent;
import org.apache.curator.framework.api.CuratorListener;

/**
 * User: qiuyongchen Nicolas.David
 * Date: 2017/4/12
 * Time: 下午9:03
 * Usage: 监听zk上的数据变化（比如服务节点新增/删除）
 */
@Slf4j
public class ZookeeperEventListener implements CuratorListener {
    @Override
    public void eventReceived(CuratorFramework curatorFramework, CuratorEvent curatorEvent) throws Exception {
        log.info("zk上的数据发生了变化！curatorFramework:{}, curatorEvent:{}", curatorFramework, curatorEvent);
    }
}
