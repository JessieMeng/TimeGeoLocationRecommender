package TimeModel;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class VisitRecord {
    public Map<String,List<TimeProbability>>  getThirdprocess(){
    	           VisitRecord vr = new VisitRecord();
    	           Map<String,List<TimeAttribute>> hm = vr.getHM();
    	          Map<String,List<TimeAttribute>>  hhmm =  vr.conbineHM(hm);
    	           Map<String,List<TimeProbability>>   hmp = vr.cpuP(hhmm);
    	           //vr.showTP(hmp);
    	           return hmp;
    	           
    }
	public  void showTP(Map<String, List<TimeProbability>> hmp) {
		Set<Entry<String,List<TimeProbability>>>    entry  = hmp.entrySet();
		for(Entry<String,List<TimeProbability>>  ee : entry){
		          String userid = ee.getKey();
		          List<TimeProbability>   tplist = ee.getValue();
		          int i = 0;
		          while(i<tplist.size()){
		        	          System.out.println("userid:"+userid+tplist.get(i));
		        	       i++;
		          }
		}
		
	}
	public  Map<String,List<TimeAttribute>> getHM(){
	  Map<String,List<TimeAttribute>> hm = new HashMap<
			  String,List<TimeAttribute>>();
     try{
    	          BufferedReader br = new BufferedReader(new FileReader(new File("train.txt")));           
    	          String line = null;
    	          while((line = br.readLine())!= null){
    	        	          String[] arr = line.split("\t");
    	        	          String userid = arr[0];
    	        	          String category = arr[4];
    	        	          SimpleDateFormat  sdf = new SimpleDateFormat("HH");
    	        	 		 SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    	        	 		 Date date = sdf1.parse(arr[5]);
    	        	          String  time =  sdf.format(date);
    	        	          TimeAttribute ta = new TimeAttribute();
    	        	          ta.setTime(time);
    	        	          ta.setWeight(1);
    	        	          ta.setCategory(category);
    	        	          if(hm.containsKey(userid)){
    	        	        	         List<TimeAttribute>  value = hm.get(userid);
    	        	        	         value.add(ta);
    	        	          }
    	        	          else{
    	        	        	       List<TimeAttribute>  list = new ArrayList<TimeAttribute>();
    	        	        	       list.add(ta);
    	        	        	       hm.put(userid, list);
    	        	          }
    	        	         
    	            }
    	          br.close();
     }catch(Exception e){
    	     e.printStackTrace();
     }
     return hm;
  }

	public void showHM(Map<String,  List<TimeAttribute>> hm) {
		 Set<Entry<String,List<TimeAttribute>>>  entry = hm.entrySet();
         for(Entry<String,List<TimeAttribute>> e:entry){
        	          String user_id = e.getKey();
        	          List<TimeAttribute>  map = e.getValue();
        	          int i = 0;
        	          System.out.println("userid:"+user_id);
        	          while(i<map.size()){
        	              System.out.println(map.get(i));       
        	              i++;
        	          }
         }
		
	}
    public Map<String,List<TimeAttribute>> conbineHM(Map<String,List<TimeAttribute>> hm){
    	    Set<Entry<String,List<TimeAttribute>>>  entry = hm.entrySet();
    	    Map<String,List<TimeAttribute>>   newhm = new HashMap<String,
    	    		 List<TimeAttribute>>();
    	    for(Entry<String,List<TimeAttribute>> e:entry){
    	    	                String userid = e.getKey();
    	    	                List<TimeAttribute>  list =  e.getValue();
    	    	                Map<String,Integer> map = new HashMap<String,Integer>();
    	    	                int i = 0;
    	    	                int length = list.size();
    	    	                while(i<list.size()){
    	    	                	       String category =  list.get(i).getCategory();
    	    	                	       String  time = list.get(i).getTime();
    	    	                	       String newkey = category+"	"+time;
    	    	                	        if(map.containsKey(newkey)){
    	    	                	        	      map.put(newkey, map.get(newkey)+1);
    	    	                	        }
    	    	                	        else{
    	    	                	        	    map.put(newkey, 1);
    	    	                	        }
    	    	                	        i++;
    	    	                }
    	    	                List<TimeAttribute> newlist = new ArrayList<TimeAttribute>();
    	    	               Set<Entry<String,Integer>>  entry1 = map.entrySet();
    	    	               for(Entry<String,Integer> ee:entry1){
    	    	            	            String key = ee.getKey();
    	    	            	            String[] arr = key.split("\t");
    	    	            	            String category = arr[0];
    	    	            	            String time = arr[1];
    	    	            	            int  weight= ee.getValue();
    	    	            	            double pweight =(double)weight/length;
    	    	            	            TimeAttribute ta = new TimeAttribute();
    	    	            	            ta.setCategory(category);
    	    	            	            ta.setTime(time);
    	    	            	            ta.setWeight(weight);
    	    	            	            ta.setPweight(pweight);
    	    	            	            newlist.add(ta);
    	    	            	         
    	    	               }
    	    	               
    	    	               newhm.put(userid, newlist);
    	    }
    	    return  newhm;
    }
     public Map<String,List<TimeProbability>> cpuP(Map<String,List<TimeAttribute>>  hm){
    	 Set<Entry<String,List<TimeAttribute>>>  entry = hm.entrySet();
    	 Map<String,List<TimeProbability>> tphm = new HashMap<String,List<
    			 TimeProbability>>();
    	 for(Entry<String,List<TimeAttribute>> e:entry){
    		  String userid = e.getKey();
    		  List<TimeAttribute>  lista = e.getValue();
    		  List<TimeProbability>  listp = new ArrayList<TimeProbability>();
    		  int i  = 0;
    		  while( i < lista.size()){
    			      TimeAttribute  ta  =  lista.get(i);
    			      String category = ta.getCategory();
    			      String time = ta.getTime();
    			      double pweight = ta.getPweight();
    			      TimeProbability tp = new TimeProbability();
    			     List<Internal>  intern = new ArrayList<Internal>();
    			       for(int now = 1; now < 25;now++){
    			    	           int t =Integer.parseInt(time);
    			    	          double minust  =  (double)Math.abs(now-t);
    			    	          double ft = Math.exp(-minust);
    			    	          double wt = pweight*ft;
    			    	          Internal in = new Internal();
    			    	          in.time =  now;
    			    	          in.prob = wt;
    			    	          intern.add(in);
    			       }
    			       tp.setCategory(category);
    			       tp.setIntern(intern);
    			      listp.add(tp);
    			      i++;
    		  }
          tphm.put(userid, listp); 
    	    }
    	   return tphm;
     }
}
