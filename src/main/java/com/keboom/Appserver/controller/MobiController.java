package com.keboom.Appserver.controller;

import com.alibaba.fastjson2.JSONObject;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.keboom.Appserver.controller.vo.*;
import jakarta.annotation.Resource;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.messaging.simp.user.SimpUser;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * {@code @author:} keboom
 * {@code @date:} 2023/9/21
 * {@code @description:}
 */
@Slf4j
@RestController
public class MobiController {

    private final SimpUserRegistry simpUserRegistry;

    @Value("${cogent-admin}")
    private String cogentAdmin;
    private static final ConcurrentHashMap<String, MediaInfoReq> mediaInfoMap = new ConcurrentHashMap<>();

    // 由于websocket并不是像http那样一问一答，我需要一个池子存放响应结果。由于数量不多，可以暂时这么写。
    private static final ConcurrentHashMap<String, Object> respPool = new ConcurrentHashMap<>();

    @Resource
    private RestTemplate restTemplate;

    private final SimpMessagingTemplate template;

    @Autowired
    public MobiController(SimpMessagingTemplate template, SimpUserRegistry simpUserRegistry) {
        this.template = template;
        this.simpUserRegistry = simpUserRegistry;
    }

    @GetMapping("/online/users")
    public RespVO onlineUsers() {
        Set<SimpUser> users = simpUserRegistry.getUsers();
        return new RespVO(200, "success", users.stream().map(SimpUser::getName).collect(Collectors.toList()));
    }

    @GetMapping("/deviceInfo/{androidID}")
    public RespVO deviceInfo(@PathVariable String androidID) throws InterruptedException {
        this.template.convertAndSendToUser(androidID, "/topic/queryParam", "");

        int tryCount = 0;
        while (tryCount++ < 3) {
            Thread.sleep((long) Math.pow(10, tryCount) * tryCount);
            // 这个get很可能获得到的是旧的数据，但是感觉关系不大，这个get接口只有设备上线的时候调用
            // 如果设备上线了，大部分情况都能够获得设备media信息
            MediaInfoReq mediaInfo = mediaInfoMap.get(androidID);
            if (mediaInfo != null) {
                return new RespVO(200, "success", mediaInfo);
            }
        }
        return new RespVO(500, "error", null);
    }

    @MessageMapping("/queryParam")
    public void deviceMediaInfo(@Payload MediaInfoReq mediaInfoReq) {
        log.info("deviceMediaInfo: " + mediaInfoReq.toString());
        mediaInfoMap.put(mediaInfoReq.getAndroidID(), mediaInfoReq);
    }

    @MessageMapping("/live")
    @SendToUser("/topic/live")
    public RespVO live(@Payload LiveReq liveReq) throws Exception {
        // invoke FC to start or stop live
        log.info("live: " + liveReq.toString());

        ResponseEntity<String> postForEntity = restTemplate.postForEntity(cogentAdmin + "/mobicaster/live", liveReq, String.class);
        LiveRespVO liveRespVO = new LiveRespVO();
        liveRespVO.setLiveAction(liveReq.getLiveAction());
        if (postForEntity.getStatusCode() != HttpStatus.OK || postForEntity.getBody() == null) {
            return new RespVO(500, "error", liveRespVO);
        }
        JsonObject response = JsonParser.parseString(postForEntity.getBody()).getAsJsonObject();
        if (response.get("code").getAsInt() != 200) {
            return new RespVO(500, "error", liveRespVO);
        }
        JsonObject data = response.getAsJsonObject("data");
        boolean liveAction = data.get("liveAction").getAsBoolean();
        if (liveAction) {
            liveRespVO.setPort(data.get("port").getAsInt());
            liveRespVO.setIp(data.get("ip").getAsString());
        }

        log.info("liveRespVO: " + liveRespVO);
        return new RespVO(200, "success", liveRespVO);
    }

