package Geographical;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import DataReader.Location;
import DataReader.TrainDataLoader;


public class GeoAlgo {

	public static void main(String[] args) throws Exception  {
	     //new GeoAlgo().Compute();
		new Geodata().distanceMinusDistribute();
	}
	//do some 
	public void init(){
		
	}
	public void  Compute() throws Exception{
		long  before = System.currentTimeMillis();
		Map<String,List<Location>>   user_location_test = new TrainDataLoader().getUserLocTestmap();
		int hour = 8 ;
		int hit  = 0;
		int presionsum = 0;
		int  recallsum = 0;
		String userid = "1446746";
		Geodata geodata = new Geodata();
		List<Location> locationscorelist = geodata.finalRecommendLocation(userid, hour);
		List<Location>   testlocationlist = user_location_test.get(userid);
		System.out.println("rec size:"+locationscorelist.size());
		System.out.println("test size:"+testlocationlist.size());
		   int i = 0;
		   while( i < locationscorelist.size()){
			    if(testlocationlist.contains(locationscorelist.get(i)))
		                     hit = hit+1;
		         i++; 
		   }
         presionsum = presionsum + locationscorelist.size();
         recallsum = recallsum + testlocationlist.size();
  	/*
         Set<Entry<String,List<Location>>>  user_location_entry = user_location.entrySet();
		for(Entry<String,List<Location>>  e:user_location_entry){
			   String userid = e.getKey();
			   Geodata geodata = new Geodata();
			   List<Location>  locationscorelist = geodata.finalRecommendLocation(userid, hour);
			   List<Location>  testlocationlist = user_location_test.get(userid);
			   int i = 0;
			   while( i < locationscorelist.size()){
				    if(testlocationlist.contains(locationscorelist.get(i)))
			                     hit = hit+1;
			         i++; 
			   }
              presionsum = presionsum + locationscorelist.size();
               recallsum = recallsum + testlocationlist.size();
	  }*/
		double precision = hit /(1.0*presionsum);
        double  recall = hit/(1.0*recallsum);
        System.out.println("precision:"+precision);
        System.out.println("recall:"+recall);
		long after = System.currentTimeMillis();
		System.out.println(after-before+"ms");
	}

}
