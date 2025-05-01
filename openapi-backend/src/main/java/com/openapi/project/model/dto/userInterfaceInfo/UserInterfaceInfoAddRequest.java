package com.openapi.project.model.dto.userInterfaceInfo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 创建请求
 *
 * @TableName product
 */
@Data
public class UserInterfaceInfoAddRequest implements Serializable {


    /**
     * 接口调用次数
     */
    private Integer totalNum;

    /**
     * 剩余接口调用次数
     */
    private Integer leftNum;

    /**
     * 状态
     */
    private Integer status;


    private static final long serialVersionUID = 1L;
}
