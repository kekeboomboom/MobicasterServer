package com.keboom.Appserver.controller.vo;

import lombok.Data;

/**
 * {@code @author:} keboom
 * {@code @date:} 2023/10/13
 * {@code @description:}
 */
@Data
public class CommonResp {
    private String androidID;
    private Integer code;
    private String msg;
    /**
     * 用于区分不同的消息
     */
    private String requestId;
}
