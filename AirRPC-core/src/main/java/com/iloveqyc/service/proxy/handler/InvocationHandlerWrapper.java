package com.iloveqyc.service.proxy.handler;

import com.google.common.collect.Lists;
import com.iloveqyc.service.proxy.context.InvocationContext;
import com.iloveqyc.service.proxy.handler.filter.Filter;
import com.iloveqyc.service.proxy.handler.filter.impl.*;

import java.util.List;

/**
 * User: qiuyongchen Nicolas.David
 * Date: 2017/4/15
 * Time: 下午5:10
 * Usage: xxx
 */
public class InvocationHandlerWrapper {

    private static FilterInvocationHandler filterInvocationHandler;

    private static List<Filter> filterList = Lists.newArrayList();

    private static boolean inited;

    private static void init() {
        if (!inited) {
            synchronized (InvocationHandlerWrapper.class) {
                if (!inited) {
                    filterList.add(new MonitorFilter());
                    filterList.add(new LoadBalanceFilter());
                    filterList.add(new RemoteInvokeFilter());
                    filterInvocationHandler = buildFilterInvocationHandler();
                    inited = true;
                }
            }
        }
    }

    public static FilterInvocationHandler getFilterInvocationHandler() {
        if (filterInvocationHandler == null) {
            init();
        }
        return filterInvocationHandler;
    }

    private static FilterInvocationHandler buildFilterInvocationHandler() {
        FilterInvocationHandler previous = null;

        // 从后向前，逐渐构建出一条责任链
        for (int i = filterList.size() - 1; i >= 0; --i) {
            final FilterInvocationHandler next = previous;
            final Filter filter = filterList.get(i);
            previous = new FilterInvocationHandler() {
                @Override
                public Object invoke(InvocationContext context) throws Exception {
                    // 责任链每个节点除了处理自身逻辑，可以调用下一个节点
                    return filter.invoke(next, context);
                }
            };
        }

        return previous;
    }

}
