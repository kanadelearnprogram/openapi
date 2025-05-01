package com.openapi.project.service;

import com.openapi.project.mapper.UserInterfaceInfoMapper;
import com.openapi.project.model.vo.InterfaceNumVO;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.List;

@SpringBootTest
public class UserInterfaceInfoTest {
    @Resource
    private UserInterfaceInfoService userInterfaceInfoService;
    @Resource
    UserInterfaceInfoMapper userInterfaceInfoMapper;
    @Test
    void testAddUser() {
       // boolean b = userInterfaceInfoService.invokeCount(1,1);
        //List<UserInterfaceInfo> userInterfaceInfoList = userInterfaceInfoMapper.listTopInvokeInterfaceInfo(3);
        List<InterfaceNumVO> interfaceNumVOList =  userInterfaceInfoMapper.listInterfaceNum(1);
        System.out.println("\n\n\n\n");
        System.out.println(interfaceNumVOList);
    }

    @Test
    void testAddUseeeer() {
        userInterfaceInfoService.addLeftNum(1,1);

    }


}
