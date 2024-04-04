package com.mmm.mmmrpc.model;

import com.mmm.mmmrpc.constant.RpcConstant;
import lombok.Data;

/**
 * 服务元信息
 */

@Data
public class ServiceMetaInfo {

    /*
     * 服务名称
     */
    private String serviceName;

    /*
     * 版本号
     */
    private String serviceVersion = RpcConstant.DEFAULT_SERVICE_VESION;

    /*
     * 服务域名
     */
    private String serviceHost;

    /*
     * 服务端口号
     */
    private Integer servicePort;

    /*
     * todo 服务分组(待实现)
     */
    private String serviceGroup ="default";

    /*
     * 服务key
     */
    public String getServiceKey() {
        return String.format("%s:%s",serviceName, serviceVersion);
    }

    /*
     * 服务注册节点名
     */
    public String getServiceNodeKey() {
        return String.format("%s%s%s",getServiceKey(),serviceHost,servicePort);
    }
}
