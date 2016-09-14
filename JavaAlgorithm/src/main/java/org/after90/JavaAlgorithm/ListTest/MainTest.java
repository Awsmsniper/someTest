package org.after90.JavaAlgorithm.ListTest;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MainTest {

	public static void main(String[] args) {
		log.info("begin main");

		Thread t1 = new AddThread();
		t1.setName("t1");
		t1.start();
		Thread t2 = new AddThread();
		t2.setName("t2");
		t2.start();
		Thread t3 = new AddThread();
		t3.setName("t3");
		t3.start();
		//Thread t = new CountThread();
		//t.setName("t");
		//t.start();
		Thread t4 = new RemoveThread();
		t4.setName("t4");
		t4.start();
		log.info("end main");
	}

}
