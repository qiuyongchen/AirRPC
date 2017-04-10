package com.iloveqyc.provider.process;

/**
 * User: qiuyongchen Nicolas.David
 * Date: 2017/4/10
 * Time: 下午8:30
 * Usage:
 */
public class RequestDealerFactory {

    private static RequestDealer requestDealer;

    public static RequestDealer getRequestDealer() {
        if (requestDealer == null) {
            requestDealer = new RequestDealer();
        }
        return requestDealer;
    }
}
