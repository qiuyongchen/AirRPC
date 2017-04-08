package com.iloveqyc.serializer;

import com.caucho.hessian.io.HessianInput;
import com.caucho.hessian.io.HessianOutput;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * User: qiuyongchen Nicolas.David
 * Date: 2017/4/8
 * Time: 下午7:36
 * Usage: Hessian序列化器
 */
@Slf4j
public class HessianSerializer {

    /**
     * 将 对象 序列化成 字节数组
     */
    public <T> byte[] serialize(T o) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        HessianOutput hessianOutput = new HessianOutput(outputStream);
        try {
            hessianOutput.writeObject(o);
        } catch (IOException e) {
            log.error(String.valueOf(e));
        }
        return outputStream.toByteArray();
    }

    /**
     * 将 字节数组 反序列化成 对象
     */
    public <T> T deSerialize(byte[] bytes, Class<T> tClass) {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
        HessianInput hessianInput = new HessianInput(inputStream);
        T o = null;
        try {
            o = (T) hessianInput.readObject();
        } catch (IOException e) {
            log.error(String.valueOf(e));
        }
        return o;
    }
}
