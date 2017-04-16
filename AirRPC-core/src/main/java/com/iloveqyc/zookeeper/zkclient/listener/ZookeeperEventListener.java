package com.iloveqyc.zookeeper.zkclient.listener;

import com.iloveqyc.bean.ServerParam;
import com.iloveqyc.constants.PropertyConstant;
import com.iloveqyc.zookeeper.zkclient.ZookeeperClient;
import com.iloveqyc.zookeeper.registry.listener.RegistryEventListener;
import com.iloveqyc.zookeeper.registry.listener.impl.DefaultRegistryEventListener;
import com.iloveqyc.zookeeper.util.HostUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.api.CuratorEvent;
import org.apache.curator.framework.api.CuratorListener;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;

import java.util.List;

/**
 * User: qiuyongchen Nicolas.David
 * Date: 2017/4/12
 * Time: 下午9:03
 * Usage: 监听zk上的数据变化（比如服务节点新增/删除）
 */
@Slf4j
public class ZookeeperEventListener implements CuratorListener {

    private ZookeeperClient client;

    private RegistryEventListener registryEventListener = new DefaultRegistryEventListener();

    public ZookeeperEventListener(ZookeeperClient client) {
        this.client = client;
    }

    @Override
    public void eventReceived(CuratorFramework curatorFramework, CuratorEvent curatorEvent) throws Exception {
        log.info("zk上的数据发生了变化！curatorFramework:{}, curatorEvent:{}", curatorFramework, curatorEvent);
        WatchedEvent event = curatorEvent.getWatchedEvent();
        if (event.getType() == Watcher.Event.EventType.NodeCreated ||
                event.getType() == Watcher.Event.EventType.NodeDeleted ||
                event.getType() == Watcher.Event.EventType.NodeDataChanged ||
                event.getType() == Watcher.Event.EventType.NodeChildrenChanged) {
            return;
        }
        handleNodeEvent(event);
    }

    private void handleNodeEvent(WatchedEvent event) {
        String changedPath = event.getPath();
        if (changedPath.startsWith(PropertyConstant.ZK_PATH)) {
            String serviceName = changedPath.substring(PropertyConstant.ZK_PATH.length() + 1);
            client.get(changedPath);
            List<ServerParam> hosts = HostUtil.buildChangeHostSet(client, changedPath);
            registryEventListener.onServiceChanged(serviceName, hosts);

        }
    }

}
