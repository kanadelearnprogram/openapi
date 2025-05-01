package com.openapi.project.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.openapi.project.common.ErrorCode;
import com.openapi.project.exception.BusinessException;
import com.openapi.project.mapper.UserInterfaceInfoMapper;
import com.openapi.project.model.vo.InterfaceNumVO;
import com.openapi.project.service.UserInterfaceInfoService;
import org.openpai.common.model.entity.UserInterfaceInfo;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
* @author Lenovo
* @description 针对表【user_interface_info(用户调用接口信息表)】的数据库操作Service实现
* @createDate 2025-04-07 20:32:43
*/
@Service
public class UserInterfaceInfoServiceImpl extends ServiceImpl<UserInterfaceInfoMapper, UserInterfaceInfo>
    implements UserInterfaceInfoService {

    @Resource
    UserInterfaceInfoMapper userInterfaceInfoMapper;

    @Override
    public void validUserInterfaceInfo(UserInterfaceInfo userInterfaceInfo, boolean add) {
        if (userInterfaceInfo == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        Long interfaceInfoId = userInterfaceInfo.getInterfaceInfoId();
        Long userId = userInterfaceInfo.getUserId();
        Integer status = userInterfaceInfo.getStatus();
        Integer totalNum = userInterfaceInfo.getTotalNum();
        Integer leftNum = userInterfaceInfo.getLeftNum();

        if (add){
            if (userInterfaceInfo.getInterfaceInfoId() <= 0|| userInterfaceInfo.getUserId() <= 0){
                throw new BusinessException(ErrorCode.PARAMS_ERROR);
            }
        }
        if (userInterfaceInfo.getLeftNum()< 0){
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
    }

    @Async
    public CompletableFuture<Boolean> invokeCountAsync(long interfaceInfoId, long userId) {
        return CompletableFuture.completedFuture(invokeCount(interfaceInfoId, userId));
    }
    @Override
    public boolean invokeCount(long interfaceInfoId, long userId) {
        if (!checkInvoke(interfaceInfoId,userId)){
            throw new BusinessException(ErrorCode.OPERATION_ERROR);
        }
        UpdateWrapper<UserInterfaceInfo> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("interfaceInfoId",interfaceInfoId);
        updateWrapper.eq("userId",userId);
        updateWrapper.setSql("leftNum = leftNum - 1,totalNum = totalNum + 1");
        boolean update = this.update(updateWrapper);

        return update;
    }
    public Boolean checkInvoke(long interfaceInfoId, long userId){
        if (interfaceInfoId <= 0 || userId <= 0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        //  检查是否有剩余次数
        UpdateWrapper<UserInterfaceInfo> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("interfaceInfoId",interfaceInfoId);
        updateWrapper.eq("userId",userId);
        // 校验 剩余 > 0
        UserInterfaceInfo userInterfaceInfo = userInterfaceInfoMapper.selectOne(updateWrapper);
        if (userInterfaceInfo == null){
            // 创建条目
            UserInterfaceInfo userInterfaceInfo1 = new UserInterfaceInfo();
            userInterfaceInfo1.setUserId(userId);
            userInterfaceInfo1.setInterfaceInfoId(interfaceInfoId);
            this.save(userInterfaceInfo1);
            return false;
        }
        if (userInterfaceInfo.getLeftNum() == 0){
            return false;
        }
        return true;
    }

    @Override
    public List<InterfaceNumVO> getList(long userId) {
        List<InterfaceNumVO> list = userInterfaceInfoMapper.listInterfaceNum(userId);
        return list;
    }

    @Override
    public Boolean addLeftNum(long userId, long interfaceInfoId) {
        QueryWrapper<UserInterfaceInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userId", userId);
        queryWrapper.eq("interfaceInfoId", interfaceInfoId);

        // 查询是否存在记录
        UserInterfaceInfo userInterfaceInfo = userInterfaceInfoMapper.selectOne(queryWrapper);

        if (userInterfaceInfo == null) {
            // 记录不存在：创建新记录并初始化剩余次数为100
            UserInterfaceInfo newUser = new UserInterfaceInfo();
            newUser.setUserId(userId);
            newUser.setInterfaceInfoId(interfaceInfoId);
            newUser.setLeftNum(100);
            this.save(newUser);
        } else {
            // 记录存在：增加剩余次数
            int currentNum = userInterfaceInfo.getLeftNum();
            userInterfaceInfo.setLeftNum(currentNum + 100);
            UpdateWrapper updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("userId", userId);
            updateWrapper.eq("interfaceInfoId", interfaceInfoId);
            updateWrapper.setSql("leftNum = leftNum + 100");
            this.update(updateWrapper);
        }
        return true;
    }


}




