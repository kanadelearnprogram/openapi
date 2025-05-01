package org.openpai.common.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 接口信息表
 * @TableName interface_info
 */
@TableName(value ="interface_info")
@Data
public class InterfaceInfo implements Serializable {
    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
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

    /**
     * 是否删除：0 未删除，1 已删除
     */
    @TableLogic
    private Integer isDelete;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;
}
