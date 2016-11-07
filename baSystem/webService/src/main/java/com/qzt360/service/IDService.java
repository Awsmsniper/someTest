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
    private EmailService email;

    // 类型转换成名称
    public String getIdTypeName(String strCode) {
        for (IdTypeEnum idType : IdTypeEnum.values()) {
            if (idType.getStrCode().equals(strCode)) {
                return idType.getStrName();
            }
        }
        email.sendEmail2Manager("身份类型解析失败", "类型编号:" + strCode);
        return "-";
    }
}
