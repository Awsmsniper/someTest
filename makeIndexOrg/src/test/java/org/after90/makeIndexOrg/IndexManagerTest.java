package org.after90.makeIndexOrg;

import org.junit.Before;
import org.junit.Test;

public class IndexManagerTest {
	private IndexManager im = new IndexManager();

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testMakeIndexOrg() {
		im.makeIndexOrg("/Users/zhaogj/private/after90/org/", "indexTest.org");
	}

}
