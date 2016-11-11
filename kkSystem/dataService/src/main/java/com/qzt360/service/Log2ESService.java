package com.qzt360.service;

import com.google.common.base.Splitter;
import com.qzt360.repository.ESRepository;
import com.qzt360.utils.FuncUtil;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.index.IndexRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by zhaogj on 11/11/2016.
 */
@Service
@Slf4j
public class Log2ESService {
    @Autowired
    private ESRepository es;
    private boolean isRunning = false;
    private Lock statLock = new ReentrantLock();

    /**
     * 启动入库程序，避免出现并发情况
     */
    public void startTMacLog2ES() {
        log.info("startTMacLog2ES...");
        if (!isRunning) {
            statLock.lock();
            if (!isRunning) {
                isRunning = true;
                statLock.unlock();
                new Thread() {
                    @Override
                    public void run() {
                        log.info("thread is running...");
                        tmacLog2ES();
                        isRunning = false;
                    }
                }.start();
            }
        }
    }

    private void tmacLog2ES() {

        File fileTMacLogPath = new File("/home/qzt_java/kkSystemDataService/data/tmac/");
        log.info("找到{}个文件", fileTMacLogPath.listFiles().length);
        for (File fileTMac : fileTMacLogPath.listFiles()) {
            log.info("处理文件:{}", fileTMac.getPath());
            int nLine = 0;
            BufferedReader br = null;
            try {
                br = new BufferedReader(new InputStreamReader(new FileInputStream(fileTMac.getPath())));
                String strLine = null;
                while ((strLine = br.readLine()) != null) {
                    nLine++;
                    String[] astrLine = (strLine + "\tendmark").split("\t");
                    if (astrLine.length == 18) {
                        try {
                            Map<String, Object> json = new HashMap<String, Object>();
                            json.put("strTmac", astrLine[0].trim());
                            json.put("strTbrand", astrLine[1].trim());
                            json.put("strTssidList", astrLine[2].trim());
                            json.put("lCollectTime", Integer.parseInt(astrLine[3].trim()));
                            long lCollectTime = 1000L * Long.parseLong(astrLine[3].trim());
                            String strIndex = "zhaogj_tmac_" + FuncUtil.Long2StrTime(lCollectTime, "yyyy_MM_dd");
                            json.put("dtCollectTime", new Date(lCollectTime));
                            json.put("strTfieldIntensity", astrLine[4].trim());
                            json.put("strIdType", astrLine[5].trim());
                            json.put("strIdContent", astrLine[6].trim());
                            json.put("strApSsid", astrLine[7].trim());
                            json.put("strApMac", astrLine[8].trim());
                            json.put("strApChannel", astrLine[9].trim());
                            json.put("strApEncType", astrLine[10].trim());
                            json.put("strApX", astrLine[11].trim());
                            json.put("strApY", astrLine[12].trim());
                            json.put("strPlaceCode", astrLine[13].trim());
                            json.put("strDeviceCode", astrLine[14].trim());
                            json.put("strDeviceLongitude", astrLine[15].trim());
                            json.put("strDeviceLatitude", astrLine[16].trim());
                            es.bulkProcessor.add(new IndexRequest(strIndex,
                                    "type",
                                    fileTMac.getName() + "_" + nLine).source(json));
                        } catch (Exception e) {
                            log.warn("时间转换失败:{}", astrLine[3].trim());
                        }
                    } else {
                        log.warn("字段个数不对:{}", astrLine.length);
                    }
                }
            } catch (Exception e) {
                log.error("", e);
            } finally {
                if (br != null) {
                    try {
                        br.close();
                    } catch (IOException e) {
                        log.error("", e);
                    }
                }
            }
            fileTMac.renameTo(new File("/home/qzt_java/kkSystemDataService/data/tmac_bak/" + fileTMac.getName()));
        }
    }
}
