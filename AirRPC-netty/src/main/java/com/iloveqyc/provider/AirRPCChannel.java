package com.iloveqyc.provider;

import com.iloveqyc.bean.AirResponse;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;


/**
 * User: qiuyongchen Nicolas.David
 * Date: 2017/4/8
 * Time: 下午6:15
 * Usage: 在Netty里，Channel是通讯的载体
 *        AirRPC是天生的异步调用，以异步的方式使用了Channel
 */
@AllArgsConstructor
@Data
@Slf4j
public class AirRPCChannel {

    /**
     * Netty里的channel，用于传输数据
     */
    private Channel channel;

    public void write(final AirResponse response) {

        // 将response异步传回客户端
        ChannelFuture future = channel.writeAndFlush(response);
        // 监听回传结果，完成时输出日志
        future.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                log.info("from future: {}, response: {} write complete", future, response);
            }
        });
    }
}
