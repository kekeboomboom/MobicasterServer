package com.keboom.Appserver.controller.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * {@code @author:} keboom
 * {@code @date:} 2023/9/21
 * {@code @description:}
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class RespVO<T> {

    private int code;
    private String msg;
    private T data;

}
