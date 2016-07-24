package HitsAlgorithmAllTime;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class Statics {
	public static void main(String[] args) {
		try {
			BufferedReader br = new BufferedReader(new FileReader(new File("timeRecord.txt")));
			Map<String, Integer> usermap = new HashMap<String, Integer>();
			Map<String, Integer> locationmap = new HashMap<String, Integer>();
			String[] userarr = new String[30758];
			String[] locationarr = new String[130111];

			String line;
			while ((line = br.readLine()) != null) {
				String[] arr = line.split("\t");
				String userid = arr[0];
				String venueid = arr[1];
				if (!usermap.containsKey(userid)) {

					usermap.put(userid, 1);
				}
				if (!locationmap.containsKey(venueid)) {
					locationmap.put(venueid, 1);
				}
			}

			int ucount = 0;
			int lcount = 0;

			Set<Entry<String, Integer>> uentry = usermap.entrySet();
			Set<Entry<String, Integer>> lentry = locationmap.entrySet();
			for (Entry<String, Integer> e : uentry) {
				ucount = ucount + e.getValue();
			}
			for (Entry<String, Integer> e : lentry) {
				lcount = lcount + e.getValue();
			}
			System.out.println("ucount:" + ucount);
			System.out.println("lcount: " + lcount);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
