package com.lym.springboot.web.util.encrypt;

import com.lym.springboot.web.config.properties.LoginProperties;
import org.springframework.stereotype.Component;
import sun.misc.BASE64Encoder;

/**
 * Rsa 加密工具类
 * Created by liuyanmin on 2019/10/21.
 */
@Component
public class RsaEncryptUtil {

    private static BASE64Encoder base64Encoder = new BASE64Encoder();
    private static String publicKey;

    public RsaEncryptUtil(LoginProperties loginProperties) {
        RsaEncryptUtil.publicKey = loginProperties.getPublicKey();
    }

    public static String encrypt(String data) throws Exception {
        byte[] bytes = RsaUtil.publicEncrypt(data, publicKey);
        String result = base64Encoder.encodeBuffer(bytes);
        return result;
    }

}
