import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;


public class PrimeCounter{
	
	public static void main(String[] args) {
		int to = 4_999_999;

		int result = 0;
		// for(int i = 0; i <= to; i++){
		// 	result += countFactors(i);
		// }
		// System.out.println(result);

		countFactors(to, 10);
	}

	public static int countFactors(int p) {
		if (p < 2)
			return 0;
		int factorCount = 1, k = 2;
		while (p >= k * k) {
			if (p % k == 0) {
				factorCount++;
				p /= k;
			} else
			k++;
		}
		return factorCount;
	}

	public static void countFactors(int total, int numThreads){
		final AtomicInteger result = new AtomicInteger();
		Thread[] threads = new Thread[numThreads];
		int count = total / numThreads;
		

		for(int i = 0; i < numThreads; i++){
			final int from, to;
			from = count * i;
			to = (i+1==numThreads) ? (total+1) : count * (i+1); 
			threads[i] = new Thread( () -> 
			{
				for(int j = from; j<to; j++){
					result.addAndGet(countFactors(j)); 
				}
			}

			);
		}

		for(Thread t : threads){
			t.start();
		}
		for(Thread t: threads){
			try{
				t.join();
			}catch(Exception e)
			{
				//Nope..
			}
		}

		System.out.println(result.get());
	}
}