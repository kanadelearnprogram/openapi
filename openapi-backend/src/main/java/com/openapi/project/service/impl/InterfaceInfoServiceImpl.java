package com.openapi.project.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.openapi.project.common.ErrorCode;
import com.openapi.project.exception.BusinessException;
import com.openapi.project.mapper.InterfaceInfoMapper;
import com.openapi.project.service.InterfaceInfoService;

import org.apache.commons.lang3.StringUtils;
import org.openpai.common.model.entity.InterfaceInfo;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
* @author Lenovo
* @description 针对表【interface_info(接口信息表)】的数据库操作Service实现
* @createDate 2025-04-01 20:45:01
*/
@Service
public class InterfaceInfoServiceImpl extends ServiceImpl<InterfaceInfoMapper, InterfaceInfo>
    implements InterfaceInfoService{

    @Override
    public void validInterfaceInfo(InterfaceInfo interfaceInfo, boolean add) {
        if (interfaceInfo == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        String name = interfaceInfo.getName();
        String method = interfaceInfo.getMethod();
        Long userId = interfaceInfo.getUserId();
        String url = interfaceInfo.getUrl();
        Integer status = interfaceInfo.getStatus();
        String RequestHeader = interfaceInfo.getRequestHeader();
        String ResponseHeader = interfaceInfo.getResponseHeader();
        String des = interfaceInfo.getDescription();

        // 创建时，所有参数必须非空
        if (add) {
            // 检查必填字段是否为空
            if (StringUtils.isAnyBlank(name, method, url)) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "名称、HTTP方法、URL不能为空");
            }
            // 检查用户ID是否有效
            if (userId == null || userId <= 0) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户ID无效");
            }
            // 检查状态码是否有效（假设0-1为有效状态）
            if (status == null || (status < 0 || status > 1)) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "状态值无效，必须为0或1");
            }
        }

// 校验名称长度
        if (StringUtils.isNotBlank(name) && name.length() > 50) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "接口名称过长（最大50字符）");
        }

// 校验HTTP方法有效性
       final Set<String> ALLOWED_METHODS = new HashSet<>(Arrays.asList("GET", "POST", "PUT", "DELETE", "HEAD", "OPTIONS", "PATCH"));
        if (StringUtils.isNotBlank(method) && !ALLOWED_METHODS.contains(method.toUpperCase())) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "无效的HTTP方法，允许值：GET/POST/PUT/DELETE/HEAD/OPTIONS/PATCH");
        }

// 校验URL格式
        if (StringUtils.isNotBlank(url)) {
            if (url.length() > 255) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "URL过长（最大255字符）");
            }
            if (!url.startsWith("/")) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "URL必须以'/'开头");
            }
        }

// 校验请求头长度
        if (StringUtils.isNotBlank(RequestHeader) && RequestHeader.length() > 1024) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求头过长（最大1024字符）");
        }

// 校验响应头长度
        if (StringUtils.isNotBlank(ResponseHeader) && ResponseHeader.length() > 1024) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "响应头过长（最大1024字符）");
        }

// 校验描述长度
        if (StringUtils.isNotBlank(des) && des.length() > 200) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "描述信息过长（最大200字符）");
        }

    }


}




