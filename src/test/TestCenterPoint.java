package test;

import java.util.List;
import java.util.Map;

import DataReader.Location;
import DataReader.TrainDataLoader;

public class TestCenterPoint {


	public static void main(String[] args) {
		try {
			new TestCenterPoint().centerPoint();
			new TestCenterPoint().averagePoint();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
    public void centerPoint() throws Exception{
    	Map<String,List<Location>>  user_location = new TrainDataLoader().getUserLocMap();
    	List<Location>  loc = user_location.get("2511155");
        
    	double x = 0;
    	double y = 0;
    	double z = 0;
    	int i = 0;
    	while(i < loc.size()){
    		System.out.println("lat   lon:"+loc.get(i).latitude+"\t"+loc.get(i).longtitude);
    		double lat = Double.parseDouble(loc.get(i).latitude)*Math.PI/180;
    		double lon = Double.parseDouble(loc.get(i).longtitude)*Math.PI/180;
    		x+=Math.cos(lat)*Math.cos(lon);
    		y+=Math.cos(lat)*Math.sin(lon);
    		z+=Math.sin(lat);
    		i++;
    	}
    	x =x/loc.size();
    	y=y/loc.size();
    	z=z/loc.size();
    	Location centerPoint = new Location();
    	centerPoint.longtitude = String.valueOf(Math.atan2(y, x)*180/Math.PI);
    	centerPoint.latitude= String.valueOf(Math.atan2(z, Math.sqrt(x*x+y*y))*180/Math.PI);
    	System.out.println("center poiny average: "+ centerPoint.latitude +"\t"+centerPoint.longtitude );
    	///System.out.println("center point  jingweidu :"+ centerPoint);
    	
    }
    public void averagePoint() throws Exception{
        	Map<String,List<Location>>  user_location = new TrainDataLoader().getUserLocMap();
        	List<Location>  loc = user_location.get("2511155");

        	double x = 0;
        	double y = 0;
        	int i = 0;
        	while(i < loc.size()){
        		double lat = Double.parseDouble(loc.get(i).latitude);
        		double lon = Double.parseDouble(loc.get(i).longtitude);
        		x+=lat;
        		y+=lon;
        		i++;
        	}
        	x= x/loc.size();
        	y=y/loc.size();
        	System.out.println("center poiny average: "+ x+"\t"+y);
    }
}
