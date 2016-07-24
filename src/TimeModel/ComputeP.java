package TimeModel;
 
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import HitsAlgorithmAllTime.User;

public class ComputeP {

    public Map<String,List<TwoProbability>>  candidateUser(Map<String,Float> usermap,
    		Map<String,List<TwoProbability>>  tmap){
    	   Map<String,List<TwoProbability>>  candidatemap = new HashMap<String,List<TwoProbability>>();
    	       Set<Entry<String,Float>> uuentry = usermap.entrySet();
    	       for(Entry<String,Float>  ue:uuentry){
    	    	            String  seconduser = ue.getKey();
    	    	           List<TwoProbability>  slist =  tmap.get(seconduser);
    	    	           candidatemap.put(seconduser, slist);
    	       }
    	       return candidatemap;
    }
    public Map<String,Float>  change(List<User> userlist){
    	            	Map<String,Float>  usermap = new HashMap<String,Float>();
    	            	int i = 0;
    	            	while(i<userlist.size()){
    	            		String userid = userlist.get(i).userid;
    	            		float value = userlist.get(i).value;
    	            		usermap.put(userid, value);
    	            		i++;
    	            	}
    	            	return usermap;
    }
	public void showMp(Map<String,Map<String,Double>>  mp){
		Set<Entry<String,Map<String,Double>>>   entry = mp.entrySet();
		for(Entry<String,Map<String,Double>> e:entry){
			    String userid1 = e.getKey();
			    Map<String,Double>  in = e.getValue();
			    System.out.println("userid1 : "+ userid1);
			    Set<Entry<String,Double>>  entry1 = in.entrySet();
			    for(Entry<String,Double>  ee:entry1){
			    	     String userid2 = ee.getKey();
			    	     double prob = ee.getValue();
			    	     System.out.println("userid2: "+userid2+"sim: "+prob );
			    }
		}
	}
	 public Map<String,Map<String,Double>> computeSim(Map<String,List<TwoProbability>>  targetuser,
			 Map<String,List<TwoProbability>>  tmap){
		      Map<String,Map<String,Double>>  mp = new HashMap<String,Map<String,Double>>();
		      Set<Entry<String,List<TwoProbability>>>   entry  = tmap.entrySet();
//		      Set<Entry<String,List<TwoProbability>>> tentry = targetuser.entrySet();
		      for(Entry<String,List<TwoProbability>>  te :entry){
		    	           String targetid = te.getKey();
		    	           List<TwoProbability>   list1 = te.getValue();
		    	           Map<String,Double>  newmap = new HashMap<String,Double>();
		    	           for(Entry<String,List<TwoProbability>> e :entry){
			    	              String userid1 = e.getKey();   
			    	              List<TwoProbability>   list2= e.getValue();
			    	              int i = 0;
	    	            	          int j = 0;
	    	            	        double simisum  = 0;
	    	            	        while(i<list1.size()){
	    	            	        	         TwoProbability tp1 = list1.get(i);
	    	            	        	         TwoProbability tp3 = null ;
	    	            	        	         while( j < list2.size()){
	    	            	        	        	     TwoProbability tp2 = list2.get(j);
	    	            	        	        	     if(tp1.equals(tp2)){
	    	            	        	        	    	   tp3 = tp2;
	    	            	        	        	    	   break;
	    	            	        	        	     }
	    	            	        	        	      j++;
	    	            	        	         }
	    	            	        	       simisum+=sim(tp1,tp3);
	    	            	        	        i++;
	    	            	        }
	    	            	        double similarity = 1/(simisum+1);
	    	            	        newmap.put(userid1, similarity);
		          }
		    	      mp.put(targetid, newmap);
		    	           
		      }    
		       return mp;
	 }
	 public double sim(TwoProbability tp1,TwoProbability tp2){
		  if(tp2 == null){
			   return 0;
		  }
		  Map<String,Double>   map1 = tp1.tipo;
		  Map<String,Double>   map2 = tp2.tipo;
		  
		  double[][] arr = new double[24][24];
		  double[] arr1 = new double[24];
		  double[] arr2 = new double[24];
		  double[][]  f = new double[24][24];
		  for(int i = 0; i<24;i++){
			  arr1[i] = map1.get(String.valueOf(i+1));
			  arr2[i] = map2.get(String.valueOf(i+1));
		  }
		  
          for(int i = 0 ; i <24 ;i++){
        	        for(int j = 0; j< 24; j++){
        	        	     arr[i][j] = Math.abs(arr1[i]-arr2[j]);
        	        }
          }
          f[0][0] = 2.0*arr[0][0];
          for(int i = 1 ;i<24;i++){
        	        f[0][i]=f[0][0]+arr[0][i];
          }
          for(int j = 1;j<24;j++){
        	      f[j][0] = f[0][0]+arr[j][0];
          }
          for(int i = 1;i <24;i++){
        	           for(int j = 1;j <24;j++){
        	        	          f[i][j] = Math.min(f[i-1][j]+arr[i][j], Math.min(f[i-1][j-1]+2*arr[i][j],f[i][j-1]+arr[i][j]));
        	           }
          }
          return f[23][23];
	 }
	public void showTmap(Map<String,List<TwoProbability>> tmap){
		     Set<Entry<String,List<TwoProbability>>>  m = tmap.entrySet();
		     for(Entry<String,List<TwoProbability>>  e: m){
		    	          String userid = e.getKey();
		    	          List<TwoProbability> list = e.getValue();
		    	          System.out.println("userid:  "+userid);
		    	         int i = 0;
		    	         while(i<list.size()){
		    	            	 TwoProbability tp = list.get(i);
		    	            	 String category = tp.category;
		    	            	 System.out.println("category:  "+category);
		    	            	 Map<String,Double>  map = tp.tipo;
		    	            	 Set<Entry<String,Double>>  entry = map.entrySet();
		    	            	 for(Entry<String,Double> ee :entry){
		    	            		     String time = ee.getKey();
		    	            		     double prob = ee.getValue();
		    	            		     System.out.println("time: "+time+"prob:"+prob);
		    	            	 }
		    	        	     i++;
		    	         }
		     }
	}
	public void showCP(Map<String,Map<String,Double>> map){
		   Set<Entry<String,Map<String,Double>>>  entry = map.entrySet();
		   for(Entry<String,Map<String,Double>> e:entry){
			String userid = e.getKey();
			Map<String,Double> hmmap = e.getValue();
			Set<Entry<String,Double>> entry1 = hmmap.entrySet();
 			for(Entry<String,Double>  ee:entry1){
 				    String categorytime = ee.getKey();
 				    Double prob = ee.getValue();
 				    System.out.println("userid:"+userid + "categorytime:"+categorytime+"prob:"+prob);
 			}
		   }
	}
	public Map<String,List<TwoProbability>> secondProcess(Map<String,Map<String,Double>>  hm){
		 Map<String,List<TwoProbability>>  mp = new HashMap<String,List<TwoProbability>>();
		Set<Entry<String,Map<String,Double>>>   entry = hm.entrySet();
		for(Entry<String,Map<String,Double>>  e:entry){
			  String userid = e.getKey();
			  Map<String,Map<String,Double>>   mmp = new HashMap<String,Map<String,Double>>();
			  Set<Entry<String,Double>>  entry1 = e.getValue().entrySet();
			  for(Entry<String,Double> ee:entry1){
				       String line =  ee.getKey();
				       String[] arr = line.split("\t");
				        String category = arr[0];
				        String time = arr[1];
				        double prob = ee.getValue();
				        if(mmp.containsKey(category)){
				             Map<String,Double> hhh =  mmp.get(category);
				             hhh.put(time, prob);
				        }
				        else{
				              	Map<String,Double>  mmmp = new HashMap<String,Double>();
				              	mmmp.put(time,prob);
				        	       mmp.put(category,mmmp);
				        }
			  }
              List<TwoProbability> ll =  Traverse(mmp);   
              mp.put(userid, ll);
		}
		 return mp;
	}
	 public List<TwoProbability> Traverse(Map<String,Map<String,Double>> hm){
		   List<TwoProbability> list = new ArrayList<TwoProbability>();
		   Set<Entry<String,Map<String,Double>>>  mp = hm.entrySet();
		   for(Entry<String,Map<String,Double>> e :mp){
			       String category = e.getKey();
			      Map<String,Double> mmp = e.getValue();
			      TwoProbability   tp =new TwoProbability();
			        tp.category = category;
			        tp.tipo = mmp;
			        list.add(tp);
		   }
		   return list;
	 }

