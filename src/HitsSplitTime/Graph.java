package HitsSplitTime;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import DataReader.Location;
import DataReader.TrainDataLoader;

public class Graph {
	private  static Map<String,List<Location>>  user_location= null;
	//基于时间处理，按时间签到分为4段，0-5，6-11，12-17，18-23
    public  void  baseTimeProcess() throws Exception{
    	if(user_location == null){
	    	Map<String,List<Location>>  user_location_map = new TrainDataLoader().getUserLocMap();
	    	Set<Entry<String,List<Location>>>  user_location_entry = user_location_map.entrySet();
	    	for(Entry<String,List<Location>> user_location_e:user_location_entry){
	    		  String userid = user_location_e.getKey();
	    		  List<Location> locationlist = user_location_e.getValue();
	    		   int i = 0;
	    		   while(i < locationlist.size()){
	    			    Location l = locationlist.get(i);
	    			    int time = Integer.parseInt(l.hour_time);
	    			    String hour_time = String.valueOf(time/6+1);
	    			    l.hour_time = hour_time;
	    			    i++;
	    		   }
	    	}
	    	  user_location = user_location_map;
    	}
    }
   //按时间找到对应的用户的签到情况，先假定输入时间为8
	public Map<String, List<Location>> timeCheckin( int inputt) throws Exception {
		baseTimeProcess();
		Map<String,List<Location>>  user_location_map = user_location;
		int correspondt = inputt / 6 + 1;
		String current_time_interval = String.valueOf(correspondt);
		Set<Entry<String, List<Location>>> entry = user_location_map.entrySet();
		Map<String, List<Location>> nowtimemap = new HashMap<String, List<Location>>();
		for (Entry<String, List<Location>> e : entry) {
			String userid = e.getKey();
			List<Location> locationlist = e.getValue();
			List<Location> newlocationlist = new ArrayList<Location>();
			int i = 0;
			while (i < locationlist.size()) {
				Location location = locationlist.get(i);
				if (location.hour_time.equals(current_time_interval)) {
					newlocationlist.add(location);
				}
				i++;
			}
			if (newlocationlist.size() == 0)
				continue;
			nowtimemap.put(userid, newlocationlist);
		}
		return nowtimemap;
	}
   //将用户的相同位置个数相加
	public Map<String, Map<String, Integer>> sumUserLocationMap(int inputt) throws Exception {
		Map<String,List<Location>>  nowtimemap = timeCheckin(inputt);
		Map<String, Map<String, Integer>> user_location_count = new HashMap<String, Map<String, Integer>>();
		Set<Entry<String, List<Location>>> entry = nowtimemap.entrySet();
		for (Entry<String, List<Location>> e : entry) {
			String userid = e.getKey();
			List<Location> list = e.getValue();
			Map<String, Integer> location_count = new HashMap<String, Integer>();
			int i = 0;
			while (i < list.size()) {
				Location location = list.get(i);
				String venueid = location.venueid;
				if (location_count.containsKey(venueid)) {
					location_count.put(venueid, location_count.get(venueid) + 1);
				} else {
					location_count.put(venueid, 1);
				}
				i++;
			}
			user_location_count.put(userid, location_count);
		}
		return user_location_count;
	}
   //通过上述的用户位置map，得到位置用户map
	public Map<String, Map<String, Integer>> getLocationUser(int inputt) throws Exception {
		Map<String,Map<String,Integer>>   user_location_map =  sumUserLocationMap(inputt);
		Map<String, Map<String, Integer>> location_user_map = new HashMap<String, Map<String, Integer>>();
		Set<Entry<String, Map<String, Integer>>> entry = user_location_map.entrySet();
		for (Entry<String, Map<String, Integer>> e : entry) {
			String userid = e.getKey();
			Set<Entry<String, Integer>> locationentry = e.getValue().entrySet();
			for (Entry<String, Integer> ine : locationentry) {
				String venueid = ine.getKey();
				int count = ine.getValue();
				if (location_user_map.containsKey(venueid)) {
					Map<String, Integer> tempmap = location_user_map.get(venueid);
					tempmap.put(userid, count);
				} else {
					Map<String, Integer> usermap = new HashMap<String, Integer>();
					usermap.put(userid, count);
					location_user_map.put(venueid, usermap);
				}
			}
		}
		return location_user_map;
	}
    //通过上述方法，得到用户map
	public Map<String, Float> userMap(int inputt) throws Exception{
		Map<String,Map<String,Integer>>  user_location_map = sumUserLocationMap(inputt);
		Set<Entry<String, Map<String, Integer>>> entry = user_location_map.entrySet();
		Map<String, Float> user_map = new HashMap<String, Float>();
		for (Entry<String, Map<String, Integer>> e : entry) {
			String userid = e.getKey();
			user_map.put(userid, 1.0f);
		}
		return user_map;
	}
	public void  showUsermap(int inputt) throws Exception{
		Map<String,Float>    user_map = userMap(inputt);
		Set<Entry<String,Float>>  entry = user_map.entrySet();
		for(Entry<String,Float>  e:entry){
			System.out.println(e.getKey()+"\t"+e.getValue());
		}
	}
    //通过上述方法，得到位置map
	public Map<String, Float> locationMap(int inputt) throws Exception {
		Map<String,Map<String,Integer>>  location_user_map = getLocationUser(inputt);
		Set<Entry<String, Map<String, Integer>>> entry = location_user_map.entrySet();
		Map<String, Float> location_map = new HashMap<String, Float>();
		for (Entry<String, Map<String, Integer>> e : entry) {
			String venueid = e.getKey();
			location_map.put(venueid, 1.0f);
		}
		return location_map;
	}
	public void showLocationMap(int inputt) throws Exception{
	  Map<String,Float>  location_map = locationMap(inputt);
	  Set<Entry<String,Float>>   entry = location_map.entrySet();
	  for(Entry<String,Float> e:entry){
		  System.out.println(e.getKey()+"\t"+e.getValue());
	  }
	}
     //hits算法
	public List<User> hits(int inputt) throws Exception {
       Map<String,Map<String,Integer>>  user_location_map = sumUserLocationMap(inputt);
       Map<String,Map<String,Integer>>  location_user_map = getLocationUser(inputt);
       Map<String,Float>  user_map = userMap(inputt);
       Map<String,Float>   location_map = locationMap(inputt);
		for (int k = 0; k < 30; k++) {
			Set<Entry<String, Map<String, Integer>>> entry1 = user_location_map.entrySet();
			float usersum = 0;
			for (Entry<String, Map<String, Integer>> e1 : entry1) {
				String userid = e1.getKey();
				Map<String, Integer> lomap = e1.getValue();
				float sumvalue = 0;
				Set<Entry<String, Integer>> entry2 = lomap.entrySet();
				for (Entry<String, Integer> e2 : entry2) {
					String venueid = e2.getKey();
					int value = e2.getValue();
					float value1 = location_map.get(venueid);
					float value2 = value * value1;
					sumvalue += value2;
				}
				usersum += sumvalue * sumvalue;
				user_map.put(userid, sumvalue);
			}
			Set<Entry<String, Float>> en = user_map.entrySet();
			for (Entry<String, Float> e : en) {
				String userid = e.getKey();
				float userhub = e.getValue() / (float) Math.sqrt(usersum);
				user_map.put(userid, userhub);
			}
			float locationsum = 0;
			Set<Entry<String, Map<String, Integer>>> entry3 = location_user_map.entrySet();
			for (Entry<String, Map<String, Integer>> e3: entry3) {
				String locationid = e3.getKey();
				Map<String, Integer> usermap = e3.getValue();
				float sumvalue = 0;
				Set<Entry<String, Integer>> entry4 = usermap.entrySet();
				for (Entry<String, Integer> e4 : entry4) {
					String userid = e4.getKey();
					int value = e4.getValue();
					float value1 = user_map.get(userid);
					float value2 = value * value1;
					sumvalue += value2;
				}
				locationsum += sumvalue * sumvalue;
				location_map.put(locationid, sumvalue);
			}
			Set<Entry<String, Float>> enn = location_map.entrySet();
			for (Entry<String, Float> e : enn) {
				String locationid = e.getKey();
				float locationauthority = e.getValue() / (float) Math.sqrt(locationsum);
				location_map.put(locationid, locationauthority);
			}
		}
		List<User> userlist = sort(user_map);
		return userlist;
	}
   // 对用户的map进行排序，将authority值大的用户输出
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

	public void showSum(Map<String, Map<String, Integer>> mm) {
		Set<Entry<String, Map<String, Integer>>> entry = mm.entrySet();
		for (Entry<String, Map<String, Integer>> e : entry) {
			String userid = e.getKey();
			System.out.println("useid" + userid);
			Map<String, Integer> inmap = e.getValue();
			Set<Entry<String, Integer>> inentry = inmap.entrySet();
			for (Entry<String, Integer> ine : inentry) {
				String venuid = ine.getKey();
				int count = ine.getValue();
				System.out.println("venueid: " + venuid + "count:" + count);
			}
		}
	}

	public void show(Map<String, List<Location>> uvmap) {
		Set<Entry<String, List<Location>>> entry = uvmap.entrySet();
		for (Entry<String, List<Location>> e : entry) {
			String userid = e.getKey();
			List<Location> list = e.getValue();
			int i = 0;
			System.out.println("userid" + userid);
			while (i < list.size()) {
				System.out.println(list.get(i));
				i++;
			}
			System.out.println();
		}
	}

}

