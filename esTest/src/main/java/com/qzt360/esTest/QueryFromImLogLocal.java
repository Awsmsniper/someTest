package com.qzt360.esTest;

import java.util.Map;

import org.apache.log4j.Logger;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.WildcardQueryBuilder;
import org.elasticsearch.search.SearchHit;

public class QueryFromImLogLocal {
	private static Logger logger = Logger.getLogger(QueryFromImLogLocal.class);;

	public static void main(String[] args) {
		String strAccount = "19462294";
		if (args.length == 1) {
			strAccount = args[0];
		}
		logger.debug("QueryFromImLogLocal begin");
		long lBegin = System.currentTimeMillis();
		ESManager esm = new ESManager();

		esm.setup();

		WildcardQueryBuilder wqb = new WildcardQueryBuilder("strCallerId", "*" + strAccount + "*");
		QueryBuilder qb = QueryBuilders.boolQuery().should(wqb);
		logger.debug("SearchResponse begin");
		SearchResponse response = esm.client.prepareSearch("im_20160303").setTypes("im")
				.setSearchType(SearchType.DFS_QUERY_THEN_FETCH).setQuery(qb).setFrom(0).setSize(100).setExplain(true)
				.execute().actionGet();
		logger.debug("SearchResponse end");
		for (SearchHit hit : response.getHits()) {
			Map<String, Object> source = hit.getSource();
			// logger.info("user: " + source.get("user"));
			logger.info("source: " + source.toString());
		}

		esm.cleanup();

		long lEnd = System.currentTimeMillis();
		logger.info("time: " + (lEnd - lBegin));
		logger.debug("QueryFromImLogLocal end");
	}

}
