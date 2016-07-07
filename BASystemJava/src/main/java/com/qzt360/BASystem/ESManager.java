package com.qzt360.BASystem;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.log4j.Logger;
import org.elasticsearch.action.admin.indices.template.put.PutIndexTemplateRequest;
import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.get.MultiGetItemResponse;
import org.elasticsearch.action.get.MultiGetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.IndicesAdminClient;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.collect.MapBuilder;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.unit.ByteSizeUnit;
import org.elasticsearch.common.unit.ByteSizeValue;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.WildcardQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;

public class ESManager {
	private String strClusterName;
	private String strTransportHostName;
	private int nConcurrentRequests = 8;// 貌似是单机cpu核心数时速度最佳
	private Logger logger = Logger.getLogger(ESManager.class);

	public Client client = null;
	public BulkProcessor bulkProcessor = null;

	public ESManager() {
		super();
	}

	public ESManager(String strClusterName, String strTransportHostName) {
		super();
		this.strClusterName = strClusterName;
		this.strTransportHostName = strTransportHostName;
	}

	public ESManager(String strClusterName, String strTransportHostName, int nConcurrentRequests) {
		super();
		this.strClusterName = strClusterName;
		this.strTransportHostName = strTransportHostName;
		this.nConcurrentRequests = nConcurrentRequests;
	}

	private void admin(IndicesAdminClient iac) {
		// 定义IM数据模版
		PutIndexTemplateRequest pitr = new PutIndexTemplateRequest("im").template("im*");
		// setting
		// pitr.settings(new MapBuilder<String,
		// Object>().put("number_of_shards", 6).put("number_of_replicas", 1)
		// .put("refresh_interval", "1s").put("merge.policy.floor_segment",
		// "2mb").map());

		Map<String, Object> defaultMapping = new HashMap<String, Object>();
		// 关闭_all
		defaultMapping.put("_all", new MapBuilder<String, Object>().put("enabled", false).map());
		defaultMapping.put("numeric_detection", false);
		defaultMapping.put("dynamic_templates", new Object[] {
				new MapBuilder<String, Object>()
						.put("date_tpl", new MapBuilder<String, Object>().put("match", "date*")
								.put("mapping",
										new MapBuilder<String, Object>().put("type", "date")
												.put("index", "not_analyzed").put("doc_values", true).map())
								.map())
						.map(),
				new MapBuilder<String, Object>()
						.put("all_tpl", new MapBuilder<String, Object>().put("match", "*")
								.put("mapping",
										new MapBuilder<String, Object>().put("type", "{dynamic_type}")
												.put("index", "not_analyzed").put("doc_values", true).map())
								.map())
						.map() });
		pitr.mapping("_default_", defaultMapping);
		iac.putTemplate(pitr);

		// 定义mac数据模版
		pitr = new PutIndexTemplateRequest("mac").template("mac*");
		// setting
		// pitr.settings(new MapBuilder<String,
		// Object>().put("number_of_shards", 6).put("number_of_replicas", 1)
		// .put("refresh_interval", "1s").put("merge.policy.floor_segment",
		// "2mb").map());

		defaultMapping = new HashMap<String, Object>();
		// 关闭_all
		defaultMapping.put("_all", new MapBuilder<String, Object>().put("enabled", false).map());
		defaultMapping.put("numeric_detection", false);
		defaultMapping.put("dynamic_templates", new Object[] {
				new MapBuilder<String, Object>()
						.put("date_tpl", new MapBuilder<String, Object>().put("match", "date*")
								.put("mapping",
										new MapBuilder<String, Object>().put("type", "date")
												.put("index", "not_analyzed").put("doc_values", true).map())
								.map())
						.map(),
				new MapBuilder<String, Object>()
						.put("all_tpl", new MapBuilder<String, Object>().put("match", "*")
								.put("mapping",
										new MapBuilder<String, Object>().put("type", "{dynamic_type}")
												.put("index", "not_analyzed").put("doc_values", true).map())
								.map())
						.map() });
		pitr.mapping("_default_", defaultMapping);
		iac.putTemplate(pitr);
	}

	private static Lock initLock = new ReentrantLock();

