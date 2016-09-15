package com.qzt360.esTest;

import org.apache.log4j.Logger;

public class TestMain {
	private static Logger logger = Logger.getLogger(TestMain.class);

	public static void main(String[] args) {
		logger.info("TestMain begin");
		for (String arg : args) {
			logger.info("arg:" + arg);
		}
		logger.info("TestMain end");
	}

	public String doSomeThing(String strInput) {
		logger.info("strInput: " + strInput);
		return strInput;
	}
}
