package DataReader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import contant.Constants;

public class TrainDataLoader {

	private static Map<String, List<Location>> user_location_map = null;
	/**
	 * lazy read train file and get user->location list 
	 * @return
	 * @throws Exception
	 */
	private static Map<String,List<Location>>  user_location_test_map = null;
	public Map<String,List<Location>>  getUserLocTestmap() throws Exception{
		if(user_location_test_map == null){
			    user_location_test_map = new HashMap<String,List<Location>>();
			    File file = new File(Constants.test_file);
			    BufferedReader br = new BufferedReader(new FileReader(file));
			    String line ;
			    while((line = br.readLine())!=null){
			    	String[] arr = line.split("\t");
			    	String userid = arr[0];
					String venueid = arr[1];
					String latitude = arr[2];
					String longtitude = arr[3];
					String category  = arr[4];
					String time = arr[5];
					
					Location lo = new Location();
					//将时间转换成小时
					SimpleDateFormat sdf = new SimpleDateFormat("HH");
					SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
					Date date = sdf1.parse(time);
				    String hour_time = sdf.format(date);
					lo.venueid = venueid;
					lo.latitude = latitude;
					lo.longtitude = longtitude;
					lo.category = category;
					lo.count = 1;
					lo.hour_time = hour_time;
					
					if (user_location_test_map.containsKey(userid)) {
						List<Location> locationlist = user_location_test_map.get(userid);
						locationlist.add(lo);
					} else {
						List<Location> locationlist = new ArrayList<Location>();
						locationlist.add(lo);
						user_location_test_map.put(userid, locationlist);
					}
			    }
			   br.close(); 
		}
		return user_location_test_map;
	}
	public  Map<String, List<Location>> getUserLocMap() throws Exception {
		if (user_location_map == null) {
			user_location_map = new HashMap<String, List<Location>>();
			File file = new File(Constants.train_file);
			BufferedReader br = new BufferedReader(new FileReader(file));
			String line;
			while ((line = br.readLine()) != null) {
				String[] arr = line.split("\t");
				//将train数据集解析
				String userid = arr[0];
				String venueid = arr[1];
				String latitude = arr[2];
				String longtitude = arr[3];
				String category  = arr[4];
				String time = arr[5];

				Location lo = new Location();
				//将时间转换成小时
				SimpleDateFormat sdf = new SimpleDateFormat("HH");
				SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
				Date date = sdf1.parse(time);
			    String hour_time = sdf.format(date);
				lo.venueid = venueid;
				lo.latitude = latitude;
				lo.longtitude = longtitude;
				lo.category = category;
				lo.count = 1;
				lo.hour_time = hour_time;
				if (user_location_map.containsKey(userid)) {
					List<Location> locationlist = user_location_map.get(userid);
					locationlist.add(lo);
				} else {
					List<Location> locationlist = new ArrayList<Location>();
					locationlist.add(lo);
					user_location_map.put(userid, locationlist);
				}
			}
			br.close();
		}
		return user_location_map;
	}

}
