package com.qzt360.utils;

import lombok.extern.slf4j.Slf4j;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.Date;
import java.util.Properties;

/**
 * @author zhaogj
 * @version 1.1 20161104
 */
@Slf4j
public class EmailUtil {
    private static String strMailSmtpHost = "mail.qzt360.com";
    private static String strUserName = "wj@qzt360.com";
    private static String strPassword = "Qzt360wj";

    /**
     * 发送简单邮件
     *
     * @param astrRecipient 收件人列表
     * @param strSubject    标题
     * @param strText       正文
     */
    public static boolean sendSimpleTextEmail(String[] astrRecipient, String strSubject, String strText) {
        Properties props = new Properties();
        props.setProperty("mail.smtp.auth", "true");
        props.put("mail.smtp.host", strMailSmtpHost);
        // props.put("mail.debug", "true");
        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(strUserName, strPassword);
            }
        });
        // session.setDebug(true);
        try {
            // create a message
            MimeMessage msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(strUserName));
            for (String strRecipient : astrRecipient) {
                msg.addRecipient(Message.RecipientType.TO, new InternetAddress(strRecipient));
            }
            msg.setSubject(strSubject);
            msg.setSentDate(new Date());
            msg.setText(strText + "\n" + FuncUtil.getLocalhostInfo());
            Transport.send(msg);
            log.info("邮件发送成功,{}", strSubject);
        } catch (Exception e) {
            log.error("", e);
            return false;
        }
        return true;
    }

    public static boolean sendSimpleTextEmail(Iterable<String> itRecipient, String strSubject, String strText) {
        Properties props = new Properties();
        props.setProperty("mail.smtp.auth", "true");
        props.put("mail.smtp.host", strMailSmtpHost);
        // props.put("mail.debug", "true");
        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(strUserName, strPassword);
            }
        });
        // session.setDebug(true);
        try {
            // create a message
            MimeMessage msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(strUserName));
            for (String strRecipient : itRecipient) {
                msg.addRecipient(Message.RecipientType.TO, new InternetAddress(strRecipient));
            }
            msg.setSubject(strSubject);
            msg.setSentDate(new Date());
            msg.setText(strText + "\n" + FuncUtil.getLocalhostInfo());
            Transport.send(msg);
            log.info("邮件发送成功,{}", strSubject);
        } catch (Exception e) {
            log.error("", e);
            return false;
        }
        return true;
    }

    /**
     * 发送带附件的邮件
     *
     * @param astrRecipient     收件人列表
     * @param strSubject        标题
     * @param strText           正文
     * @param strAttachmentPath 附件绝对路径
     */
    public static void sendEmailWithAttachment(String[] astrRecipient, String strSubject, String strText,
                                               String strAttachmentPath) {
        Properties props = new Properties();
        props.setProperty("mail.smtp.auth", "true");
        props.put("mail.smtp.host", strMailSmtpHost);
        // props.put("mail.debug", "true");
        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(strUserName, strPassword);
            }
        });
        // session.setDebug(true);
        try {
            // create a message
            MimeMessage msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(strUserName));
            // InternetAddress[] address = { new
            // InternetAddress("22655080@qq.com") };
            // msg.setRecipients(Message.RecipientType.TO, address);
            for (String strRecipient : astrRecipient) {
                msg.addRecipient(Message.RecipientType.TO, new InternetAddress(strRecipient));
            }
            msg.setSubject(strSubject);

            // Create a multipar message
            Multipart mp = new MimeMultipart();

            // Create the message part
            MimeBodyPart mbp = new MimeBodyPart();

            // Now set the actual message
            mbp.setText(strText);

            // Set text message part
            mp.addBodyPart(mbp);

            // Part two is attachment
            mbp = new MimeBodyPart();
            FileDataSource fds = new FileDataSource(strAttachmentPath) {
                public String getContentType() {
                    return "application/octet-stream";
                }
            };
            mbp.setDataHandler(new DataHandler(fds));
            mbp.setFileName(fds.getName());
            mp.addBodyPart(mbp);

            // Send the complete message parts
            msg.setContent(mp);

            msg.setSentDate(new Date());
            // Send message
            Transport.send(msg);
            log.info("邮件发送成功,{}", strSubject);
        } catch (Exception e) {
            log.error("", e);
        }
    }
}
