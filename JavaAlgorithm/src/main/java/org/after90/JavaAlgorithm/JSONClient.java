package org.after90.JavaAlgorithm;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JSONClient {
	public void submitIds() {
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpPost httpPost = new HttpPost("http://gdba.qzt360.com:36162/submitIds");
		List<NameValuePair> formparams = new ArrayList<NameValuePair>();
		formparams.add(new BasicNameValuePair("strSN", "helpmehelpyou"));
		formparams.add(new BasicNameValuePair("strIds", "22655080,QQ;15010281799,Tel"));
		try {
			UrlEncodedFormEntity uefEntity = new UrlEncodedFormEntity(formparams, "UTF-8");
			httpPost.setEntity(uefEntity);
			CloseableHttpResponse response = httpclient.execute(httpPost);
			try {
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					log.info("Response content: " + EntityUtils.toString(entity, "UTF-8"));
				}
			} catch (Exception e) {
				log.error("", e);
			} finally {
				response.close();
			}
		} catch (Exception e) {
			log.error("", e);
		} finally {
			try {
				if (httpclient != null) {
					httpclient.close();
				}
			} catch (Exception e) {
				log.error("", e);
			}
		}
	}

	public void getIdsDirect() {
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpPost httpPost = new HttpPost("http://gdba.qzt360.com:36162/getIdsDirect");
		List<NameValuePair> formparams = new ArrayList<NameValuePair>();
		formparams.add(new BasicNameValuePair("strSN", "helpmehelpyou"));
		formparams.add(new BasicNameValuePair("strId", "22655080,QQ"));
		try {
			UrlEncodedFormEntity uefEntity = new UrlEncodedFormEntity(formparams, "UTF-8");
			httpPost.setEntity(uefEntity);
			CloseableHttpResponse response = httpclient.execute(httpPost);
			try {
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					log.info("Response content: " + EntityUtils.toString(entity, "UTF-8"));
				}
			} catch (Exception e) {
				log.error("", e);
			} finally {
				response.close();
			}
		} catch (Exception e) {
			log.error("", e);
		} finally {
			try {
				if (httpclient != null) {
					httpclient.close();
				}
			} catch (Exception e) {
				log.error("", e);
			}
		}
	}

}
