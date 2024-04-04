package com.mmm.mmmrpc.model;

import com.mmm.mmmrpc.constant.RpcConstant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RpcRequest implements Serializable {

    private String serviceName;

    private String methodName;

    private String serviceVersion = RpcConstant.DEFAULT_SERVICE_VESION;

    private Class<?>[] parameterTypes;

    private Object[] args;
}
