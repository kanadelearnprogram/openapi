package org.example.simapi.controller;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;

import nl.flotsam.xeger.Xeger;
import org.example.openapiclientsdk.User;
import org.example.simapi.entity.Distance;
import org.example.simapi.entity.GenRegStr;
import org.example.simapi.entity.SongCi;
import org.example.simapi.job.ReadData;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.example.openapiclientsdk.Utils.SignUtils.getSign;


@RestController
@RequestMapping("/name")
public class sim {
    @Resource
    private RedisTemplate redisTemplate;
    private List<SongCi> readData() {
        Path path = Paths.get("src/main/resources/data/songci.json");
        try {
            // 使用 Files 类的 readAllBytes 方法，将文件的所有字节读取到一个 byte 数组中
            byte[] bytes = Files.readAllBytes(path);
            // 使用 Charset 类的 forName 方法，指定字符编码为 UTF-8，并将 byte 数组转换为字符串
            String json = new String(bytes, Charset.forName("UTF-8"));
            // 打印输出字符串
            JSONArray array = JSONUtil.parseArray(json);
            List<SongCi> songCiList = array.toList(SongCi.class);

            return songCiList;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
    @PostMapping("/post")
    public String postName(@RequestParam String name){

        return "simulate post"+name;
    }
    @GetMapping("/get")
    public String getName(String name, HttpServletRequest request){

        System.out.println("???");
        return "simulate get"+name;
    }
    @PostMapping("/user")
    public String getUserName(@RequestBody User user, HttpServletRequest request){
        String appKey = request.getHeader("appKey");
        String nonce = request.getHeader("nonce");
        String timestamp = request.getHeader("timestamp");
        String body = request.getHeader("body");
        String sign = request.getHeader("sign");
        //
        String result = "simulate post userName"+ user.getName();
        System.out.println(result);
        return result;
    }
    @GetMapping("/shici")
    public String getShici( HttpServletRequest request){
        ValueOperations<String ,List<SongCi>> valueOperations = redisTemplate.opsForValue();
        List<SongCi> songCiList =  valueOperations.get("gushi");
        if (songCiList== null){
            songCiList = readData();
            valueOperations.set("gushi",songCiList,60*60*1000, TimeUnit.DAYS);
        }
        SongCi para = songCiList.get(RandomUtil.randomInt(0,songCiList.size()));
        int size = para.getParagraphs().size();
        /*System.out.println(para.getRhythmic());
        System.out.println(para.getAuthor());
        System.out.println(para.getParagraphs().get(RandomUtil.randomInt(0,size - 1)));*/
        return para.getRhythmic() + "\n" + para.getAuthor() + "\n" +
                para.getParagraphs().get(RandomUtil.randomInt(0,size - 1));
    }
    //  正则表达式生成字符串
    @PostMapping("/reggen")
    public List<String> getRegStr(@RequestBody GenRegStr genRegStr){
        Xeger generator = new Xeger(genRegStr.getReg());
        List<String> stringList = new ArrayList<>();
        for (int i = 0; i < genRegStr.getNum(); i++) {
            stringList.add(generator.generate());
        }
        return stringList;
    }

    @PostMapping("/distance")
    public double getRegStr(@RequestBody Distance distance){
        double EARTH_RADIUS = 6371.0;
        double radLat1 = Math.toRadians(distance.getLat1());
        double radLon1 = Math.toRadians(distance.getLon1());
        double radLat2 = Math.toRadians(distance.getLat2());
        double radLon2 = Math.toRadians(distance.getLon2());

        // 差值
        double deltaLat = radLat2 - radLat1;
        double deltaLon = radLon2 - radLon1;

        // Haversine公式
        double a = Math.pow(Math.sin(deltaLat / 2), 2) +
                Math.cos(radLat1) * Math.cos(radLat2) *
                        Math.pow(Math.sin(deltaLon / 2), 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        // 计算距离
        return EARTH_RADIUS * c;

    }
}
