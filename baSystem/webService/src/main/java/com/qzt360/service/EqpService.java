package com.qzt360.service;

import com.qzt360.model.EqpInfo;
import com.qzt360.model.ListHtml;
import com.qzt360.repository.ESRepository;
import com.qzt360.utils.FuncUtil;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

@Slf4j
@Service
public class EqpService {
    @Autowired
    private ESRepository es;

    @Value("${es.index.strEqpIndex}")
    private String strEqpIndex;

    @Value("${es.index.strEqpType}")
    private String strEqpType;


    // 查询设备列表
    public ListHtml getEqpList(String strBusinessLicense, String strUnitName, String strUnitType, String strUnitZone,
                               String strFeedback, String strEqpId, String strBAStatus, String strConfirmStatus, String strBeginTime,
                               String strEndTime, int nPage, int nLimit) {
        ListHtml eqpHtml = new ListHtml();
        eqpHtml.setStrResult("success");
        eqpHtml.setnPage(nPage);
        eqpHtml.setnLimit(nLimit);
        try {
            BoolQueryBuilder bqb = QueryBuilders.boolQuery();
            if (!"".equals(strBusinessLicense)) {
                bqb.must(QueryBuilders.termsQuery("strBusinessLicense", strBusinessLicense));
            }
            if (!"".equals(strUnitName)) {
                bqb.must(QueryBuilders.termsQuery("strUnitName", strUnitName));
            }
            if (!"".equals(strUnitType)) {
                bqb.must(QueryBuilders.termsQuery("strUnitType", strUnitType));
            }
            if (!"".equals(strUnitZone) && !"不限".equals(strUnitZone)) {
                bqb.must(QueryBuilders.wildcardQuery("strUnitZoneCode", FuncUtil.trimEndChar(strUnitZone, "0") + "*"));
            }
            if (!"".equals(strFeedback) && !"不限".equals(strFeedback)) {
                if ("是".equals(strFeedback)) {
                    bqb.must(QueryBuilders.termsQuery("bFeedback", true));
                } else {
                    bqb.mustNot(QueryBuilders.termsQuery("bFeedback", true));
                }
            }
            if (!"".equals(strEqpId)) {
                bqb.must(QueryBuilders.termsQuery("_id", strEqpId));
            }

            if (!"".equals(strBAStatus) && !"不限".equals(strBAStatus)) {
                if ("是".equals(strBAStatus)) {
                    bqb.must(QueryBuilders.termsQuery("bBAStatus", true));
                } else {
                    bqb.mustNot(QueryBuilders.termsQuery("bBAStatus", true));
                }
            }
            if (!"".equals(strConfirmStatus) && !"不限".equals(strConfirmStatus)) {
                if ("是".equals(strConfirmStatus)) {
                    bqb.must(QueryBuilders.termsQuery("bConfirmStatus", true));
                } else {
                    bqb.mustNot(QueryBuilders.termsQuery("bConfirmStatus", true));
                }
            }
            if (!"".equals(strBeginTime) && !"".equals(strEndTime)) {
                bqb.must(QueryBuilders.rangeQuery("dtCreateTime").gte(FuncUtil.Local2TZTime(strBeginTime))
                        .lte(FuncUtil.Local2TZTime(strEndTime)));
            }
            SearchResponse response = es.client.prepareSearch(strEqpIndex).setTypes(strEqpType)
                    .setQuery(bqb).setSearchType(SearchType.DFS_QUERY_THEN_FETCH).setFrom((nPage - 1) * nLimit)
                    .setSize(nLimit).addSort("dtCreateTime", SortOrder.DESC).setExplain(false).execute().actionGet();
            eqpHtml.setlCount(response.getHits().getTotalHits());
            List<Map<String, Object>> listMap = new ArrayList<Map<String, Object>>();
            for (SearchHit hit : response.getHits()) {
                Map<String, Object> source = hit.getSource();
                source.put("strEqpId", hit.getId());
                listMap.add(source);
            }
            for (Map<String, Object> map : listMap) {
                // 转换时间
                try {
                    map.put("dtCreateTime", FuncUtil.TZ2LocalTime((String) map.get("dtCreateTime")));
                } catch (Exception e) {
                    log.error("", e);
                }
                try {
                    map.put("dtUpdateTimeM", FuncUtil.TZ2LocalTime((String) map.get("dtUpdateTimeM")));
                } catch (Exception e) {
                    log.error("", e);
                }
            }
            eqpHtml.setListMap(listMap);
        } catch (Exception e) {
            log.error("", e);
        }
        return eqpHtml;
    }

