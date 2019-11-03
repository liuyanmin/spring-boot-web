package com.lym.springboot.web;

import com.lym.springboot.web.util.encrypt.RsaDecryptUtil;
import com.lym.springboot.web.util.encrypt.RsaEncryptUtil;
import com.lym.springboot.web.util.encrypt.RsaUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.security.Key;
import java.util.Map;

/**
 * Created by liuyanmin on 2019/10/21.
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class RsaUtilTest {

    /**
     * 生成Rsa 公私钥
     * @throws Exception
     */
    @Test
    public void genKey() throws Exception {
        Map<String, Key> map = RsaUtil.initKey();
        String publicKey = RsaUtil.getPublicKey(map);
        String privateKey = RsaUtil.getPrivateKey(map);
        System.out.println("publicKey: " + publicKey);
        System.out.println("privateKey: " + privateKey);
    }

    /**
     * 公钥加密
     */
    @Test
    public void encrypt() throws Exception {
        String str = RsaEncryptUtil.encrypt("123456");
        System.out.println(str);
    }

    /**
     * 私钥解密
     */
    @Test
    public void decrypt() throws Exception {
        String str = "eUjoJsDiQvUsRoNRbR5nT5OUs6AP02lXc50xKTNWBCqfPU1C9UJSFMluYM4h+gWNfqbpY98/59T+\n" +
                "Eq7eHBguhJ7Kq1qOI/fVpyrZ9Cpw6ZD3xa2vStm0Dsp5tsc6CXal4FT8xzErQtw2+YZtJghRf425\n" +
                "AIEw3WqTxDCCfmhlPwk=";
        String result = RsaDecryptUtil.decrypt(str);
        System.out.println(result);
    }
}
