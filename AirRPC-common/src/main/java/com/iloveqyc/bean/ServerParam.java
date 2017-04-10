package com.iloveqyc.bean;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * User: qiuyongchen Nicolas.David
 * Date: 2017/4/8
 * Time: 下午6:35
 * Usage: 服务器的参数
 */
@Data
public class ServerParam {

    private String ip;

    private String port;

    public ServerParam(String ip, String port) {
        this.ip = ip;
        this.port = port;
    }
}
