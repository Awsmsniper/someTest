package com.qzt360.service;

import com.qzt360.pub.PubPara;
import com.qzt360.repository.ESRepository;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.index.IndexRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * Created by zhaogj on 04/11/2016.
 */
@Service
@Slf4j
public class LogService {
    @Autowired
    private ESRepository es;

    @Value("${es.index.strSysLogIndexPre}")
    private String strSysLogIndexPre;

    @Value("${es.index.strSysLogTypeIdSystem}")
    private String strSysLogTypeIdSystem;

    //日志入库ES
    public void sysLog2ES() {
        try {
            while (!PubPara.listSystemLog.isEmpty()) {
                Map<String, Object> json = PubPara.listSystemLog.get(0);
                es.bulkProcessor.add(new IndexRequest(strSysLogIndexPre + getDay((Date) json.get("dtTime")),
                        strSysLogTypeIdSystem).source(json));
                PubPara.listSystemLog.remove(0);
            }
        } catch (Exception e) {
            log.error("", e);
        }
    }

    private String getDay(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        return simpleDateFormat.format(date);
    }
}
