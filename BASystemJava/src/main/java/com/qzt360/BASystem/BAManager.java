package com.qzt360.BASystem;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;

public class BAManager {
	private static Logger logger = Logger.getLogger(BAManager.class);

	public BAInfo getBAInfoById(String strId) {
		ESManager esm = new ESManager("qzt360-es", "172.16.29.29");
		esm.setup();
		QueryBuilder qb = null;
		if (strId.split(":").length == 6) {
			// strId为MAC
			logger.debug("MAC:" + strId);
			qb = QueryBuilders.termQuery("_id", strId);
		} else {
			// strId为组织结构代码strGroupCode
			logger.debug("strGroupCode:" + strId);
			qb = QueryBuilders.termQuery("strGroupCode", strId);
			qb = QueryBuilders.matchQuery("strGroupCode", strId);
		}
		SearchResponse response = esm.client.prepareSearch("ba_info").setTypes("aWifi")
				.setSearchType(SearchType.DFS_QUERY_THEN_FETCH).setQuery(qb).setFrom(0).setSize(60).setExplain(true)
				.execute().actionGet();
		BAInfo baInfo = new BAInfo();
		List<EqpInfo> listEqp = new ArrayList<EqpInfo>();
		for (SearchHit hit : response.getHits()) {
			Map<String, Object> source = hit.getSource();
			// logger.info("user: " + source.get("user"));
			logger.debug("source: " + source.toString());
			// {strBAStatus=2016-04-11 12:33:11, strZone=广州市-天河区,
			// strGroupCode=0632323-2, strBATime=正常, strName=七天连锁酒店天河店,
			// strAddr=广州市天河区龙口东路118号, strDataStatus=已备案, strUnitType=酒店,
			// dtBATime=2016-07-07T02:10:45.245Z}
			baInfo.setStrName("" + source.get("strName"));
			baInfo.setStrGroupCode("" + source.get("strGroupCode"));
			baInfo.setStrAddr("" + source.get("strAddr"));
			baInfo.setStrUnitType("" + source.get("strUnitType"));
			baInfo.setStrZone("" + source.get("strZone"));
			EqpInfo eqpInfo = new EqpInfo(hit.getId(), "" + source.get("strDataStatus"), "" + source.get("strBAStatus"),
					"" + source.get("strBATime"));
			listEqp.add(eqpInfo);
		}
		baInfo.setListEqp(listEqp);
		esm.cleanup();
		return baInfo;
	}
}
