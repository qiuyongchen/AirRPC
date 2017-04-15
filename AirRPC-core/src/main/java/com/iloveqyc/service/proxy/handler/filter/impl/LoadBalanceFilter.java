package com.iloveqyc.service.proxy.handler.filter.impl;

import com.iloveqyc.service.proxy.context.InvocationContext;
import com.iloveqyc.service.proxy.handler.filter.Filter;
import com.iloveqyc.service.proxy.handler.FilterInvocationHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * User: qiuyongchen Nicolas.David
 * Date: 2017/4/15
 * Time: 下午5:30
 * Usage: xxx
 */
@Slf4j
public class LoadBalanceFilter implements Filter {

    @Override
    public Object invoke(FilterInvocationHandler invocationHandler, InvocationContext context) {
        log.info("调用了LoadBalanceFilter, invocationHandler:{}, context:{}", invocationHandler, context);
        return invocationHandler.invoke(context);
    }
}
