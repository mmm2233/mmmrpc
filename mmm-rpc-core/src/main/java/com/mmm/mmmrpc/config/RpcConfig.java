package com.mmm.mmmrpc.config;

import lombok.Data;

@Data
public class RpcConfig {

    /*
     *  名称
     */
    private String name = "mmm-rpc";

    private String version = "1.0";

    private String serverHost = "localhost";

    private Integer serverPort = 8080;
}
