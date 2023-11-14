package com.keboom.Appserver.controller.vo;

import lombok.Data;

/**
 * {@code @author:} keboom
 * {@code @date:} 2023/10/12
 * {@code @description:}
 */
@Data
public class AppMedia {
    /**
     * 音轨数：1表示单声道，2表示立体声
     */
    private Integer audioCount;
    /**
     * 音频码率：96/128/160/256/320，单位kbps
     */
    private Integer audioRate;
    /**
     * 音频采样率：固定48000，单位hz
     */
    private Integer audioSamplingRate;
    /**
     * 编码模式：h264/h265
     */
    private String encodeMode;
    /**
     * srt延时：600~29000单位ms
     */
    private Integer srtDelay;
    /**
     * 视频帧率：30/60，单位fps
     */
    private Integer videoFrameRate;
    /**
     * 视频码率：单位kbps
     */
    private Integer videoRate;
    /**
     * 视频分辨率，720P/1080P/4K
     */
    private String videoResolution;

    private String requestId;
}
