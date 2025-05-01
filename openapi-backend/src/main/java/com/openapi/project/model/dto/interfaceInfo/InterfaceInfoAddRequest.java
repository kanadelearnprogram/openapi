package com.openapi.project.model.dto.interfaceInfo;

import lombok.Data;

import java.io.Serializable;

/**
 * 创建请求
 *
 * @TableName product
 */
@Data
public class InterfaceInfoAddRequest implements Serializable {

    /**
     * 接口名称
     */
    private String name;
    private String description;

    /**
     * 接口地址
     */
    private String url;

    /**
     * 请求类型，如 GET、POST 等
     */
    private String method;
    private String requestParams;

    /**
     * 请求头，JSON 格式存储
     */
    private String requestHeader;

    /**
     * 响应头，JSON 格式存储
     */
    private String responseHeader;


    private static final long serialVersionUID = 1L;
}
