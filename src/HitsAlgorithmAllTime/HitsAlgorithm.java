package HitsAlgorithmAllTime;


import java.util.ArrayList;
import java.util.List;


public class HitsAlgorithm {
	public static void main(String[] args){
		 try {
			new HitsAlgorithm().caculateUserList();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public List<User> caculateUserList() throws Exception {
		Graph g = new Graph();
	    List<User>  userlist = g.hits();
		List<User> newuserlist = new ArrayList<User>();
		int i = 0;
		// 选出5000个候选用户
		while (i < 5000) {
			newuserlist.add(userlist.get(i));
			System.out.println(userlist.get(i));
			i++;
		}
   return newuserlist;
	}
	
}
