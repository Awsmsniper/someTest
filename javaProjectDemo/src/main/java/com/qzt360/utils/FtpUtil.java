package com.qzt360.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import lombok.extern.slf4j.Slf4j;

/**
 * @author zhaogj
 * @version 1.0
 */
@Slf4j
public class FtpUtil {
    private boolean isNeedTmp = false;//是否需要上传为.tmp再改名回来
    private String strRemoteHost;
    private int nPort;
    private String strUserName;
    private String strPassword;

    public FtpUtil(String strRemoteHost, String strUserName, String strPassword) {
        super();
        this.strRemoteHost = strRemoteHost;
        this.strUserName = strUserName;
        this.strPassword = strPassword;
        this.nPort = 21;// 默认值
    }

    public FtpUtil(String strRemoteHost, int nPort, String strUserName, String strPassword) {
        super();
        this.strRemoteHost = strRemoteHost;
        this.nPort = nPort;
        this.strUserName = strUserName;
        this.strPassword = strPassword;
    }

    public FtpUtil(String strRemoteHost, int nPort, String strUserName, String strPassword, boolean isNeedTmp) {
        super();
        this.strRemoteHost = strRemoteHost;
        this.nPort = nPort;
        this.strUserName = strUserName;
        this.strPassword = strPassword;
        this.isNeedTmp = isNeedTmp;
    }

    /**
     * 检查FTP连接是否正常
     * @return
     */
    public boolean checkFtpStatus() {
        FTPClient ftp = new FTPClient();
        try {
            ftp.connect(strRemoteHost, nPort);
            ftp.login(strUserName, strPassword);
            int nReplyCode = ftp.getReplyCode();
            if (FTPReply.isPositiveCompletion(nReplyCode)) {
                log.info("current ftp status is normal:{}",strRemoteHost);
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (ftp.isConnected()) {
                    ftp.disconnect();
                }
            } catch (Exception e) {
                log.error("", e);
            }
        }
        log.warn("warning ! current ftp status is abnormal:{}",strRemoteHost);
        return false;
    }

    /**
     * @param strRemotePath ftp服务器目录
     * @param fileSrc       上传本地文件
     */
    public boolean upload(String strRemotePath, File fileSrc) {
        if (fileSrc.exists()) {
            // strRemoteHost = "gdba.qzt360.com";
            // strUserName = "qzt_fenghuo";
            // strPassword = "fenghuo30_w43r";
            FTPClient ftp = new FTPClient();
            FileInputStream fis = null;
            try {
                ftp.connect(strRemoteHost, nPort);
                // logger.info("Connected to " + strRemoteHost);
                // logger.info("ftp.getReplyString():" + ftp.getReplyString());
                ftp.login(strUserName, strPassword);
                int nReplyCode = ftp.getReplyCode();
                // logger.info("ftp.getReplyCode():" + nReplyCode);
                if (!FTPReply.isPositiveCompletion(nReplyCode)) {
                    ftp.disconnect();
                    log.error("FTP server refused connection.strRemoteHost={}", strRemoteHost);
                    return false;
                } else {
                    // 创建目录有bug，多级目录会失败
                    if (!ftp.changeWorkingDirectory(strRemotePath)) {
                        if (!createDirectory(strRemotePath, ftp)) {
                            log.error("目录创建失败:{}", strRemotePath);
                            return false;
                        } else {
                            if (!ftp.changeWorkingDirectory(strRemotePath)) {
                                log.error("目录不存在无法上传:{}", strRemotePath);
                                return false;
                            }
                        }
                    }
                    ftp.setBufferSize(1024);
                    ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
                    fis = new FileInputStream(fileSrc);
                    ftp.enterLocalPassiveMode();
                    // 加入重试机制，重试3次
                    boolean bUploadResult = false;
                    for (int i = 0; i < 3; i++) {
                        if (isNeedTmp) {
                            bUploadResult = ftp.storeFile(fileSrc.getName() + ".tmp", fis);
                        } else {
                            bUploadResult = ftp.storeFile(fileSrc.getName(), fis);
                        }
                        if (bUploadResult) {
                            if (isNeedTmp) {
                                if (ftp.rename(fileSrc.getName() + ".tmp", fileSrc.getName())) {
                                    log.info("上传成功:{}", fileSrc.getPath());
                                } else {
                                    log.info("擦除.tmp失败:{}", fileSrc.getPath());
                                }
                            }
                            break;
                        } else {
                            // 睡会再试试
                            Thread.sleep(1000);
                        }
                    }
                    ftp.logout();
                    // logger.info("ftp.logout()");
                    if (bUploadResult) {
                        return true;
                    } else {
                        log.warn("upload fail:{}", fileSrc.getName());
                        return false;
                    }

                }
            } catch (Exception e) {
                log.error("", e);
                return false;
            } finally {
                try {
                    if (fis != null) {
                        fis.close();
                    }
                } catch (Exception e) {
                    log.error("", e);
                }
                try {
                    if (ftp.isConnected()) {
                        ftp.disconnect();
                    }
                } catch (Exception e) {
                    log.error("", e);
                }
            }
        } else {
            log.warn("上传文件不存在:" + fileSrc.getPath());
            return false;
        }
    }

    private boolean createDirectory(String strRemotePath, FTPClient ftp) {
        String[] astrRemotePath = strRemotePath.split("/");
        String strPathNow = "";
        try {
            for (String strPath : astrRemotePath) {
                strPathNow += "/" + strPath;
                if (!ftp.changeWorkingDirectory(strPathNow)) {
                    if (!ftp.makeDirectory(strPathNow)) {
                        log.error("创建目录失败:{}", strPathNow);
                        return false;
                    }
                }
            }
            return true;
        } catch (Exception e) {
            log.error("", e);
            return false;
        }
    }
}
