package HitsAlgorithmAllTime;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import contant.Constants;

import DataReader.Location;
import DataReader.TrainDataLoader;



public class Graph   {
	private static Map<String,Map<String,Integer>>  user_location = null;
	//将用户的相同位置进行统计,得到每个用户的每个位置的签到次数
	public void  totalUserLocation() throws Exception {
		if(user_location == null){
			     Map<String,List<Location>> user_location_map = new TrainDataLoader().getUserLocMap();
				Map<String, Map<String, Integer>> user_location_count = new HashMap<String, Map<String, Integer>>();
				Set<Entry<String, List<Location>>> entry = user_location_map.entrySet();
				for (Entry<String, List<Location>> e : entry) {
					String userid = e.getKey();
					List<Location> location_list = e.getValue();
					Map<String, Integer> location_count = new HashMap<String, Integer>();
					int i = 0;
					while (i < location_list.size()) {
						Location lo = location_list.get(i);
						String venueid = lo.venueid;
						if (location_count.containsKey(venueid)) {
							location_count.put(venueid,location_count.get(venueid) + 1);
						} else {
							location_count.put(venueid, 1);
						}
						i++;
					}
					user_location_count.put(userid,location_count);
				}
				user_location = user_location_count;
		}
	}
	public  void showMap() throws Exception{
		totalUserLocation();
		Map<String,Map<String,Integer>>  user_location_count = user_location;
		Set<Entry<String,Map<String,Integer>>>  entry = user_location_count.entrySet();
		int i = 0;
		int j = 0;
		for(Entry<String,Map<String,Integer>> e: entry){
			String userid = e.getKey();
			Set<Entry<String,Integer>> locationlist = e.getValue().entrySet();
			for(Entry<String,Integer> ee: locationlist){
				String venuid = ee.getKey();
				j++;
				int count =ee.getValue();
			}
		}
		System.out.println("i :"+i);
		  System.out.println("j :"+ j);
	}
   //将上述方法得到的结果反转，得到每个位置的用户的签到次数
	public Map<String, Map<String, Integer>> totalLocationUser() throws Exception {
		totalUserLocation();
		Map<String,Map<String,Integer>>    user_location_count = user_location;
		Map<String, Map<String, Integer>>  location_user_count = new HashMap<String, Map<String, Integer>>();
		Set<Entry<String, Map<String, Integer>>> entry = user_location_count.entrySet();
		for (Entry<String, Map<String, Integer>> e : entry) {
			String userid = e.getKey();
			Set<Entry<String, Integer>> location_count_entry = e.getValue().entrySet();
			for (Entry<String, Integer> ine : location_count_entry) {
				String venueid = ine.getKey();
				int count = ine.getValue();
				if (location_user_count.containsKey(venueid)) {
					Map<String, Integer> user_count_map = location_user_count.get(venueid);
					user_count_map.put(userid, count);
				} else {
					Map<String, Integer> user_count_map = new HashMap<String, Integer>();
					user_count_map.put(userid, count);
					location_user_count.put(venueid, user_count_map);
				}
			}
		}
		return location_user_count;
	}
    //得到用户的列表
	public Map<String, Float> usermap() throws Exception {
		totalUserLocation();
		Map<String,Map<String,Integer>> user_location_count =  user_location;
		Set<Entry<String, Map<String, Integer>>> entry = user_location_count.entrySet();
		Map<String, Float> usermap = new HashMap<String, Float>();
		for (Entry<String, Map<String, Integer>> e : entry) {
			String userid = e.getKey();
			usermap.put(userid, 1.0f);
		}
		return usermap;
	}
	public  void showSum() throws Exception{
		Map<String,Float> user = locationmap();
		int i = 0;
		Set<Entry<String,Float>>  entry = user.entrySet();
		for(Entry<String,Float> e:entry){
			  i++;
		}
		System.out.println(i);
	}
   //得到位置的列表
	public Map<String, Float> locationmap() throws Exception {
		Map<String,Map<String,Integer>> location_user_count = totalLocationUser();
		Set<Entry<String, Map<String, Integer>>> entry = location_user_count.entrySet();
		Map<String, Float> locationmap = new HashMap<String, Float>();
		for (Entry<String, Map<String, Integer>> e : entry) {
			String venueid = e.getKey();
			locationmap.put(venueid, 1.0f);
		}
		return locationmap;
	}
   //得到 user  list  通过 hits算法得到
	public List<User> hits() throws Exception {
		totalUserLocation();
       Map<String,Map<String,Integer>>   user_location_count  = user_location;
       Map<String,Map<String,Integer>>   location_user_count = totalLocationUser();
       Map<String,Float> usermap = usermap();
       Map<String,Float>  locationmap = locationmap();
		for (int k = 0; k < 30; k++) {
			Set<Entry<String, Map<String, Integer>>> u_l_entry = user_location_count.entrySet();
			float user_result = 0;
			for (Entry<String, Map<String, Integer>> e1 : u_l_entry) {
				String userid = e1.getKey();
				Map<String, Integer> lo_map = e1.getValue();
				float sumvalue = 0;
				Set<Entry<String, Integer>> entry2 = lo_map.entrySet();
				for (Entry<String, Integer> e2 : entry2) {
					String venueid = e2.getKey();
					int value = e2.getValue();
					float value1 = locationmap.get(venueid);
					float value2 = value * value1;
					sumvalue += value2;
				}
				user_result += sumvalue * sumvalue;
				usermap.put(userid, sumvalue);
			}
			Set<Entry<String, Float>> en = usermap.entrySet();
			for (Entry<String, Float> ee : en) {
				String userid = ee.getKey();
				float userhub = ee.getValue() / (float) Math.sqrt(user_result);
				usermap.put(userid, userhub);
			}
			//System.out.println(Math.sqrt(user_result));
			float location_result = 0;
			Set<Entry<String, Map<String, Integer>>> entry3 = location_user_count.entrySet();
			for (Entry<String, Map<String, Integer>> e3 : entry3) {
				String locationid = e3.getKey();
				Map<String, Integer> userinnermap = e3.getValue();
				float sumvalue = 0;
				Set<Entry<String, Integer>> entry4 = userinnermap.entrySet();
				for (Entry<String, Integer> e4 : entry4) {
					String userid = e4.getKey();
					int value = e4.getValue();
					float value1 = usermap.get(userid);
					float value2 = value * value1;
					sumvalue += value2;
				}
				location_result += sumvalue * sumvalue;
				locationmap.put(locationid, sumvalue);
			}
			Set<Entry<String, Float>> enn = locationmap.entrySet();
			for (Entry<String, Float> eee : enn) {
				String locationid = eee.getKey();
				float locationauthority = eee.getValue() / (float) Math.sqrt(location_result);
				locationmap.put(locationid, locationauthority);
			}
            //System.out.println(Math.sqrt(location_result));
		}
		List<User> list = sort(usermap);
		return list;
	}

	public List<User> sort(Map<String, Float> usermap) {
		Set<Entry<String, Float>> entry = usermap.entrySet();
		List<User> userlist = new ArrayList<User>();
		for (Entry<String, Float> e : entry) {
			String userid = e.getKey();
			float value = e.getValue();
			User user = new User();
			user.userid = userid;
			user.value = value;
			userlist.add(user);
		}
		Collections.sort(userlist);
		return userlist;
	}
}
