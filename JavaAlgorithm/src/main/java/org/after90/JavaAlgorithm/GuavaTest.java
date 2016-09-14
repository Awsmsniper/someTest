package org.after90.JavaAlgorithm;

import java.util.Iterator;

import com.google.common.base.Splitter;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GuavaTest {
	public void doTestSplitter() {
		String strInput = "a\tb\tc\t\t\tg\t\t";
		Splitter splitter = Splitter.on("\t").trimResults();
		Iterable<String> itInput = splitter.split(strInput);
		for (String str : itInput) {
			log.info(str);
		}
		for (Iterator<String> iter = itInput.iterator(); iter.hasNext();) {
			String strWord = iter.next();
			log.info(strWord);
		}

	}
}
