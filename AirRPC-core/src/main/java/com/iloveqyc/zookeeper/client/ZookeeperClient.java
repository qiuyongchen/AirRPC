package com.iloveqyc.zookeeper.client;

import io.netty.util.concurrent.DefaultThreadFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.ensemble.EnsembleProvider;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * User: qiuyongchen Nicolas.David
 * Date: 2017/4/12
 * Time: 下午8:52
 * Usage: xxx
 */
@Slf4j
public class ZookeeperClient {

    private static ExecutorService listenerPool = Executors.newCachedThreadPool(
            new DefaultThreadFactory("zookeeper-listener-event-pool")
    );
    private String zkAddress = "";
    private CuratorFramework client = null;

    public ZookeeperClient(final String zkAddress) {
        this.zkAddress = zkAddress;
        // 构建zk客户端
        CuratorFramework client = CuratorFrameworkFactory.builder()
                .sessionTimeoutMs(30 * 1000)
                .connectionTimeoutMs(15 * 1000)
                .retryPolicy(new ExponentialBackoffRetry(1000, Integer.MAX_VALUE))
                .ensembleProvider(new EnsembleProvider() {
                    @Override
                    public void start() throws Exception {
                    }

                    @Override
                    public String getConnectionString() {
                        return zkAddress;
                    }

                    @Override
                    public void close() throws IOException {
                    }
                })
                .build();
        // 添加zk监听器
        client.getCuratorListenable().addListener(new ZookeeperListener(), listenerPool);
        // 启动zk客户端
        client.start();
        try {
            client.getZookeeperClient().blockUntilConnectedOrTimedOut();
        } catch (InterruptedException e) {
            log.error("connect to zookeeper fail", e);
        }
        this.client = client;
    }

    public boolean exits(String path) {
        Stat stat;
        try {
            stat = client.checkExists().watched().forPath(path);
        } catch (Exception e) {
            log.error("checkExists fail, path:{}", path, e);
            return false;
        }
        return stat != null;
    }

    public String get(String path) {
        try {
            if (client.checkExists().forPath(path) == null) {
                return null;
            }
            Stat stat = new Stat();
            byte[] valueBytes = client.getData().storingStatIn(stat).forPath(path);
            return new String(valueBytes, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void set(String path, String value) {
        try {
            if (client.checkExists().forPath(path) == null) {
                return;
            }
            client.setData().forPath(path, value.toString().getBytes("UTF-8"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void creteEphNode(String path, String value) {
        try {
            byte[] valueBytes = value.toString().getBytes("UTF-8");
            String ephNode = client.create().withMode(CreateMode.EPHEMERAL).forPath(path, valueBytes);
            log.info("create eph node:{}", ephNode);
        } catch (Exception e) {
            log.error("fail to create ephemeral node, path:{}, value:{}", path, value);
        }
    }

    public void createPersPath(String path) {
        Stat stat;
        try {
            stat = client.checkExists().forPath(path);
            // 存在该节点，无需重复创建
            if (stat != null) {
                return;
            }
            client.create().creatingParentsIfNeeded().forPath(path);
        } catch (Exception e) {
            log.error("create persistent path fail, path:{}", path, e);
        }
    }

    public void delete(String path) {
        try {
            client.delete().forPath(path);
        } catch (Exception e) {
            log.error("delete path fail, path:{}", path, e);
        }
    }
}