	public void setup() {
		logger.debug("init settings");
		if (client == null) {
			initLock.lock();
			Settings settings;
			if (strClusterName == null) {
				settings = Settings.settingsBuilder().put("cluster.name", "qzt360esmacbookpro")
						.put("client.transport.sniff", true).build();
			} else {
				settings = Settings.settingsBuilder().put("cluster.name", strClusterName)
						.put("client.transport.sniff", true).build();
			}
			try {
				logger.debug("init client");
				if (strTransportHostName == null) {
					client = TransportClient.builder().settings(settings).build().addTransportAddress(
							new InetSocketTransportAddress(InetAddress.getByName("localhost"), 9300));
				} else {
					client = TransportClient.builder().settings(settings).build().addTransportAddress(
							new InetSocketTransportAddress(InetAddress.getByName(strTransportHostName), 9300));
				}
				IndicesAdminClient iac = client.admin().indices();
				admin(iac);
			} catch (UnknownHostException e) {
				logger.error(e);
			} finally {
				initLock.unlock();
			}
		}
		if (bulkProcessor == null) {
			bulkProcessor = BulkProcessor.builder(client, new BulkProcessor.Listener() {
				@Override
				public void beforeBulk(long executionId, BulkRequest request) {
				}

				@Override
				public void afterBulk(long executionId, BulkRequest request, BulkResponse response) {
				}

				@Override
				public void afterBulk(long executionId, BulkRequest request, Throwable failure) {
				}
			}).setBulkActions(5000).setBulkSize(new ByteSizeValue(10, ByteSizeUnit.MB))
					.setFlushInterval(TimeValue.timeValueSeconds(5)).setConcurrentRequests(nConcurrentRequests).build();
		}
	}

	// 删除某个索引
	public void deleteIndex(String strIndex) {
		client.admin().indices().prepareDelete(strIndex).get();
	}

	public void createIndex(String strIndex) {
		client.admin().indices().prepareCreate(strIndex).get();
	}

	public void cleanup() {
		logger.debug("bulkProcessor close");
		if (bulkProcessor != null) {
			try {
				bulkProcessor.awaitClose(10, TimeUnit.SECONDS);
			} catch (InterruptedException e) {
				logger.error(e);
			}
		}
		logger.debug("client close");
		if (client != null) {
			client.close();
		}
	}

	public boolean update(Map<String, Object> json, String index, String type, String id) {

		UpdateResponse resp = client.update(new UpdateRequest(index, type, id).doc(json).upsert(json)).actionGet();
		if (resp.isCreated())
			return true;
		return false;
	}

	public void index(Map<String, Object> json, String strIndex, String strType) {
		// logger.debug("json init");
		// Map<String, Object> json = new HashMap<String, Object>();
		// json.put("user", "kimchy");
		// json.put("postDate", new Date());
		// json.put("message", "trying out Elasticsearch");
		logger.debug("response init");
		// IndexResponse response =
		client.prepareIndex(strIndex, strType).setSource(json).get();

		// logger.debug("index:" + response.getIndex());
		// logger.debug("type:" + response.getType());
		// logger.debug("id:" + response.getId());
		// logger.debug("version:" + response.getVersion());
		// logger.debug("isCreated:" + response.isCreated());
	}

	public void index(Map<String, Object> json, String strIndex, String strType, String strId) {
		// logger.debug("json init");
		// Map<String, Object> json = new HashMap<String, Object>();
		// json.put("user", "kimchy");
		// json.put("postDate", new Date());
		// json.put("message", "trying out Elasticsearch");
		logger.debug("response init");
		// IndexResponse response =
		client.prepareIndex(strIndex, strType, strId).setSource(json).get();
		// logger.debug("index:" + response.getIndex());
		// logger.debug("type:" + response.getType());
		// logger.debug("id:" + response.getId());
		// logger.debug("version:" + response.getVersion());
		// logger.debug("isCreated:" + response.isCreated());
	}

	public void get() {
		logger.debug("GetResponse init");
		GetResponse response = client.prepareGet("twitter", "tweet", "1").get();
		logger.debug("index:" + response.getIndex());
		logger.debug("type:" + response.getType());
		logger.debug("id:" + response.getId());
		logger.debug("version:" + response.getVersion());
	}

	public void delete() {
		logger.debug("DeleteResponse init");
		DeleteResponse response = client.prepareDelete("twitter", "tweet", "1").get();
		logger.debug("index:" + response.getIndex());
		logger.debug("type:" + response.getType());
		logger.debug("id:" + response.getId());
		logger.debug("version:" + response.getVersion());
	}

	public void update() {

	}

