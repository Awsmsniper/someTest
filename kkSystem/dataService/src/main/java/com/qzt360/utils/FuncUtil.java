package com.qzt360.utils;

import com.google.common.base.CharMatcher;
import com.google.common.base.Splitter;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.net.URL;
import java.net.URLDecoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.TimeZone;

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
        sb.append("\njvm内存使用情况\n最大可用内存:");
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

    //抹掉结果指定字符
    public static String trimEndChar(String strInput, String strChar) {
        if (isNull(strInput) || isNull(strChar)) {
            return "";
        }
        while (true) {
            if (strInput.endsWith(strChar)) {
                strInput = strInput.substring(0, strInput.length() - 1);
            } else {
                return strInput;
            }
        }
    }

    /**
     * 将long型的IP转换成形如111.111.111.111的字符串
     */
    public static String Long2StrIP(long lIp) {
        String strIp = "-";
        if (lIp > 0)
            strIp = "" + ((lIp & 0xff000000) >> 24) + "." + ((lIp & 0xff0000) >> 16) + "." + ((lIp & 0xff00) >> 8) + "."
                    + (lIp & 0xff);
        return strIp;
    }

    /**
     * 将yyyy-MM-dd'T'HH:mm:ss.SSS'Z'格式时间转换为yyyy-MM-dd HH:mm:ss
     */
    public static String TZ2LocalTime(String strInput) {
        if (strInput == null || "1970-01-01T00:00:00.000Z".equals(strInput)) {
            return "-";
        }
        String strOutput = strInput;
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        try {
            Date date = df.parse(strInput);
            df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            strOutput = df.format(date);
        } catch (Exception e) {
            log.error("", e);
            return "-";
        }
        return strOutput;
    }

    /**
     * 将yyyy-MM-dd HH:mm:ss格式时间转换为yyyy-MM-dd'T'HH:mm:ss.SSS'Z'
     */
    public static String Local2TZTime(String strInput) {
        String strOutput = strInput;
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date = df.parse(strInput);
            df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            df.setTimeZone(TimeZone.getTimeZone("UTC"));
            strOutput = df.format(date);
        } catch (ParseException e) {
            log.error("", e);
        }
        return strOutput;
    }

    // 时间转换，将long转为str
    public static String Long2StrTime(long lTime) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(lTime);
        return df.format(date);
    }

    // 时间转换，将long转为str
    public static String Long2StrTime(long lTime, String strFormater) {
        SimpleDateFormat df = new SimpleDateFormat(strFormater);
        Date date = new Date(lTime);
        return df.format(date);
    }

    /**
     * 清洗mac
     *
     * @param strInput 允许的输入AA-BB-CC-DD-EE-FF,AA:BB:CC:DD:EE:FF,AABBCCDDEEFF,
     *                 aabbccddeeff<br>
     *                 允许有空格
     * @return 输出AA-BB-CC-DD-EE-FF
     */
    public static String washMac(String strInput) {
        // 按字符分隔，去掉空格
        Splitter splitter = Splitter.fixedLength(1).trimResults().omitEmptyStrings();
        // 只要数字和字母，转换成大写
        Iterable<String> itInput = splitter.split(CharMatcher.JAVA_LETTER_OR_DIGIT.retainFrom(strInput).toUpperCase());
        StringBuffer sb = new StringBuffer();
        int nCount = 0;
        for (String str : itInput) {
            if (sb.length() > 0) {
                if (nCount % 2 == 0) {
                    sb.append("-");
                }
            }
            nCount++;
            sb.append(str);
        }
        if (sb.length() == 17) {
            return sb.toString();
        } else {
            return strInput;
        }
    }


}
