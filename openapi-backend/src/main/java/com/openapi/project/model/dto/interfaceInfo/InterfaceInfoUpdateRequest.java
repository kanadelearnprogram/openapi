package com.openapi.project.model.dto.interfaceInfo;

import lombok.Data;

import java.io.Serializable;

/**
 * 更新请求
 *
 * @TableName product
 */
@Data
public class InterfaceInfoUpdateRequest implements Serializable {

    /**

    /**
     * 接口名称
     */
    private Integer id;
    private String name;

    /**
     * 接口地址
     */
    private String url;
    private String description;

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

    /**
     * 接口状态：0 关闭，1 开启
     */
    private Integer status;


    private static final long serialVersionUID = 1L;
}
