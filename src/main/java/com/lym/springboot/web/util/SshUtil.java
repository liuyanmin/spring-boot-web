package com.lym.springboot.web.util;

import lombok.extern.log4j.Log4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * SSH 工具类
 * Created by liuyanmin on 2019/9/1.
 */
@Log4j
public class SshUtil {

    /**
     * 执行linux命令
     * @param command 命令，多个命令之间用分号隔开，例如: "cd /usr/local;ll -h"
     * @return
     */
    public static String ssh(String command) throws IOException {
        String[] cmd = new String[] {"/bin/sh", "-c", command};

        BufferedReader br = null;
        try {
            Process ps = Runtime.getRuntime().exec(cmd);

            br = new BufferedReader(new InputStreamReader(ps.getInputStream()));
            StringBuffer sb = new StringBuffer();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line).append("\n");
            }
            String result = sb.toString();
            return result;
        } catch (IOException e) {
            throw e;
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    log.error("流关闭异常: ", e);
                }
            }
        }
    }

}
