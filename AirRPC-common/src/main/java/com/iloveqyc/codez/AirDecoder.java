package com.iloveqyc.codez;

import com.iloveqyc.serializer.SerializerFactory;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * User: qiuyongchen Nicolas.David
 * Date: 2017/4/8
 * Time: 下午6:51
 * Usage: 服务提供端的解码器，一般用于解码客户端传来的request
 */
public class AirDecoder extends ByteToMessageDecoder {

    private Class<?> targetClassType;

    public AirDecoder(Class<?> targetClassType) {
        this.targetClassType = targetClassType;
    }

    /**
     * Decode the from one {@link ByteBuf} to an other. This method will be called till either the input
     * {@link ByteBuf} has nothing to read when return from this method or till nothing was read from the input
     * {@link ByteBuf}.
     *
     * @param ctx the {@link ChannelHandlerContext} which this {@link ByteToMessageDecoder} belongs to
     * @param in  the {@link ByteBuf} from which to read data
     * @param out the {@link List} to which decoded messages should be added
     * @throws Exception is thrown if an error accour
     */
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        if (in.readableBytes() <= 0)
            return;
        in.markReaderIndex();

        // 第一个字节用于表示序列化方式
        byte b=in.readByte();

        // 长度不够4个字节（即1个整数）
        if (in.readableBytes() < 4) {
            return;
        }
        in.markReaderIndex();

        // 第一个字节后的第一个整数（即第2个到第5个字节）表示消息长度（以字节为单位）
        int dataLength = in.readInt();
        if (in.readableBytes() < dataLength) {
            in.resetReaderIndex();
            return;
        }

        // 获取所有的字节
        byte[] objData = new byte[dataLength];
        in.readBytes(objData);

        // 把传输过来的字节反序列化
        out.add(SerializerFactory.getHessianSerializer().deSerialize(objData, targetClassType));
    }
}
