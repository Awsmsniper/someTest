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

public class ImLog2ES {
	private static Logger logger = Logger.getLogger(ImLog2ES.class);

	public static void main(String[] args) {
		logger.debug("ImLog2ES begin");

		ESManager esm = new ESManager("wj-es", "192.168.36.31", Integer.parseInt(args[1]));
		esm.setup();
		File filePath = new File(args[0]);
		File[] aFile = filePath.listFiles();

		long lBegin = System.currentTimeMillis();
		BufferedReader br = null;
		String strLine = null;
		int nRow = 0;
		IMManager im = new IMManager();
		try {
			for (File file : aFile) {
				br = new BufferedReader(new InputStreamReader(new FileInputStream(file.getPath())));
				while ((strLine = br.readLine()) != null) {
					nRow++;
					if (im.isLegal(strLine)) {
						Map<String, Object> json = new HashMap<String, Object>();
						json.put("nCustomerId", im.getnCustomerId());
						json.put("nDeptId", im.getnDeptId());
						json.put("strHostName", im.getStrHostName());
						json.put("strEqpName", im.getStrEqpName());
						json.put("dateTime", new Date((long) im.getnTime() * 1000L));
						json.put("lSip", im.getlSip());
						json.put("strSmac", im.getStrSmac());
						json.put("nApplication", im.getnApplication());
						json.put("nType", im.getnType());
						json.put("lSessionId", im.getlSessionId());
						json.put("strCallerId", im.getStrCallerId());
						json.put("strCalledId", im.getStrCalledId());
						json.put("nDir", im.getnDir());
						json.put("nContentLen", im.getnContentLen());
						json.put("strContent", im.getStrContent());
						json.put("nPartyNum", im.getnPartyNum());
						json.put("strCallerNickname", im.getStrCallerNickname());
						json.put("strCalledNickname", im.getStrCalledNickname());
						json.put("nCertType", im.getnCertType());
						json.put("strCertCode", im.getStrCertCode());
						json.put("strName", im.getStrName());
						json.put("strCountryCode", im.getStrCountryCode());
						esm.bulkProcessor
								.add(new IndexRequest("im", file.getName(), file.getName() + "_" + nRow).source(json));
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
		logger.debug("ImLog2ES end");
	}

}
