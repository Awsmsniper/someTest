package com.qzt360.service;

import com.qzt360.model.BAInfo;
import com.qzt360.model.EqpInfoBA;
import com.qzt360.repository.ESRepository;
import com.qzt360.utils.FuncUtil;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class BAService {
    @Autowired
    private ESRepository es;
    @Value("${es.index.strEqpIndex}")
    private String strEqpIndex;

    @Value("${es.index.strEqpType}")
    private String strEqpType;

    public BAInfo getBAInfoById(String strId) {
        BAInfo baInfo = null;
        try {
            QueryBuilder qb = null;
            if (strId.split("-").length == 6) {
                // strId为MAC
                log.info("strEqpId:{}", strId);
                qb = QueryBuilders.termQuery("_id", strId);
            } else {
                // strId为营业执照strBusinessLicense
                log.info("strBusinessLicense:{}", strId);
                // qb = QueryBuilders.termQuery("strGroupCode", strId);
                qb = QueryBuilders.matchQuery("strBusinessLicenseM", strId);
            }
            SearchResponse response = es.client.prepareSearch(strEqpIndex).setTypes(strEqpType)
                    .setSearchType(SearchType.DFS_QUERY_THEN_FETCH).setQuery(qb).setFrom(0).setSize(20).setExplain(true)
                    .execute().actionGet();

            List<EqpInfoBA> listEqp = new ArrayList<EqpInfoBA>();
            for (SearchHit hit : response.getHits()) {
                Map<String, Object> source = hit.getSource();
                if (baInfo == null) {
                    baInfo = new BAInfo(
                            FuncUtil.isNull("" + source.get("strUnitNameM")) ? "" + source.get("strUnitName")
                                    : "" + source.get("strUnitNameM"),
                            FuncUtil.isNull("" + source.get("strUnitLinkmanM")) ? "" + source.get("strUnitLinkman")
                                    : "" + source.get("strUnitLinkmanM"),
                            FuncUtil.isNull("" + source.get("strUnitLinkmanTelM")) ? "" + source.get("strUnitLinkmanTel")
                                    : "" + source.get("strUnitLinkmanTelM"),
                            FuncUtil.isNull("" + source.get("strUnitAddrM")) ? "" + source.get("strUnitAddr")
                                    : "" + source.get("strUnitAddrM"),
                            "" + source.get("strUnitPostcode"), "" + source.get("strUnitFax"),
                            "" + source.get("strCustmerManager"), "" + source.get("strCustmerManagerTel"),
                            "" + source.get("strUnitType"), "" + source.get("strAccessType"),
                            FuncUtil.isNull("" + source.get("strBusinessLicenseM"))
                                    ? "" + source.get("strBusinessLicense") : "" + source.get("strBusinessLicenseM"),
                            "" + source.get("strCorporateID"), "" + source.get("strCorporateTel"),
                            "" + source.get("strRoomNum"), "" + source.get("strCreateTime"),
                            "" + source.get("strUnitZoneName"));
                }
                String strDataStatus = "异常";
                String strBAStatus = "未备案";
                String strBATime = "";
                if (!FuncUtil.isNull(source.get("bDataStatus"))) {
                    if ((Boolean) source.get("bDataStatus")) {
                        strDataStatus = "正常";
                    }
                }
                if (!FuncUtil.isNull(source.get("bBAStatus"))) {
                    if ((Boolean) source.get("bBAStatus")) {
                        strBAStatus = "已备案";
                        if (!FuncUtil.isNull(source.get("dtBATime"))) {
                            strBATime = FuncUtil.TZ2LocalTime((String) source.get("dtBATime"));
                        }
                    }
                }
                listEqp.add(new EqpInfoBA(hit.getId(), strDataStatus, strBAStatus, strBATime));
            }
            if (baInfo == null) {
                baInfo = new BAInfo();
                baInfo.setStrUnitName("不存在");
                if (strId.split("-").length == 6) {
                    EqpInfoBA eqpInfo = new EqpInfoBA(strId);
                    listEqp.add(eqpInfo);
                    baInfo.setListEqp(listEqp);
                } else {
                    baInfo.setStrBusinessLicense(strId);

                }
            } else {
                baInfo.setListEqp(listEqp);
            }

        } catch (Exception e) {
            log.error("", e);
        }
        return baInfo;
    }
}
