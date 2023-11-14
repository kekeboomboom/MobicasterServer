package com.keboom.Appserver.controller.vo;

import lombok.Data;

/**
 * {@code @author:} keboom
 * {@code @date:} 2023/10/12
 * {@code @description:}
 */
@Data
public class AppBase {
    /**
     * 设备名称
     */
    private String alias;
    /**
     * 返送状态：0-未准备好；1-已准备好；2-返送中；3-返送异常
     */
    private Integer foldbackState;
    /**
     * 发送开关，0表示没开，1表示开了
     */
    private Integer foldbackStateSwitch;
    /**
     * 编号：数据库中的编号
     */
    private Long id;
    /**
     * 直播状态：0-未准备好：编码器未初始化完成；1-已准备好；2-直播中；3-直播异常
     */
    private Integer liveState;
    /**
     * 序列号：手机androidID
     */
    private String sn;
    /**
     * 状态：offline表示离线，online表示在线
     */
    private String state;
    /**
     * 设备类型：Mobicaster
     */
    private String type;
}
