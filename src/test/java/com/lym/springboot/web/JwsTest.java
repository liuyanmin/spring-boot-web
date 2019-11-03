package com.lym.springboot.web;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * Created by liuyanmin on 2019/10/17.
 */
@Slf4j
public class JwsTest {

    @Test
    public void test() {
        String token = "eyJjdHkiOiJjdHkiLCJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJzcHJpbmctYm9vdC13ZWItand0IiwiYXVkIjoid2ViIiwiaXNzIjoic3ByaW5nLWJvb3Qtd2ViIiwiZXhwIjoxNTcxMzA5NDExLCJpYXQiOjE1NzEzMDc2MTEsImp0aSI6IjZlM2FlMWFlNjEyOTRmZDI4MDNiMmY0ODM1YWRkOGM2In0.R0oQjRMMW-mHbj3AlzrgXyIONaXHsTrVTS0nlhl8ox4";
        try {
            Jws<Claims> jws = Jwts.parser()
                    .setSigningKey("6d19aa76fe4b4851971c04aaa25e3b83".getBytes("UTF-8"))
                    .parseClaimsJws(token);
            System.out.println(jws);
        } catch (Exception e) {
            log.error("verify token exception: ", e);
        }
    }
}
