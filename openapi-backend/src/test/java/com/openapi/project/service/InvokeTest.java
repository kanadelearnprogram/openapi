package com.openapi.project.service;

import cn.hutool.core.util.RandomUtil;
import com.google.gson.Gson;
import org.example.openapiclientsdk.Client;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class InvokeTest {

    @Test
    public void invoke(){
        String appkey = "admin";
        String secretKey = "abc";
        String userRequestParams = "{\"name \":\"ccc\"}";
        Client tempClient = new Client(appkey,secretKey,2,1123456);
        Gson gson = new Gson();
        org.example.openapiclientsdk.User user1 = gson.fromJson(userRequestParams, org.example.openapiclientsdk.User.class);
        // TODO 根据path 和 method 决定请求方式
        String usernameByPost =  tempClient.getUserName(user1);
        String usernameByPost1 =  tempClient.getUserName(user1);
        String usernameByPost2 =  tempClient.getUserName(user1);
        String usernameByPost3 =  tempClient.getUserName(user1);
        String usernameByPost4 =  tempClient.getUserName(user1);
        String usernameByPost5 =  tempClient.getUserName(user1);
        String usernameByPost6 =  tempClient.getUserName(user1);
        String usernameByPost7 =  tempClient.getUserName(user1);
        String usernameByPost8 =  tempClient.getUserName(user1);

    }
    @Test
    public void exTest() throws Exception {
        String appkey = "admin";
        String secretKey = "abc";
        String userRequestParams = "{\"name \":\"ccc\"}";
        long start = System.currentTimeMillis();
        for (int i = 0; i < 100; i++) {

                try {
                    // 初始化 Client
                    Client tempClient = new Client(
                            appkey,
                            secretKey,
                            RandomUtil.randomInt(1200),
                            System.currentTimeMillis()
                    );
                    // 执行请求并打印结果
                    String result = tempClient.execute("POST", "/api/name/user", userRequestParams);
                    System.out.println("Response: " + result);

                } catch (Exception e) {
                    // 打印异常堆栈
                    e.printStackTrace();
                    throw new RuntimeException("请求失败", e);
                }

        }

        // 计算总时间
        long end = System.currentTimeMillis();
        long exTime = end - start;
        System.out.println(" " + exTime / 100  + " milseconds");
        // 25 millisecond




    }
}
