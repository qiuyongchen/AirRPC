package com.iloveqyc.service.invoker;

import com.iloveqyc.bean.ServerParam;
import com.iloveqyc.invoker.AirClient;
import com.iloveqyc.invoker.AirClientFactory;
import com.iloveqyc.service.invoker.heartbeat.HeartBeatTask;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * User: qiuyongchen Nicolas.David
 * Date: 2017/4/17
 * Time: 下午12:01
 * Usage: 管理与服务提供者的网络连接，包括心跳监测
 */
@Slf4j
public class ClientManager {

    private static boolean isInit;

    private static ExecutorService heartBeatPool;

    private static void init() {
        if (!isInit) {
            synchronized (ClientManager.class) {
                if (!isInit) {
                    heartBeatPool = Executors.newFixedThreadPool(1);
                    HeartBeatTask task = new HeartBeatTask();
                    heartBeatPool.execute(task);
                }
            }
        }
    }

    public static AirClient getAirClient(ServerParam serverParam) {
        init();
        return AirClientFactory.getAirClient(serverParam);
    }

    public static List<AirClient> getAllClients() {
        return AirClientFactory.getAllClients();
    }

    public static void deleteClient(AirClient airClient) {
        AirClientFactory.deleteClient(airClient);
    }
}
