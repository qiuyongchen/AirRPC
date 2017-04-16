package com.iloveqyc.zookeeper.util;

import com.google.common.collect.Lists;
import com.iloveqyc.bean.ServerParam;
import com.iloveqyc.zookeeper.zkclient.ZookeeperClient;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;

/**
 * User: qiuyongchen Nicolas.David
 * Date: 2017/4/16
 * Time: 下午2:57
 * Usage: xxx
 */
public class HostUtil {

    /**
     * 获取某个服务的所有提供者
     * @param client
     * @param changedPath
     * @return
     */
    public static List<ServerParam> buildChangeHostSet(ZookeeperClient client, String changedPath) {
        String hostStr = client.get(changedPath);
        if (StringUtils.isEmpty(hostStr)) {
            return Lists.newArrayList();
        }

        // 解析服务提供者的ip和port
        String[] addrArray = hostStr.split(" ");
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
