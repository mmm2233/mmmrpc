package com.mmm.example;

import com.mmm.mmmrpc.config.RpcConfig;
import com.mmm.mmmrpc.utils.ConfigUtils;

public class ConsumerExample {
    public static void main(String[] args) {
        RpcConfig rpcConfig = ConfigUtils.loadConfig(RpcConfig.class,"rpc");
        System.out.println(rpcConfig);
    }
}
