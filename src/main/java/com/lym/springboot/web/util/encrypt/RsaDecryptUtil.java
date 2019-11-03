package com.lym.springboot.web.util.encrypt;

import com.lym.springboot.web.config.properties.LoginProperties;
import org.springframework.stereotype.Component;
import sun.misc.BASE64Decoder;

/**
 * Rsa 解密工具类
 * Created by liuyanmin on 2019/10/21.
 */
@Component
public class RsaDecryptUtil {

    private static BASE64Decoder base64Decoder = new BASE64Decoder();
    private static String privateKey;

    public RsaDecryptUtil(LoginProperties loginProperties) {
        RsaDecryptUtil.privateKey = loginProperties.getPrivateKey();
    }

    public static String decrypt(String encryptData) throws Exception {
        byte[] decodeValue = RsaUtil.privateDecrypt(base64Decoder.decodeBuffer(encryptData), privateKey);
        String data = new String(decodeValue);
        return data;
    }

}
