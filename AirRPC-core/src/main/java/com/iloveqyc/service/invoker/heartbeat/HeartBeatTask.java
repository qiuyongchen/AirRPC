package com.iloveqyc.service.invoker.heartbeat;

import com.google.common.collect.Maps;
import com.iloveqyc.bean.AirRequest;
import com.iloveqyc.bean.AirResponse;
import com.iloveqyc.constants.MsgConstant;
import com.iloveqyc.invoker.AirClient;
import com.iloveqyc.service.invoker.ClientManager;
import com.iloveqyc.zookeeper.registry.ZookeeperRegistryFactory;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * User: qiuyongchen Nicolas.David
 * Date: 2017/4/17
 * Time: 下午12:06
 * Usage: xxx
 */
@Slf4j
public class HeartBeatTask implements Runnable {

    private static AtomicLong heartBeatSeq = new AtomicLong();
    private static ConcurrentHashMap<String, HeartBeatStatus> heartBeatStatusMap = new ConcurrentHashMap<>();

    @Override
    public void run() {
        // 每隔5秒发送一次心跳监测
        long sleepTime = 5000;
        while (!Thread.currentThread().isInterrupted()) {
            try {
                Thread.sleep(sleepTime);
                List<AirClient> airClients = ClientManager.getAllClients();
                for (AirClient client : airClients) {
                    checkClientAlive(client);
                }
            } catch (InterruptedException e) {
                log.error("sleep error!", e);
            }
        }
    }

    private void checkClientAlive(AirClient client) {
        AirRequest request = buildHeartBeatRequest();
        AirResponse response = client.sendRequest(request);
        processResponse(request, response, client);
    }

    /**
     * 从缓存中获取心跳状态
     * @param ip
     * @return
     */
    private HeartBeatStatus getHeartBeatStatus(String ip) {
        HeartBeatStatus status = heartBeatStatusMap.get(ip);
        if (status == null) {
            status = new HeartBeatStatus(ip);
            heartBeatStatusMap.putIfAbsent(ip, status);
        }
        return status;
    }

    /**
     * 处理心跳结果
     * @param request
     * @param response
     * @param client
     */
    private void processResponse(AirRequest request, AirResponse response, AirClient client) {
        if (response != null && request.getHeartBeatSeq() == response.getHearBeatSeq()) {
            HeartBeatStatus status = getHeartBeatStatus(client.getServerParam().getIp());
            status.increSucceed();
        } else {
            HeartBeatStatus status = getHeartBeatStatus(client.getServerParam().getIp());
            status.increFailed();
        }
        notifyHearBeatResult(client);
    }

    /**
     * 删除无效服务提供者
     * @param client
     */
    private void notifyHearBeatResult(AirClient client) {
        HeartBeatStatus status = getHeartBeatStatus(client.getServerParam().getIp());
        if (status.succeedCounter.longValue() >= 5) {
            log.info("心跳成功5次，该服务提供者存活");
            status.resetCounter();
        } else if (status.failedCounter.longValue() >= 5) {
            log.info("心跳失败5次，该服务提供者已失效，将删除该服务(service缓存层及netty客户端连接层)");
            status.resetCounter();
            ZookeeperRegistryFactory.getZkRegistry().
                    deleteLocalCacheServiceProviderWithoutServiceName(client.getServerParam());
            ClientManager.deleteClient(client);
        }
    }

    /**
     * 构建心跳请求request
     * @return request
     */
    private AirRequest buildHeartBeatRequest() {
        AirRequest request = new AirRequest();
        request.setMsgType(MsgConstant.MSG_TYPE_HEART_BEAT);
        request.setHeartBeatSeq(heartBeatSeq.getAndIncrement());
        request.setRequestId(UUID.randomUUID().toString());
        return request;
    }
}
