package com.iloveqyc.bean;

import lombok.Data;

/**
 * User: qiuyongchen Nicolas.David
 * Date: 2017/4/9
 * Time: 上午4:27
 * Usage: 服务提供者的参数
 */
@Data
public class ProviderParam {

    /**
     * 服务的名称，用户可随便自定义，可以拼凑上服务的版本
     */
    private String serviceName;

    /**
     * 服务接口的类的实例
     */
    private Object serviceInstance;

}
