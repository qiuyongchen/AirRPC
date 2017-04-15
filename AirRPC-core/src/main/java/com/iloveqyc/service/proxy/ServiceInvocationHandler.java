package com.iloveqyc.service.proxy;

import com.iloveqyc.bean.AirRequest;
import com.iloveqyc.bean.AirResponse;
import com.iloveqyc.bean.InvokerParam;
import com.iloveqyc.bean.ServerParam;
import com.iloveqyc.constants.PropertyConstant;
import com.iloveqyc.invoker.AirClient;
import com.iloveqyc.invoker.AirClientFactory;
import com.iloveqyc.utils.ConfigLoader;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.UUID;

/**
 * User: qiuyongchen Nicolas.David
 * Date: 2017/4/9
 * Time: 下午9:42
 * Usage: xxx
 */
@Slf4j
public class ServiceInvocationHandler implements InvocationHandler {

    private InvokerParam invokerParam;

    public ServiceInvocationHandler(InvokerParam invokerParam) {
        this.invokerParam = invokerParam;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        AirRequest request = buildRequest(method, args);

        // TODO 加入zookeeper
        // 获取服务器配置
        ServerParam serverParam = new ServerParam(ConfigLoader.loadPropertyByKey(PropertyConstant.LOCAL_IP),"4080");
        AirClient airClient = AirClientFactory.getAirClient(serverParam);

        AirResponse response = airClient.sendRequest(request);

        if (response == null) {
            return null;
        }
        if (response.getE() != null) {
            throw response.getE();
        }
        return response.getResult();
    }

    private AirRequest buildRequest(Method method, Object[] args) {
        AirRequest request = new AirRequest();
        request.setRequestId(UUID.randomUUID().toString());
        request.setServiceName(invokerParam.getServiceName());
        request.setMethodName(method.getName());
        request.setParameters(args);
        request.setParamTypes(method.getParameterTypes());
        return request;
    }

}
