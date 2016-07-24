package DataReader;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import contant.Constants;

public class TipsParser {
	
	public static void main(String[] args) {
		StringBuffer sb = new StringBuffer();
		CategoryParser cate_parser = new CategoryParser();
		Map<String, String> first_second_cate = cate_parser.parseCategoryTree();
		String line = null;
		try {
			BufferedReader br = new BufferedReader(new FileReader(new File(Constants.raw_checkin_file)));
			while ((line = br.readLine()) != null) {
				String[] arr = line.split("\t");
				String userid = arr[0];
				for (int i = 1; i < arr.length;) {
					String venueid = arr[i];
					String time = arr[i + 3];
					if (i + 7 >= arr.length) {
						break;
					}
					String category = arr[i + 7];
					if (!first_second_cate.containsKey(category)) {
						String otherline = userid + "\t" + venueid + "\t" + time + "\n";
						sb.append(otherline);
						i = i + 7;
					} else {
						i = i + 7;
						if (first_second_cate.containsKey(category)) {
							String value = first_second_cate.get(category);
							if (value != null) {
								category = value;
							}
						}
						String newline = userid + "\t" + venueid + "\t" + time + "\t" + category
								+ "\n";
						sb.append(newline);
						i++;
						while (i < arr.length && first_second_cate.containsKey(arr[i])) {
							i++;
						}
					}
				}
			}
			br.close();

			BufferedWriter bw = new BufferedWriter(new FileWriter(new File(Constants.formatted_checkin_file)));
			VenueParser vp = new VenueParser();
			Map<String, String> locationmap = vp.process();
			String result = sb.toString();
			String[] rearr = result.split("\n");
			int i = 0;
			while (i < rearr.length) {
				String theline = rearr[i];
				String[] arr = theline.split("\t");
				int length = arr.length;
				if (length == 4) {
					String userid = arr[0];
					String venueid = arr[1];
					String geo = null;
					if (locationmap.containsKey(venueid)) {
						geo = locationmap.get(venueid);
					}
					String time = arr[2];
					Long timel = Long.parseLong(time);
					String category = arr[3];
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss ");
					String date = sdf.format(new Date(timel * 1000));
					String newline = userid + "\t" + venueid + "\t" + geo + "\t" + category + "\t"
							+ date;
					bw.write(newline);
					bw.newLine();
				}
				i++;
			}
			bw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
