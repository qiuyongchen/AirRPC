package com.iloveqyc.service.proxy;

import com.iloveqyc.bean.InvokerParam;
import com.iloveqyc.service.proxy.context.InvocationContext;
import com.iloveqyc.service.proxy.handler.FilterInvocationHandler;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * User: qiuyongchen Nicolas.David
 * Date: 2017/4/15
 * Time: 下午5:03
 * Usage: xxx
 */
public class RemotingServiceProxy implements InvocationHandler {

    private FilterInvocationHandler invocationHandler;

    private InvokerParam invokerParam;

    public RemotingServiceProxy(FilterInvocationHandler invocationHandler, InvokerParam invokerParam) {
        this.invocationHandler = invocationHandler;
        this.invokerParam = invokerParam;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        InvocationContext context = new InvocationContext(invokerParam, method, method.getParameterTypes(), args);
        return invocationHandler.invoke(invocationHandler, context);
    }
}
