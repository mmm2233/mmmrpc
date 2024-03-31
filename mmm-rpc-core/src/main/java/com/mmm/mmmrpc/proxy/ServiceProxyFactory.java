package com.mmm.mmmrpc.proxy;

import com.mmm.mmmrpc.RpcApplication;
import com.mmm.mmmrpc.config.RpcConfig;

import java.lang.reflect.Proxy;

public class ServiceProxyFactory {
    public static <T> T getProxy(Class<T> clazz)
    {
        // 如果RpcApplication的配置为模拟模式
        if(RpcApplication.getRpcConfig().isMock()) {
            // 返回模拟模式的代理对象
            return getMockProxy(clazz);
        }
        // 返回使用ServiceProxy作为处理器的新代理实例
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(),
                new Class[]{clazz}, new ServiceProxy());
    }


    public static <T>  T getMockProxy(Class<T> serivceClass)
    {
        // 创建一个MockServiceProxy的实例，用于模拟服务调用
        // 创建一个新的代理实例，并将MockServiceProxy作为处理器的参数
        // 使用serivceClass的类加载器来加载代理类的字节码
        // 将serivceClass作为代理类实现的接口
        return (T) Proxy.newProxyInstance(serivceClass.getClassLoader(),
                new Class[]{serivceClass}, new MockServiceProxy());
    }


}
