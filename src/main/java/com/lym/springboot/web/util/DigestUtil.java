package com.lym.springboot.web.util;

import org.apache.commons.codec.digest.DigestUtils;

/**
 * Created by liuyanmin on 2019/10/20.
 */
public class DigestUtil {

    public static String md5(String str) {
        return DigestUtils.md5Hex(str);
    }

    public static String md5() {
        String str = UUIDUtil.getUUID();
        return md5(str);
    }

}
