package com.openapi.project.model.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.openpai.common.model.entity.InterfaceInfo;

@EqualsAndHashCode(callSuper = true)
@Data
public class InterfaceInfoVO extends InterfaceInfo {

    private Integer totalNum;

    private static final long serialVersionUID = 1L;
}
