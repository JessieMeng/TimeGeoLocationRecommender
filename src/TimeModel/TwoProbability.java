package TimeModel;

import java.util.Map;

public class TwoProbability {
        String category;
        Map<String,Double> tipo;
        public boolean equals(TwoProbability tp){
        	     if(tp.category.equals(this.category) ){
        	    	         return true;
        	     }
        	       return false;
        }
}
