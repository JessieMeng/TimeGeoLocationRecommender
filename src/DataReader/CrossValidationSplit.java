package DataReader;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import contant.Constants;

public class CrossValidationSplit {

	public static void main(String[] args) {
		try {
			BufferedReader br = new BufferedReader(new FileReader(new File(
					Constants.formatted_checkin_file)));
			BufferedWriter bw = new BufferedWriter(new FileWriter(new File(Constants.train_file)));
			BufferedWriter bw1 = new BufferedWriter(new FileWriter(new File(Constants.test_file)));
			Map<String, List<String>> recordmap = new HashMap<String, List<String>>();
			String line;
			while ((line = br.readLine()) != null){
				String[] arr = line.split("\t");
				String userid = arr[0];
				if (recordmap.containsKey(userid)) {
					List<String> linelist = recordmap.get(userid);
					linelist.add(line);
				} else {
					List<String> linelist = new ArrayList<String>();
					linelist.add(line);
					recordmap.put(userid, linelist);
				}
			}
			Set<Entry<String, List<String>>> entry = recordmap.entrySet();
			for (Entry<String, List<String>> e : entry) {
				List<String> linelist = e.getValue();
				int size = linelist.size();
				if (linelist.size() < 4) {
					continue;
				}
				int trainsize = (int) (size * 0.8);
				int testsize = size - trainsize;
				int i = 0;
				while (i < testsize) {
					String record = linelist.get(i);
					bw1.write(record);
					bw1.newLine();
					i++;
				}
				while (i < size) {
					String record = linelist.get(i);
					bw.write(record);
					bw.newLine();
					i++;
				}
			}
			br.close();
			bw.close();
			bw1.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
