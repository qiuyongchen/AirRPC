package com.iloveqyc.service.invoker.heartbeat;

import java.util.concurrent.atomic.AtomicLong;

/**
 * User: qiuyongchen Nicolas.David
 * Date: 2017/4/17
 * Time: 下午7:21
 * Usage: xxx
 */
public class HeartBeatStatus {

    private String ip;

    AtomicLong succeedCounter = new AtomicLong();

    AtomicLong failedCounter = new AtomicLong();

    public HeartBeatStatus(String ip) {
        this.ip = ip;
    }

    public void increSucceed() {
        succeedCounter.incrementAndGet();
        failedCounter.set(0L);
    }

    public void increFailed() {
        succeedCounter.set(0L);
        failedCounter.incrementAndGet();
    }

    public void resetCounter() {
        succeedCounter.set(0L);
        failedCounter.set(0L);
    }

}
