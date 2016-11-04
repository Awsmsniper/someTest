package com.qzt360.service;

import com.qzt360.repository.ESRepository;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.admin.cluster.health.ClusterHealthResponse;
import org.elasticsearch.cluster.health.ClusterHealthStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * Created by zhaogj on 04/11/2016.
 */
@Service
@Slf4j
public class ESService {
    @Autowired
    private ESRepository es;

    @Autowired
    private EmailService email;

    /**
     * 检查es连接是否正常，如果不正常就尝试重新连接
     */
    public void keepAlive() {
        if (es.client == null) {
            esInit();
        }
        try {
            ClusterHealthResponse healths = es.client.admin().cluster().prepareHealth().get();
            ClusterHealthStatus status = healths.getStatus();
            log.info("es status:{}", status.name());
        } catch (Exception e) {
            log.error("es status err", e);
            esInit();
        }
    }

    /**
     * es连接初始化
     */
    private void esInit() {
        try {
            log.info("es init");
            es.init();
        } catch (Exception e) {
            log.error("es init err", e);
            email.sendEmail2Manager("es初始化失败", "身份库系统初始化es失败，请手工检查es集群");
        }
    }

}
