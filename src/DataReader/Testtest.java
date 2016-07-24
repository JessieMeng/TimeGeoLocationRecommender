package DataReader;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class Testtest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
	  try {
		Map<String,List<Location>>  user_location_test_map = new TrainDataLoader().getUserLocTestmap();
		Set<Entry<String,List<Location>>>  entry = user_location_test_map.entrySet();
		for(Entry<String,List<Location>> e : entry){
			 String  userid = e.getKey();
			 System.out.println("userid: "+userid);
			 List<Location>  locationlist = e.getValue();
			  int i = 0;
			  while(i < locationlist.size()){
				  System.out.println(locationlist.get(i));
				  i++;
			  }
		}
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	}

}
