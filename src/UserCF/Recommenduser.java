package UserCF;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class Recommenduser {
   
   //处理文件
  public  Map<String,List<String>> dataprocess(File file) throws Exception{
	    Map<String,List<String>>  location_user = new HashMap<String,List<String>>();
	    BufferedReader br = new BufferedReader(new FileReader(file));
	    String line ;
	    while((line = br.readLine())!=null){
	    	      String[] arr = line.split("\t");
	    	      String venueid = arr[1];
	    	      String userid = arr[0];
	    	      if(location_user.containsKey(venueid)){
	    	    	            List<String>   list = location_user.get(venueid);
	    	    	            list.add(userid);
	    	    	            location_user.put(venueid, list);
	    	      }else{
	    	    	        List<String>  list = new ArrayList<String>();
	    	    	        list.add(userid);
	    	    	        location_user.put(venueid, list);
	    	      }
	    }
	    br.close();
	    return location_user;
  }
  //user－user 共同的location个数
  public Map<String,Map<String,Integer>> userToUser(Map<String,List<String>>  location_user){
	  Map<String,Map<String,Integer>>  user_user = new HashMap<String,Map<String,Integer>>();
	  Set<Entry<String,List<String>>>  luentry = location_user.entrySet();
	  for(Entry<String,List<String>>  e :luentry){
		  List<String>  userlist = e.getValue();
		  int i = 0;
		  while(i < userlist.size()){
			  String userid = userlist.get(i);
			  if(!user_user.containsKey(userid)){
				  Map<String,Integer>  userid2 = new HashMap<String,Integer>();
				  user_user.put(userid, userid2);
  			  }
			  Map<String,Integer>  tempuserid  = user_user.get(userid);
			  int j = i+1;
			  while(j < userlist.size()){
				  String secondid =  userlist.get(j);
				  if(!tempuserid.containsKey(secondid)){
					  tempuserid.put(secondid, 1);
				  }else{
					  tempuserid.put(secondid, tempuserid.get(secondid)+1);
				  }
				  j++;
			  }
			  i++;
		  }
	  }
	  return user_user;
  }
   //每个user的location个数
  public  Map<String,Integer>  locationNum(Map<String,List<String>>  location_user){
	  Set<Entry<String,List<String>>>  luentry = location_user.entrySet();
	  Map<String,Integer>   locationnum =  new HashMap<String,Integer>();
	  for(Entry<String,List<String>>  e:luentry){
		        List<String>  useridlist = e.getValue();
		        int i = 0;
		        while(i < useridlist.size()){
		        	     String userid = useridlist.get(i);
		        	     if(locationnum.containsKey(userid)){
		        	    	            locationnum.put(userid, locationnum.get(userid)+1);
		        	     }
		        	     else{
		        	    	        locationnum.put(userid, 1);
		        	     }
		        	     i++;
		        }
	  }
	   return locationnum;
  }
  //计算相似度
  public Map<String,Map<String,Double>> comSimilarity(Map<String,Map<String,Integer>> user_user,Map<String,Integer> locationnum){
	             Map<String,Map<String,Double>> similarity = new HashMap<String,Map<String,Double>>();
	             Set<Entry<String,Map<String,Integer>>>  uuentry = user_user.entrySet();
	             for(Entry<String,Map<String,Integer>>  uue : uuentry){
	            	           String userid1 = uue.getKey();
	            	           Set<Entry<String,Integer>>  uentry = uue.getValue().entrySet();
	            	           Map<String,Double>   usmap = new HashMap<String,Double>();
	            	           for(Entry<String,Integer>  ue:uentry){
	            	        	             String userid2 = ue.getKey();
	            	        	             int commonnum = ue.getValue();
	            	        	             double sim = (double)commonnum/(double) Math.sqrt(locationnum.get(userid1) * locationnum.get(userid2));
	            	        	             usmap.put(userid2, sim);
	            	           }
	            	           similarity.put(userid1, usmap);
	             }
	             return similarity;
  }
  // 获得每个用户的签到位置
  public Map<String,List<String>>  getUserLocation(Map<String,List<String>>  location_user){
	  Set<Entry<String,List<String>>>  luentry = location_user.entrySet();
	  Map<String,List<String>>   user_location = new HashMap<String,List<String>>();
	  for(Entry<String,List<String>>  e: luentry){
		        String venueid = e.getKey();
		        List<String>  userlist = e.getValue();
		        int i = 0;
		        while(i < userlist.size()){
		        	         String userid = userlist.get(i);
		        	         if(user_location.containsKey(userid)){
		        	        	      List<String> locationlist =  user_location.get(userid);
		        	        	      locationlist.add(venueid);
		        	         }
		        	         else{
		        	        	      List<String>  locationlist = new ArrayList<String>();
		        	        	      locationlist.add(venueid);
  		        	        	        user_location.put(userid, locationlist);
		        	         }
		        	         i++;
		        }
	  }
	  return user_location;
  }
  // 获得候选用户的候选位置
  public  Map<String,List<String>>  getRecommendLocation(Map<String,List<String>> user_location,
		  Map<String,Map<String,Double>>  user_user,String userid){
	      Map<String,Double>  candidateuser = user_user.get(userid);
	      List<String>   checklocation = user_location.get(userid);
	      Map<String,List<String>>   user_reclocation = new HashMap<String,List<String>>();
	      List<String>  reclocation = new ArrayList<String>();
	      Set<Entry<String,Double>>  entry = candidateuser.entrySet();
	      for(Entry<String,Double>  e:entry){
	    	           String cuser = e.getKey();
	    	           List<String>  candidatelocation = user_location.get(cuser);
	    	           int  i = 0;
	    	           while( i < candidatelocation.size()){
	    	        	           String venueid =   candidatelocation.get(i);
	    	        	           if(!checklocation.contains(venueid))
	    	        	           {
	    	        	        	           reclocation.add(venueid);
	    	        	           }
	    	        	             i++;
	    	           }
	      }
	      user_reclocation.put(userid, reclocation);
	      return  user_reclocation;
  }

}