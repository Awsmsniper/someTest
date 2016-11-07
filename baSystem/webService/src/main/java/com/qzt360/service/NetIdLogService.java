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
public class NetIdLogService {
    @Autowired
    private ESRepository es;
    @Value("${es.index.strEqpIndex}")
    private String strEqpIndex;

    @Value("${es.index.strEqpType}")
    private String strEqpType;

    @Value("${es.index.strAWifiNetlogIndexPre}")
    private String strAWifiNetlogIndexPre;

    @Value("${es.index.strAWifiNetIdType}")
    private String strAWifiNetIdType;

    @Autowired
    private IDService id;

    public ListHtml getNetIdLog(String strUnitCode, String strAPMac, String strNetId, String strBeginTime,
                                String strEndTime, int nPage, int nLimit) {
        strAPMac = FuncUtil.washMac(strAPMac);
        log.info(
                "begin getNetIdLog, strUnitCode:{}, strAPMac:{}, strNetId:{},strBeginTime:{}, strEndTime:{}, nPage:{}, nLimit:{}",
                strUnitCode, strAPMac, strNetId, strBeginTime, strEndTime, nPage, nLimit);
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
                        // bqb.must(QueryBuilders.termsQuery("AP_MAC",
                        // source.get("strAPMAC")));
                        bqb.must(QueryBuilders.wildcardQuery("COLLECTION_EQUIPMENT_ID",
                                ("*" + source.get("strAPMAC")).replaceAll("-", "")));
                    }
                } else {
                    eqpHtml.setlCount(0);
                    return eqpHtml;
                }
            }

            if (!"".equals(strAPMac)) {
                log.info("set AP_MAC:" + strAPMac);
                // bqb.must(QueryBuilders.termsQuery("AP_MAC", strAPMac));
                bqb.must(QueryBuilders.wildcardQuery("COLLECTION_EQUIPMENT_ID", "*" + strAPMac.replaceAll("-", "")));
            }

            if (!"".equals(strNetId)) {
                log.info("set CERTIFICATE_CODE:" + strNetId);
                bqb.must(QueryBuilders.termsQuery("CERTIFICATE_CODE", strNetId));
            }
            if (!"".equals(strBeginTime) && !"".equals(strEndTime)) {
                log.info("set Time:" + strBeginTime + ", " + strEndTime);
                bqb.must(QueryBuilders.rangeQuery("dtTime").gte(FuncUtil.Local2TZTime(strBeginTime))
                        .lte(FuncUtil.Local2TZTime(strEndTime)));
            }
            SearchResponse response = es.client.prepareSearch(strAWifiNetlogIndexPre + "*")
                    .setTypes(strAWifiNetIdType).setQuery(bqb).setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
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
                String strEqpId = (String) map.get("COLLECTION_EQUIPMENT_ID");
                if (strEqpId.length() == 21) {
                    String strEqpMac = strEqpId.substring(9, 11) + "-" + strEqpId.substring(11, 13) + "-"
                            + strEqpId.substring(13, 15) + "-" + strEqpId.substring(15, 17) + "-"
                            + strEqpId.substring(17, 19) + "-" + strEqpId.substring(19, 21);
                    map.put("AP_MAC", strEqpMac);
                    // log.debug("补充场所基本信息,strEqpMac: " + strEqpMac);
                    if (hashEqp.containsKey(strEqpMac)) {
                        // log.debug("补充场所基本信息,hashEqp存在strEqpMac: " +
                        // strEqpMac);
                        for (Map.Entry<String, String> entry : hashEqp.get(strEqpMac).entrySet()) {
                            map.put(entry.getKey(), entry.getValue());
                        }
                    } else {
                        // log.debug("补充场所基本信息,hashEqp不存在strEqpMac: " +
                        // strEqpMac);
                        bqb = QueryBuilders.boolQuery();
                        bqb.must(QueryBuilders.termsQuery("_id", strEqpMac));
                        response = es.client.prepareSearch(strEqpIndex).setTypes(strEqpType)
                                .setQuery(bqb).setSearchType(SearchType.DFS_QUERY_THEN_FETCH).setFrom(0).setSize(10)
                                .setExplain(false).execute().actionGet();
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
                } else {
                    Map<String, String> hashTmp = new HashMap<String, String>();
                    hashTmp.put("strUnitCode", "无设备MAC");
                    hashTmp.put("strLat", "");
                    hashTmp.put("strLng", "");
                    map.putAll(hashTmp);
                }
                // 更新虚拟身份类型
                map.put("IDENTIFICATION_TYPE", id.getIdTypeName((String) map.get("IDENTIFICATION_TYPE")));

                // 转换时间
                try {
                    map.put("dtCreate", FuncUtil.TZ2LocalTime((String) map.get("dtCreate")));
                } catch (Exception e) {
                    log.error("", e);
                }
                try {
                    map.put("dtTime", FuncUtil.TZ2LocalTime((String) map.get("dtTime")));
                } catch (Exception e) {
                    log.error("", e);
                }
            }
            eqpHtml.setListMap(listNetLog);
        } catch (Exception e) {
            log.error("", e);
        }
        log.info("end getAuthLog");
        return eqpHtml;
    }

}
