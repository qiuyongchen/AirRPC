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

    public static void main(String[] args) {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("AirClient.xml");
        final ILoveYouService iLoveYouService = (ILoveYouService) applicationContext.getBean("iLoveYouService");
        System.out.println(iLoveYouService.iLoveYou("邱永臣", "陆莎"));

//        ThreadPoolExecutor executor = new ThreadPoolExecutor(5, 10, 1000L, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(100000));
//
//        long beginTime = System.currentTimeMillis();
//        for (int j = 0; j < 100000; j++) {
//            iLoveYouService.iLoveYou("邱永臣", "陆莎");
//            System.out.println(j);
//        }
//        System.out.println("平均响应时间(ms)：" + (System.currentTimeMillis() - beginTime) / 100000.0);

//        ServerParam serverParam = new ServerParam();
//        serverParam.setIp("192.168.100.112");
//        serverParam.setPort("4080");
//        for (int i = 0; i < 100; i++) {
//            AirRequest request = new AirRequest();
//            request.setRequestId(String.valueOf(i) + "邱永臣发出的请求");
//            request.setServiceName("hello");
//            long time = System.currentTimeMillis();
//            AirResponse response = AirClientFactory.getAirClient(serverParam).sendRequest(request);
//            System.out.println("本次请求耗时：" + String.valueOf(System.currentTimeMillis() - time) + "ms");
//            System.out.println(response.getRequestId() + " " + response.getResult());
//        }
//        System.out.println("done");
//        System.exit(0);
    }
}
