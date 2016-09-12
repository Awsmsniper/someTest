package org.after90.JavaAlgorithm;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class Dom4jTestTest {
	private Dom4jTest d4j = new Dom4jTest();

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testWriteXmlFile() {
		d4j.writeXmlFile();
	}

}
