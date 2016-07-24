package Geographical;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import contant.Constants;

import DataReader.Location;
import DataReader.TrainDataLoader;
import HitsSplitTime.HitsAlgorithm;
import HitsSplitTime.User;

public class Geodata {
	private static final double EARTH_RADIUS = 6378137;
	private static Map<String, List<Double>> user_distanceminus = null;
	private static Map<String, Double> user_h = null;

	public Map<String, Double> getH() throws Exception {
		if (user_h == null) {
			long time1 = System.currentTimeMillis();

			user_h = new HashMap<String, Double>();
			Map<String, List<Double>> user_distanceminus = distanceMinus();
			Set<Entry<String, List<Double>>> user_distanceminus_entry = user_distanceminus
					.entrySet();
			for (Entry<String, List<Double>> e : user_distanceminus_entry) {
				String userid = e.getKey();
				List<Double> distanceminuslist = e.getValue();
				int i = 0;
				double sum = 0;
				while (i < distanceminuslist.size()) {
					sum += distanceminuslist.get(i);
					i++;
				}
				double meanvalue = sum / distanceminuslist.size();
				// 样本平均值
				int j = 0;
				double powersum = 0;
				while (j < distanceminuslist.size()) {
					double x = distanceminuslist.get(j);
					powersum += (x - meanvalue) * (x - meanvalue);
					j++;
				}
				double h = 1.06 * Math.sqrt(powersum / distanceminuslist.size())
						* Math.pow(distanceminuslist.size(), -0.2);
				user_h.put(userid, h);
			}
			long time2 = System.currentTimeMillis();
			System.out.println("getH cost :" + (time2 - time1) + "ms");
		}
		return user_h;
	}
   public int[]  distanceMinusDistribute() throws Exception{
	    Map<String,List<Double>>  userMinus = distanceMinus();
	    int[] arr = new int[5000];
	    Set<Entry<String,List<Double>>>  userMinusEntry = userMinus.entrySet();
	    for(Entry<String,List<Double>>   e : userMinusEntry){
	    	List<Double>  minusList = e.getValue();
	    	for(double  minus :minusList){
	    		  int group = (int)minus%5000;
	    		  arr[group] = arr[group]+1;
	    	}
	    	
	    }
	    BufferedWriter bw = new BufferedWriter(new FileWriter(new File("minusdistribute.csv")));
	    bw.write("distanceminus,number");
	    bw.newLine();
	   for(int i = 0; i <100;i++){
		   bw.write(i+1+","+arr[i]);
		   bw.newLine();
	   }
	    bw.close();
	    return arr;
   }
     //每个用户的中心点的位置为
     public void  centerPoint(){
				
    }
	// 得到每个用户自己的距离差
	public Map<String, List<Double>> distanceMinus() throws Exception {
		if (user_distanceminus == null) {
			long time1 = System.currentTimeMillis();
			user_distanceminus = new HashMap<String, List<Double>>();
			Map<String, List<Location>> user_location = new TrainDataLoader().getUserLocMap();
			Set<Entry<String, List<Location>>> entry_user_location = user_location.entrySet();
			for (Entry<String, List<Location>> e : entry_user_location) {
				String userid = e.getKey();
				List<Location> locationlist = e.getValue();
				List<Double> minuslist = new ArrayList<Double>();
				int i = 0;
				while (i < locationlist.size()) {
					Location lo1 = locationlist.get(i);
					int j = i + 1;
					while (j < locationlist.size()) {
						Location lo2 = locationlist.get(j);
						double minus = getDistance(lo1, lo2);
						minuslist.add(minus);
						j++;
					}
					i++;
				}
				user_distanceminus.put(userid, minuslist);

			}
			long time2 = System.currentTimeMillis();
			System.out.println("distanceMinus cost :" + (time2 - time1) + "ms");
		}
		return user_distanceminus;
	}

	public void showMinus() throws Exception {
		Map<String, List<Double>> minusmap = distanceMinus();
		Set<Entry<String, List<Double>>> entry = minusmap.entrySet();
		for (Entry<String, List<Double>> e : entry) {
			String userid = e.getKey();
			List<Double> list = e.getValue();
			System.out.println(userid);
			System.out.println(list);
		}
	}

	public void showH() throws Exception {
		Map<String, Double> hlist = getH();
		Set<Entry<String, Double>> hentry = hlist.entrySet();
		for (Entry<String, Double> e : hentry) {
			System.out.println(e.getKey() + "  " + e.getValue());
		}
	}

