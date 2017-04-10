package com.iloveqyc.test;

import com.iloveqyc.bean.ProviderParam;
import com.iloveqyc.bean.ServerParam;
import com.iloveqyc.provider.AirServer;

import java.util.ArrayList;
import java.util.List;

/**
 * User: qiuyongchen Nicolas.David
 * Date: 2017/4/9
 * Time: 上午1:39
 * Usage: xxx
 */
public class ServerContainer {
    AirServer airServer;

    public ServerContainer() {
        ServerParam serverParam = new ServerParam("192.168.100.104", "4080");
        airServer = new AirServer();
        ProviderParam param = new ProviderParam();
        param.setServiceName("http://www.iloveqyc.com/service/iLoveYouService_1.0.0");
        List<ProviderParam> providerParams = new ArrayList<ProviderParam>();
        providerParams.add(param);
        airServer.active(providerParams, serverParam);
    }
}
