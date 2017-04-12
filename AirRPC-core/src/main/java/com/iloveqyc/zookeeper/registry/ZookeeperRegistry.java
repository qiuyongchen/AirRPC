package com.iloveqyc.zookeeper.registry;

import com.google.common.collect.Lists;
import com.iloveqyc.bean.ServerParam;
import com.iloveqyc.constants.PropertyConstant;
import com.iloveqyc.zookeeper.client.ZookeeperClient;
import io.netty.util.internal.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.Collections;
import java.util.List;

/**
 * User: qiuyongchen Nicolas.David
 * Date: 2017/4/12
 * Time: 下午8:50
 * Usage: xxx
 */
@Slf4j
public class ZookeeperRegistry {

    private ZookeeperClient client;

    private boolean isInit;

    public void init(String zkAddress) {
        if (!isInit) {
            synchronized (ZookeeperRegistry.class) {
                if (!isInit) {
                    client = new ZookeeperClient(zkAddress);
                    isInit = true;
                }
            }
        }
    }

    public void registerService(String serviceName, ServerParam param) {
        String serviceAddress = param.getIp() + ":" + param.getPort();

        String zkRootNodePath = PropertyConstant.ZK_PATH;
        if (!client.exits(zkRootNodePath)) {
            client.createPersPath(zkRootNodePath);
        }

        String zkServicePath = zkRootNodePath + "/" + serviceName;
        if (!client.exits(zkServicePath)) {
            client.creteEphNode(zkServicePath, serviceAddress);
            log.info("zk的服务列表不存在，新建一个临时节点用于存在服务列表, serviceName:{}, new value:{}",
                    serviceName, serviceAddress);
        } else {
            String allAddress = client.get(zkServicePath);
            if (allAddress != null && allAddress.length() > 0) {
                String[] addresses = allAddress.split(" ");
                List<String> addressList = Lists.newArrayList();
                for (String addr : addresses) {
                    if (!addressList.contains(addr)) {
                        addressList.add(addr);
                    }
                }
                if (!addressList.contains(serviceAddress)) {
                    addressList.add(serviceAddress);
                    Collections.sort(addressList);
                    client.set(zkServicePath, StringUtils.join(addressList, " "));
                    log.info("重新设置zk的服务列表, serviceName:{}, old value:{}, new value:{}",
                            serviceName, allAddress, StringUtils.join(addressList, " "));
                }
            }
        }
    }
}
