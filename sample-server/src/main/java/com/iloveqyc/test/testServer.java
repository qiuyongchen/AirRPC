package com.iloveqyc.test;

import org.springframework.context.ApplicationContextAware;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * User: qiuyongchen Nicolas.David
 * Date: 2017/4/9
 * Time: 上午1:25
 * Usage: xxx
 */
public class testServer {
    public static void main(String[] args) {
        new ClassPathXmlApplicationContext("AirServer.xml");
    }
}
