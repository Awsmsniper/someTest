package org.after90.JavaAlgorithm;

import java.util.ArrayList;
import java.util.List;

public class PokerSort {
	/**
	 * 1,2,3...J,Q,K<br>
	 * 拿一张，切一张，拿一张，切两张...拿出的牌顺序排好，问原顺序
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		List<String> listPokerOK = new ArrayList<String>();
		listPokerOK.add("K");
		listPokerOK.add("Q");
		listPokerOK.add("J");
		listPokerOK.add("10");
		listPokerOK.add("9");
		listPokerOK.add("8");
		listPokerOK.add("7");
		listPokerOK.add("6");
		listPokerOK.add("5");
		listPokerOK.add("4");
		listPokerOK.add("3");
		listPokerOK.add("2");
		listPokerOK.add("1");
		List<String> listPoker = new ArrayList<String>();
		for (int i = 0; i <= 12; i++) {
			listPoker.add(listPokerOK.get(i));
			for (int j = 0; j <= 11 - i; j++) {
				String strTmp = listPoker.get(0);
				listPoker.remove(0);
				listPoker.add(strTmp);
			}
		}
		for (String strTmp : listPoker) {
			System.out.println(strTmp);
		}
	}

}
