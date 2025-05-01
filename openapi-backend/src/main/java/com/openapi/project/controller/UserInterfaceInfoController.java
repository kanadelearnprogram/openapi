package com.openapi.project.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.openapi.project.annotation.AuthCheck;
import com.openapi.project.common.BaseResponse;
import com.openapi.project.common.DeleteRequest;
import com.openapi.project.common.ErrorCode;
import com.openapi.project.common.ResultUtils;
import com.openapi.project.constant.UserConstant;
import com.openapi.project.exception.BusinessException;
import com.openapi.project.model.dto.userInterfaceInfo.UserInterfaceInfoAddRequest;
import com.openapi.project.model.dto.userInterfaceInfo.UserInterfaceInfoQueryRequest;
import com.openapi.project.model.dto.userInterfaceInfo.UserInterfaceInfoUpdateRequest;
import com.openapi.project.model.vo.InterfaceNumVO;
import com.openapi.project.service.UserInterfaceInfoService;
import com.openapi.project.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.example.openapiclientsdk.Client;
import org.openpai.common.model.entity.UserInterfaceInfo;
import org.openpai.common.model.entity.User;

import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;


@RestController
@RequestMapping("/userinterfaceinfo")
@Slf4j
public class UserInterfaceInfoController {

    @Resource
    private UserInterfaceInfoService userInterfaceInfoService;

    @Resource
    private UserService userService;
    @Resource
    private Client client;

    // region 增删改查

