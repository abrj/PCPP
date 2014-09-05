public class MyAtomicInteger{

	private int value = 0;

	public synchronized int addAndGet(int amount){
		value += amount;
		return value;
	}

	public synchronized int get(){
		return value;
	}

}