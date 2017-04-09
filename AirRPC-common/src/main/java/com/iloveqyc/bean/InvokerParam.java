package com.iloveqyc.bean;

import lombok.Data;

/**
 * User: qiuyongchen Nicolas.David
 * Date: 2017/4/9
 * Time: 下午5:16
 * Usage: 服务调用端的参数
 */
@Data
public class InvokerParam {

    /**
     * 接口的相对地址，例如com.iloveqyc.sample.api.ILoveYouService
     */
    private String iface;

    /**
     * 服务的名称，用户可随便自定义，可以拼凑上服务的版本
     */
    private String serviceName;

    /**
     * 接口的类
     */
    private Class<?> serviceClass;

}
