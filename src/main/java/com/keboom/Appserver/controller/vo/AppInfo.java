package com.keboom.Appserver.controller.vo;

import lombok.Data;

/**
 * {@code @author:} keboom
 * {@code @date:} 2023/10/12
 * {@code @description:}
 */
@Data
public class AppInfo {

    /**
     * 基础信息数据
     */
    private AppBase base;
    /**
     * 媒体流信息
     */
    private AppMedia media;
}
