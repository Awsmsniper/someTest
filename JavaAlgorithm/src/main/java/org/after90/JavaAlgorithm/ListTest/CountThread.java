package org.after90.JavaAlgorithm.ListTest;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CountThread extends Thread {
	public void run() {
		while (PriPara.listBox.size() > 0) {
			log.info("PriPara.listBox.size():" + PriPara.listBox.size());
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
