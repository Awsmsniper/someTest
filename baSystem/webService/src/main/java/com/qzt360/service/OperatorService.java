package com.qzt360.service;

import com.qzt360.model.OperatorInfo;
import com.qzt360.pub.PubPara;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class OperatorService {
	@Value("${login.strTimeOut}")
	private String strTimeOut;// 登录超时时间(秒)

	public OperatorInfo login(String strUserName, String strPassword) {
		log.info("login strUserName:{}, strPasswd:{} ", strUserName, strPassword);
		OperatorInfo operator = new OperatorInfo();
		if ("zhaogj".equals(strUserName) && "062e058bab4712217e0afdc987457430".equals(strPassword)) {
			operator.setStrEmail(strUserName + "@qzt360.com");
			operator.setStrResult("success");
			operator.setStrUserName(strUserName);
		} else if ("liangyf".equals(strUserName) && "062e058bab4712217e0afdc987457430".equals(strPassword)) {
			operator.setStrEmail(strUserName + "@qzt360.com");
			operator.setStrResult("success");
			operator.setStrUserName(strUserName);
		} else if ("jiangjb".equals(strUserName) && "062e058bab4712217e0afdc987457430".equals(strPassword)) {
			operator.setStrEmail(strUserName + "@qzt360.com");
			operator.setStrResult("success");
			operator.setStrUserName(strUserName);
		} else if ("zhuyb".equals(strUserName) && "062e058bab4712217e0afdc987457430".equals(strPassword)) {
			operator.setStrEmail(strUserName + "@qzt360.com");
			operator.setStrResult("success");
			operator.setStrUserName(strUserName);
		} else if ("liuzz".equals(strUserName) && "062e058bab4712217e0afdc987457430".equals(strPassword)) {
			operator.setStrEmail(strUserName + "@qzt360.com");
			operator.setStrResult("success");
			operator.setStrUserName(strUserName);
		} else if ("hem".equals(strUserName) && "062e058bab4712217e0afdc987457430".equals(strPassword)) {
			operator.setStrEmail(strUserName + "@qzt360.com");
			operator.setStrResult("success");
			operator.setStrUserName(strUserName);
		} else if ("liangy".equals(strUserName) && "062e058bab4712217e0afdc987457430".equals(strPassword)) {
			operator.setStrEmail(strUserName + "@qzt360.com");
			operator.setStrResult("success");
			operator.setStrUserName(strUserName);
		} else if ("liangzm".equals(strUserName) && "062e058bab4712217e0afdc987457430".equals(strPassword)) {
			operator.setStrEmail(strUserName + "@qzt360.com");
			operator.setStrResult("success");
			operator.setStrUserName(strUserName);
		} else if ("rongzx".equals(strUserName) && "062e058bab4712217e0afdc987457430".equals(strPassword)) {
			operator.setStrEmail(strUserName + "@qzt360.com");
			operator.setStrResult("success");
			operator.setStrUserName(strUserName);
		} else if ("chensy".equals(strUserName) && "062e058bab4712217e0afdc987457430".equals(strPassword)) {
			operator.setStrEmail(strUserName + "@qzt360.com");
			operator.setStrResult("success");
			operator.setStrUserName(strUserName);
		} else if ("yumj".equals(strUserName) && "062e058bab4712217e0afdc987457430".equals(strPassword)) {
			operator.setStrEmail(strUserName + "@qzt360.com");
			operator.setStrResult("success");
			operator.setStrUserName(strUserName);
		} else {
			operator.setStrResult("error");
		}
		if (!operator.getStrResult().equals("error")) {
			String strSN = strUserName + "_" + System.currentTimeMillis();
			operator.setStrSN(strSN);
			PubPara.hashOperatorLogin.put(strSN, System.currentTimeMillis());
		}
		return operator;
	}

	public void logout(String strSN) {
		log.info("logout strSN:{}", strSN);
		PubPara.hashOperatorLogin.remove(strSN);
	}

	public void cleanLoginSession() {
		List<String> listTmp = new ArrayList<String>();
		long lNow = System.currentTimeMillis();
		for (Map.Entry<String, Long> entry : PubPara.hashOperatorLogin.entrySet()) {
			if ((lNow - entry.getValue()) > ((Integer.parseInt(strTimeOut) * 10) * 1000)) {
				listTmp.add(entry.getKey());
			}
		}
		for (String strKey : listTmp) {
			log.info("清理PubPara.hashOperatorLogin, userId:{}", strKey);
			PubPara.hashOperatorLogin.remove(strKey);
		}
	}
}
