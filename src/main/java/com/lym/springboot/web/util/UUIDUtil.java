package com.lym.springboot.web.util;

import java.util.UUID;

/**
 * Created by liuyanmin on 2019/9/29.
 */
public class UUIDUtil {

    public static String getUUID(){
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        return uuid;
    }

}
