package com.keboom.Appserver.config;

import com.keboom.Appserver.controller.vo.OfflineReportReq;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ExecutorChannelInterceptor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.security.Principal;


/**
 * {@code @author:} keboom
 * {@code @date:} 2023/9/26
 * {@code @description:}
 */
@Slf4j
@Component
public class MyChannelInterceptor implements ExecutorChannelInterceptor {

    @Value("${cogent-admin}")
    private String cogentAdmin;

    @Resource
    private RestTemplate restTemplate;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        StompCommand command = accessor.getCommand();
        // 告知FC设备离线了
        if (StompCommand.DISCONNECT.equals(command)) {
            Principal simpUser = (Principal) message.getHeaders().get("simpUser");
            log.info("androidID: " + simpUser.getName() + " is offline");
            restTemplate.postForObject(cogentAdmin + "/mobicaster/offlineReport", new OfflineReportReq(simpUser.getName()), String.class);
        }
        return message;
    }
}