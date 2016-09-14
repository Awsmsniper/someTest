package org.after90.JavaAlgorithm;

import java.util.Iterator;

import com.google.common.base.Splitter;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GuavaTest {
	public void doTestSplitter() {
		String strInput = "a\tb\tc\t\t\tg\t\t";
		Iterable<String> itInput = Splitter.on("\t").trimResults().split(strInput);
		for (Iterator<String> iter = itInput.iterator(); iter.hasNext();) {
			String strWord = iter.next();
			log.info(strWord);
		}
		
	}
}
