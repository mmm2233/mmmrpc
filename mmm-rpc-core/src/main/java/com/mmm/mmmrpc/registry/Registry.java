package com.mmm.mmmrpc.registry;

import com.mmm.mmmrpc.config.RegistryConfig;
import com.mmm.mmmrpc.model.ServiceMetaInfo;

import java.util.List;


public interface Registry {

    /*
     * 初始化
     */
    void init(RegistryConfig registryConfig);

    /*
     * 注册
     */
    void register(ServiceMetaInfo serviceMetaInfo) throws Exception;

    /*
     * 取消注册
     */
    void unregister(ServiceMetaInfo serviceMetaInfo);

    List<ServiceMetaInfo> serviceDiscovery(String serviceKey);

    /*
     * 销毁
     */
    void destroy();
}
