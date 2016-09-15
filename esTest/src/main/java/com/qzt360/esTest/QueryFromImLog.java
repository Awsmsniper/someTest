package com.qzt360.esTest;

import java.util.Map;

import org.apache.log4j.Logger;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.WildcardQueryBuilder;
import org.elasticsearch.search.SearchHit;

public class QueryFromImLog {
	private static Logger logger = Logger.getLogger(QueryFromImLog.class);

	public static void main(String[] args) {
		if (args.length != 5) {
			System.out.println("<indexs> <types> <field> <like or notlike> <account>");
			System.exit(5);
		}
		String[] astrIndex = args[0].split(",");
		String[] astrType = args[1].split(",");
		logger.debug("QueryFromImLog begin");
		ESManager esm = new ESManager("wj-es", "192.168.36.31");
		esm.setup();
		long lBegin = System.currentTimeMillis();

		QueryBuilder qb = QueryBuilders.boolQuery().must(QueryBuilders.termQuery(args[2], args[4]));
		if ("like".equals(args[3])) {
			WildcardQueryBuilder wqb = new WildcardQueryBuilder(args[2], "*" + args[4] + "*");
			qb = QueryBuilders.boolQuery().should(wqb);
		}

		SearchResponse response = esm.client.prepareSearch(astrIndex).setTypes(astrType)
				.setSearchType(SearchType.DFS_QUERY_THEN_FETCH).setQuery(qb).setFrom(0).setSize(100).setExplain(true)
				.execute().actionGet();

		for (SearchHit hit : response.getHits()) {
			Map<String, Object> source = hit.getSource();
			logger.info("source: " + source.toString());
		}
		long lEnd = System.currentTimeMillis();
		esm.cleanup();
		logger.info("time: " + (lEnd - lBegin));
		logger.debug("QueryFromImLog end");
	}

}
