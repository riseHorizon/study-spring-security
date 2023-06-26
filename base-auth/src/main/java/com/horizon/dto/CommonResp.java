package com.horizon.dto;

import lombok.Data;

/**
 * 通用返回响应
 */
@Data
public class CommonResp<T> {

    private String code;
    private String message;
    private T data;

    public CommonResp(String code, String message) {
        this.code = code;
        this.message = message;
    }

}
