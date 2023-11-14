package com.keboom.Appserver.controller.vo;

import lombok.Data;

/**
 * {@code @author:} keboom
 * {@code @date:} 2023/9/27
 * {@code @description:}
 */
@Data
public class LiveRespVO {

    private String ip;
    private Integer port;
    private Boolean liveAction;
}
