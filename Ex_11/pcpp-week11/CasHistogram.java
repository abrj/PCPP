import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.Callable;
import java.util.concurrent.CyclicBarrier;

//Ex 11.1
public class CasHistogram implements Histogram {
  private final AtomicInteger[] counts;

  public CasHistogram(int span) {
    this.counts = new AtomicInteger[span];
    for(int i = 0; i<counts.length; i++){
      //instanciating the values in counts
      counts[i] = new AtomicInteger(0);
    }
  }
  //Ex 11.1.1
  public void increment(int bin) {
     int oldValue, newValue;
     do {
		 oldValue = counts[bin].get();
		 newValue = oldValue + 1;
	 } while (!counts[bin].compareAndSet(oldValue, newValue));
  }

  public int getCount(int bin) {
    return counts[bin].get();
  }

  public int getSpan() {
      return counts.length;
  }

  //Ex 11.1.1
  public int[] getBins() {
    int[] ar = new int[getSpan()];
    for(int i = 0; i < ar.length; i++){
      ar[i] = getCount(i);
    }
    return ar;
  }

  //Ex 11.1.1
  public int getAndClear(int bin) {
  	int oldValue, newValue;
    do {
		oldValue = counts[bin].get();
		newValue = 0;
	} while (!counts[bin].compareAndSet(oldValue, newValue));
	return oldValue;
  }

  //helper method to transferBins()
  public void set(int bin, int newValue) {
  	int oldValue;
    do {
		oldValue = counts[bin].get();
	} while (!counts[bin].compareAndSet(oldValue, newValue));
  }

    //Ex 11.1.1
	public void transferBins(Histogram hist) {
    for(int i = 0 ; i<hist.getSpan(); i++){
      set(counts[i].get(), getAndClear(counts[i].get()));
    }
  }
}

interface Histogram {
  void increment(int bin);
  int getCount(int bin);
  int getSpan();
  int[] getBins();
  int getAndClear(int bin);
  void transferBins(Histogram hist);
}

class TestCasHistogram {
  public static void main(String[] args) {
    countPrimeFactorsWithCasHistogram();
  }

  private static void countPrimeFactorsWithCasHistogram() {
    final Histogram histogram = new CasHistogram(30);
    final Histogram total = new CasHistogram(30);
    final int range = 4_000_000;
    final int threadCount = 10, perThread = range / threadCount;
    final CyclicBarrier startBarrier = new CyclicBarrier(threadCount + 1), 
      stopBarrier = startBarrier;
    Timer timer = new Timer();	    //Ex 11.1.3
    final Thread[] threads = new Thread[threadCount];
    for (int t=0; t<threadCount; t++) {
      final int from = perThread * t, 
                  to = (t+1 == threadCount) ? range : perThread * (t+1); 
        threads[t] = 
          new Thread(new Runnable() {
              public void run() { 
                try { startBarrier.await(); } catch (Exception exn) { }
                for (int p=from; p<to; p++) 
                  histogram.increment(countFactors(p));
                System.out.print("*");
                try { stopBarrier.await(); } catch (Exception exn) { }
              }
            });
        threads[t].start();
        try{
          Thread.sleep(30);
        }
        catch (Exception e) {
          //should do something
        }
        total.transferBins(histogram);

    }
    try { startBarrier.await(); } catch (Exception exn) { }
    try { stopBarrier.await(); } catch (Exception exn) { }
    System.out.println("TIME: " + timer.check());	    //Ex 11.1.3
    dump(histogram);
    dump(total);

  }

  public static void dump(Histogram histogram) {
    int totalCount = 0;
    for (int bin=0; bin<histogram.getSpan(); bin++) {
      System.out.printf("%4d: %9d%n", bin, histogram.getCount(bin));
      totalCount += histogram.getCount(bin);
    }
    System.out.printf("      %9d%n", totalCount);
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
}