    public Map<String,Map<String,Double>> firstProcess(Map<String,List<TimeProbability>> hm){
    	           Map<String,Map<String,Double>>   newhm =  new HashMap<String,Map<
    	        		    String,Double>>();
    	            Set<Entry<String,List<TimeProbability>>>  entry = hm.entrySet();
    	            for(Entry<String,List<TimeProbability>>  e: entry){
    	                  String userid = e.getKey();
    	                  List<TimeProbability>  list = e.getValue();
    	                 Map<String,Double>  secondhm = new HashMap<String,Double>();
    	                  int i = 0;
    	                  while(i < list.size()){
    	                	         TimeProbability tp = list.get(i);
    	                	         String category = tp.getCategory();
    	                	         List<Internal> intern = tp.getIntern();
    	                	         int j = 0;
    	                	         while( j < intern.size()){
    	                	        	          String time = String.valueOf(intern.get(j).time);
    	                	        	          double prob = intern.get(j).prob;
    	                	        	          String key = category+"\t"+time;
    	                	        	          if(secondhm.containsKey(key)){
    	                	        	        	         secondhm.put(key, secondhm.get(key)+prob);
    	                	        	          }else{
    	                	        	        	         secondhm.put(key, prob);
    	                	        	          }
    	                	        	          j++;
    	                	         }
    	                	        i++;
    	                  }
    	                  newhm.put(userid, secondhm);
    	            }
    	            return newhm;
    }
}
