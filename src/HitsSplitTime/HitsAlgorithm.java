package HitsSplitTime;

import java.util.ArrayList;
import java.util.List;


public class HitsAlgorithm {

//	public static void main(String[] args){
//		 try {
//			new HitsAlgorithm().caculateUserList(8);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
	public List<User> caculateUserList(int hour) throws Exception {
				Graph g = new Graph();
		//		g.showUsermap(hour);
		//		g.showLocationMap(hour);
			    List<User>  userlist = g.hits(hour);
				List<User>  newuserlist  = new ArrayList<User>();
				int i = 0;
				// 待推荐用户先设为5000个
				while (i < 5000) {
					newuserlist.add(userlist.get(i));
					i++;
				}
    return newuserlist;
	}

}

