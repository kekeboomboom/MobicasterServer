package com.keboom.Appserver;

import com.keboom.Appserver.controller.vo.DeviceInfo;
import lombok.SneakyThrows;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

/**
 * {@code @author:} keboom
 * {@code @date:} 2023/9/22
 * {@code @description:}
 */
public class RestTemplateTest {

    @SneakyThrows
    @Test
    void getTest() {
        RestTemplate restTemplate = new RestTemplate();


        ResponseEntity<String> forEntity = restTemplate.postForEntity("http://127.0.0.1:8081/mobicaster/deviceInfo", new DeviceInfo(), String.class);
        JSONObject jsonObject = new JSONObject(forEntity.getBody());
        System.out.println(jsonObject.toString());
        int code = jsonObject.getInt("code");
        if (code != 200) {
            System.out.println("error");
        } else {
            System.out.println("success");
        }


    }
}
