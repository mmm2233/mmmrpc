package com.mmm.mmmrpc;

import com.mmm.mmmrpc.config.RpcConfig;
import com.mmm.mmmrpc.constant.RpcConstant;
import com.mmm.mmmrpc.utils.ConfigUtils;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RpcApplication {

    private static volatile RpcConfig rpcConfig;

    public static void init(RpcConfig newRpcConfig) {
        rpcConfig = newRpcConfig;
        log.info("rpc init,rpcConfig:{}",rpcConfig);
    }

    public static void init() {
        RpcConfig newRpcConfig;
        try {
            newRpcConfig  = ConfigUtils.loadConfig(RpcConfig.class, RpcConstant.DEFAULT_CONFIG_PREFIX);
        }catch (Exception e) {
            newRpcConfig = new RpcConfig();
        }

        init(newRpcConfig);
    }

    public static RpcConfig getRpcConfig() {
        if (rpcConfig == null) {
            synchronized (RpcConfig.class) {
                if (rpcConfig == null) {
                    init();
                }
            }
        }
        return rpcConfig;
    }
}
