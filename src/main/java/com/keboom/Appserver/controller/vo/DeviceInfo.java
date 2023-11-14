package com.keboom.Appserver.controller.vo;

import lombok.Data;

/**
 * {@code @author:} keboom
 * {@code @date:} 2023/9/22
 * {@code @description:}
 */
@Data
public class DeviceInfo {

    private String androidID;
    private String androidVersion;
    private String manufacturer;
    private String model;
}
