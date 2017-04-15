package com.iloveqyc.bean;

import lombok.Data;

import java.io.Serializable;

/**
 * User: qiuyongchen Nicolas.David
 * Date: 2017/4/8
 * Time: 下午5:22
 * Usage: AirRPC框架提供者发回的response
 */
@Data
public class AirResponse implements Serializable {

    private static final long serialVersionUID = 8380079647192110098L;

    /**
     * request的唯一标注
     */
    private String requestId;

    /**
     * 调用的返回结果体
     */
    private Object result;

    /**
     * 调用发生的异常
     */
    private Exception e;

    public AirResponse() {
    }

    public AirResponse(String requestId) {
        this.requestId = requestId;
    }

}
