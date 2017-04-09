package com.iloveqyc.spring;

import com.iloveqyc.service.ServiceFactory;
import com.iloveqyc.utils.AirClassUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.FactoryBean;

/**
 * User: qiuyongchen Nicolas.David
 * Date: 2017/4/9
 * Time: 上午3:48
 * Usage: 用于调用端，生产某个接口的代理对象
 */
@Data
@Slf4j
public class ServiceProxy implements FactoryBean {

    /**
     * 某个服务的接口，即interface
     */
    private String iface;

    /**
     * 服务的名称，用户可随便自定义，可以拼凑上服务的版本
     */
    private String serviceName;

    /**
     * 服务接口的类
     */
    private Class<?> serviceClass;

    /**
     * 服务接口的类的实例
     */
    private Object serviceInstance;

    /**
     * 供spring初始化bean时调用
     */
    public void init() {
        serviceClass = AirClassUtil.getClass(iface);
        serviceInstance = ServiceFactory.getService(iface, serviceName, serviceClass);
    }

    @Override
    public Object getObject() throws Exception {
        return serviceInstance;
    }

    @Override
    public Class<?> getObjectType() {
        return null;
    }

    @Override
    public boolean isSingleton() {
        // spring框架通过getObject方法获得的对象永远是同一个对象
        return true;
    }
}
