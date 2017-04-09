package com.iloveqyc.test;

import com.iloveqyc.sample.api.ILoveYouService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * User: qiuyongchen Nicolas.David
 * Date: 2017/4/9
 * Time: 上午1:25
 * Usage: xxx
 */
public class testClient {

    public static void main(String[] args) {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("AirClient.xml");
        ILoveYouService iLoveYouService = (ILoveYouService) applicationContext.getBean("iLoveYouService");
        System.out.println(iLoveYouService.iLoveYou("s", "sdf"));

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
