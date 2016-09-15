package org.after90.JavaAlgorithm;

import org.junit.Before;
import org.junit.Test;

public class JSONClientTest {
	private JSONClient jc = new JSONClient();

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testSubmitIds() {
		jc.submitIds();
	}

	@Test
	public void testGetIdsDirect() {
		jc.getIdsDirect();
	}

}
