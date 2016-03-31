package utils;

import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Map.Entry;

public class Data_imdb {
	public static String readFile(File file) {
		Long filelength = file.length(); // 获取文件长度
		byte[] filecontent = new byte[filelength.intValue()];
		try {
			FileInputStream in = new FileInputStream(file);
			in.read(filecontent);
			in.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new String(filecontent);// 返回文件内容,默认编码
	}

	public static Map<String, ArrayList<Phrase>> getDataset(String filePath) {
		Map<String, ArrayList<Phrase>> dataset = new HashMap<String, ArrayList<Phrase>>();
		dataset.put("train", new ArrayList<Phrase>());
		dataset.put("test", new ArrayList<Phrase>());
		try {

			File file = new File(filePath);
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line = null;
			int readDataCount = 0;
			while ((line = reader.readLine()) != null) {
				String key = "train";
				int index = Integer.parseInt(line.split(" ")[0].substring(2));
				if (25000 <= index && index < 50000)
					key = "test";
				int cc = -1;
				if (index < 12500)
					cc = 1;
				if (12500 <= index && index < 25000)
					cc = 0;
				if (25000 <= index && index < 25000 + 12500)
					cc = 1;
				if (25000 + 12500 <= index && index < 25000 + 25000)
					cc = 0;
				String content = line.substring(line.split(" ")[0].length()).trim();
				// System.out.println(content);
				Phrase p = new Phrase(content, cc);
				dataset.get(key).add(p);
				readDataCount++;
				if (readDataCount % 10000 == 0)
					System.out.println(readDataCount);
			}
			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dataset;
	}
	//
	// public static Map<String, ArrayList<Phrase>> getDataset() {
	// Param.c = 2;
	// Map<String, ArrayList<Phrase>> dataset = new HashMap<String,
	// ArrayList<Phrase>>();
	// dataset.put("train", new ArrayList<Phrase>());
	// dataset.put("test", new ArrayList<Phrase>());
	// try {
	// for (String key : new String[] { "train", "test" }) {
	// File file = new File("./imdb/" + key + ".txt");
	// BufferedReader reader = new BufferedReader(new FileReader(file));
	// String line = null;
	// int index = 0;
	// while ((line = reader.readLine()) != null) {
	// index++;
	// if (index % 100 != 0) {
	// System.out.println(index);
	// //continue;
	// }
	// String tokens[] = line.split("\\|\\|\\|");
	// // System.out.println(tokens[0]);
	// int cc = Integer.parseInt(tokens[0]);
	// String content = tokens[1];
	// Phrase p = new Phrase(content, cc);
	// dataset.get(key).add(p);
	// }
	// reader.close();
	// }
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// return dataset;
	// }

}
