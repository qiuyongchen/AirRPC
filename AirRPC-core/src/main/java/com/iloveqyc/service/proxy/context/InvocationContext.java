package com.iloveqyc.service.proxy.context;

import com.iloveqyc.bean.InvokerParam;
import lombok.Data;

import java.lang.reflect.Method;

/**
 * User: qiuyongchen Nicolas.David
 * Date: 2017/4/15
 * Time: 下午5:16
 * Usage: 调用链的上下文
 */
@Data
public class InvocationContext {

    private InvokerParam invokerParam;

    private Method method;

    private Class<?>[] parameterTypes;

    private Object[] args;

    public InvocationContext(InvokerParam invokerParam, Method method, Class<?>[] parameterTypes, Object[] args) {
        this.invokerParam = invokerParam;
        this.method = method;
        this.parameterTypes = parameterTypes;
        this.args = args;
    }
}