    @MessageMapping("/deviceInfo")
    @SendToUser("/topic/deviceInfo")
    public RespVO deviceInfo(@Payload DeviceInfo deviceInfo) {
        log.info("deviceInfo: " + deviceInfo.toString());
        // invoke FC to put device info
        ResponseEntity<String> postForEntity = restTemplate.postForEntity(cogentAdmin + "/mobicaster/deviceInfo", deviceInfo, String.class);
        if (postForEntity.getStatusCode() != HttpStatus.OK || postForEntity.getBody() == null) {
            return new RespVO(500, "error", null);
        }
        JsonObject response = JsonParser.parseString(postForEntity.getBody()).getAsJsonObject();
        if (response.get("code").getAsInt() != 200) {
            return new RespVO(500, "error", null);
        }
        return new RespVO(200, "success", null);
    }

    @MessageMapping("/statusReport")
    public void statusReport(@Payload AndroidStatusReq androidStatusReq) {
        log.info("androidStatusReq: " + androidStatusReq.toString());
        // invoke FC to put device info
        ResponseEntity<String> postForEntity = restTemplate.postForEntity(cogentAdmin + "/mobicaster/statusReport", androidStatusReq, String.class);
        log.info("statusReport {} :  resp:{}", androidStatusReq, Objects.requireNonNull(postForEntity.getBody()).toString());
    }

    @PostMapping("/modifySettings")
    public RespVO modifySettings(@RequestBody UpdateAppReq req) throws InterruptedException {
        String requestId = UUID.randomUUID().toString();
        req.getDevice().getMedia().setRequestId(requestId);
        this.template.convertAndSendToUser(req.getSn(), "/topic/modifyParam", JSONObject.toJSONString(req.getDevice().getMedia()));
        int tryCount = 0;
        while (tryCount++ < 3) {
            Thread.sleep((long) Math.pow(10, tryCount) * tryCount);
            CommonResp resp = (CommonResp) respPool.get(requestId);
            if (resp != null) {
                respPool.remove(requestId);
                return new RespVO(resp.getCode(), resp.getMsg(), resp);
            }
        }
        return new RespVO(500, "error", null);
    }

    @MessageMapping("/modifyParam")
    public void modifyParam(@Payload CommonResp resp) {
        log.info("modifyParam: " + resp.toString());
        respPool.put(resp.getRequestId(), resp);
    }

    @SneakyThrows
    @PostMapping("/appLive")
    public RespVO appLive(@RequestBody AppLiveReq req) {
        log.info("appLive: " + req.toString());
        this.template.convertAndSendToUser(req.getAndroidID(), "/topic/appLive", JSONObject.toJSONString(req));
        int tryCount = 0;
        while (tryCount++ < 3) {
            Thread.sleep((long) Math.pow(10, tryCount) * tryCount);
            CommonResp resp = (CommonResp) respPool.get(req.getRequestId());
            if (resp != null) {
                respPool.remove(req.getRequestId());
                return new RespVO(resp.getCode(), resp.getMsg(), resp);
            }
        }
        return new RespVO(500, "error", null);
    }

    @MessageMapping("/appLive")
    public void appLiveMsg(@Payload CommonResp resp) {
        log.info("appLive: " + resp.toString());
        respPool.put(resp.getRequestId(), resp);
    }

    @SneakyThrows
    @PostMapping("/foldback")
    public RespVO foldback(@RequestBody FoldbackReq req) {
        log.info("foldback: " + req.toString());
        this.template.convertAndSendToUser(req.getAndroidID(), "/topic/feedback", JSONObject.toJSONString(req));
        return new RespVO(200, "success", null);
    }

    @PostMapping("/delDev/{androidID}")
    public RespVO delDev(@PathVariable String androidID) {
        log.info("delDev: " + androidID);
        this.template.convertAndSendToUser(androidID, "/topic/delDev", "");
        return new RespVO(200, "success", null);
    }

}
