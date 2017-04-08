package com.iloveqyc.invoker;

import com.iloveqyc.bean.AirRequest;
import com.iloveqyc.bean.AirResponse;
import com.iloveqyc.bean.ServerParam;
import com.iloveqyc.codez.AirDecoder;
import com.iloveqyc.codez.AirEncoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * User: qiuyongchen Nicolas.David
 * Date: 2017/4/8
 * Time: 下午11:35
 * Usage: Netty客户端，用于和某个Netty服务端连接
 */
@Slf4j
public class AirClient {

    // 连接的上下文（建立连接后，该上下文一直有效）
    private ChannelFuture channelFuture;

    // 连接是否已建立
    private boolean isActive;

    // 存储某个request和其response
    private Map<String, LinkedBlockingQueue<AirResponse>> requestResponseMap = new ConcurrentHashMap<>(20);

    /**
     * 启动一个netty客户端
     */
    public void activate(EventLoopGroup group, final ServerParam serverParam) {
        if (isActive) {
            return;
        }
        Bootstrap bootstrap = new Bootstrap();
        // 将group绑定在netty客户端上下文里
        // 注：每个netty客户端与不同的netty服务端连接，所有的netty客户端共用一个group，共用一套线程池
        bootstrap.group(group);
        // 设置底层Channel
        bootstrap.channel(NioSocketChannel.class);
        bootstrap.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                // 获取channel中的pipeline
                ChannelPipeline pipeline = ch.pipeline();

                // 编码request
                pipeline.addLast(new AirEncoder(AirRequest.class));

                // 解码response
                pipeline.addLast(new AirDecoder(AirResponse.class));

                // 业务处理Handler（处理response）
                pipeline.addLast(new SimpleChannelInboundHandler<AirResponse>() {
                    @Override
                    protected void channelRead0(ChannelHandlerContext ctx, AirResponse response) throws Exception {
                        doProcess(response);
                    }
                });
            }
        });
        // 关闭TCP DELAY ACK，不延迟 Ack 包的发送，以追求速度
        bootstrap.option(ChannelOption.TCP_NODELAY, true);
        try {
            channelFuture = bootstrap.bind(serverParam.getIp(),Integer.valueOf(serverParam.getPort())).sync()
                    .addListener(new ChannelFutureListener() {
                        @Override
                        public void operationComplete(ChannelFuture future) throws Exception {
                            log.info("启动netty客户端成功，serverParam: {}", serverParam);
                        }
                    });
        } catch (InterruptedException e) {
            log.error("启动netty客户端失败，serverParam: {}", serverParam);
        }
        // 成功启动netty客户端，且连接netty服务端
        if (channelFuture != null && channelFuture.isSuccess()) {
            isActive = true;
        }
    }

    /**
     * 发送请求，阻塞，直到得到响应
     * @param request 请求
     * @return 响应
     */
    public AirResponse sendRequest(AirRequest request) {
        LinkedBlockingQueue<AirResponse> blockingQueue = new LinkedBlockingQueue<>(1);
        requestResponseMap.put(request.getRequestId(), blockingQueue);
        Channel channel = channelFuture.channel();
        // 将request发给netty服务端
        if (channel.isWritable()) {
            channel.writeAndFlush(request);
        }
        AirResponse response = null;
        try {
            // 阻塞等待response返回
            response = blockingQueue.take();
        } catch (InterruptedException e) {
            log.error("get response error", e);
        } finally {
            requestResponseMap.remove(request.getRequestId());
        }
        return response;
    }

    private void doProcess(AirResponse response) {
        log.info("收到一个response: {}", response);
        if (!requestResponseMap.containsKey(response.getRequestId())) {
            log.warn("收到一个非我方的response: {}", response);
            return;
        }
        LinkedBlockingQueue<AirResponse> blockingQueue = requestResponseMap.get(response.getRequestId());
        blockingQueue.add(response);
    }
}
