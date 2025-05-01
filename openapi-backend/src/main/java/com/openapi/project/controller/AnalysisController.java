package com.openapi.project.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.openapi.project.annotation.AuthCheck;
import com.openapi.project.common.BaseResponse;
import com.openapi.project.common.ResultUtils;
import com.openapi.project.mapper.UserInterfaceInfoMapper;
import com.openapi.project.model.vo.InterfaceInfoVO;
import com.openapi.project.service.InterfaceInfoService;
import lombok.extern.slf4j.Slf4j;
import org.openpai.common.model.entity.InterfaceInfo;
import org.openpai.common.model.entity.UserInterfaceInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/analysis")
@Slf4j
public class AnalysisController {
    @Resource
    private UserInterfaceInfoMapper userInterfaceInfoMapper;
    @Resource
    private InterfaceInfoService interfaceInfoService;
    @GetMapping("/top/interface/invoke")
    @AuthCheck(mustRole = "admin")
    public BaseResponse<List<InterfaceInfoVO>> listTopInvokeInterfaceInfo() {
// 获取调用次数前三的接口统计信息
        List<UserInterfaceInfo> userInterfaceInfoList = userInterfaceInfoMapper.listTopInvokeInterfaceInfo(3);
        if (CollectionUtils.isEmpty(userInterfaceInfoList)) {
            return ResultUtils.success(Collections.emptyList());
        }
        //System.out.println("userInterfaceInfo"+userInterfaceInfoList);//3

// 按接口ID分组（修复1：使用接口ID分组）
        Map<Long, List<UserInterfaceInfo>> interfaceIdMap = userInterfaceInfoList.stream()
                .collect(Collectors.groupingBy(UserInterfaceInfo::getInterfaceInfoId));
        //System.out.println("interfaceMap"+interfaceIdMap);//3
// 处理空keySet的情况（修复2：避免生成无效SQL）
        if (interfaceIdMap.isEmpty()) {
            return ResultUtils.success(Collections.emptyList());
        }


        QueryWrapper<InterfaceInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("id", interfaceIdMap.keySet());
        List<InterfaceInfo> interfaceInfoList = interfaceInfoService.list(queryWrapper);
        //System.out.println("interfaceList"+interfaceInfoList);
// 调整空结果处理逻辑（修复3：使用业务数据为空状态码）
        if (CollectionUtils.isEmpty(interfaceInfoList)) {
            return ResultUtils.success(Collections.emptyList());
        }


// 转换VO对象（修复4：使用正确ID映射并添加空检查）
        List<InterfaceInfoVO> voList = interfaceInfoList.stream().map(info -> {
            InterfaceInfoVO vo = new InterfaceInfoVO();
            BeanUtils.copyProperties(info, vo);

            List<UserInterfaceInfo> infoList = interfaceIdMap.get(info.getId());
            if (!CollectionUtils.isEmpty(infoList)) {
                vo.setTotalNum(infoList.get(0).getTotalNum());
            } else {
                // 根据业务需求设置默认值或记录日志
                vo.setTotalNum(0);
            }
            return vo;
        }).collect(Collectors.toList());

        //System.out.println("volist"+voList);
        return ResultUtils.success(voList);
    }
}
