package org.openpai.common.service;


import com.baomidou.mybatisplus.extension.service.IService;
import org.openpai.common.model.entity.UserInterfaceInfo;

/**
* @author Lenovo
* @description 针对表【user_interface_info(用户调用接口信息表)】的数据库操作Service
* @createDate 2025-04-07 20:32:43
*/
public interface InnerUserInterfaceInfoService  {
    void validUserInterfaceInfo(UserInterfaceInfo userInterfaceInfo, boolean add);
    boolean invokeCount(long interfaceInfoId, long userId);

    boolean checkInterfaceIsActive(long interfaceInfoId, long userId);
}
