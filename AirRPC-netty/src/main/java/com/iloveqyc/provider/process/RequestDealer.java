package com.iloveqyc.provider.process;

import com.iloveqyc.bean.AirRequest;
import com.iloveqyc.bean.ProviderParam;
import com.iloveqyc.provider.process.task.RequestTask;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * User: qiuyongchen Nicolas.David
 * Date: 2017/4/10
 * Time: 下午8:12
 * Usage: 请求处理者，把所有的请求放入线程池
 */
@Slf4j
public class RequestDealer {

    private  ThreadPoolExecutor executor;

    public RequestDealer() {
        /*
          最小线程数：3
          最大线程数：10
          线程最长歇息时间：1000秒
          等待进入线程的最大任务数(超过被抛弃)：10000
         */
        executor = new ThreadPoolExecutor(3, 10, 1000L, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(10000));
    }

    /**
     * 处理请求（将其放入线程池）
     * @param channel
     * @param request
     * @param param
     */
    public void dealRequest(Channel channel, AirRequest request, ProviderParam param) {
        executor.submit(new RequestTask(channel, request, param));
        log.info("线程池的大小：{}", executor.getPoolSize());
    }
}
