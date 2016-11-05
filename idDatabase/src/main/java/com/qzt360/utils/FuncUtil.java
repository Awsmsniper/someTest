package com.qzt360.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Properties;

/**
 * @author zhaogj
 * @version 1.1 20161013
 */
@Slf4j
public class FuncUtil {
    // 返回指定长度的随机数
    public static String getRandom(int nLength) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < nLength; i++) {
            sb.append((int) (Math.random() * 10));
        }
        return sb.toString();
    }

    /**
     * 清理目录内文件
     *
     * @param strPath  绝对路径
     * @param nKeepDay 保留天数
     */

    public static boolean cleanFile(String strPath, int nKeepDay) {
        File filePath = new File(strPath);
        if (!filePath.exists()) {
            log.error("目录:" + strPath + "不存在，无法清理");
            return false;
        }
        if (!filePath.isDirectory()) {
            log.error(strPath + "不是目录，无法清理");
            return false;
        }
        long lTime = System.currentTimeMillis() - (1000L * 60L * 60L * 24L * nKeepDay);
        for (File file : filePath.listFiles()) {
            if (file.isFile() && file.lastModified() < lTime) {
                log.info("delete file:{}", file.getPath());
                file.delete();
            }
        }
        return true;
    }

    /**
     * 判断是否为空
     *
     * @param object
     * @return
     */
    public static boolean isNull(Object object) {
        try {
            if (object == null || "".equals(("" + object).trim())
                    || "null".equals(("" + object).trim().toLowerCase())) {
                return true;
            }
        } catch (Exception e) {
            log.error("", e);
            log.info("object:{}", object);
        }
        return false;
    }

    // 取得本机信息
    public static String getLocalhostInfo() {
        StringBuffer sb = new StringBuffer();
        Properties props = System.getProperties(); // 获得系统属性集
        sb.append("操作系统:\n名称 os.name:");
        sb.append(props.getProperty("os.name"));
        sb.append("\n");
        sb.append("构架 os.arch:");
        sb.append(props.getProperty("os.arch"));
        sb.append("\n");
        sb.append("版本 os.version:");
        sb.append(props.getProperty("os.version"));
        sb.append("\n");
        URL url = FuncUtil.class.getProtectionDomain().getCodeSource().getLocation();
        try {
            sb.append("jar信息:\n");
            sb.append(URLDecoder.decode(url.getPath(), "utf-8"));
        } catch (Exception e) {
            log.error("", e);
        }
        return sb.toString();
    }

    //取得内存使用情况
    public static String getMemoryStatus() {
        StringBuffer sb = new StringBuffer();
        sb.append("jvm内存使用情况\n最大可用内存:");
        sb.append(Runtime.getRuntime().maxMemory());
        sb.append("\n当前JVM空闲内存:");
        sb.append(Runtime.getRuntime().freeMemory());
        sb.append("\n当前JVM占用的内存总数:");
        sb.append(Runtime.getRuntime().totalMemory());
        sb.append("\n内存使用率:");
        sb.append((Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) * 100 / Runtime.getRuntime().maxMemory());
        sb.append("%");
        return sb.toString();
    }
}
