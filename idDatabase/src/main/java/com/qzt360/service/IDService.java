package com.qzt360.service;

import com.google.common.base.Splitter;
import com.qzt360.repository.ESRepository;
import com.qzt360.utils.FuncUtil;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsRequest;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.*;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

/**
 * Created by zhaogj on 04/11/2016.
 */
@Slf4j
@Service
public class IDService {
    @Autowired
    private ESRepository es;

    @Autowired
    private EmailService email;

    @Value("${es.index.strIdIndex}")
    private String strIdIndex;

    @Value("${es.index.strIdType}")
    private String strIdType;

    @Value("${system.strIdDatabaseBackPath}")
    private String strIdDatabaseBackPath;

    private boolean hasIdIndex = false;

    private void checkEqpIdCount(List<String> listEqpIds) {
        while (listEqpIds.size() > 50) {
            listEqpIds.remove(0);
        }
    }

    private void checkIdCount(List<String> listIds) {
        while (listIds.size() > 100) {
            listIds.remove(0);
        }
    }

    private void checkIdIndex() {
        if (!hasIdIndex) {
            try {
                IndicesExistsRequest request = new IndicesExistsRequest(strIdIndex);
                IndicesExistsResponse response = es.client.admin().indices().exists(request).actionGet();
                if (!response.isExists()) {
                    es.client.admin().indices().prepareCreate(strIdIndex).get();
                    hasIdIndex = true;
                }
            } catch (Exception e) {
                log.error("", e);
            }
        }
    }

    // 类型转换成名称
    private String getIdTypeName(String strCode) {
        for (IdTypeEnum idType : IdTypeEnum.values()) {
            if (idType.getStrCode().equals(strCode)) {
                return idType.getStrName();
            }
        }
        email.sendEmail2Manager("身份类型解析失败", "类型编号:" + strCode);
        return "-";
    }

    /**
     * @param strIds 分号分割的身份组
     * @return
     */
    public String submitIds(String strEqpId, String strIds) {
        checkIdIndex();

        Splitter splitter = Splitter.on(";").trimResults().omitEmptyStrings();
        Iterable<String> itInput = splitter.split(strIds);
        List<String> listIds = new ArrayList<String>();
        for (String str : itInput) {
            if (!listIds.contains(str)) {
                String[] astr = str.split(",");
                if (astr.length == 2) {
                    String strIdTypeName = getIdTypeName(astr[1]);
                    if (!"-".equals(strIdTypeName)) {
                        StringBuffer sb = new StringBuffer();
                        sb.append(astr[0]);
                        sb.append(",");
                        sb.append(strIdTypeName);
                        listIds.add(sb.toString());
                    }
                }
            }
        }
        try {
            if (listIds.size() >= 1) {
                for (String strId : listIds) {
                    BoolQueryBuilder bqb = QueryBuilders.boolQuery();
                    bqb.must(QueryBuilders.termsQuery("_id", strId));
                    SearchResponse response = es.client.prepareSearch(strIdIndex).setTypes(strIdType)
                            .setSearchType(SearchType.DFS_QUERY_THEN_FETCH).setQuery(bqb).setFrom(0).setSize(10)
                            .setExplain(true).execute().actionGet();
                    if (response.getHits().getTotalHits() > 0) {
                        // 库里有的情况，拿出来更新
                        for (SearchHit hit : response.getHits()) {
                            Map<String, Object> map = hit.getSource();
                            // 更新listEqpIds
                            try {
                                @SuppressWarnings("unchecked")
                                List<String> listEqpIdsES = (List<String>) map.get("listEqpIds");
                                if (listEqpIdsES == null) {
                                    listEqpIdsES = new ArrayList<String>();
                                }
                                if (!listEqpIdsES.contains(strEqpId)) {
                                    listEqpIdsES.add(strEqpId);
                                }
                                checkEqpIdCount(listEqpIdsES);
                                UpdateRequest updateRequest = new UpdateRequest(strIdIndex,
                                        strIdType, strId)
                                        .doc(jsonBuilder().startObject().field("listEqpIds", listEqpIdsES)
                                                .field("dtUpdateEqpIds", new Date(System.currentTimeMillis()))
                                                .endObject());
                                es.client.update(updateRequest).get();
                            } catch (Exception e) {
                                log.error("", e);
                            }
                            // 更新listIdsDirect
                            if (listIds.size() >= 2) {
                                try {
                                    @SuppressWarnings("unchecked")
                                    List<String> listIdsES = (List<String>) map.get("listIdsDirect");
                                    if (listIdsES == null) {
                                        listIdsES = new ArrayList<String>();
                                    }
                                    for (String strTmp : listIds) {
                                        // 排除自己
                                        if (!strTmp.equals(strId)) {
                                            if (!listIdsES.contains(strTmp)) {
                                                listIdsES.add(strTmp);
                                            }
                                        }
                                    }
                                    checkIdCount(listIdsES);
                                    UpdateRequest updateRequest = new UpdateRequest(strIdIndex,
                                            strIdType, strId).doc(jsonBuilder().startObject()
                                            .field("listIdsDirect", listIdsES)
                                            .field("dtUpdateIdsDirect", new Date(System.currentTimeMillis()))
                                            .endObject());
                                    es.client.update(updateRequest).get();
                                } catch (Exception e) {
                                    log.error("", e);
                                }
                            }
                            break;
                        }
                    } else {
                        Map<String, Object> json = new HashMap<String, Object>();
                        List<String> listEqpIds = new ArrayList<String>();
                        listEqpIds.add(strEqpId);
                        json.put("listEqpIds", listEqpIds);
                        if (listIds.size() >= 2) {
                            List<String> listTmp = new ArrayList<String>();
                            listTmp.addAll(listIds);
                            listTmp.remove(strId);
                            json.put("dtUpdateIdsDirect", new Date(System.currentTimeMillis()));
                            json.put("listIdsDirect", listTmp);
                        }
                        json.put("dtCreate", new Date(System.currentTimeMillis()));
                        json.put("dtUpdateEqpIds", new Date(System.currentTimeMillis()));
                        json.put("strId", strId);
                        es.bulkProcessor
                                .add(new IndexRequest(strIdIndex, strIdType, strId).source(json));
                    }
                }
            }
        } catch (Exception e) {
            log.error("", e);
            return "入库失败";
        }
        return "success";
    }

