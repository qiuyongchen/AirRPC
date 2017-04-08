package com.iloveqyc.test;

import com.iloveqyc.bean.ServerParam;
import com.iloveqyc.provider.AirServerManager;

/**
 * User: qiuyongchen Nicolas.David
 * Date: 2017/4/9
 * Time: 上午1:39
 * Usage: xxx
 */
public class ServerContainer {
    AirServerManager airServerManager;

    public ServerContainer() {
        ServerParam serverParam = new ServerParam();
        serverParam.setIp("192.168.100.112");
        serverParam.setPort("4080");
        airServerManager = new AirServerManager();
        airServerManager.active(serverParam);
    }
}
