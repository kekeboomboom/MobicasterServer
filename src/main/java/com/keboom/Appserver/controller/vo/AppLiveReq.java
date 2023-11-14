package com.keboom.Appserver.controller.vo;

import lombok.Data;

/**
 * {@code @author:} keboom
 * {@code @date:} 2023/10/13
 * {@code @description:}
 */
@Data
public class AppLiveReq {

    private String androidID;
    private Boolean live;
    private String requestId;
    private String ip;
    private Integer port;
}
