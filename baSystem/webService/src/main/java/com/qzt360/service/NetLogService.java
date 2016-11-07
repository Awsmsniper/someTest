package com.qzt360.service;

import com.qzt360.model.ListHtml;
import com.qzt360.repository.ESRepository;
import com.qzt360.utils.FuncUtil;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class NetLogService {
    @Autowired
    private ESRepository es;
    @Value("${es.index.strEqpIndex}")
    private String strEqpIndex;

    @Value("${es.index.strEqpType}")
    private String strEqpType;

    @Value("${es.index.strAWifiNetlogIndexPre}")
    private String strAWifiNetlogIndexPre;

    @Value("${es.index.strAWifiNetlogType}")
    private String strAWifiNetlogType;

    public ListHtml getNetLog(String strUnitCode, String strCollectionEquipmentId, String strAPMac, String strMac,
                              String strBeginTime, String strEndTime, int nPage, int nLimit) {
        strAPMac = FuncUtil.washMac(strAPMac);
        strMac = FuncUtil.washMac(strMac);
        log.info("begin getNetLog, strUnitCode: " + strUnitCode + ", strCollectionEquipmentId: "
                + strCollectionEquipmentId + ", strAPMac: " + strAPMac + ", strMac:" + strMac + ", strBeginTime:"
                + strBeginTime + ", strEndTime:" + strEndTime + ", nPage:" + nPage + ", nLimit:" + nLimit);
        ListHtml eqpHtml = new ListHtml();
        eqpHtml.setStrResult("success");
        eqpHtml.setnPage(nPage);
        eqpHtml.setnLimit(nLimit);
        try {
            BoolQueryBuilder bqb = QueryBuilders.boolQuery();
            if (!"".equals(strUnitCode)) {
                log.info("set strUnitCode:" + strUnitCode);
                bqb.must(QueryBuilders.termsQuery("strUnitCode", strUnitCode));
                SearchResponse response = es.client.prepareSearch(strEqpIndex).setTypes(strEqpType)
                        .setQuery(bqb).setSearchType(SearchType.DFS_QUERY_THEN_FETCH).setFrom(0).setSize(10)
                        .setExplain(false).execute().actionGet();
                if (response.getHits().getTotalHits() >= 1) {
                    for (SearchHit hit : response.getHits()) {
                        Map<String, Object> source = hit.getSource();
                        bqb = QueryBuilders.boolQuery();
                        bqb.must(QueryBuilders.termsQuery("AP_MAC", source.get("strAPMAC")));
                    }
                } else {
                    eqpHtml.setlCount(0);
                    return eqpHtml;
                }
            }
            if (!"".equals(strCollectionEquipmentId)) {
                log.info("set strCollectionEquipmentId:" + strCollectionEquipmentId);
                bqb.must(QueryBuilders.termsQuery("COLLECTION_EQUIPMENT_ID", strCollectionEquipmentId));
            }
            if (!"".equals(strAPMac)) {
                log.info("set AP_MAC:" + strAPMac);
                bqb.must(QueryBuilders.termsQuery("AP_MAC", strAPMac));
            }
            if (!"".equals(strMac)) {
                log.info("set MAC:" + strMac);
                bqb.must(QueryBuilders.termsQuery("MAC", strMac));
            }
            if (!"".equals(strBeginTime) && !"".equals(strEndTime)) {
                log.info("set Time:" + strBeginTime + ", " + strEndTime);
                bqb.must(QueryBuilders.rangeQuery("dtTime").gte(FuncUtil.Local2TZTime(strBeginTime))
                        .lte(FuncUtil.Local2TZTime(strEndTime)));
            }
            SearchResponse response = es.client.prepareSearch(strAWifiNetlogIndexPre + "*")
                    .setTypes(strAWifiNetlogType).setQuery(bqb).setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                    .setFrom((nPage - 1) * nLimit).setSize(nLimit).addSort("dtTime", SortOrder.DESC).setExplain(false)
                    .execute().actionGet();
            eqpHtml.setlCount(response.getHits().getTotalHits());
            log.info("count:" + eqpHtml.getlCount());
            List<Map<String, Object>> listNetLog = new ArrayList<Map<String, Object>>();
            for (SearchHit hit : response.getHits()) {
                Map<String, Object> source = hit.getSource();
                source.put("strLineId", hit.getId());
                listNetLog.add(source);
            }
            // 结果数据的二次处理
            // key:eqpMac,key:fileName,value:value
            Map<String, Map<String, String>> hashEqp = new HashMap<String, Map<String, String>>();
            for (Map<String, Object> map : listNetLog) {
                String strEqpMac = (String) map.get("AP_MAC");
                // log.debug("补充场所基本信息,strEqpMac: " + strEqpMac);
                if (hashEqp.containsKey(strEqpMac)) {
                    // log.debug("补充场所基本信息,hashEqp存在strEqpMac: " + strEqpMac);
                    for (Map.Entry<String, String> entry : hashEqp.get(strEqpMac).entrySet()) {
                        map.put(entry.getKey(), entry.getValue());
                    }
                } else {
                    // log.debug("补充场所基本信息,hashEqp不存在strEqpMac: " + strEqpMac);
                    bqb = QueryBuilders.boolQuery();
                    bqb.must(QueryBuilders.termsQuery("_id", strEqpMac));
                    response = es.client.prepareSearch(strEqpIndex).setTypes(strEqpType).setQuery(bqb)
                            .setSearchType(SearchType.DFS_QUERY_THEN_FETCH).setFrom(0).setSize(10).setExplain(false)
                            .execute().actionGet();
                    if (response.getHits().getTotalHits() >= 1) {
                        for (SearchHit hit : response.getHits()) {
                            Map<String, Object> source = hit.getSource();
                            Map<String, String> hashTmp = new HashMap<String, String>();
                            if (source.containsKey("strUnitCode")) {
                                hashTmp.put("strUnitCode", (String) source.get("strUnitCode"));
                            } else {
                                hashTmp.put("strUnitCode", "");
                            }
                            if (source.containsKey("strLat")) {
                                hashTmp.put("strLat", (String) source.get("strLat"));
                            } else {
                                hashTmp.put("strLat", "");
                            }
                            if (source.containsKey("strLng")) {
                                hashTmp.put("strLng", (String) source.get("strLng"));
                            } else {
                                hashTmp.put("strLng", "");
                            }
                            map.putAll(hashTmp);
                            hashEqp.put(strEqpMac, hashTmp);
                            // 只取第一行
                            break;
                        }
                    } else {
                        Map<String, String> hashTmp = new HashMap<String, String>();
                        hashTmp.put("strUnitCode", "设备不存在");
                        hashTmp.put("strLat", "");
                        hashTmp.put("strLng", "");
                        map.putAll(hashTmp);
                        hashEqp.put(strEqpMac, hashTmp);
                    }
                }
                // 更新网络应用服务类型
                map.put("NETWORK_APP", getNetworkAppName((String) map.get("NETWORK_APP")));
                // 转换IP
                try {
                    map.put("SRC_IP", FuncUtil.Long2StrIP(Long.parseLong((String) map.get("SRC_IP"))));
                } catch (Exception e) {
                    log.error("", e);
                }
                // 转换IP
                try {
                    map.put("IP_ADDRESS", FuncUtil.Long2StrIP(Long.parseLong((String) map.get("IP_ADDRESS"))));
                } catch (Exception e) {
                    log.error("", e);
                }
                // 转换时间
                map.put("dtTime", FuncUtil.TZ2LocalTime((String) map.get("dtTime")));
            }
            eqpHtml.setListMap(listNetLog);
        } catch (Exception e) {
            log.error("", e);
        }
        log.info("end getNetLog");
        return eqpHtml;
    }

    private String getNetworkAppName(String strNetworkApp) {
        String strName = "";
        if ("01".equalsIgnoreCase(strNetworkApp)) {
            strName = "HTTP协议";
        } else if ("02".equalsIgnoreCase(strNetworkApp)) {
            strName = "WAP协议";
        } else if ("03".equalsIgnoreCase(strNetworkApp)) {
            strName = "SMTP协议";
        } else if ("04".equalsIgnoreCase(strNetworkApp)) {
            strName = "POP3协议";
        } else if ("05".equalsIgnoreCase(strNetworkApp)) {
            strName = "IMAP协议";
        } else if ("06".equalsIgnoreCase(strNetworkApp)) {
            strName = "NNTP协议";
        } else if ("07".equalsIgnoreCase(strNetworkApp)) {
            strName = "FTP协议";
        } else if ("08".equalsIgnoreCase(strNetworkApp)) {
            strName = "SFTP协议";
        } else if ("09".equalsIgnoreCase(strNetworkApp)) {
            strName = "TELNET协议";
        } else if ("12".equalsIgnoreCase(strNetworkApp)) {
            strName = "HTTPS协议";
        } else if ("11".equalsIgnoreCase(strNetworkApp)) {
            strName = "RSTP协议";
        } else if ("12".equalsIgnoreCase(strNetworkApp)) {
            strName = "MMS协议";
        } else if ("13".equalsIgnoreCase(strNetworkApp)) {
            strName = "WEP协议";
        } else if ("14".equalsIgnoreCase(strNetworkApp)) {
            strName = "WPA协议";
        } else if ("15".equalsIgnoreCase(strNetworkApp)) {
            strName = "PPTP协议";
        } else if ("16".equalsIgnoreCase(strNetworkApp)) {
            strName = "L2TP协议";
        } else if ("17".equalsIgnoreCase(strNetworkApp)) {
            strName = "SOCKS代理协议";
        } else if ("18".equalsIgnoreCase(strNetworkApp)) {
            strName = "Compo";
        } else if ("19".equalsIgnoreCase(strNetworkApp)) {
            strName = "Cmsmtp";
        } else if ("91".equalsIgnoreCase(strNetworkApp)) {
            strName = "私有协议";
        } else if ("99".equalsIgnoreCase(strNetworkApp)) {
            strName = "其他";
        } else {
            strName = "--";
        }
        return strName;
    }
}
