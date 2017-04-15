package com.iloveqyc.service.proxy.handler;

import com.iloveqyc.service.proxy.context.InvocationContext;

/**
 * User: qiuyongchen Nicolas.David
 * Date: 2017/4/15
 * Time: 下午5:07
 * Usage: 责任链处理器
 */
public interface FilterInvocationHandler {

    Object invoke(InvocationContext context);

}
