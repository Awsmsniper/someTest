package com.qzt360.esTest;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.elasticsearch.action.index.IndexRequest;

public class WifiLog2ES {
	private static Logger logger = Logger.getLogger(WifiLog2ES.class);

	public static void main(String[] args) {
		logger.debug("WifiLog2ES begin");

		ESManager esm = new ESManager("wj-es", "192.168.36.31", Integer.parseInt(args[1]));
		esm.setup();
		File filePath = new File(args[0]);
		File[] aFile = filePath.listFiles();

		long lBegin = System.currentTimeMillis();
		BufferedReader br = null;
		String strLine = null;
		int nRow = 0;
		WifiLogManager wm = new WifiLogManager();
		try {
			for (File file : aFile) {
				br = new BufferedReader(new InputStreamReader(new FileInputStream(file.getPath())));
				while ((strLine = br.readLine()) != null) {
					nRow++;
					if (wm.isLegal(strLine)) {
						Map<String, Object> json = new HashMap<String, Object>();
						json.put("strTMac", wm.getStrTMac());
						json.put("strTBrand", wm.getStrTBrand());
						json.put("strTMac", wm.getStrTSsidList());
						json.put("dateCollectTime", new Date((long) wm.getnCollectTime() * 1000L));
						json.put("strTFieldIntensity", wm.getStrTFieldIntensity());
						json.put("nIdType", wm.getnIdType());
						json.put("strIdCode", wm.getStrIdCode());
						json.put("strApSsid", wm.getStrApSsid());
						json.put("strApMac", wm.getStrApMac());
						json.put("strApChannel", wm.getStrApChannel());
						json.put("strApEncType", wm.getStrApEncType());
						json.put("strApX", wm.getStrApX());
						json.put("strApY", wm.getStrApY());
						json.put("strPlaceCode", wm.getStrPlaceCode());
						json.put("strDeviceCode", wm.getStrDeviceCode());
						json.put("strDeviceLongitude", wm.getStrDeviceLongitude());
						json.put("strDeviceLatitude", wm.getStrDeviceLatitude());
						esm.bulkProcessor
								.add(new IndexRequest("mac", file.getName(), file.getName() + "_" + nRow).source(json));
					}
				}
			}
		} catch (Exception e) {
			logger.error(e);
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					logger.error(e);
				}
			}
		}
		long lEnd = System.currentTimeMillis();
		esm.cleanup();
		logger.info("time: " + (lEnd - lBegin));
		logger.debug("WifiLog2ES end");
	}

}
