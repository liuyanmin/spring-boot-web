package com.lym.springboot.web;

import com.lym.springboot.web.util.VerificationCode;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by liuyanmin on 2019/10/10.
 */
public class VerificationCodeTest {

    @Test
    public void test() {
        VerificationCode verificationCode = new VerificationCode();
        BufferedImage bufferedImage = verificationCode.getImage(4);
        try {
            ImageIO.write(bufferedImage, "JPEG", new File("D:/code.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