    /**
     * @param strIds 分号分割的身份组
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<String> getIdsDirect(String strId) {
        try {
            BoolQueryBuilder bqb = QueryBuilders.boolQuery();
            bqb.must(QueryBuilders.termsQuery("_id", strId));
            SearchResponse response = es.client.prepareSearch(strIdIndex).setTypes(strIdType)
                    .setSearchType(SearchType.DFS_QUERY_THEN_FETCH).setQuery(bqb).setFrom(0).setSize(10)
                    .setExplain(true).execute().actionGet();
            if (response.getHits().getTotalHits() > 0) {
                for (SearchHit hit : response.getHits()) {
                    Map<String, Object> map = hit.getSource();
                    return (List<String>) map.get("listIdsDirect");
                }
            }
        } catch (Exception e) {
            log.error("", e);
        }
        return new ArrayList<String>();
    }

    /**
     * @param strIds 分号分割的身份组
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<String> getIdsSmart(String strId) {
        try {
            BoolQueryBuilder bqb = QueryBuilders.boolQuery();
            bqb.must(QueryBuilders.termsQuery("_id", strId));
            SearchResponse response = es.client.prepareSearch(strIdIndex).setTypes(strIdType)
                    .setSearchType(SearchType.DFS_QUERY_THEN_FETCH).setQuery(bqb).setFrom(0).setSize(10)
                    .setExplain(true).execute().actionGet();
            if (response.getHits().getTotalHits() > 0) {
                for (SearchHit hit : response.getHits()) {
                    Map<String, Object> map = hit.getSource();
                    return (List<String>) map.get("listIdsSmart");
                }
            }
        } catch (Exception e) {
            log.error("", e);
        }
        return new ArrayList<String>();
    }

    /**
     * @param strId
     * @return
     */
    public List<String> getIds(String strId) {
        try {
            BoolQueryBuilder bqb = QueryBuilders.boolQuery();
            bqb.must(QueryBuilders.wildcardQuery("strId", "*" + strId + "*"));
            SearchResponse response = es.client.prepareSearch(strIdIndex).setTypes(strIdType)
                    .setSearchType(SearchType.DFS_QUERY_THEN_FETCH).setQuery(bqb).setFrom(0).setSize(50)
                    .setExplain(true).execute().actionGet();
            if (response.getHits().getTotalHits() > 0) {
                List<String> listIds = new ArrayList<String>();
                for (SearchHit hit : response.getHits()) {
                    listIds.add(hit.getId());
                }
                return listIds;
            }
        } catch (Exception e) {
            log.error("", e);
        }
        return new ArrayList<String>();
    }

