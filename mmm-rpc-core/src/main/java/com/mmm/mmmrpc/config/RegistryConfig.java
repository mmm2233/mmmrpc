package com.mmm.mmmrpc.config;

import lombok.Data;

@Data
public class RegistryConfig {

    /**
     * 注册中心类型
     */
    private String registry = "etcd";

    /*
     * 注册中心地址
     */
    private String address = "http://localhost:2380";

    /*
     * 注册中心用户名
     */
    private String username;

    /*
     * 注册中心密码
     */
    private String password;

    /*
     * 注册中心超时时间
     */
    private Long timeout = 10000L;
}
