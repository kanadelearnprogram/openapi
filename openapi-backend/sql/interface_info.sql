
CREATE TABLE `interface_info` (
                                 `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
                                 `userId` BIGINT UNSIGNED NOT NULL COMMENT '用户ID，关联用户表',
                                 `name` VARCHAR(255) NOT NULL COMMENT '接口名称',
                                 `url` VARCHAR(1024) NOT NULL COMMENT '接口地址',
                                 `method` VARCHAR(10) NOT NULL COMMENT '请求类型，如 GET、POST 等',
                                 `requestHeader` TEXT COMMENT '请求头，JSON 格式存储',
                                 `responseHeader` TEXT COMMENT '响应头，JSON 格式存储',
                                 `status` TINYINT NOT NULL DEFAULT 1 COMMENT '接口状态：0 关闭，1 开启',
                                 `isDelete` TINYINT NOT NULL DEFAULT 0 COMMENT '是否删除：0 未删除，1 已删除',
                                 `createTime` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                 `updateTime` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                 PRIMARY KEY (`id`),
                                 KEY `idx_user_id` (`userId`) COMMENT '用户ID索引'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='接口信息表';
