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
public class AuthLogService {
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


    public ListHtml getAuthLog(String strUnitCode, String strCollectionEquipmentId, String strAPMac, String strMac,
                               String strAccount, String strBeginTime, String strEndTime, int nPage, int nLimit) {
        strAPMac = FuncUtil.washMac(strAPMac);
        strMac = FuncUtil.washMac(strMac);
        log.info("begin getAuthLog, strUnitCode: " + strUnitCode + ", strCollectionEquipmentId: "
                + strCollectionEquipmentId + ", strAPMac: " + strAPMac + ", strMac:" + strMac + ", strAccount:"
                + strAccount + ", strBeginTime:" + strBeginTime + ", strEndTime:" + strEndTime + ", nPage:" + nPage
                + ", nLimit:" + nLimit);
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
            if (!"".equals(strAccount)) {
                log.info("set AUTH_ACCOUNT:" + strAccount);
                bqb.must(QueryBuilders.termsQuery("AUTH_ACCOUNT", strAccount));
            }
            if (!"".equals(strBeginTime) && !"".equals(strEndTime)) {
                log.info("set Time:" + strBeginTime + ", " + strEndTime);
                bqb.must(QueryBuilders.rangeQuery("dtOnTime").gte(FuncUtil.Local2TZTime(strBeginTime))
                        .lte(FuncUtil.Local2TZTime(strEndTime)));
            }
            SearchResponse response = es.client.prepareSearch(strAWifiNetlogIndexPre + "*")
                    .setTypes(strAWifiOnOffLineType).setQuery(bqb)
                    .setSearchType(SearchType.DFS_QUERY_THEN_FETCH).setFrom((nPage - 1) * nLimit).setSize(nLimit)
                    .addSort("dtOnTime", SortOrder.DESC).setExplain(false).execute().actionGet();
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
                // 更新认证类型
                map.put("AUTH_TYPE", getAuthTypeName((String) map.get("AUTH_TYPE")));
                // 更新证件类型
                map.put("CERTIFICATE_TYPE", getCertName((String) map.get("CERTIFICATE_TYPE")));

                // 转换IP
                try {
                    map.put("SRC_IP", FuncUtil.Long2StrIP(Long.parseLong((String) map.get("SRC_IP"))));
                } catch (Exception e) {
                    log.error("", e);
                }
                // 转换时间
                try {
                    map.put("dtCreate", FuncUtil.TZ2LocalTime((String) map.get("dtCreate")));
                } catch (Exception e) {
                    log.error("", e);
                }
                try {
                    map.put("dtOffTime", FuncUtil.TZ2LocalTime((String) map.get("dtOffTime")));
                } catch (Exception e) {
                    log.error("", e);
                }
                try {
                    map.put("dtOnTime", FuncUtil.TZ2LocalTime((String) map.get("dtOnTime")));
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

    private String getAuthTypeName(String strAuthType) {
        String strName = "";
        if ("1020001".equalsIgnoreCase(strAuthType)) {
            strName = "ADSL宽带帐号";
        } else if ("1020002".equalsIgnoreCase(strAuthType)) {
            strName = "MAC";
        } else if ("1020003".equalsIgnoreCase(strAuthType)) {
            strName = "IMSI";
        } else if ("1020004".equalsIgnoreCase(strAuthType)) {
            strName = "手机号";
        } else if ("1020005".equalsIgnoreCase(strAuthType)) {
            strName = "网吧上网卡";
        } else if ("1020100".equalsIgnoreCase(strAuthType)) {
            strName = "APP认证（UID）";
        } else if ("1021000".equalsIgnoreCase(strAuthType)) {
            strName = "身份证件";
        } else if ("1021111".equalsIgnoreCase(strAuthType)) {
            strName = "身份证";
        } else if ("1021133".equalsIgnoreCase(strAuthType)) {
            strName = "学生证";
        } else if ("1021335".equalsIgnoreCase(strAuthType)) {
            strName = "驾驶证";
        } else if ("1021114".equalsIgnoreCase(strAuthType)) {
            strName = "军官证";
        } else if ("1021123".equalsIgnoreCase(strAuthType)) {
            strName = "警官证";
        } else if ("1021113".equalsIgnoreCase(strAuthType)) {
            strName = "户口簿";
        } else if ("1021414".equalsIgnoreCase(strAuthType)) {
            strName = "护照";
        } else if ("1021511".equalsIgnoreCase(strAuthType)) {
            strName = "台胞证";
        } else if ("1021516".equalsIgnoreCase(strAuthType)) {
            strName = "回乡证";
        } else if ("1021159".equalsIgnoreCase(strAuthType)) {
            strName = "社保卡";
        } else if ("1021233".equalsIgnoreCase(strAuthType)) {
            strName = "士兵证/军人证";
        } else if ("1021990".equalsIgnoreCase(strAuthType)) {
            strName = "其他证件";
        } else if ("1029999".equalsIgnoreCase(strAuthType)) {
            strName = "其他";
        } else {
            strName = "其他";
        }
        return strName;
    }

    private String getCertName(String strCertType) {
        String strName = "";
        if ("111".equalsIgnoreCase(strCertType)) {
            strName = "身份证";
        } else if ("112".equalsIgnoreCase(strCertType)) {
            strName = "临时身份证";
        } else if ("113".equalsIgnoreCase(strCertType)) {
            strName = "户口簿";
        } else if ("116".equalsIgnoreCase(strCertType)) {
            strName = "暂住证";
        } else if ("131".equalsIgnoreCase(strCertType)) {
            strName = "工作证";
        } else if ("133".equalsIgnoreCase(strCertType)) {
            strName = "学生证";
        } else {
            strName = "其他";
        }
        return strName;
    }
}
