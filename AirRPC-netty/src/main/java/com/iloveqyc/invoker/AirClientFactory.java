package com.iloveqyc.invoker;

import com.google.common.collect.Lists;
import com.iloveqyc.bean.ServerParam;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * User: qiuyongchen Nicolas.David
 * Date: 2017/4/9
 * Time: 上午12:51
 * Usage: 生成与获取netty客户端的工厂，理论上可以生成多个netty客户端
 */
@Slf4j
public class AirClientFactory {

    private static EventLoopGroup workerGroup = new NioEventLoopGroup(5);

    private static ConcurrentHashMap<ServerParam, AirClient> clients = new ConcurrentHashMap<>(5);

    public static AirClient getAirClient(ServerParam serverParam) {
        AirClient airClient = clients.get(serverParam);
        if (airClient != null) {
            return airClient;
        }
        airClient = new AirClient();
        // 启动客户端
        airClient.activate(workerGroup, serverParam);
        clients.put(serverParam, airClient);
        return airClient;
    }

    public static List<AirClient> getAllClients() {
        List<AirClient> allClients = Lists.newArrayList();
        allClients.addAll(clients.values());
        return allClients;
    }

    public static void deleteClient(AirClient airClient) {
        if (clients.containsKey(airClient.getServerParam())) {
            log.info("remove:{} from clients:{}", airClient, clients);
            clients.remove(airClient.getServerParam());
        }
    }

}
