package com.qzt360.esTest;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;
import static org.elasticsearch.index.query.QueryBuilders.matchAllQuery;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.SearchHit;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Map2ES {

	public void map2ES() {
		log.info("start map2ES");
		ESManager esm = new ESManager();
		esm.setup();
		Map<String, Object> json = new HashMap<String, Object>();
		json.put("strName", "zhaogj");
		List<String> listTel = new ArrayList<String>();
		listTel.add("13391879355");
		listTel.add("15010281799");
		json.put("strTels", listTel);
		json.put("dtCreate", new Date(System.currentTimeMillis()));
		esm.bulkProcessor.add(new IndexRequest("index_test", "type_test", "id_test").source(json));
		esm.cleanup();
		log.info("end map2ES");
	}

	public void es2Map() {
		log.info("start es2Map");
		ESManager esm = new ESManager();
		esm.setup();
		QueryBuilder qb = matchAllQuery();
		SearchResponse response = esm.client.prepareSearch("index_test").setTypes("type_test")
				.setSearchType(SearchType.DFS_QUERY_THEN_FETCH).setQuery(qb).setFrom(0).setSize(10).setExplain(true)
				.execute().actionGet();
		for (SearchHit hit : response.getHits()) {
			Map<String, Object> map = hit.getSource();
			String strName = (String) map.get("strName");
			log.info("strName:{}", strName);
			// String strTels = (String) map.get("strTels");
			// log.info("strTels:{}", strTels);
			@SuppressWarnings("unchecked")
			List<String> listTel = (List<String>) map.get("strTels");
			for (String strTel : listTel) {
				log.info("strTel:{}", strTel);
			}
			try {
				listTel.add("15010281788");
				// 更新数据
				IndexRequest indexRequest = new IndexRequest("index_test", "type_test", "id_test")
						.source(jsonBuilder().startObject().field("strName", "zhaogj").field("strTels", listTel)
								.field("dtCreate", new Date(System.currentTimeMillis())).endObject());
				UpdateRequest updateRequest = new UpdateRequest("index_test", "type_test", "id_test")
						.doc(jsonBuilder().startObject().field("strName", "zhaogj").field("strTels", listTel)
								.field("dtUpdate", new Date(System.currentTimeMillis())).endObject())
						.upsert(indexRequest);
				esm.client.update(updateRequest).get();
			} catch (Exception e) {
				log.error("update es err", e);
			}
		}
		esm.cleanup();
		log.info("end es2Map");
	}
}
