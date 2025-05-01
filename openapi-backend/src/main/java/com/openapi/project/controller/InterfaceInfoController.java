package com.openapi.project.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.openapi.project.annotation.AuthCheck;
import com.openapi.project.common.BaseResponse;
import com.openapi.project.common.DeleteRequest;
import com.openapi.project.common.ErrorCode;
import com.openapi.project.common.ResultUtils;
import com.openapi.project.constant.CommonConstant;
import com.openapi.project.exception.BusinessException;
import com.openapi.project.mapper.UserInterfaceInfoMapper;
import com.openapi.project.model.dto.interfaceInfo.*;

import com.openapi.project.model.enums.InterfaceInfoEnum;
import com.openapi.project.model.vo.InterfaceNumVO;
import com.openapi.project.service.InterfaceInfoService;
import com.openapi.project.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.example.openapiclientsdk.Client;
import org.openpai.common.model.entity.InterfaceInfo;
import org.openpai.common.model.entity.User;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 帖子接口
 *
 * @author yupi
 */
@RestController
@RequestMapping("/interfaceinfo")
@Slf4j
public class InterfaceInfoController {

    @Resource
    private InterfaceInfoService InterfaceInfoService;
    @Resource
    private UserInterfaceInfoMapper userInterfaceInfoMapper;

    @Resource
    private UserService userService;
    @Resource
    private Client client;

    // region 增删改查

