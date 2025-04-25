package com.example.folder.upload.entity;


import cn.hutool.core.map.MapUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Objects;


public class Test {
    public static void main(String[] args) throws JsonProcessingException {
        String json = "{\"id\":123,\"username\":\"john_doe\",\"email\":\"john@example.com\",\"isActive\":false}";

        // jackson
        ObjectMapper objectMapper = new ObjectMapper();
        User user = objectMapper.readValue(json, User.class);
        System.out.println(user);

        //   Gson
        Gson gson = new Gson();
        User user1 = gson.fromJson(json, User.class);
        System.out.println(user1);

        //hutu
        JSONObject jsonObjectHutu = JSONUtil.parseObj(json);
        System.out.println(jsonObjectHutu);
        System.out.println("111111111111");
        System.out.println(JSON.parse(json));

        User user2 = JSON.parseObject(json, User.class);
        System.out.println(user2);

        User user3 = JSON.parseObject(json, new TypeReference<User>() {});
        System.out.println(user3);

        //fastjson ali
        com.alibaba.fastjson.JSONObject jsonObjectALi = JSON.parseObject(json);
        System.out.println(jsonObjectALi);


        User user4 = JSON.parseObject(json, User.class);
        System.out.println(user4);

        HashMap<String, Object> stringStringHashMap = new HashMap<>();
        stringStringHashMap.put("id",1);
        stringStringHashMap.put("username","john_doe");
        stringStringHashMap.put("email","john@example.com");
        stringStringHashMap.put("isActive",true);

        ObjectMapper objectMapper2 = new ObjectMapper();
        User person = objectMapper2.convertValue(stringStringHashMap, User.class);
        System.out.println(person);


        User user5 = JSON.parseObject(JSON.toJSONString(stringStringHashMap), User.class);
        System.out.println(user5);

        Gson gson1 = new Gson();
        User user6 = gson1.fromJson(gson1.toJson(stringStringHashMap), User.class);
        System.out.println(user6);

    }
}
