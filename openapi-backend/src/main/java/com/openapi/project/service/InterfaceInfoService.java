package com.openapi.project.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.openpai.common.model.entity.InterfaceInfo;

/**
* @author Lenovo
* @description 针对表【interface_info(接口信息表)】的数据库操作Service
* @createDate 2025-04-01 20:45:01
*/
public interface InterfaceInfoService extends IService<InterfaceInfo> {

    void validInterfaceInfo(InterfaceInfo interfaceInfo, boolean add);
}
