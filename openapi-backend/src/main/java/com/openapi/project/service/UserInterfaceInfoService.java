package com.openapi.project.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.openapi.project.model.vo.InterfaceNumVO;
import org.openpai.common.model.entity.UserInterfaceInfo;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface UserInterfaceInfoService extends IService<UserInterfaceInfo> {


    void validUserInterfaceInfo(UserInterfaceInfo userInterfaceInfo, boolean add);

    /**
     * 调用接口统计
     * @param interfaceInfoId
     * @param userId
     * @return
     */
    boolean invokeCount(long interfaceInfoId, long userId);

    List<InterfaceNumVO> getList(long userId);

    Boolean addLeftNum(long userId, long interfaceInfoId);

    CompletableFuture<Boolean> invokeCountAsync(long interfaceInfoId, long userId);
    Boolean checkInvoke(long interfaceInfoId, long userId);
}
