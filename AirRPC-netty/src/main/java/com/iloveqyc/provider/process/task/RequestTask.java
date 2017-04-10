package com.iloveqyc.provider.process.task;

import com.iloveqyc.bean.AirRequest;
import com.iloveqyc.bean.AirResponse;
import com.iloveqyc.bean.ProviderParam;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import lombok.extern.slf4j.Slf4j;
import net.sf.cglib.reflect.FastClass;
import net.sf.cglib.reflect.FastMethod;


import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;

/**
 * User: qiuyongchen Nicolas.David
 * Date: 2017/4/10
 * Time: 下午8:23
 * Usage: xxx
 */
@Slf4j
public class RequestTask implements Callable {

    private Channel channel;

    private AirRequest request;

    private ProviderParam providerParam;

    public RequestTask(Channel channel, AirRequest request, ProviderParam providerParam) {
        this.channel = channel;
        this.request = request;
        this.providerParam = providerParam;
    }

    @Override
    public Object call() throws Exception {
        log.info("get a request:{}", request);

        final AirResponse response = new AirResponse();
        response.setRequestId(request.getRequestId());
        try {
            Object rlt = doHandleRequest();
            response.setResult(rlt);
        } catch (Exception e) {
            response.setE(e);
        }

        // 将response传回客户端
        ChannelFuture future = channel.writeAndFlush(response);
        // 监听回传结果，完成时输出日志
        future.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                log.info("from future: {}, response: {} write complete", future, response);
            }
        });
        return null;
    }

    private Object doHandleRequest() throws InvocationTargetException {
        Object serviceInstance = providerParam.getServiceInstance();
        Class<?> serviceClass = serviceInstance.getClass();
        FastClass fastClass = FastClass.create(serviceClass);
        FastMethod method = fastClass.getMethod(request.getMethodName(), request.getParamTypes());
        return method.invoke(serviceInstance, request.getParameters());
    }

}
