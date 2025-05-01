package com.openapi.project.model.dto.interfaceInfo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
@Data
public class InterfaceInfoInvokeRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;

    private String url;

    private String method;

    private String userRequestParams;

    private Integer nonce;

    private long timestamp;

}
