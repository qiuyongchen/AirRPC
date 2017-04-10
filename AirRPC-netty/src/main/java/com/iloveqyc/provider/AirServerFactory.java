package com.iloveqyc.provider;

/**
 * User: qiuyongchen Nicolas.David
 * Date: 2017/4/10
 * Time: 上午12:43
 * Usage: 生成与获取netty服务端的工厂，理论上仅生成一个netty服务端
 */
public class AirServerFactory {

    private static AirServer airServer;

    public static AirServer getAirServer() {
        if (airServer == null) {
            airServer = new AirServer();
        }
        return airServer;
    }
}
