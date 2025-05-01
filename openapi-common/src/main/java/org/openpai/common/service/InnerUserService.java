package org.openpai.common.service;


import org.openpai.common.model.entity.User;

/**
 * 用户服务
 *
 * @author yupi
 */
public interface InnerUserService {

    User getInvokeUser(String appKey);
}
