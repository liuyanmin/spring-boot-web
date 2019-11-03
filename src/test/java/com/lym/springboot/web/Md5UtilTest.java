package com.lym.springboot.web;

import com.lym.springboot.web.util.Md5Util;
import org.junit.Test;

/**
 * Created by liuyanmin on 2019/10/21.
 */
public class Md5UtilTest {

    @Test
    public void test() {
        String str = Md5Util.md5("123456");
        System.out.println(str);
    }
}
