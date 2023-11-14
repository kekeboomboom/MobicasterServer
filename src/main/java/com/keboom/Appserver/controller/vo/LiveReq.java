package com.keboom.Appserver.controller.vo;

import lombok.Data;

/**
 * {@code @author:} keboom
 * {@code @date:} 2023/9/21
 * {@code @description:}
 */
@Data
public class LiveReq {

    private String androidID;
    // start or stop
    private Boolean liveAction;
}
