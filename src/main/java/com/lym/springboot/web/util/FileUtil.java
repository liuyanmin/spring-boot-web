package com.lym.springboot.web.util;

import lombok.extern.log4j.Log4j;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

/**
 * 文件工具类
 * Created by liuyanmin on 2019/9/4.
 */
@Log4j
public class FileUtil {

    /**
     * 下载本地文件
     * @param filePath 文件绝对路径
     * @param response
     */
    public static void download(String filePath, HttpServletResponse response) throws IOException {
        OutputStream outputStream = response.getOutputStream();
        byte[] buff = new byte[1024];
        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(new File(filePath)));
        int len;
        while ((len = bis.read(buff)) != -1) {
            outputStream.write(buff, 0, len);
            outputStream.flush();
        }
        if (bis != null) {
            bis.close();
        }
    }

    /**
     * 在文件末尾追加内容
     * @param file
     *             待追加的文件
     * @param content
     *              追加内容
     * @throws IOException
     */
    public static void appendContent(File file, String content) throws IOException {
        FileChannel fc = new FileOutputStream(file, true).getChannel();
        fc.position(fc.size());
        fc.write(ByteBuffer.wrap(content.getBytes()));
        fc.close();
    }

    public static List<Boolean> delFiles(String... filenames) {
        List<Boolean> result = new ArrayList<>();
        for (String filename : filenames) {
            result.add(delFile(filename));
        }
        return result;
    }

    public static boolean delFile(String filename) {
        File file = new File(filename);
        return delFile(file);
    }

    public static boolean delFile(File file) {
        if (!file.exists()) {
            return false;
        }

        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (File f : files) {
                delFile(f);
            }
        }
        return file.delete();
    }
}
