package HitsAlgorithmAllTime;

public class User implements Comparable<User> {
	public String userid;
	public float value;

	public int compareTo(User u) {
		if (this.value > u.value)
			return -1;
		if (this.value < u.value)
			return 1;
		return 0;
	}

	public String toString() {
		return "userid :  " + userid + "value  : " + value;
	}
}
