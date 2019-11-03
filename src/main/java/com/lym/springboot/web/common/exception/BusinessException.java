package com.lym.springboot.web.common.exception;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by liuyanmin on 2019/10/11.
 */
@Data
public class BusinessException extends RuntimeException {

    private Integer code;
    private String message;

    public BusinessException() {
    }

    public BusinessException(String message) {
        super(message);
        this.message = message;
    }

    public BusinessException(Integer code, String message) {
        this(StringUtils.isBlank(message) ? String.valueOf(code) : message);
        this.code = code;
        this.message = message;
    }

}
