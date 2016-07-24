package TimeModel;

import java.util.List;

public class TimeProbability {
      private String category;
      private  List<Internal>  intern ;
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public List<Internal> getIntern() {
		return intern;
	}
	public void setIntern(List<Internal> intern) {
		this.intern = intern;
	}
     public String toString(){
    	        return "category:"+category+"intern:"+intern;
     }
        
}
class Internal{
	 public  int time;
	 public  double prob;
	 public String toString(){
		 return "time:"+time+"prob:"+prob;
	 }
}
