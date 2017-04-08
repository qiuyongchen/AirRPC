package com.iloveqyc.codez;

import com.iloveqyc.serializer.SerializerFactory;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * User: qiuyongchen Nicolas.David
 * Date: 2017/4/8
 * Time: 下午8:05
 * Usage: 服务提供端的编码器，一般用于编码回传给客户端的response
 *        （传输一个对象的最大值为4G）
 */
public class AirEncoder extends MessageToByteEncoder{

    private Class<?> targetClassType;

    public AirEncoder(Class<?> targetClassType) {
        this.targetClassType = targetClassType;
    }

    /**
     * Encode a message into a {@link ByteBuf}. This method will be called for each written message that can be handled
     * by this encoder.
     *
     * @param ctx the {@link ChannelHandlerContext} which this {@link MessageToByteEncoder} belongs to
     * @param msg the message to encode
     * @param out the {@link ByteBuf} into which the encoded message will be written
     * @throws Exception is thrown if an error accour
     */
    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {
        if (!targetClassType.isInstance(msg)) {
            return;
        }
        byte[] objBytes = SerializerFactory.getHessianSerializer().serialize(msg);
        out.writeByte(1);
        out.writeInt(objBytes.length);
        out.writeBytes(objBytes);
    }
}
