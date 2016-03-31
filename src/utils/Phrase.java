package utils;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Phrase {
	// private String words[];
	private int wordIds[];
	public int posIds[];
	public int label;
	public static boolean lowcase = false;
	public static Random random = new Random();
	public static double powFactor = 0.75;

	public static Map<String, Integer> wordIdMap = new HashMap<String, Integer>();
	public static ArrayList<Integer> wordIdCountList = new ArrayList<Integer>();
	public static int[] wordIdSumList = null;
	public void setWordIds(String ws[]) {
		wordIds = new int[ws.length];
		for (int i = 0; i < wordIds.length; i++) {
			if (!wordIdMap.containsKey(ws[i])) {
				wordIdMap.put(ws[i], wordIdCountList.size());
				wordIdCountList.add(0);
			}
			int index = wordIdMap.get(ws[i]);
			wordIdCountList.set(index, wordIdCountList.get(index) + 1);
			wordIds[i] = index;
		}
	}

	public Phrase(int ids[], int l) {
		this.wordIds = ids;
		this.label = l;
	}
	public Phrase(String s, int l) {
		// s = "you and me are good man";
		// System.out.println( s);
		if (wordIdMap.size() == 0) { // 加一个index 保证rareword
			wordIdMap.put("rareWord", wordIdCountList.size());
			wordIdCountList.add(0);
		}
		// System.out.println(tagged);
		String[] tokens = s.split(" ");
		String[] ws = new String[tokens.length];
		for (int i = 0; i < tokens.length; i++) {
			ws[i] = tokens[i];
			if (lowcase)
				ws[i] = ws[i].toLowerCase();
		}
		setWordIds(ws);
		label = l;
	}

	public int[] getWordIds() {
		int wi[] = new int[wordIds.length];
		int realLength = 0;
		for (int i = 0; i < wi.length; i++) {
			wi[i] = getWordId(wordIds[i]);
			if (wi[i] != 0)
				realLength++;
		}
		int rwi[] = new int[realLength];
		int index = 0;
		for (int i = 0; i < wi.length; i++) {
			if (wi[i] != 0)
				rwi[index++] = wi[i];
		}
		return rwi;
	}

	public static int getWordId(int i) {
		if (wordIdCountList.get(i) <= 0)
			return 0;
		return i;
	}

	public static int getRandomWordId() {
		int i = random.nextInt(Phrase.wordIdSumList[Phrase.wordIdSumList.length - 1]) + 1;
		int l = 0, r = wordIdSumList.length - 1;
		while (l != r) {
			int m = (l + r ) / 2;
			if (i <= wordIdSumList[m])
				r = m;
			else
				l = m + 1;
		}
		//System.out.println(wordIdSumList[l - 1] + "|" + i + "|" + wordIdSumList[l]);
		return l;
		// return allWord.get(random.nextInt(allWord.size()));
	}

	public static void set() {
		wordIdMap.clear();
		wordIdMap= null;
		wordIdSumList = new int[wordIdCountList.size()];
		wordIdSumList[0] = (int) Math.round(Math.pow(wordIdCountList.get(0), powFactor));
		for (int t = 1; t < wordIdCountList.size(); t++)
			wordIdSumList[t] = (int) Math.round(Math.pow(wordIdCountList.get(t), powFactor) + wordIdSumList[t - 1]);
	}
}
