package org.after90.JavaAlgorithm.ListTest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PriPara {
	// public static List<String> listBox = new ArrayList<String>();
	public static List<String> listBox = Collections.synchronizedList(new ArrayList<String>());
}
