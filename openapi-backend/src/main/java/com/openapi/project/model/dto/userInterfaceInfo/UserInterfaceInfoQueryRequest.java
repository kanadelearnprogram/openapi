package com.openapi.project.model.dto.userInterfaceInfo;

import com.openapi.project.common.PageRequest;
import lombok.Data;

import java.io.Serializable;

/**
 * 查询请求
 *
 * @author yupi
 */

@Data
public class UserInterfaceInfoQueryRequest extends PageRequest implements Serializable {
    /**
     * 调用接口id
     */
    private Long interfaceInfoId;
    private static final long serialVersionUID = 1L;
}
