package com.openapi.project.service;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.openapi.project.mapper.UserInterfaceInfoMapper;
import org.junit.jupiter.api.Test;
import org.openpai.common.model.entity.UserInterfaceInfo;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
@SpringBootTest//npe
public class InvokeAnalysis {
    @Resource
    UserInterfaceInfoMapper userInterfaceInfoMapper;
     @Resource
    private UserInterfaceInfoService userInterfaceInfoService;

    @Test
    public void findTest() {

        UpdateWrapper<UserInterfaceInfo> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("interfaceInfoId", 1)
                .eq("userId", 1)
                .gt("leftNum", 0);
                //.setSql("leftNum = leftNum - 1, totalNum = totalNum + 1");

        System.out.println(userInterfaceInfoMapper.selectOne(updateWrapper));
       // boolean update = userInterfaceInfoService.update(updateWrapper);
        //System.out.println(update);
    }
    @Test
    public void finddddTest() {
        System.out.println(userInterfaceInfoMapper.listInterfaceNum(1));

    }
}
