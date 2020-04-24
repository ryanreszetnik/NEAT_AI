package application;

public class Counter {

	int count = 0;

	public Counter() {

	}

	public Counter(int start) {
		this.count = start;
	}

	public int addToCount() {
		count++;
		return count;
	}

	public int getCount() {
		return count;
	}

}
