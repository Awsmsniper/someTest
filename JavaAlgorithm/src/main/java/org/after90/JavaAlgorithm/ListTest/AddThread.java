package org.after90.JavaAlgorithm.ListTest;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AddThread extends Thread {
	public void run() {
		long lStart = System.currentTimeMillis();
		for (int i = 0; i < 1000000; i++) {
			PriPara.listBox.add(this.getName() + i);
		}
		long lInterval = System.currentTimeMillis() - lStart;
		log.info("thread:{}, lInterval:{}", this.getName(), lInterval);
	}
}
