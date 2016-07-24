package DataReader;



public class Location implements Comparable<Location>{
	     public  String venueid;
	      public  String  latitude;
	      public   String longtitude;
	      public int  count;
	      public String category;
	      public  String hour_time ;
	      public double score;
	      public boolean equals(Location lo){
	    	   if(this.venueid.equals(lo.venueid)){
	    	    	   return true;
	    	     }
	    	     return false;
	     }
	      public String toString(){
	    	     return "venueid : "+venueid+" latitude : "+latitude+" longtitude: "+longtitude
	    	    		 +" count : "+count+" category: "+category+" hour_time: "+hour_time+"score: "+score;
	      }
	  	public int compareTo(Location ls) {
			if(this.score > ls.score){
				 return -1;
			}
	       if(this.score < ls.score){
	    	       return 1;
	       }
			return 0;
		}

      
}