    /**
     * 身份库恢复
     */
    @SuppressWarnings("unused")
    private void restoreIdDatabase(String strFile) {
        try {
            BufferedReader br = null;
            String strLine = null;
            try {
                br = new BufferedReader(new InputStreamReader(
                        new FileInputStream(strFile)));
                while ((strLine = br.readLine()) != null) {
                    // {listEqpIds=[20-8B-37-AF-AC-1B, 20-8B-37-AF-AC-1C],
                    // listIdsDirect=[00-0E-C6-C9-0C-95,mac,
                    // 18-DC-56-F1-76-4E,mac],
                    // dtUpdateIdsDirect=2016-09-19T08:07:18.166Z,
                    // _id=772076055,qq, dtCreate=2016-09-18T03:00:15.270Z,
                    // dtUpdateEqpIds=2016-09-19T08:07:18.073Z}
                    Map<String, Object> json = new HashMap<String, Object>();
                    // 去掉前后的大括号
                    strLine = strLine.substring(1, strLine.length() - 1);
                    String[] astrLine = strLine.split("=");
                    String strKey = null;
                    for (int i = 0; i < astrLine.length; i++) {
                        if (i == 0) {
                            // 第0号肯定是个key
                            strKey = astrLine[i];
                        } else {
                            if (strKey.startsWith("list")) {
                                // key是list开头，那么此字符为[xxxx,xx, xxxx,xx], xxx的形式
                                // 去掉开头的[
                                String[] astrTmp = astrLine[i].substring(1,
                                        astrLine[i].length()).split("], ");
                                List<String> listTmp = new ArrayList<String>();
                                for (String strTmp : astrTmp[0].split(", ")) {
                                    listTmp.add(strTmp);
                                }
                                json.put(strKey, listTmp);
                                strKey = astrTmp[1];
                            } else {
                                String[] astrTmp = astrLine[i].split(", ");
                                json.put(strKey, astrTmp[0]);
                                if (astrTmp.length == 2) {
                                    strKey = astrTmp[1];
                                }
                            }
                        }
                    }
                    String strId = (String) json.get("_id");
                    json.remove("_id");
                    if (json.size() > 0) {
                        es.bulkProcessor.add(new IndexRequest(
                                strIdIndex, strIdType, strId)
                                .source(json));
                    }
                }
            } catch (Exception e) {
                log.error("", e);
            } finally {
                if (br != null) {
                    try {
                        br.close();
                    } catch (IOException e) {
                        log.error("", e);
                    }
                }
            }
        } catch (Exception e) {
            log.error("", e);
        }
    }

    /**
     * 备份id_database成文件
     */
    public void backupIdDatabase() {
        //清理历史数据
        FuncUtil.cleanFile(strIdDatabaseBackPath, 60);

        String strFile = strIdDatabaseBackPath + "id_database_" + System.currentTimeMillis();
        BufferedWriter out = null;
        try {
            @SuppressWarnings("deprecation")
            SearchResponse response = es.client
                    .prepareSearch(strIdIndex)
                    .setTypes(strIdType)
                    .setQuery(QueryBuilders.matchAllQuery()).setSize(10000)
                    .setScroll(new TimeValue(600000))
                    .setSearchType(SearchType.SCAN).execute().actionGet();
            // setSearchType(SearchType.Scan) 告诉ES不需要排序只要结果返回即可
            // setScroll(new TimeValue(600000)) 设置滚动的时间
            String strScrollId = response.getScrollId();
            out = new BufferedWriter(new FileWriter(strFile, true));
            // 每次返回数据10000条。一直循环查询直到所有的数据都查询出来
            while (true) {
                SearchResponse searchResponse = es.client
                        .prepareSearchScroll(strScrollId)
                        .setScroll(new TimeValue(1000000)).execute()
                        .actionGet();
                SearchHits searchHit = searchResponse.getHits();
                // 再次查询不到数据时跳出循环
                if (searchHit.getHits().length == 0) {
                    break;
                }
                for (int i = 0; i < searchHit.getHits().length; i++) {
                    Map<String, Object> source = searchHit.getHits()[i]
                            .getSource();// .toString();//getSourceAsString();
                    source.put("_id", searchHit.getHits()[i].getId());
                    out.write(source.toString());
                    out.write("\r\n");
                }
            }
        } catch (Exception e) {
            log.error("", e);
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    log.error("", e);
                }
            }
        }
    }
}
