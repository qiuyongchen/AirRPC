package com.iloveqyc.service.proxy.handler.filter;

import com.iloveqyc.service.proxy.context.InvocationContext;
import com.iloveqyc.service.proxy.handler.FilterInvocationHandler;

/**
 * User: qiuyongchen Nicolas.David
 * Date: 2017/4/15
 * Time: 下午5:20
 * Usage: xxx
 */
public interface Filter {

    Object invoke(FilterInvocationHandler invocationHandler, InvocationContext context) throws Exception;

}
