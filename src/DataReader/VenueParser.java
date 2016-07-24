package DataReader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

import contant.Constants;

public class VenueParser {

	public Map<String, String> process() {
		Map<String, String> hap = new HashMap<String, String>();
		try {
			BufferedReader br = new BufferedReader(new FileReader(new File(Constants.venue_file)));
			String line = null;
			while ((line = br.readLine()) != null) {
				String[] arr = line.split("\t");
				String venueid = arr[0];
				String geo = arr[2] + "\t" + arr[3];
				hap.put(venueid, geo);
			}

			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return hap;
	}

}
