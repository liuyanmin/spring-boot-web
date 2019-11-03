package com.lym.springboot.web.util;

import com.lym.springboot.web.config.properties.MailProperties;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Component;

import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.Properties;

/**
 * 发邮件工具类
 * Created by liuyanmin on 2019/10/20.
 */
@Log4j
@Component
public class MailUtil {

    private static String UTF8 = "UTF-8";
    private static MailProperties mailProperties;

    public MailUtil(MailProperties mailProperties) {
        MailUtil.mailProperties = mailProperties;
    }

    /**
     * 发送邮件
     * @param receive  收件人
     * @param receiveName  收件人名称
     * @param title  标题
     * @param content  内容
     * @param sendTime  发送时间
     * @throws Exception
     */
    public static void send(String receive, String receiveName, String title, String content, Date sendTime) throws Exception {
        // 1. 创建参数配置, 用于连接邮件服务器的参数配置
        Properties props = new Properties();                    // 参数配置
        props.setProperty("mail.transport.protocol", mailProperties.getProtocol());   // 使用的协议（JavaMail规范要求）
        props.setProperty("mail.host", mailProperties.getHost());        // 发件人的邮箱的 SMTP 服务器地址
        props.setProperty("mail.smtp.auth", "true");            // 请求认证，参数名称与具体实现有关
        // 2. 根据配置创建会话对象, 用于和邮件服务器交互
        Session session = Session.getDefaultInstance(props);
        session.setDebug(false);                                 // 设置为debug模式, 可以查看详细的发送 log
        // 3. 创建一封邮件
        MimeMessage message = createMimeMessage(session, mailProperties.getFrom(), receive, mailProperties.getFromName(),
                receiveName, title, content, mailProperties.getMessageType(), sendTime);
        // 4. 根据 Session 获取邮件传输对象
        Transport transport = session.getTransport();
        // 5. 使用 邮箱账号 和 密码 连接邮件服务器
        //    这里认证的邮箱必须与 message 中的发件人邮箱一致，否则报错
        transport.connect(mailProperties.getFrom(), mailProperties.getFromPassword());
        // 6. 发送邮件, 发到所有的收件地址, message.getAllRecipients() 获取到的是在创建邮件对象时添加的所有收件人, 抄送人, 密送人
        transport.sendMessage(message, message.getAllRecipients());
        // 7. 关闭连接
        transport.close();
        log.debug("发件人: " + mailProperties.getFrom() + "，收件人: " + receive);
        log.debug("send email success");
    }

    /**
     * 创建文本邮件
     * @param session
     * @param sendMail  发件人
     * @param receiveMail  接收人
     * @param sendName  发件人名称
     * @param receiveName  接件人名称
     * @param title  邮件标题
     * @param content  邮件内容
     * @param messageType  消息类型
     * @param sendTime  发送时间
     * @return
     * @throws Exception
     */
    private static MimeMessage createMimeMessage(Session session, String sendMail, String receiveMail, String sendName,
                                                String receiveName, String title, String content, String messageType, Date sendTime) throws Exception {
        // 1. 创建一封邮件
        MimeMessage message = new MimeMessage(session);
        // 2. From: 发件人
        message.setFrom(new InternetAddress(sendMail, sendName, UTF8));
        // 3. To: 收件人（可以增加多个收件人、抄送、密送）
        message.setRecipient(MimeMessage.RecipientType.TO, new InternetAddress(receiveMail, receiveName, UTF8));
        // 4. Subject: 邮件主题
        message.setSubject(title, UTF8);
        // 5. Content: 邮件正文（可以使用html标签）
        message.setContent(content, messageType);
        // 6. 设置发件时间
        message.setSentDate(sendTime);
        // 7. 保存设置
        message.saveChanges();
        return message;
    }
}
