package org.after90.JavaAlgorithm.ListTest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

public class PriPara {
	// public static List<String> listBox = new ArrayList<String>();
	// public static List<String> listBox = Collections.synchronizedList(new
	// ArrayList<String>());
	// public static List<String> listBox = new Vector<String>();
	public static List<String> listBox = Collections.synchronizedList(new LinkedList<String>());
}
