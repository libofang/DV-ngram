package utils;

import java.io.*;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import utils.Phrase;

public class ConvertToSVM {
	public static void main(String args[]) {
		convertToSVM(500, "./result_pp_NN/mikolov 3 500 2", true);
	}

	public static double convertToSVM(int n, String filePath, boolean showDetail) {
		Map<String, List<Example>> dataset = new HashMap<String, List<Example>>();
		// String[] itemList = new String[] { "train", "dev_root", "dev_all",
		// "test_root", "test_all" };
		String[] itemList = new String[] { "train", "test" };
		for (String item : itemList) {
			dataset.put(item, new ArrayList<Example>());
			int m[] = { n };
			int total = 0;
			for (int t : m)
				total += t;
			File file[] = { new File(filePath + "/" + item + "_vector.txt") };
			try {
				BufferedReader reader[] = new BufferedReader[m.length];
				for (int i = 0; i < m.length; i++)
					reader[i] = new BufferedReader(new FileReader(file[i]));
				String line[] = new String[m.length];
				int index = 0;
				while ((line[0] = reader[0].readLine()) != null) {
					for (int i = 1; i < m.length; i++)
						line[i] = reader[i].readLine();
					index++;
					
					if (showDetail && index % 100 != 0) {
						System.out.println(index);
						// continue;
					}
					Example example = new Example(total);
					int ei = 0;
					for (int i = 0; i < m.length; i++) {
						String tokens[] = line[i].split("\t");
						example.label = Integer.parseInt(tokens[0]);
						for (int t = 1; t < tokens.length; t++)
							example.vector[ei++] = Double.parseDouble(tokens[t]);
					}
					// System.out.println(ei + "|" + total);

					dataset.get(item).add(example);
				}
				for (int i = 0; i < m.length; i++)
					reader[i].close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		for (String item : itemList) {
			try {
				File file = new File("./liblinear/" + item + ".txt");
				FileWriter fw = new FileWriter(file);
				for (Example example : dataset.get(item)) {
					fw.write((example.label * 2 - 1) + " ");
					for (int i = 0; i < example.vector.length; i++)
						fw.write((i + 1) + ":" + example.vector[i] + " ");
					fw.write("\n");
				}
				fw.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		double result = Classifier.runAndPrint_liblinear(showDetail);
		fileChannelCopy(new File("./liblinear/windows/prediction.txt"), new File(filePath + "/prediction.txt" ));
		return result;
		// SVM.runAndPrint();
		// misClassSentence();
	}
	 public static void fileChannelCopy(File s, File t) {

	        FileInputStream fi = null;

	        FileOutputStream fo = null;

	        FileChannel in = null;

	        FileChannel out = null;

	        try {

	            fi = new FileInputStream(s);

	            fo = new FileOutputStream(t);

	            in = fi.getChannel();

	            out = fo.getChannel();

	            in.transferTo(0, in.size(), out);

	        } catch (IOException e) {

	            e.printStackTrace();

	        } finally {

	            try {

	                fi.close();

	                in.close();

	                fo.close();

	                out.close();

	            } catch (IOException e) {

	                e.printStackTrace();

	            }

	        }

	    }
	public static class Example {
		public double vector[];
		public int label;

		public Example(int n) {
			vector = new double[n];
		}
	}

}
