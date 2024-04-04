package com.mmm.mmmrpc.registry;

import cn.hutool.json.JSONUtil;
import com.mmm.mmmrpc.config.RegistryConfig;
import com.mmm.mmmrpc.model.ServiceMetaInfo;
import io.etcd.jetcd.*;
import io.etcd.jetcd.kv.GetResponse;
import io.etcd.jetcd.options.GetOption;
import io.etcd.jetcd.options.PutOption;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

/**
 * etcd注册中心实现
 */
@Slf4j
public class EtcdRegistry implements Registry{
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        Client client = Client.builder().endpoints("http://localhost:2379").build();
        KV kvClient = client.getKVClient();
        ByteSequence key = ByteSequence.from("test_key".getBytes());
        ByteSequence value = ByteSequence.from("test_value".getBytes());

        kvClient.put(key, value);
        CompletableFuture<GetResponse> getFuture = kvClient.get(key);

        GetResponse response = getFuture.join();
        System.out.println("----------------------------" + response);
        kvClient.delete(key).get();
    }

    private Client client;

    private KV kvClient;
    /*
     * 根节点
     */
    private static final String ETCD_ROOT_NODE = "/rpc/";
    @Override
    public void init(RegistryConfig registryConfig) {
        client = Client.builder().endpoints(registryConfig.getAddress())
                .connectTimeout(Duration.ofMillis(registryConfig.getTimeout())).build();
        kvClient = client.getKVClient();
    }

    @Override
    public void register(ServiceMetaInfo serviceMetaInfo) throws Exception {
        Lease leaseClient = client.getLeaseClient();
        // 创建租约
        long leaseId = leaseClient.grant(30).get().getID();

        //设置键值对
        String registerKvKey = ETCD_ROOT_NODE + serviceMetaInfo.getServiceNodeKey();
        ByteSequence key = ByteSequence.from(registerKvKey, StandardCharsets.UTF_8);
        ByteSequence value = ByteSequence.from(JSONUtil.toJsonStr(serviceMetaInfo), StandardCharsets.UTF_8);

        //关联租约和键值对
        PutOption putOption = PutOption.builder().withLeaseId(leaseId).build();
        kvClient.put(key, value, putOption).get();
    }

    @Override
    public void unregister(ServiceMetaInfo serviceMetaInfo) {
        kvClient.delete(ByteSequence.from(ETCD_ROOT_NODE + serviceMetaInfo.getServiceNodeKey(), StandardCharsets.UTF_8));
    }

    @Override
    public List<ServiceMetaInfo> serviceDiscovery(String serviceKey) {
        // 前缀搜索要加/
        String servicePrefix = ETCD_ROOT_NODE + serviceKey + "/";
        try{
            // 前缀搜索服务信息
            GetOption getOption = GetOption.builder().isPrefix(true).build();
            List<KeyValue> keyValues = kvClient.get(ByteSequence.from(servicePrefix, StandardCharsets.UTF_8),
                    getOption).get().getKvs();

            return keyValues.stream().map(kv -> {String value = kv.getValue().toString(StandardCharsets.UTF_8);
                return JSONUtil.toBean(value, ServiceMetaInfo.class);}).collect(Collectors.toList());
        }catch (Exception e){
            throw new RuntimeException("获取服务列表失败！",e);
        }
    }

    @Override
    public void destroy() {
        log.info("当前节点下线");
        if(kvClient != null){
            kvClient.close();
        }
        if(client != null){
            client.close();
        }
    }
}
