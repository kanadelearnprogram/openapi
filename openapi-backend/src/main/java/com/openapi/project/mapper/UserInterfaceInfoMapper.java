package com.openapi.project.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.openapi.project.model.vo.InterfaceNumVO;
import org.openpai.common.model.entity.UserInterfaceInfo;

import java.util.List;

/**
* @author Lenovo
* @description 针对表【user_interface_info(用户调用接口信息表)】的数据库操作Mapper
* @createDate 2025-04-07 20:32:43
* @Entity com.yupi.project.model.entity.UserInterfaceInfo
*/
public interface UserInterfaceInfoMapper extends BaseMapper<UserInterfaceInfo> {


    List<UserInterfaceInfo> listTopInvokeInterfaceInfo(int limit);
    List<InterfaceNumVO> listInterfaceNum(long userId);
}




