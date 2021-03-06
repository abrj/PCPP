// For week 10
// sestoft@itu.dk * 2014-11-05

// NOT TO BE HANDED OUT, CONTAINS SOLUTIONS FOR WEEK 10

// Compile and run like this:
//   javac -cp multiverse-core-0.7.0.jar TestStmHistogram.java
//   java -cp multiverse-core-0.7.0.jar:. TestStmHistogram

// For the Multiverse library:
import org.multiverse.api.references.*;
import static org.multiverse.api.StmUtils.*;

// Multiverse locking:
import org.multiverse.api.LockMode;
import org.multiverse.api.Txn;
import org.multiverse.api.callables.TxnVoidCallable;

import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.CyclicBarrier;

class TestStmHistogram {
  public static void main(String[] args) {
    countPrimeFactorsWithStmHistogram();
  }

  private static void countPrimeFactorsWithStmHistogram() {
    final Histogram histogram = new StmHistogram(30);
    final Histogram total = new StmHistogram(30);
    final int range = 4_000_000;
    final int threadCount = 10, perThread = range / threadCount;
    final CyclicBarrier startBarrier = new CyclicBarrier(threadCount + 1), 
      stopBarrier = startBarrier;
    Timer timer = new Timer();
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
        //Ex 10.2.6
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
    System.out.println("TIME: " + timer.check());
    dump(histogram);
    dump(total);

    //Ex 10.2.7
    System.out.println("EX 10.2.7");
    total.transferBins(total);
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

interface Histogram {
  void increment(int bin);
  int getCount(int bin);
  int getSpan();
  int[] getBins();
  int getAndClear(int bin);
  void transferBins(Histogram hist);
}

class StmHistogram implements Histogram {
  private final TxnInteger[] counts;

  public StmHistogram(int span) {
    this.counts = new TxnInteger[span];
    for(int i = 0; i<counts.length; i++){
      //instanciating the values in counts
      counts[i] = newTxnInteger();
    }
  }
  //Ex 10.2.1 
  public void increment(int bin) {
    atomic(new Runnable() { public void run(){
      counts[bin].increment();
    }});
  }

  //Ex 10.2.1
  public int getCount(int bin) {
    return atomic(new Callable<Integer>() {public Integer call(){
      return counts[bin].get();
    }});
  }

  //Ex 10.2.1
  public int getSpan() {
      return counts.length;
  }

  //10.2.3
  public int[] getBins() {
    int[] ar = new int[getSpan()];
    for(int i = 0; i < ar.length; i++){
      ar[i] = getCount(i);
    }
    return ar;
  }

  //10.2.4
  public int getAndClear(int bin) {
    return atomic(new Callable<Integer>() {public Integer call(){
      int res = counts[bin].get();
      counts[bin].set(0);
      return res;
    }});
  }

  //10.2.5
  public void transferBins(Histogram hist) {
    for(int i = 0 ; i<hist.getSpan(); i++){
      final int j = i;
      atomic(new Runnable() {public void run(){
        counts[j].set(hist.getAndClear(j));
      }});
    }
  }
}

