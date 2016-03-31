package nn;

import java.io.File;
import java.io.FileWriter;
import java.util.*;
import java.util.Map.Entry;

import utils.*;

//our model
public class DV {
	public static boolean useIMDBData = true; // use additional unlabeled data
	public static Random random = new Random();
	public static float lr = 0.025f; // learning rate
	public static float original_lr = lr; // initialized learning rate
	public static int negSize = 5; // negative sampling size
	public static int gram = 3;
	public static int iter = 20; // iteration number
	public static int batchSize = 100;
	public static int n = 500; // vector size for both words and documents
	public static String dataFilePath = "J:/javaworkspace/DV-ngram/data/imdb/alldata-id_p" + gram + "gram.txt";
	public static int v; // vocabulary size
	public static int p; // document size

	public static float WV[][];
	public static float WP[][];

	public static void initNet() {
		v = Phrase.wordIdCountList.size();
		p = datasetExampleList.size();
		System.out.println("v=" + v + " p=" + p);
		WV = new float[v][];
		int realV = 0;
		for (int i = 0; i < v; i++)
			if (Phrase.wordIdCountList.get(i) >= 2) {
				realV++;
				WV[i] = new float[n];
				for (int j = 0; j < n; j++)
					WV[i][j] = (random.nextFloat() - 0.5f) / n;
			}

		System.out.println("realV=" + realV);
		WP = new float[p][n];
		for (int i = 0; i < p; i++) {
			for (int j = 0; j < n; j++)
				WP[i][j] = (random.nextFloat() - 0.5f) / n;
		}
	}

	public static class Example {
		public int wordIds[];
		public int label;
		public int index;
		public String item;

		public Example(Phrase p, int i, String ii) {
			wordIds = p.getWordIds();
			label = p.label;
			index = i;
			item = ii;
		}

		public Example(int[] ids, int l, int i, String ii) {
			wordIds = ids;
			label = l;
			index = i;
			item = ii;
		}
	}

	public static class TrainThread extends Thread {

		public List<Example> el;

		public TrainThread(List<Example> el) {
			this.el = el;
		}

		public void run() {
			train();
		}

		public void backprop(float a[], float b[], float t) {
			if (a == null || b == null)
				return;
			float y = 0;
			for (int i = 0; i < n; i++)
				y += a[i] * b[i];
			y = (float) (1.0 / (1 + Math.exp(-y)));
			for (int i = 0; i < n; i++) {
				float wv = a[i];
				a[i] += -(y - t) * b[i] * lr;
				b[i] += -(y - t) * wv * lr;
			}

		}

		public int[] getRandomList(int n) {
			int list[] = new int[n];
			for (int i = 0; i < n; i++)
				list[i] = i;
			for (int i = 0; i < n; i++) {
				int a = random.nextInt(n);
				int b = random.nextInt(n);
				int c = list[a];
				list[a] = list[b];
				list[b] = c;
			}
			return list;
		}

		public void train() {
			for (Example e : el) {
				int pi = e.index;
				int ids[] = e.wordIds;
				int forIds[] = getRandomList(ids.length);

				for (int l : forIds) {
					backprop(WP[pi], WV[ids[l]], 1);
					for (int index_neg = 0; index_neg < negSize; index_neg++) {
						backprop(WP[pi], WV[Phrase.getRandomWordId()], 0);
					}
				}
			}
		}
	}

	public static List<DV.Example> datasetExampleList = new ArrayList<DV.Example>();
	public static List<DV.Example> ode = new ArrayList<DV.Example>();

	public static void main(String args[]) {

		Map<String, ArrayList<Phrase>> dataset = null;
		dataset = Data_imdb.getDataset(dataFilePath);

		int index = 0;
		for (Entry<String, ArrayList<Phrase>> entry : dataset.entrySet()) {
			if (entry.getKey().equals("train") || entry.getKey().equals("test_root") || entry.getKey().equals("test")) {
				for (Phrase phrase : entry.getValue()) {
					if (useIMDBData) {
						datasetExampleList.add(new DV.Example(phrase, index++, entry.getKey()));
					} else {
						if (phrase.label != -1)
							datasetExampleList.add(new DV.Example(phrase, index++, entry.getKey()));
					}
				}
			}
		}

		for (Example e : datasetExampleList)
			ode.add(e);

		// initialize dictionary
		Phrase.set();

		initNet();

		TrainThread ttList[] = new TrainThread[30];
		for (int epoch = 0; epoch < iter; epoch++) {
			long startTime = System.currentTimeMillis();
			System.out.print(epoch + "::");
			int p = 0;
			Collections.shuffle(datasetExampleList);
			while (true) {
				boolean over = false;
				for (int i = 0; i < ttList.length; i++) {
					if (ttList[i] == null || !ttList[i].isAlive()) {
						if (p < datasetExampleList.size() / batchSize) {
							int s = batchSize * p;
							int e = batchSize * p + batchSize;
							if (datasetExampleList.size() < e)
								e = datasetExampleList.size();
							ttList[i] = new TrainThread(datasetExampleList.subList(s, e));
							ttList[i].start();
							p++;
							lr = original_lr
									* (1 - (epoch * datasetExampleList.size() / batchSize + p) * 1.0f
											/ (iter * datasetExampleList.size() / batchSize));
						} else {
							over = true;
							break;
						}
					} else {
					}
				}
				if (over)
					break;
			}
			System.out.println("||" + "|" + "time:" + (System.currentTimeMillis() - startTime));
			try {
				String dir = "result_pp_NN/" + "considerWord" + "iter" + iter + "useIMDBData"
						+ useIMDBData + "neg" + negSize + "n" + n + "/";
				File file = new File(dir);
				if (!file.exists())
					file.mkdirs();
				FileWriter fw_train = new FileWriter(dir + "train_vector.txt");
				FileWriter fw_test = new FileWriter(dir + "test_vector.txt");
				for (Example pp : ode) {
					if (pp.label == -1)
						continue;
					FileWriter fw = fw_test;
					if (pp.item.equals("train"))
						fw = fw_train;
					fw.write(pp.label + "\t");
					for (int i = 0; i < n; i++)
						fw.write(WP[pp.index][i] + "\t");
					fw.write("\r\n");
				}
				fw_train.close();
				fw_test.close();
				System.out.println(ConvertToSVM.convertToSVM(n, dir, false));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
