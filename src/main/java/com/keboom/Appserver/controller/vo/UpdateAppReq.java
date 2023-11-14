package com.keboom.Appserver.controller.vo;

import lombok.Data;

/**
 * {@code @author:} keboom
 * {@code @date:} 2023/10/12
 * {@code @description:}
 */
@Data
public class UpdateAppReq {

    private Integer id;
    private String sn;
    private AppInfo device;
}
