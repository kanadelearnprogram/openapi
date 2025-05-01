package com.openapi.project.service.impl;

import com.openapi.project.mapper.UserInterfaceInfoMapper;
import com.openapi.project.service.UserInterfaceInfoService;
import org.apache.dubbo.config.annotation.DubboService;
import org.openpai.common.model.entity.UserInterfaceInfo;
import org.openpai.common.service.InnerUserInterfaceInfoService;

import javax.annotation.Resource;

@DubboService
public class InnerUserInterfaceInfoServiceImpl implements InnerUserInterfaceInfoService {


    @Resource
    UserInterfaceInfoService userInterfaceInfoService;
    @Resource
    UserInterfaceInfoMapper userInterfaceInfoMapper;
    @Override
    public void validUserInterfaceInfo(UserInterfaceInfo userInterfaceInfo, boolean add) {

    }

    @Override
    public boolean invokeCount(long interfaceInfoId, long userId) {
        userInterfaceInfoService.invokeCountAsync(interfaceInfoId,userId)
                .exceptionally(ex->{
                    return false;
                });
        return true;
    }

    @Override
    public boolean checkInterfaceIsActive(long interfaceInfoId, long userId) {
        return userInterfaceInfoService.checkInvoke(interfaceInfoId,userId);
    }
}