	public void search() {
		// SearchResponse response = client.prepareSearch("twitter",
		// "tweet").setTypes("type1", "type2")
		// .setSearchType(SearchType.DFS_QUERY_THEN_FETCH).setQuery(QueryBuilders.termQuery("multi",
		// "test")) // Query
		// .setPostFilter(QueryBuilders.rangeQuery("age").from(12).to(18)) //
		// Filter
		// .setFrom(0).setSize(60).setExplain(true).execute().actionGet();
		// MatchAll on the whole cluster with all default options
		// SearchResponse responseAll =
		// client.prepareSearch().execute().actionGet();
		// logger.debug("contextSize:" + responseAll.contextSize());
		// logger.debug("toString:" + responseAll.toString());
		// logger.debug("getContext().size:" + responseAll.getContext().size());
		QueryBuilder qb1 = QueryBuilders.termQuery("strCallerId", "522738778");
		SearchResponse response = client.prepareSearch("im_20160303", "im_20160304").setTypes("im")
				.setSearchType(SearchType.DFS_QUERY_THEN_FETCH).setQuery(qb1).setFrom(0).setSize(60).setExplain(true)
				.execute().actionGet();
		SearchHits hits = response.getHits();
		for (int i = 0; i < 60; i++) {
			logger.info("" + hits.getAt(i).getScore());
		}
	}

	public void search(String strNetid) {
		setup();
		WildcardQueryBuilder wqb = new WildcardQueryBuilder("strCallerId", "*" + strNetid + "*");
		QueryBuilder qb = QueryBuilders.boolQuery().should(wqb);
		SearchResponse response = client.prepareSearch("im_20160303").setTypes("im")
				.setSearchType(SearchType.DFS_QUERY_THEN_FETCH).setQuery(qb).setFrom(0).setSize(100).setExplain(true)
				.execute().actionGet();

		for (SearchHit hit : response.getHits()) {
			Map<String, Object> source = hit.getSource();
			// logger.info("user: " + source.get("user"));
			logger.info("source: " + source.toString());
		}
		cleanup();
	}

	public void multiGet() {
		MultiGetResponse multiGetItemResponses = client.prepareMultiGet().add("twitter", "tweet", "1")
				.add("twitter", "tweet", "2", "3", "4").add("another", "type", "foo").get();

		for (MultiGetItemResponse itemResponse : multiGetItemResponses) {
			GetResponse response = itemResponse.getResponse();
			if (response.isExists()) {
				String json = response.getSourceAsString();
				logger.debug(json);
			}
		}
	}

	public void bulk(List<Map<String, Object>> listJson, String strIndex, String strType) {
		logger.debug("BulkRequestBuilder init");
		BulkRequestBuilder bulkRequest = client.prepareBulk();
		for (Map<String, Object> json : listJson) {
			bulkRequest.add(client.prepareIndex(strIndex, strType).setSource(json));
		}
		BulkResponse bulkResponse = bulkRequest.get();
		if (bulkResponse.hasFailures()) {
			logger.debug("bulkResponse.hasFailures():true");
		} else {
			logger.debug("bulkResponse.hasFailures():false");
		}
	}

	// private static void initBulkProcessor() {
	// if (bulkProcessor == null) {
	// bulkProcessor = BulkProcessor.builder(client, new
	// BulkProcessor.Listener() {
	// @Override
	// public void beforeBulk(long executionId, BulkRequest request) {
	// }
	//
	// @Override
	// public void afterBulk(long executionId, BulkRequest request, BulkResponse
	// response) {
	// }
	//
	// @Override
	// public void afterBulk(long executionId, BulkRequest request, Throwable
	// failure) {
	// }
	// }).setBulkActions(5000).setBulkSize(new ByteSizeValue(10,
	// ByteSizeUnit.MB))
	// .setFlushInterval(TimeValue.timeValueSeconds(5)).setConcurrentRequests(1).build();
	// }
	// }

	public void bulkProcessor(List<Map<String, Object>> listJson, String strIndex, String strType) {
		for (Map<String, Object> json : listJson) {
			bulkProcessor.add(new IndexRequest(strIndex, strType).source(json));
		}
	}

	public void bulkProcessor(Map<String, Object> json, String strIndex, String strType) {
		bulkProcessor.add(new IndexRequest(strIndex, strType).source(json));
	}

	public void bulkProcessor(List<Map<String, Object>> listJson, String strIndex, String strType, String strIdKey) {
		for (Map<String, Object> json : listJson) {
			bulkProcessor.add(new IndexRequest(strIndex, strType, (String) json.get(strIdKey)).source(json));
		}
	}

	public static void count() {

	}

	public static void query() {

	}
}
