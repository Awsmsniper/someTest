package org.after90.JavaAlgorithm.ListTest;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RemoveThread extends Thread {
	public void run() {
		int nCount = 0;
		long lStart = System.currentTimeMillis();
		while (!PriPara.listBox.isEmpty()) {
			// String strTmp = PriPara.listBox.get(0);
			PriPara.listBox.remove(0);
			nCount++;
		}
		long lInterval = System.currentTimeMillis() - lStart;
		log.info("thread:{} remove {}, lInterval:{}", this.getName(), nCount, lInterval);
	}
}