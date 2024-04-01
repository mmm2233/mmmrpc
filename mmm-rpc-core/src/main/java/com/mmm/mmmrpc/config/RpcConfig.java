package com.mmm.mmmrpc.config;

import com.mmm.mmmrpc.serializer.SerializerKeys;
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

    private boolean mock = false;

    private String serializer = SerializerKeys.JDK;
}
