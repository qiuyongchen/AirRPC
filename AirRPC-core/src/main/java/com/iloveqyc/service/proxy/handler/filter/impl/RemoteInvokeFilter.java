package com.iloveqyc.service.proxy.handler.filter.impl;

import com.iloveqyc.bean.AirRequest;
import com.iloveqyc.bean.AirResponse;
import com.iloveqyc.bean.InvokerParam;
import com.iloveqyc.bean.ServerParam;
import com.iloveqyc.invoker.AirClient;
import com.iloveqyc.invoker.AirClientFactory;
import com.iloveqyc.service.proxy.context.InvocationContext;
import com.iloveqyc.service.proxy.handler.filter.Filter;
import com.iloveqyc.service.proxy.handler.FilterInvocationHandler;
import com.iloveqyc.zookeeper.registry.ZookeeperRegistryFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;

import java.lang.reflect.Method;
import java.util.List;
import java.util.UUID;

/**
 * User: qiuyongchen Nicolas.David
 * Date: 2017/4/15
 * Time: 下午5:28
 * Usage: xxx
 */
@Slf4j
public class RemoteInvokeFilter implements Filter {

    @Override
    public Object invoke(FilterInvocationHandler invocationHandler, InvocationContext context) throws Exception {
        log.info("调用了RemoteInvokeFilter, invocationHandler:{}, context:{}", invocationHandler, context);
        AirRequest request = buildRequest(context.getMethod(), context.getArgs(), context.getInvokerParam());
        AirResponse response;

        // 获取服务器配置
        List<ServerParam> serverParams = ZookeeperRegistryFactory.getZkRegistry().
                getServiceProvider(context.getInvokerParam().getServiceName());
        if (CollectionUtils.isEmpty(serverParams)) {
            throw new RuntimeException("no provider for service:" + context.getInvokerParam().
                    getServiceName());
        }
        ServerParam serverParam = serverParams.get((int) (Math.random() * serverParams.size()));
        AirClient airClient = AirClientFactory.getAirClient(serverParam);

        response = airClient.sendRequest(request);

        if (response == null) {
            return null;
        }
        if (response.getE() != null) {
            throw response.getE();
        }
        return response.getResult();
    }

    private AirRequest buildRequest(Method method, Object[] args, InvokerParam invokerParam) {
        AirRequest request = new AirRequest();
        request.setRequestId(UUID.randomUUID().toString());
        request.setServiceName(invokerParam.getServiceName());
        request.setMethodName(method.getName());
        request.setParameters(args);
        request.setParamTypes(method.getParameterTypes());
        return request;
    }
}
