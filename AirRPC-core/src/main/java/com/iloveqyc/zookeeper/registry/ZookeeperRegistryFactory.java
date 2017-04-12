package com.iloveqyc.zookeeper.registry;

import com.iloveqyc.constants.PropertyConstant;
import com.iloveqyc.utils.ConfigLoader;

/**
 * User: qiuyongchen Nicolas.David
 * Date: 2017/4/12
 * Time: 下午10:50
 * Usage: xxx
 */
public class ZookeeperRegistryFactory {
    private static ZookeeperRegistry zkRegistry;

    static {
        zkRegistry = new ZookeeperRegistry();
        zkRegistry.init(ConfigLoader.loadPropertyByKey(PropertyConstant.ZKSERVER));
    }

    public static ZookeeperRegistry getZkRegistry() {
        return zkRegistry;
    }
}
