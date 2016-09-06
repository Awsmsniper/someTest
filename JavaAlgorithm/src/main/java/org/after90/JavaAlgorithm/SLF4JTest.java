package org.after90.JavaAlgorithm;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SLF4JTest {
	/**
	 * 这是一个log的标准写法
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String strName = "zhaogj";
		log.info("I love you {}", strName);
	}

}
