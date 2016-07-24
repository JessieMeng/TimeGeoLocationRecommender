package TimeModel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import HitsAlgorithmAllTime.HitsAlgorithm;
import HitsAlgorithmAllTime.User;

public class Test {

	public static void main(String[] args) throws Exception {
		VisitRecord vr = new VisitRecord();
		Map<String, List<TimeProbability>> hm = vr.getThirdprocess();
		ComputeP cp = new ComputeP();
		Map<String, Map<String, Double>> map = cp.firstProcess(hm);
		Map<String, List<TwoProbability>> tmap = cp.secondProcess(map);
		List<User> userlist = new HitsAlgorithm().caculateUserList(8);
		Map<String, Float> usermap = cp.change(userlist);
		User user = new User();
		user.userid = "2465626";
		Map<String, List<TwoProbability>> targetuser = new HashMap<String, List<TwoProbability>>();
		List<TwoProbability> targetlist = tmap.get(user.userid);
		targetuser.put(user.userid, targetlist);
		Map<String, List<TwoProbability>> ttmap = cp.candidateUser(usermap, tmap);
		long t1 = System.currentTimeMillis();
		Map<String, Map<String, Double>> mmm = cp.computeSim(targetuser, ttmap);
		long t2 = System.currentTimeMillis();
		long minus = t2 - t1;
		cp.showMp(mmm);
		System.out.println(minus);
	}

}
