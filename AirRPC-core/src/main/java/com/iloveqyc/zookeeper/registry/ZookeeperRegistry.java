package com.iloveqyc.zookeeper.registry;

import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import com.iloveqyc.bean.ServerParam;
import com.iloveqyc.constants.PropertyConstant;
import com.iloveqyc.zookeeper.client.ZookeeperClient;
import io.netty.util.internal.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * User: qiuyongchen Nicolas.David
 * Date: 2017/4/12
 * Time: 下午8:50
 * Usage: 服务注册中心（Zookeeper版）
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

    /**
     * 将服务注册到服务注册中心
     * @param serviceName 服务名称
     * @param param 服务提供者的ip端口参数
     */
    public void registerService(String serviceName, ServerParam param) {
        String serviceAddress = param.getIp() + ":" + param.getPort();

        String zkRootNodePath = PropertyConstant.ZK_PATH;
        if (!client.exits(zkRootNodePath)) {
            client.createPersPath(zkRootNodePath);
        }

        String zkServicePath = zkRootNodePath + "/" + serviceName;
        if (!client.exits(zkServicePath)) {
            client.creteEphNode(zkServicePath, serviceAddress);
            String data = client.get(zkServicePath);
            log.info("zk的服务列表不存在，新建一个临时节点用于存在服务列表, serviceName:{}, new value:{}, data:{}",
                    serviceName, serviceAddress, data);
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

    /**
     * 获取服务的服务提供者
     * @param serviceName 服务名称
     * @return 服务提供者的ip端口参数
     */
    public List<ServerParam> getServiceProvider(String serviceName) {
        // 从zk上获取服务的所有提供者地址
        String zkServicePath = PropertyConstant.ZK_PATH + "/" + serviceName;
        String serviceAddrs = client.get(zkServicePath);
        if (StringUtils.isEmpty(serviceAddrs)) {
            return Lists.newArrayList();
        }

        // 解析服务提供者的ip和port
        String[] addrArray = serviceAddrs.split(" ");
        List<String> addrList = Lists.newArrayList();
        addrList.addAll(Arrays.asList(addrArray));
        List<ServerParam> serviceProvider = Lists.newArrayList();
        for (String addr : addrList) {
            String[] ipPort = addr.split(":");
            ServerParam param = new ServerParam(ipPort[0], ipPort[1]);
            if (!serviceProvider.contains(param)) {
                serviceProvider.add(param);
            }
        }
        return serviceProvider;
    }
}
