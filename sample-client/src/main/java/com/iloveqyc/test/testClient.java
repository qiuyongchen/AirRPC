package com.iloveqyc.test;

import com.iloveqyc.sample.api.ILoveYouService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * User: qiuyongchen Nicolas.David
 * Date: 2017/4/9
 * Time: 上午1:25
 * Usage: xxx
 */
public class testClient {

    static int i = 10000;
    static volatile long allTime = 0l;

    public static void main(String[] args) {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("AirClient.xml");
        final ILoveYouService iLoveYouService = (ILoveYouService) applicationContext.getBean("iLoveYouService");

//        System.out.println(iLoveYouService.iLoveYou("qiuyongchen", "lusha"));

//        ThreadPoolExecutor executor = new ThreadPoolExecutor(5, 10, 1000L, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(100000));
//
        long beginTime = System.currentTimeMillis();
        for (int i = 0; i < 10; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    long beginTime = System.currentTimeMillis();
                    for (int j = 0; j < 100000; j++) {
                        iLoveYouService.iLoveYou("我你我你我你我你我你我你我你我你我你我你我你我你我你我你我你我你我你我你我你我", "li");
                        System.out.println(j);
                    }
                    allTime += System.currentTimeMillis() - beginTime;
                    System.out.println("平均响应时间(ms)：" + (System.currentTimeMillis() - beginTime) / 1000.0);
                }
            }).start();
        }

        System.out.println("响应时间：" + allTime / (10 * 100000));


//        System.exit(0);
    }
}