    // 更新设备信息
    public boolean updateEqpInfo(String strEqpId, EqpInfo eqpInfo) {
        try {
            // 动作类型,1 信息确认 2 取消确认 3 保存
            if (eqpInfo.getNActionType() == 1) {
                // 1 信息确认
                log.info("信息确认,strMemo:{}", eqpInfo.getStrMemo());
                UpdateRequest updateRequest = new UpdateRequest(strEqpIndex, strEqpType, strEqpId)
                        .doc(jsonBuilder().startObject().field("strUnitNameM", eqpInfo.getStrUnitName())
                                .field("strUnitLinkmanM", eqpInfo.getStrUnitLinkman())
                                .field("strUnitLinkmanTelM", eqpInfo.getStrUnitLinkmanTel())
                                .field("strUnitAddrM", eqpInfo.getStrUnitAddr())
                                .field("strBusinessLicenseM", eqpInfo.getStrBusinessLicense())
                                .field("strMemo", eqpInfo.getStrMemo())
                                .field("dtUpdateTimeM", new Date(System.currentTimeMillis()))
                                .field("bFeedback", eqpInfo.isBFeedback()).field("bConfirmStatus", true)
                                .field("bNeedComplete", true).field("strOperator", eqpInfo.getStrOperator())
                                .endObject());
                es.client.update(updateRequest).get();
            } else if (eqpInfo.getNActionType() == 2) {
                // 2 取消确认
                log.info("取消确认,strMemo:{}", eqpInfo.getStrMemo());
                UpdateRequest updateRequest = new UpdateRequest(strEqpIndex, strEqpType, strEqpId)
                        .doc(jsonBuilder().startObject().field("dtUpdateTimeM", new Date(System.currentTimeMillis()))
                                .field("bConfirmStatus", false).field("strOperator", eqpInfo.getStrOperator())
                                .endObject());
                es.client.update(updateRequest).get();
            } else if (eqpInfo.getNActionType() == 3) {
                // 3 保存
                log.info("保存,strMemo:{}", eqpInfo.getStrMemo());
                UpdateRequest updateRequest = new UpdateRequest(strEqpIndex, strEqpType, strEqpId)
                        .doc(jsonBuilder().startObject().field("strUnitNameM", eqpInfo.getStrUnitName())
                                .field("strUnitLinkmanM", eqpInfo.getStrUnitLinkman())
                                .field("strUnitLinkmanTelM", eqpInfo.getStrUnitLinkmanTel())
                                .field("strUnitAddrM", eqpInfo.getStrUnitAddr())
                                .field("strBusinessLicenseM", eqpInfo.getStrBusinessLicense())
                                .field("strMemo", eqpInfo.getStrMemo())
                                .field("dtUpdateTimeM", new Date(System.currentTimeMillis()))
                                .field("bFeedback", eqpInfo.isBFeedback()).field("bNeedComplete", true)
                                .field("strOperator", eqpInfo.getStrOperator()).endObject());
                es.client.update(updateRequest).get();
            }
        } catch (Exception e) {
            log.error("", e);
            return false;
        }
        return true;
    }

