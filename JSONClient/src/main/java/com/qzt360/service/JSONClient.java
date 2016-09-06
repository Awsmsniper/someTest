package com.qzt360.service;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import lombok.extern.java.Log;

@Log
public class JSONClient {
	public void getSS() {
		HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
		CloseableHttpClient closeableHttpClient = httpClientBuilder.build();
		RequestConfig config = RequestConfig.custom().build();
		// HttpPost httpPost = new HttpPost("http://localhost:8080/ss");
		HttpPost httpPost = new HttpPost("http://localhost:8080/greeting");
		httpPost.setConfig(config);
		StringEntity entity = new StringEntity(
				"{\"chname\":null,\"operator\":null,\"status\":null,\"page\":1,\"count\":1000}", "utf-8");
		entity.setContentType("application/json");
		httpPost.setEntity(entity);
		String str = "";
		try {
			CloseableHttpResponse response = closeableHttpClient.execute(httpPost);
			HttpEntity httpEntity = response.getEntity();
			if (httpEntity != null) {
				str = EntityUtils.toString(httpEntity, "UTF-8");
			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		log.info(str);
	}

	public void getNetLogList() {
		HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
		CloseableHttpClient closeableHttpClient = httpClientBuilder.build();
		RequestConfig config = RequestConfig.custom().build();
		// HttpPost httpPost = new HttpPost("http://localhost:8080/ss");
		HttpPost httpPost = new HttpPost("http://gdba.qzt360.com:36161/netLogList");
		httpPost.setConfig(config);
		StringEntity entity = new StringEntity(
				"{\"chname\":null,\"operator\":null,\"status\":null,\"page\":1,\"count\":1000}", "utf-8");
		entity.setContentType("application/json");
		httpPost.setEntity(entity);
		String str = "";
		try {
			CloseableHttpResponse response = closeableHttpClient.execute(httpPost);
			HttpEntity httpEntity = response.getEntity();
			if (httpEntity != null) {
				str = EntityUtils.toString(httpEntity, "UTF-8");
			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		log.info(str);
	}
}
