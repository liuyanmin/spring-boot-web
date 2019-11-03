package com.lym.springboot.web;

import com.lym.springboot.web.util.UUIDUtil;
import org.junit.Test;

/**
 * Created by liuyanmin on 2019/10/17.
 */
public class UUIDUtilTest {

    @Test
    public void test() {
        String uuid = UUIDUtil.getUUID();
        System.out.println(uuid);
    }
}
