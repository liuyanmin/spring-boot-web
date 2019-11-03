package com.lym.springboot.web.util;

import java.util.Random;

/**
 * Created by liuyanmin on 2019/10/20.
 */
public class PasswordUtil {

    private static final String[] codes = {
            "qwertyuiopasdfghjklzxcvbnm",
            "QWERTYUIOPASDFGHJKLZXCVBNM",
            "123456789",
            "!@#$%&*()_+{}|<>?:{}"
    };
    private static Random random = new Random();

    /**
     * 随机生成密码
     * @param len 长度
     * @return
     */
    public static String gen(int len) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < len; i++) {
            sb.append(randomChar());
        }
        return sb.toString();
    }

    /**
     * 获取随机字符
     */
    private static char randomChar() {
        int index = random.nextInt(codes.length);
        String pwsStr = codes[index];

        index = random.nextInt(pwsStr.length());
        return pwsStr.charAt(index);
    }
}
