package com.openapi.project.model.vo;

import lombok.Data;
import org.openpai.common.model.entity.InterfaceInfo;

@Data
public class InterfaceNumVO {
    // 需与sql字段一致
    private Long id;
    private String interfaceName;
    private String des;
    private Integer leftNum;
}
