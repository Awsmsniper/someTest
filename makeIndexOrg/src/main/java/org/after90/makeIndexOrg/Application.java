package org.after90.makeIndexOrg;

public class Application {
	public static void main(String[] args) {
		IndexManager im = new IndexManager();
		im.makeIndexOrg("./org/", "index.org");
	}
}
