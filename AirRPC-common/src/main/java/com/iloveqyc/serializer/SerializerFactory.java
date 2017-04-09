package com.iloveqyc.serializer;

/**
 * User: qiuyongchen Nicolas.David
 * Date: 2017/4/8
 * Time: 下午7:52
 * Usage: 获取序列化器的工厂，避免每次都生成序列化器
 */
public class SerializerFactory {
    private static HessianSerializer hessianSerializer;

    static {
        if (hessianSerializer == null) {
            hessianSerializer = new HessianSerializer();
        }
    }

    public static HessianSerializer getHessianSerializer() {
        return hessianSerializer;
    }
}
