package org.example.simapi;

import nl.flotsam.xeger.Xeger;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class PhoneTest {

    private static final double EARTH_RADIUS = 6371.0;

    @Test
    public void phoneTest(){

        Xeger generator = new Xeger("[A-Z0-9]{8}-[A-Z0-9]{4}-[A-Z0-9]{4}-[A-Z0-9]{12}");
        List<String> stringList = new ArrayList<>();
        String res = generator.generate();
         ;
        System.out.println(res);
        /*for (int i = 0; i < 10; i++) {
            String res = generator.generate();
            stringList.add(generator.generate());
            System.out.println(res);
        }*/

        /*
            System.out.println(generateStringFromRegex());  // 8个随机数字
            System.out.println(generateStringFromRegex("[a-z]{10}"));  // 10个随机小写字母
            System.out.println(generateStringFromRegex("[a-z]{10}[0-9]{3}"));  // 字母+数字
            System.out.println(generateStringFromRegex("[\u4e00-\u9fa5]{20}"));  // 20个汉字（包括繁体）
            System.out.println(generateStringFromRegex("[a-z]{5}\\@[a-z]{3}\\.com"));  // 随机 Email 地址
        */

    }
    @Test
    public void distanceTest(){
        double lat1 = 39.9042; // 北京的纬度
        double lon1 = 116.4074; // 北京的经度
        double lat2 = 23.125178; // 上海的纬度
        double lon2 = 113.280637; // 上海的经度


        double radLat1 = Math.toRadians(lat1);
        double radLon1 = Math.toRadians(lon1);
        double radLat2 = Math.toRadians(lat2);
        double radLon2 = Math.toRadians(lon2);

        // 差值
        double deltaLat = radLat2 - radLat1;
        double deltaLon = radLon2 - radLon1;

        // Haversine公式
        double a = Math.pow(Math.sin(deltaLat / 2), 2) +
                Math.cos(radLat1) * Math.cos(radLat2) *
                        Math.pow(Math.sin(deltaLon / 2), 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        // 计算距离
        System.out.println(EARTH_RADIUS * c);
    }
}
