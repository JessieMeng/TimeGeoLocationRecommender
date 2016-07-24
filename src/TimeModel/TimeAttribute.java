package TimeModel;

public class TimeAttribute  {

       private String time;
       private int weight;
       private String category;
       private double pweight;
	  public double getPweight() {
		return pweight;
	}
	public void setPweight(double pweight) {
		this.pweight = pweight;
	}
	public String getTime() {
		return time;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public int getWeight() {
		return weight;
	}
	public void setWeight(int weight) {
		this.weight = weight;
	}
    public boolean equals(TimeAttribute ta){
    	          if(this.category.equals(ta.category)&&this.time.equals(ta.time)){
    	        	      return true;
    	          }
    	          return false;
    }
   public String toString(){
	   return "category :"+ category +"time:"+time+"weight:"+weight+"pweight:"+pweight;
   }
    
}
