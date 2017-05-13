package com.iloveqyc.provider;

import com.iloveqyc.bean.AirRequest;
import com.iloveqyc.bean.AirResponse;
import com.iloveqyc.bean.ProviderParam;
import com.iloveqyc.bean.ServerParam;
import com.iloveqyc.codez.AirDecoder;
import com.iloveqyc.codez.AirEncoder;
import com.iloveqyc.provider.process.RequestDealerFactory;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * User: qiuyongchen Nicolas.David
 * Date: 2017/4/8
 * Time: 下午6:31
 * Usage: Netty服务端，包括启动、停止等
 */
@Data
@Slf4j
public class AirServer {

    private EventLoopGroup boss;
    private EventLoopGroup worker;
    private ServerBootstrap bootstrap;

    // 服务端是否已经启动
    private boolean isActivated;

    // 此netty服务器承载的所有服务
    Map<String, ProviderParam> providerParamsMap = new ConcurrentHashMap<>();

    /**
     * 启动Netty服务端
     * @param providerParams
     * @param serverParam 服务器的参数
     */
    public void active(final List<ProviderParam> providerParams, ServerParam serverParam) {

        // spring初始化bean时仅会在同一个线程中初始化，故无需考虑多个AirServer同时被调用active方法
        if (isActivated) {
            return;
        }
        log.info("尝试启动server: {}", serverParam);

        boss = new NioEventLoopGroup();
        worker = new NioEventLoopGroup();
        bootstrap = new ServerBootstrap();

        for (ProviderParam param : providerParams) {
            providerParamsMap.put(param.getServiceName(), param);
        }

        // 将boss组和worker组绑定在Netty上下文里
        bootstrap.group(boss, worker);
        // 设置底层Channel
        bootstrap.channel(NioServerSocketChannel.class);
        // 设置业务层Channel
        bootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel channel) throws Exception {
                // 获取channel中的pipeline
                ChannelPipeline pipeline = channel.pipeline();

                // 解码request
                pipeline.addLast(new AirDecoder(AirRequest.class));

                // 编码response
                pipeline.addLast(new AirEncoder(AirResponse.class));

                // 业务处理Handler（处理request）
                pipeline.addLast(new SimpleChannelInboundHandler<AirRequest>() {
                    @Override
                    protected void channelRead0(ChannelHandlerContext ctx, AirRequest request) throws Exception {
                        doProcess(ctx.channel(), request, providerParamsMap);
                    }
                });

            }
        });
        // 当服务器请求处理线程全满时，用于临时存放已完成三次握手的请求的队列的最大长度
        bootstrap.option(ChannelOption.SO_BACKLOG, 1024);
        // 启用心跳保活机制
        bootstrap.childOption(ChannelOption.SO_KEEPALIVE, true);
        // 关闭TCP DELAY ACK，不延迟 Ack 包的发送，以追求速度
//        bootstrap.option(ChannelOption.TCP_NODELAY, true);
        // 绑定ip和端口，并启动netty
        try {
            bootstrap.bind(serverParam.getIp(), Integer.valueOf(serverParam.getPort())).sync()
                    .addListener(new ChannelFutureListener() {
                        @Override
                        public void operationComplete(ChannelFuture future) throws Exception {
                            log.info("netty启动成功");
                            isActivated = true;
                        }
                    });
        } catch (InterruptedException e) {
            log.error("绑定ip和端口，并启动netty，失败", e);
            return;
        }
    }

    /**
     * 处理request
     * @param channel
     * @param request
     * @param providerParams
     */
    private void doProcess(Channel channel, AirRequest request, Map<String, ProviderParam> providerParams) {
        String serviceName = request.getServiceName();
        ProviderParam param = providerParamsMap.get(serviceName);
        RequestDealerFactory.getRequestDealer().dealRequest(channel, request, param);
    }

}