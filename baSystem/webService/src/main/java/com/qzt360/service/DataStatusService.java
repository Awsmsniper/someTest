package com.qzt360.service;

import com.qzt360.model.DataStatus;
import com.qzt360.repository.ESRepository;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.index.query.QueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static org.elasticsearch.index.query.QueryBuilders.matchAllQuery;

/**
 * Created by zhaogj on 07/11/2016.
 */
@Slf4j
@Service
public class DataStatusService {
    @Autowired
    private ESRepository es;
    @Value("${es.index.strEqpIndex}")
    private String strEqpIndex;

    @Value("${es.index.strEqpType}")
    private String strEqpType;

    @Value("${es.index.strAWifiNetlogIndexPre}")
    private String strAWifiNetlogIndexPre;

    @Value("${es.index.strAWifiOnOffLineType}")
    private String strAWifiOnOffLineType;
    @Value("${es.index.strAWifiNetIdType}")
    private String strAWifiNetIdType;
    @Value("${es.index.strAWifiNetlogType}")
    private String strAWifiNetlogType;
    @Value("${es.index.strIdIndex}")
    private String strIdIndex;
    @Value("${es.index.strIdType}")
    private String strIdType;


    public DataStatus ds = new DataStatus();
    private boolean isRunning = false;
    private Lock statLock = new ReentrantLock();

    public DataStatus getDataStatus() {
        if (!isRunning) {
            statLock.lock();
            if (!isRunning) {
                isRunning = true;
                statLock.unlock();
                new Thread() {
                    @Override
                    public void run() {
                        statDataStatus();
                    }
                }.start();
            }
        }
        return ds;
    }

    //计算数据状态
    private void statDataStatus() {
        long lBegin = System.currentTimeMillis();
        //循环跑5分钟
        while ((System.currentTimeMillis() - lBegin) < (1000L * 60 * 5)) {
            try {
                Thread.sleep(1000 * 3);
                QueryBuilder qb = matchAllQuery();
                // aWifi 设备数
                SearchResponse response = es.client.prepareSearch(strEqpIndex).setTypes(strEqpType)
                        .setSearchType(SearchType.DFS_QUERY_THEN_FETCH).setQuery(qb).setFrom(0).setSize(1).setExplain(true)
                        .execute().actionGet();
                ds.setLAWifiEqpCount(response.getHits().getTotalHits());
                // aWifi 审计日志
                response = es.client.prepareSearch(strAWifiNetlogIndexPre + "*")
                        .setTypes(strAWifiNetlogType).setSearchType(SearchType.DFS_QUERY_THEN_FETCH).setQuery(qb)
                        .setFrom(0).setSize(1).setExplain(true).execute().actionGet();
                ds.setLAWifiNetlogCount(response.getHits().getTotalHits());
                // aWifi 终端上下线
                response = es.client.prepareSearch(strAWifiNetlogIndexPre + "*")
                        .setTypes(strAWifiOnOffLineType).setSearchType(SearchType.DFS_QUERY_THEN_FETCH).setQuery(qb)
                        .setFrom(0).setSize(1).setExplain(true).execute().actionGet();
                ds.setLAWifiAuthlogCount(response.getHits().getTotalHits());
                // aWifi 虚拟身份
                response = es.client.prepareSearch(strAWifiNetlogIndexPre + "*").setTypes(strAWifiNetIdType)
                        .setSearchType(SearchType.DFS_QUERY_THEN_FETCH).setQuery(qb).setFrom(0).setSize(1).setExplain(true)
                        .execute().actionGet();
                ds.setLAWifiNetIdlogCount(response.getHits().getTotalHits());
                // 身份个数
                response = es.client.prepareSearch(strIdIndex).setTypes(strIdType)
                        .setSearchType(SearchType.DFS_QUERY_THEN_FETCH).setQuery(qb).setFrom(0).setSize(1).setExplain(true)
                        .execute().actionGet();
                ds.setLIdCount(response.getHits().getTotalHits());
            } catch (Exception e) {
                log.error("", e);
            }
        }
        isRunning = false;
    }

}
