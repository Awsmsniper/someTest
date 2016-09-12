package org.after90.JavaAlgorithm.ListTest;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AddThread extends Thread {
	public void run() {
		for (int i = 0; i < 1000000; i++) {
			PriPara.listBox.add(this.getName() + i);
		}
		log.info("thread:" + this.getName() + "add 1000000");
	}
}
