package com.lym.springboot.web.util;

import lombok.extern.log4j.Log4j;
import org.apache.commons.lang3.StringUtils;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.ConnectException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;

/**
 * 网络请求工具类，支持 HTTP/HTTPS 协议请求
 * Created by liuyanmin on 2019/9/29.
 */
@Log4j
public class HttpUtil {

    /**连接超时时间，单位：毫秒**/
    public static final int CONNECT_TIMEOUT = 3000;
    /**读取超时时间，单位：毫秒**/
    public static final int READ_TIMEOUT = 10000;

    /**
     * GET 请求
     * @param url 请求地址
     * @return
     */
    public static String get(String url) throws Exception {
        if (!StringUtils.isEmpty(url) && url.startsWith("https")) {
            return httpsRequest(url, "GET", null);
        } else {
            return HttpUtil.httpGet(url);
        }
    }

    /**
     * POST 请求
     * @param url 请求地址
     * @param paramsJson 请求参数，json 格式
     * @return
     */
    public static String post(String url, String paramsJson) throws Exception {
        if (!StringUtils.isEmpty(url) && url.startsWith("https")) {
            return httpsRequest(url, "POST", paramsJson);
        } else {
            return HttpUtil.httpPost(url, paramsJson);
        }
    }


    /**
     * 发送https请求
     * @param requestUrl 请求路径
     * @param requestMethod 请求类型，例如: GET POST等
     * @param outputStr 请求数据
     *                  当发送 GET 请求时，此参数为 null
     *                  当发送 POST 请求时，此参数为 json 格式字符串
     * @return
     */
    private static String httpsRequest(String requestUrl, String requestMethod, String outputStr) throws Exception {
        try {
            URL url = new URL(requestUrl);
            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            conn.setConnectTimeout(CONNECT_TIMEOUT);
            conn.setReadTimeout(READ_TIMEOUT);
            // 设置请求方式（GET/POST）
            conn.setRequestMethod(requestMethod);
            conn.setRequestProperty("content-type", "application/json");
            // 当outputStr不为null时向输出流写数据
            if (StringUtils.isNotBlank(outputStr)) {
                OutputStream outputStream = conn.getOutputStream();
                // 注意编码格式
                outputStream.write(outputStr.getBytes("UTF-8"));
                outputStream.close();
            }
            // 从输入流读取返回内容
            InputStream inputStream = conn.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String str;
            StringBuffer buffer = new StringBuffer();
            while ((str = bufferedReader.readLine()) != null) {
                buffer.append(str);
            }
            // 释放资源
            bufferedReader.close();
            inputStreamReader.close();
            inputStream.close();
            conn.disconnect();
            return buffer.toString();
        } catch (ConnectException ce) {
            throw ce;
        } catch (Exception e) {
            throw e;
        }
    }


    /**
     * 向指定 URL 发送POST方法的请求
     *
     * @param url
     *            发送请求的 URL
     * @param param
     *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return 所代表远程资源的响应结果
     */
    private static String httpPost(String url, String param) throws Exception {
        OutputStream out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            conn.setConnectTimeout(CONNECT_TIMEOUT);
            conn.setReadTimeout(READ_TIMEOUT);
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            conn.setRequestProperty("content-type", "application/json");
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            out = conn.getOutputStream();
            // 发送请求参数
            // out.print(param.getBytes("UTF-8"));
            out.write(param.getBytes("UTF-8"));
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            throw e;
        }  finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                throw ex;
            }
        }
        return result;
    }

    /**
     * 向指定URL发送GET方法的请求
     *
     * @param url
     *            发送请求的URL
     * @return URL 所代表远程资源的响应结果
     */
    private static String httpGet(String url) throws Exception {
        String result = "";
        BufferedReader in = null;
        try {
            String urlNameString = url;
            URL realUrl = new URL(urlNameString);
            // 打开和URL之间的连接
            URLConnection connection = realUrl.openConnection();
            // 设置通用的请求属性
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 建立实际的连接
            connection.connect();
            // 获取所有响应头字段
            Map<String, List<String>> map = connection.getHeaderFields();
            // 定义 BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(connection.getInputStream())); //这里如果出现乱码，请使用带编码的InputStreamReader构造方法，将需要的编码设置进去
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            throw e;
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                throw e2;
            }
        }
        return result;
    }

}
