package com.openapi.project.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.openapi.project.common.ErrorCode;
import com.openapi.project.exception.BusinessException;
import com.openapi.project.mapper.UserMapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboService;
import org.openpai.common.model.entity.User;
import org.openpai.common.service.InnerUserService;

import javax.annotation.Resource;

@DubboService
public class InnerUserServiceImpl implements InnerUserService {

    @Resource
    private UserMapper userMapper;
    @Override
    public User getInvokeUser(String appKey) {
        if (StringUtils.isAnyBlank(appKey)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("appKey",appKey);
        return userMapper.selectOne(queryWrapper);
    }
}