    /**
     * 创建
     *
     * @param UserInterfaceInfoAddRequest
     * @param request
     * @return
     */
    @PostMapping("/add")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Long> addInterfaceInfo(@RequestBody UserInterfaceInfoAddRequest UserInterfaceInfoAddRequest, HttpServletRequest request) {
        if (UserInterfaceInfoAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        UserInterfaceInfo userInterfaceInfo = new UserInterfaceInfo();
        BeanUtils.copyProperties(UserInterfaceInfoAddRequest, userInterfaceInfo);
        // 校验
        userInterfaceInfoService.validUserInterfaceInfo(userInterfaceInfo, true);
        User loginUser = userService.getLoginUser(request);
        userInterfaceInfo.setUserId(loginUser.getId());
        boolean result = userInterfaceInfoService.save(userInterfaceInfo);
        if (!result) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR);
        }
        long newInterfaceInfoId = userInterfaceInfo.getId();
        return ResultUtils.success(newInterfaceInfoId);
    }

    /**
     * 删除
     *
     * @param deleteRequest
     * @param request
     * @return
     */
    @PostMapping("/delete")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> deleteInterfaceInfo(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.getLoginUser(request);
        long id = deleteRequest.getId();
        // 判断是否存在
        UserInterfaceInfo oldInterfaceInfo = userInterfaceInfoService.getById(id);
        if (oldInterfaceInfo == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        // 仅本人或管理员可删除
        if (!oldInterfaceInfo.getUserId().equals(user.getId()) && !userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        boolean b = userInterfaceInfoService.removeById(id);
        return ResultUtils.success(b);
    }

    /**
     * 更新
     *
     * @param UserInterfaceInfoUpdateRequest
     * @param request
     * @return
     */
    @PostMapping("/update")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> updateInterfaceInfo(@RequestBody UserInterfaceInfoUpdateRequest UserInterfaceInfoUpdateRequest,
                                                     HttpServletRequest request) {
        if (UserInterfaceInfoUpdateRequest == null || UserInterfaceInfoUpdateRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        UserInterfaceInfo userInterfaceInfo = new UserInterfaceInfo();
        BeanUtils.copyProperties(UserInterfaceInfoUpdateRequest, userInterfaceInfo);

        // 参数校验
        userInterfaceInfoService.validUserInterfaceInfo(userInterfaceInfo, false);
        User user = userService.getLoginUser(request);
        long id = UserInterfaceInfoUpdateRequest.getId();
        // 判断是否存在
        UserInterfaceInfo oldInterfaceInfo = userInterfaceInfoService.getById(id);
        System.out.println(oldInterfaceInfo);
        if (oldInterfaceInfo == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        // 仅本人或管理员可修改
        if (!oldInterfaceInfo.getUserId().equals(user.getId()) && !userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        userInterfaceInfo.setId(id);
        boolean result = userInterfaceInfoService.updateById(userInterfaceInfo);
        //false
        return ResultUtils.success(result);
    }

    /**
     * 根据 id 获取
     *
     * @param id
     * @return
     */
    @GetMapping("/get")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<UserInterfaceInfo> getInterfaceInfoById(long id) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        UserInterfaceInfo userInterfaceInfo = userInterfaceInfoService.getById(id);
        return ResultUtils.success(userInterfaceInfo);
    }

    /**
     * 获取列表（仅管理员可使用）
     *
     * @param UserInterfaceInfoQueryRequest
     * @return
     */
    @AuthCheck(mustRole = "admin")
    @GetMapping("/list")
    public BaseResponse<List<UserInterfaceInfo>> listInterfaceInfo(UserInterfaceInfoQueryRequest UserInterfaceInfoQueryRequest) {
        UserInterfaceInfo interfaceInfoQuery = new UserInterfaceInfo();
        if (UserInterfaceInfoQueryRequest != null) {
            BeanUtils.copyProperties(UserInterfaceInfoQueryRequest, interfaceInfoQuery);
        }
        QueryWrapper<UserInterfaceInfo> queryWrapper = new QueryWrapper<>(interfaceInfoQuery);
        List<UserInterfaceInfo> InterfaceInfoList = userInterfaceInfoService.list(queryWrapper);
        return ResultUtils.success(InterfaceInfoList);
    }

    /*@GetMapping("/list/page")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    // 返回所有 接口信息 以及剩余次数
    public BaseResponse<Page<InterfaceInfo>> listInterfaceInfoByPage(UserInterfaceInfoQueryRequest UserInterfaceInfoQueryRequest, HttpServletRequest request) {
        if (UserInterfaceInfoQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        InterfaceInfo InterfaceInfoQuery = new InterfaceInfo();
        BeanUtils.copyProperties(UserInterfaceInfoQueryRequest, InterfaceInfoQuery);
        long current = UserInterfaceInfoQueryRequest.getCurrent();
        long size = UserInterfaceInfoQueryRequest.getPageSize();
        String sortField = UserInterfaceInfoQueryRequest.getSortField();
        String sortOrder = UserInterfaceInfoQueryRequest.getSortOrder();
        String des = InterfaceInfoQuery.getDescription();
        // content 需支持模糊搜索
        InterfaceInfoQuery.setDescription(null);
        // 限制爬虫
        if (size > 50) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        QueryWrapper<InterfaceInfo> queryWrapper = new QueryWrapper<>(InterfaceInfoQuery);
        queryWrapper.like(StringUtils.isNotBlank(des), "description", des);
        queryWrapper.orderBy(StringUtils.isNotBlank(sortField),
                sortOrder.equals(CommonConstant.SORT_ORDER_ASC), sortField);
        // Page<InterfaceInfo> InterfaceInfoPage = userInterfaceInfoService.page(new Page<>(current, size), queryWrapper);
        return ResultUtils.success(null);
    }*/
    @GetMapping("/list/page")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    // 返回所有 接口信息 以及剩余次数
    public BaseResponse<List<InterfaceNumVO>> listInterfaceInfoByList( HttpServletRequest request) {
        User loginUser = userService.getLoginUser(request);
        long userId = loginUser.getId();

        return ResultUtils.success(userInterfaceInfoService.getList(userId));
    }

    @PostMapping("/addnum")
    public BaseResponse<Boolean> addLeftNum(HttpServletRequest request,@RequestBody UserInterfaceInfoQueryRequest userInterfaceInfoQueryRequest) {
        long userId = userService.getLoginUser(request).getId();
        // 支付
        return ResultUtils.success(userInterfaceInfoService.addLeftNum(userId,userInterfaceInfoQueryRequest.getInterfaceInfoId()));
    }

}