    /**
     * 创建
     *
     * @param InterfaceInfoAddRequest
     * @param request
     * @return
     */
    @PostMapping("/add")
    public BaseResponse<Long> addInterfaceInfo(@RequestBody InterfaceInfoAddRequest InterfaceInfoAddRequest, HttpServletRequest request) {
        if (InterfaceInfoAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        InterfaceInfo InterfaceInfo = new InterfaceInfo();
        BeanUtils.copyProperties(InterfaceInfoAddRequest, InterfaceInfo);
        // 校验
        InterfaceInfoService.validInterfaceInfo(InterfaceInfo, true);
        User loginUser = userService.getLoginUser(request);
        InterfaceInfo.setUserId(loginUser.getId());
        boolean result = InterfaceInfoService.save(InterfaceInfo);
        if (!result) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR);
        }
        long newInterfaceInfoId = InterfaceInfo.getId();
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
    public BaseResponse<Boolean> deleteInterfaceInfo(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.getLoginUser(request);
        long id = deleteRequest.getId();
        // 判断是否存在
        InterfaceInfo oldInterfaceInfo = InterfaceInfoService.getById(id);
        if (oldInterfaceInfo == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        // 仅本人或管理员可删除
        if (!oldInterfaceInfo.getUserId().equals(user.getId()) && !userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        boolean b = InterfaceInfoService.removeById(id);
        return ResultUtils.success(b);
    }

    /**
     * 更新
     *
     * @param InterfaceInfoUpdateRequest
     * @param request
     * @return
     */
    @PostMapping("/update")
    public BaseResponse<Boolean> updateInterfaceInfo(@RequestBody InterfaceInfoUpdateRequest InterfaceInfoUpdateRequest,
                                            HttpServletRequest request) {
        if (InterfaceInfoUpdateRequest == null || InterfaceInfoUpdateRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        InterfaceInfo InterfaceInfo = new InterfaceInfo();
        BeanUtils.copyProperties(InterfaceInfoUpdateRequest, InterfaceInfo);

        // 参数校验
        InterfaceInfoService.validInterfaceInfo(InterfaceInfo, false);
        User user = userService.getLoginUser(request);
        long id = InterfaceInfoUpdateRequest.getId();
        // 判断是否存在
        InterfaceInfo oldInterfaceInfo = InterfaceInfoService.getById(id);
        System.out.println(oldInterfaceInfo);
        if (oldInterfaceInfo == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        // 仅本人或管理员可修改
        if (!oldInterfaceInfo.getUserId().equals(user.getId()) && !userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        InterfaceInfo.setId(id);
        boolean result = InterfaceInfoService.updateById(InterfaceInfo);
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
    public BaseResponse<InterfaceInfo> getInterfaceInfoById(long id) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        InterfaceInfo InterfaceInfo = InterfaceInfoService.getById(id);
        return ResultUtils.success(InterfaceInfo);
    }
    @GetMapping("getleftnum")
    public BaseResponse<List<InterfaceNumVO>> getInterfaceLeftNumById(HttpServletRequest request) {
        User user = userService.getLoginUser(request);
         long userid  = user.getId();
        List<InterfaceNumVO> interfaceNumVOList =  userInterfaceInfoMapper.listInterfaceNum(userid);
        return ResultUtils.success(interfaceNumVOList);
    }

    /**
     * 获取列表（仅管理员可使用）
     *
     * @param InterfaceInfoQueryRequest
     * @return
     */
    @AuthCheck(mustRole = "admin")
    @GetMapping("/list")
    public BaseResponse<List<InterfaceInfo>> listInterfaceInfo(InterfaceInfoQueryRequest InterfaceInfoQueryRequest) {
        InterfaceInfo InterfaceInfoQuery = new InterfaceInfo();
        if (InterfaceInfoQueryRequest != null) {
            BeanUtils.copyProperties(InterfaceInfoQueryRequest, InterfaceInfoQuery);
        }
        QueryWrapper<InterfaceInfo> queryWrapper = new QueryWrapper<>(InterfaceInfoQuery);
        List<InterfaceInfo> InterfaceInfoList = InterfaceInfoService.list(queryWrapper);
        return ResultUtils.success(InterfaceInfoList);
    }

    /**
     * 分页获取列表
     *
     * @param InterfaceInfoQueryRequest
     * @param request
     * @return
     */
    @GetMapping("/list/page")
    public BaseResponse<Page<InterfaceInfo>> listInterfaceInfoByPage(InterfaceInfoQueryRequest InterfaceInfoQueryRequest, HttpServletRequest request) {
        if (InterfaceInfoQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        InterfaceInfo InterfaceInfoQuery = new InterfaceInfo();
        BeanUtils.copyProperties(InterfaceInfoQueryRequest, InterfaceInfoQuery);
        long current = InterfaceInfoQueryRequest.getCurrent();
        long size =
                InterfaceInfoQueryRequest.getPageSize();
        String sortField = InterfaceInfoQueryRequest.getSortField();
        String sortOrder = InterfaceInfoQueryRequest.getSortOrder();
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
        Page<InterfaceInfo> InterfaceInfoPage = InterfaceInfoService.page(new Page<>(current, size), queryWrapper);
        return ResultUtils.success(InterfaceInfoPage);
    }

    // endregion
    @PostMapping("/online")
    @AuthCheck(mustRole = "admin")
    public BaseResponse<Boolean> onlineInterfaceInfo(@RequestBody IdRequest idRequest, HttpServletRequest request) {
        long id = idRequest.getId();
        if (idRequest == null||id <= 0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        InterfaceInfo interfaceInfo = InterfaceInfoService.getById(id);
        if (interfaceInfo == null){
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        org.example.openapiclientsdk.User user = new org.example.openapiclientsdk.User();
        user.setName("test");
        // 根据测试地址调用
        String username = client.getUserName(user);
        if (StringUtils.isBlank(username)){
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        InterfaceInfo interfaceInfo1 =new InterfaceInfo();
        interfaceInfo1.setId(id);
        interfaceInfo1.setStatus(InterfaceInfoEnum.ONLINE.getValue());
        boolean result = InterfaceInfoService.updateById(interfaceInfo1);
        return ResultUtils.success(result);
    }
    @PostMapping("/offline")
    @AuthCheck(mustRole = "admin")
    public BaseResponse<Boolean> offlineInterfaceInfo(@RequestBody IdRequest idRequest, HttpServletRequest request) {
        long id = idRequest.getId();
        if (idRequest == null||id <= 0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        InterfaceInfo interfaceInfo = InterfaceInfoService.getById(id);
        if (interfaceInfo == null){
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }

        InterfaceInfo interfaceInfo1 =new InterfaceInfo();
        interfaceInfo1.setId(id);
        interfaceInfo1.setStatus(InterfaceInfoEnum.OFFLINE.getValue());
        boolean result = InterfaceInfoService.updateById(interfaceInfo1);
        return ResultUtils.success(result);

    }

    @PostMapping("/invoke")
    public BaseResponse<String> invokeInterfaceInfo(@RequestBody InterfaceInfoInvokeRequest interfaceInfoInvokeRequest, HttpServletRequest request) throws Exception {
        long id = interfaceInfoInvokeRequest.getId();
        if (interfaceInfoInvokeRequest == null||id <= 0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String userRequestParams = interfaceInfoInvokeRequest.getUserRequestParams();
        InterfaceInfo interfaceInfo = InterfaceInfoService.getById(id);
        if (interfaceInfo == null){
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        if (interfaceInfo.getStatus() == InterfaceInfoEnum.OFFLINE.getValue()){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR);
        }

        User user = userService.getLoginUser(request);
        String appkey = user.getAppKey();
        String secretKey = user.getSecretKey();
        Integer nonce = interfaceInfoInvokeRequest.getNonce();
        long timestamp = interfaceInfoInvokeRequest.getTimestamp();
        Client tempClient = new Client(appkey,secretKey,nonce,timestamp);
        //  如何通过后端一个接口调用接口
        //  根据path 和 method 决定请求方式 接收 nonce timestamp
        String method = interfaceInfoInvokeRequest.getMethod();
        String url = interfaceInfoInvokeRequest.getUrl();
        String res = tempClient.execute(method,url,userRequestParams);
        //String usernameByPost =  tempClient.getUserName(user1);
        return ResultUtils.success(res);

    }
}
