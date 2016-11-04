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

    public void keepAlive() {
        try {
            ClusterHealthResponse healths = es.client.admin().cluster().prepareHealth().get();
            ClusterHealthStatus status = healths.getStatus();
            log.info("es status:{}", status.value());
        } catch (Exception e) {
            log.error("es status err", e);
            try {
                es.init();
            } catch (Exception eInit) {
                log.error("es init err", eInit);
            }
        }
    }
}
