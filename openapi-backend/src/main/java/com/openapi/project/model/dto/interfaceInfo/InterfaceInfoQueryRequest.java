package com.openapi.project.model.dto.interfaceInfo;

import com.openapi.project.common.PageRequest;
import lombok.Data;

import java.io.Serializable;

/**
 * 查询请求
 *
 * @author yupi
 */

@Data
public class InterfaceInfoQueryRequest extends PageRequest implements Serializable {
    /**
     * 主键ID
     */

    private Long id;

    /**
     * 用户ID，关联用户表
     */
    private Long userId;
    private String description;

    /**
     * 接口名称
     */
    private String name;

    /**
     * 接口地址
     */
    private String url;

    /**
     * 请求类型，如 GET、POST 等
     */
    private String method;

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
