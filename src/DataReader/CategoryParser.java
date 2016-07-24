package DataReader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import contant.Constants;

public class CategoryParser {
	public String parseCategoriesContent() {
		StringBuffer sb = new StringBuffer();
		String line = null;
		String cacontent = null;
		try {
			BufferedReader br = new BufferedReader(
					new FileReader(new File(Constants.category_file)));
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			JSONObject jsonObject = new JSONObject(sb.toString());
			JSONObject content = jsonObject.getJSONObject("response");
			JSONArray category = content.getJSONArray("categories");
			String name = null;
			StringBuffer jsonFileInfo = new StringBuffer();
			JSONArray secondcategory = null;
			JSONArray thirdcategory = null;
			for (int i = 0; i < category.length(); i++) {
				name = category.getJSONObject(i).getString("name");
				jsonFileInfo.append("\nname" + "\t" + name + "\n");
				secondcategory = category.getJSONObject(i).getJSONArray("categories");
				for (int j = 0; j < secondcategory.length(); j++) {
					thirdcategory = secondcategory.getJSONObject(j).getJSONArray("categories");
					jsonFileInfo.append("secondcategory" + "\t"
							+ secondcategory.getJSONObject(j).getString("id") + "\t"
							+ secondcategory.getJSONObject(j).getString("name") + "\n");
					for (int k = 0; k < thirdcategory.length(); k++) {
						jsonFileInfo.append("thirdcategory" + "\t"
								+ thirdcategory.getJSONObject(k).getString("id") + "\t"
								+ thirdcategory.getJSONObject(k).getString("name") + "\n");
					}
				}
			}

			cacontent = jsonFileInfo.toString();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return cacontent;
	}

	public Map<String, String> parseCategoryTree() {

		String categoryContent = parseCategoriesContent();
		Map<String, String> hm = new HashMap<String, String>();
		String[] strarr = categoryContent.split("\n");
		int m = 0;
		while (m < strarr.length) {
			String[] firarr = strarr[m].split("\t");
			if (firarr[0].equals("secondcategory")) {
				String second = "\"" + firarr[1] + "\"";
				int n = m + 1;
				while (n < strarr.length) {
					String[] secarr = strarr[n].split("\t");
					if (secarr[0].equals("thirdcategory")) {
						String first = "\"" + secarr[1] + "\"";
						hm.put(first, second);
					}
					if (secarr[0].equals("secondcategory")) {
						break;
					}
					n++;
				}
				m = n - 1;
				hm.put(second, null);
			}
			m++;

		}

		String m1 = "\"" + "4d4b7105d754a06375d81259" + "\"";
		String m2 = "\"" + "4d4b7105d754a06378d81259" + "\"";
		String m3 = "\"" + "4d4b7105d754a06376d81259" + "\"";
		String m4 = "\"" + "4d4b7105d754a06374d81259" + "\"";
		String m5 = "\"" + "4d4b7105d754a06377d81259" + "\"";
		String m6 = "\"4d4b7104d754a06370d81259\"";
		String m7 = "\"4deefb944765f83613cdba6e\"";
		String m8 = "\"4d4b7104d754a06370d81259\"";
		String m9 = "\"4d4b7105d754a06379d81259\"";
		hm.put(m1, null);
		hm.put(m2, null);
		hm.put(m3, null);
		hm.put(m4, null);
		hm.put(m5, null);
		hm.put(m6, null);
		hm.put(m7, m8);
		hm.put(m9, null);
		return hm;
	}

}
