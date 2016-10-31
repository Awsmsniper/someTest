package com.qzt360.utils;

import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.elasticsearch.action.admin.indices.template.put.PutIndexTemplateRequest;
import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.IndicesAdminClient;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.collect.MapBuilder;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.unit.ByteSizeUnit;
import org.elasticsearch.common.unit.ByteSizeValue;
import org.elasticsearch.common.unit.TimeValue;

import lombok.extern.slf4j.Slf4j;

/**
 * @author zhaogj
 * @version 1.4 20161026
 */
@Slf4j
public class ESUtil {
    public static Client client = null;
    public static BulkProcessor bulkProcessor = null;
    private static String strClusterName = "qzt360-es";
    private static String[] astrTransportHostName = {"192.168.36.27", "192.168.36.28", "192.168.36.29", "192.168.36.30"};
    private static int nConcurrentRequests = 1;// 貌似是单机cpu核心数时速度最佳

    // for test
    public ESUtil(String strClusterName, String[] astrTransportHostName) {
        this.strClusterName = strClusterName;
        this.astrTransportHostName = astrTransportHostName;
    }

    // 初始化
    public static void buildAll() throws Exception {
        buildClient();
        bulidBulkProcessor();
    }

    private static void buildClient() throws Exception {
        Settings settings = Settings.settingsBuilder().put("cluster.name", strClusterName)
                .put("client.transport.sniff", true).build();
        client = TransportClient.builder().settings(settings).build();
        for (String strTransportHostName : astrTransportHostName) {
            ((TransportClient) client).addTransportAddress(
                    new InetSocketTransportAddress(InetAddress.getByName(strTransportHostName), 9300));
        }
    }

    // 时间开销很小，几乎忽略不计
    private static void bulidBulkProcessor() {
        bulkProcessor = BulkProcessor.builder(client, new BulkProcessor.Listener() {
            @Override
            public void beforeBulk(long executionId, BulkRequest request) {
            }

            @Override
            public void afterBulk(long executionId, BulkRequest request, BulkResponse response) {
            }

            @Override
            public void afterBulk(long executionId, BulkRequest request, Throwable failure) {
            }
        }).setBulkActions(5000).setBulkSize(new ByteSizeValue(10, ByteSizeUnit.MB))
                .setFlushInterval(TimeValue.timeValueSeconds(5)).setConcurrentRequests(nConcurrentRequests).build();
    }

    // 关闭所有
    public void closeAll() {
        closeBulkProcessor();
        closeClient();
    }

    // 关闭bulkProcessor
    private void closeBulkProcessor() {
        if (bulkProcessor != null) {
            try {
                bulkProcessor.awaitClose(10, TimeUnit.SECONDS);
            } catch (Exception e) {
                log.error("bulkProcessor close err", e);
            }
        }
    }

    // 关闭client
    private void closeClient() {
        if (client != null) {
            client.close();
        }
    }

    // 创建模版，系统启动时创建一次
    public void buildTemplate(String[] astrTemplateNamePrefix) {
        try {
            buildClient();
            IndicesAdminClient iac = client.admin().indices();
            for (String strTemplateNamePrefix : astrTemplateNamePrefix) {
                PutIndexTemplateRequest pitr = new PutIndexTemplateRequest(strTemplateNamePrefix)
                        .template(strTemplateNamePrefix + "*");
                //number_of_shards 机器数减一,number_of_replicas 备份1份就是两份
                pitr.settings(new MapBuilder<String, Object>().put("number_of_shards", 4).put("number_of_replicas", 1)
                        .put("refresh_interval", "1s").map());
                Map<String, Object> defaultMapping = new HashMap<String, Object>();
                // 关闭_all
                defaultMapping.put("_all", new MapBuilder<String, Object>().put("enabled", false).map());
                defaultMapping.put("numeric_detection", false);
                defaultMapping.put("dynamic_templates",
                        new Object[]{
                                new MapBuilder<String, Object>().put("date_tpl",
                                        new MapBuilder<String, Object>().put("match", "dt*")
                                                .put("mapping",
                                                        new MapBuilder<String, Object>().put("type", "date")
                                                                .put("index", "not_analyzed").put("doc_values", true)
                                                                .map())
                                                .map())
                                        .map(),
                                new MapBuilder<String, Object>().put("all_tpl",
                                        new MapBuilder<String, Object>().put("match", "*").put("mapping",
                                                new MapBuilder<String, Object>().put("type", "{dynamic_type}")
                                                        .put("index", "not_analyzed").put("doc_values", true).map())
                                                .map())
                                        .map()});
                pitr.mapping("_default_", defaultMapping);
                iac.putTemplate(pitr);
            }
        } catch (Exception e) {
            log.error("buildTemplate err", e);
        } finally {
            closeClient();
        }
    }
}
