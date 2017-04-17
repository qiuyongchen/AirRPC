package com.iloveqyc.bean;

import com.iloveqyc.constants.MsgConstant;
import lombok.Data;

import java.io.Serializable;

/**
 * User: qiuyongchen Nicolas.David
 * Date: 2017/4/8
 * Time: 下午5:12
 * Usage: AirRPC框架调用者发出的request
 */
@Data
public class AirRequest implements Serializable {

    private static final long serialVersionUID = -5199456502650083949L;

    /**
     * AirRPC内部的通信类型
     * @see com.iloveqyc.constants.MsgConstant
     */
    private int msgType = MsgConstant.MSG_TYPE_SERVICE;

    /**
     * AirRPC内部的心跳监测序列
     */
    private long heartBeatSeq;

    /**
     * request的唯一标注
     */
    private String requestId;

    /**
     * 接口名称
     */
    private String serviceName;

    /**
     * 调用方法名称
     */
    private String methodName;

    /**
     * 调用方法的参数类型列表
     */
    private Class<?>[] paramTypes;

    /**
     * 调用方法的参数
     */
    private Object[] parameters;

}
