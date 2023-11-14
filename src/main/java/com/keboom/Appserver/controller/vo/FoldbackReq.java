package com.keboom.Appserver.controller.vo;

import lombok.Data;

/**
 * {@code @author:} keboom
 * {@code @date:} 2023/9/26
 * {@code @description:}
 */
@Data
public class FoldbackReq {

    private Integer port;
    private String streamId;
    private String androidID;
    private Boolean action;
    private String streamName;
}