	// 得到两个位置的距离差，单位是千米，转换成了米；
	public double getDistance(Location l1, Location l2) {
		double lat1 = Double.parseDouble(l1.latitude);
    	double lat2 = Double.parseDouble(l2.latitude);
    	double lon1 = Double.parseDouble(l1.longtitude);
    	double lon2 = Double.parseDouble(l2.longtitude);
    	 double radLat1 = rad(lat1);  
         double radLat2 = rad(lat2);  
   
         double radLon1 = rad(lon1);  
         double radLon2 = rad(lon2);  
   
         if (radLat1 < 0)  
             radLat1 = Math.PI / 2 + Math.abs(radLat1);// south  
         if (radLat1 > 0)  
             radLat1 = Math.PI / 2 - Math.abs(radLat1);// north  
         if (radLon1 < 0)  
             radLon1 = Math.PI * 2 - Math.abs(radLon1);// west  
         if (radLat2 < 0)  
             radLat2 = Math.PI / 2 + Math.abs(radLat2);// south  
         if (radLat2 > 0)  
             radLat2 = Math.PI / 2 - Math.abs(radLat2);// north  
         if (radLon2 < 0)  
             radLon2 = Math.PI * 2 - Math.abs(radLon2);// west  
         double x1 = EARTH_RADIUS * Math.cos(radLon1) * Math.sin(radLat1);  
         double y1 = EARTH_RADIUS * Math.sin(radLon1) * Math.sin(radLat1);  
         double z1 = EARTH_RADIUS * Math.cos(radLat1);  
   
         double x2 = EARTH_RADIUS * Math.cos(radLon2) * Math.sin(radLat2);  
         double y2 = EARTH_RADIUS * Math.sin(radLon2) * Math.sin(radLat2);  
         double z2 = EARTH_RADIUS * Math.cos(radLat2);  
   
         double d = Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2)+ (z1 - z2) * (z1 - z2));  
         //余弦定理求夹角  
         double theta = Math.acos((EARTH_RADIUS * EARTH_RADIUS + EARTH_RADIUS * EARTH_RADIUS - d * d) / (2 * EARTH_RADIUS * EARTH_RADIUS));  
         double dist = theta * EARTH_RADIUS/1000.0;  
         return dist;
	}

	public double rad(double d) {
		return d * Math.PI / 180.0;
	}

	// 计算待推荐用户对待推荐位置的签到概率为多少
	public double computeProbability(Location recommendlocation, String userid) throws Exception {
		Map<String, List<Location>> user_location_map = new TrainDataLoader().getUserLocMap();
		List<Location> location_list = user_location_map.get(userid);

		// 放到GeoAlgo里的init里 　　　  
		Map<String, List<Double>> distanceminusmap = distanceMinus();
		List<Double> distanceminuslist = distanceminusmap.get(userid);
		List<Double> distancelist = new ArrayList<Double>();

		// 放到外面的init里
		Map<String, Double> hlist = getH();
		double h = hlist.get(userid);
		for (Location location : location_list) {
			double yi = getDistance(recommendlocation, location);
			distancelist.add(yi);
		}
		double temp = 1;
		for (double yi : distancelist) {
			double hesum = 0;
			for (double xi : distanceminuslist) {
				double hedensity = Math.pow(Math.E, -((yi - xi) * (yi - xi)) / (h * h));
				hesum += hedensity;
			}
			temp = temp * (1 - hesum / (distanceminuslist.size() * Math.pow(2 * 3.1415926, 0.5)));
		}
		double p = 1 - temp;
		return p;
	}

	/*
	 * 得到待推荐用户的 位置推荐列表以及打分情况
	 */
	public List<Location> caculateScoreAndGetTop(List<Location> recommendlocationlist,
			String userid, int topSize) throws Exception {
		System.out.println("recommendlocationlist size" + recommendlocationlist.size());
		List<Location> scorelist = new ArrayList<Location>();
		for (Location location : recommendlocationlist) {
			double p = computeProbability(location, userid);
			Location newlocation = new Location();
			newlocation.venueid = location.venueid;
			newlocation.score = p;
			scorelist.add(location);
		}
		Collections.sort(scorelist);
		// 推荐前10个分高的位置
		List<Location> recLocationScorelist = new ArrayList<Location>();
		int num = 0;
		while (num < topSize) {
			recLocationScorelist.add(scorelist.get(num));
			num++;
		}
		return recLocationScorelist;
	}

	// 得到待推荐用户的待推荐位置列表
	public List<Location> recommendLocation(String userid, int hour) throws Exception {
		Map<String, List<Location>> user_location = new TrainDataLoader().getUserLocMap();
		long time1 = System.currentTimeMillis();
		List<User> candidateuser_list = new HitsAlgorithm().caculateUserList(hour);
		long time2 = System.currentTimeMillis();
		System.out.println("hits algorithm user cost " + (time2 - time1) + "ms");
		List<Location> checkinlocation = user_location.get(userid);
		System.out.println("check in size:" + checkinlocation.size());
		Map<Location, Integer> candidateLocation = new HashMap<Location, Integer>();
		int i = 0;
		while (i < candidateuser_list.size()) {
			String candidateuserid = candidateuser_list.get(i).userid;
			List<Location> candidatelocationlist = user_location.get(candidateuserid);
			int j = 0;
			while (j < candidatelocationlist.size()) {
				Location candidatelocation = candidatelocationlist.get(j);
				if (!checkinlocation.contains(candidatelocation))
					candidateLocation.put(candidatelocationlist.get(j), 1);
				j++;
			}
			i++;
		}
		long time3 = System.currentTimeMillis();
		System.out.println("hits candidate locations cost : " + (time3 - time2) + "ms");
		Set<Entry<Location, Integer>> entry = candidateLocation.entrySet();
		List<Location> finallocation = new ArrayList<Location>();
		for (Entry<Location, Integer> e : entry) {
			finallocation.add(e.getKey());
		}
		return finallocation;
	}

	public List<Location> finalRecommendLocation(String userid, int hour) throws Exception {
		List<Location> recommendLocation = recommendLocation(userid, hour);
		// 得到前10个推荐位置
		List<Location> recomendLocationScore = caculateScoreAndGetTop(recommendLocation, userid, 10);
		return recomendLocationScore;
	}
}