    // 根据设备Id查询设备信息
    public EqpInfo readEqpInfo(String strEqpId) {
        EqpInfo eqpInfo = new EqpInfo();
        try {
            BoolQueryBuilder bqb = QueryBuilders.boolQuery();
            if (!"".equals(strEqpId)) {
                bqb.must(QueryBuilders.termsQuery("_id", strEqpId));
                SearchResponse response = es.client.prepareSearch(strEqpIndex).setTypes(strEqpType)
                        .setQuery(bqb).setSearchType(SearchType.DFS_QUERY_THEN_FETCH).setFrom(0).setSize(10)
                        .setExplain(false).execute().actionGet();
                for (SearchHit hit : response.getHits()) {
                    Map<String, Object> source = hit.getSource();
                    eqpInfo.setStrBusinessLicense("" + source.get("strBusinessLicense"));
                    eqpInfo.setStrBusinessLicenseM(FuncUtil.isNull(source.get("strBusinessLicenseM"))
                            ? "" + source.get("strBusinessLicense") : "" + source.get("strBusinessLicenseM"));
                    eqpInfo.setStrEqpId(strEqpId);
                    eqpInfo.setStrUnitAddr("" + source.get("strUnitAddr"));
                    eqpInfo.setStrUnitAddrM(FuncUtil.isNull(source.get("strUnitAddrM")) ? "" + source.get("strUnitAddr")
                            : "" + source.get("strUnitAddrM"));
                    eqpInfo.setStrUnitCode("" + source.get("strUnitCode"));
                    eqpInfo.setStrUnitLinkman("" + source.get("strUnitLinkman"));
                    eqpInfo.setStrUnitLinkmanM(FuncUtil.isNull(source.get("strUnitLinkmanM"))
                            ? "" + source.get("strUnitLinkman") : "" + source.get("strUnitLinkmanM"));
                    eqpInfo.setStrUnitLinkmanTel("" + source.get("strUnitLinkmanTel"));
                    eqpInfo.setStrUnitLinkmanTelM(FuncUtil.isNull(source.get("strUnitLinkmanTelM"))
                            ? "" + source.get("strUnitLinkmanTel") : "" + source.get("strUnitLinkmanTelM"));
                    eqpInfo.setStrUnitName("" + source.get("strUnitName"));
                    eqpInfo.setStrUnitNameM(FuncUtil.isNull(source.get("strUnitNameM")) ? "" + source.get("strUnitName")
                            : "" + source.get("strUnitNameM"));
                    eqpInfo.setStrUnitZoneName("" + source.get("strUnitZoneName"));
                    eqpInfo.setStrUnitZoneCode("" + source.get("strUnitZoneCode"));
                    eqpInfo.setStrLat("" + source.get("strLat"));
                    eqpInfo.setStrLng("" + source.get("strLng"));
                    boolean bBAStatus = false;
                    if (!FuncUtil.isNull(source.get("bBAStatus"))) {
                        if ((Boolean) source.get("bBAStatus")) {
                            bBAStatus = true;
                        }
                    }
                    eqpInfo.setBBAStatus(bBAStatus);
                    eqpInfo.setDtBATime(FuncUtil.TZ2LocalTime((String) source.get("dtBATime")));
                    eqpInfo.setDtUpdateTimeM(FuncUtil.TZ2LocalTime((String) source.get("dtUpdateTimeM")));
                    boolean bDataStatus = false;
                    if (!FuncUtil.isNull(source.get("bDataStatus"))) {
                        if ((Boolean) source.get("bDataStatus")) {
                            bDataStatus = true;
                        }
                    }
                    eqpInfo.setBDataStatus(bDataStatus);
                    eqpInfo.setDtCreateTime(FuncUtil.TZ2LocalTime((String) source.get("dtCreateTime")));
                    eqpInfo.setStrMemo(FuncUtil.isNull(source.get("strMemo")) ? "" : "" + source.get("strMemo"));
                    eqpInfo.setBFeedback(
                            FuncUtil.isNull(source.get("bFeedback")) ? false : (Boolean) source.get("bFeedback"));
                    eqpInfo.setStrOperator(
                            FuncUtil.isNull(source.get("strOperator")) ? "" : "" + source.get("strOperator"));
                    eqpInfo.setBConfirmStatus(FuncUtil.isNull(source.get("bConfirmStatus")) ? false
                            : (Boolean) source.get("bConfirmStatus"));
                }
            } else {
                log.info("设备Id不能为空");
                return null;
            }
        } catch (Exception e) {
            log.error("", e);
            return null;
        }
        return eqpInfo;
    }
}
