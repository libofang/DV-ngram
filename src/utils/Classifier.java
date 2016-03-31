package utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

import utils.Data_imdb;
import utils.Phrase;

public class Classifier {
	public static double runAndPrint_liblinear(boolean showDetail) {
		Process process;
		double ac = 0;
		try {
			process = Runtime.getRuntime().exec("liblinear\\run_liblinear.bat");
			BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String line = null;
			while ((line = reader.readLine()) != null) {
				if(showDetail)
					System.out.println(line);
				if (line.contains("Accuracy = ")) {
					ac = Double.parseDouble(line.split("Accuracy \\= ")[1].split("%")[0]);
				}
			}
			BufferedReader ereader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
			line = null;
			while ((line = ereader.readLine()) != null) {
				if(showDetail)
				System.err.println(line);
			}
			process.getOutputStream().close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return ac;
	}
}
