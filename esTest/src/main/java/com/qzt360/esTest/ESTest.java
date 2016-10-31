package com.qzt360.esTest;

import com.qzt360.utils.ESUtil;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;

import java.util.Map;

/**
 * Created by zhaogj on 31/10/2016.
 */
@Slf4j
public class ESTest {
    //测试复用client会不会出问题
    public void doTest() {
        try {
            ESUtil.buildAll();
            BoolQueryBuilder bqb = QueryBuilders.boolQuery();
            bqb.must(QueryBuilders.existsQuery("AP_MAC"));
            SearchResponse response = ESUtil.client.prepareSearch("netlog_*").setTypes("aWifi_0001")
                    .setSearchType(SearchType.DFS_QUERY_THEN_FETCH).setQuery(bqb).setFrom(0).setSize(10000)
                    .setExplain(true).execute().actionGet();
            log.info("log count:{}", response.getHits().getTotalHits());
            for (SearchHit hit : response.getHits()) {
                Map<String, Object> source = hit.getSource();
                String strApMac = (String) source.get("AP_MAC");
                bqb = QueryBuilders.boolQuery();
                bqb.must(QueryBuilders.termQuery("_id", strApMac));
                SearchResponse responseEqp = ESUtil.client.prepareSearch("eqp").setTypes("aWifi")
                        .setSearchType(SearchType.DFS_QUERY_THEN_FETCH).setQuery(bqb).setFrom(0).setSize(10000)
                        .setExplain(true).execute().actionGet();
                log.info("eqp count:{}", responseEqp.getHits().getTotalHits());
                for (SearchHit hitEqp : responseEqp.getHits()) {
                    log.info("AP_MAC:{},UnitName:{}", strApMac, (String) hitEqp.getSource().get("strUnitName"));
                }
            }
        } catch (Exception e) {
            log.error("", e);
        }
    }
}
