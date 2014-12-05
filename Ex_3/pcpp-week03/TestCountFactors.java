// For week 2
// sestoft@itu.dk * 2014-08-29

class TestCountFactors {
  public static void main(String[] args) {
    final int range = 5_000_000;
    final int threadCount = 10;
    //30 will magically suffice for this assignemnt
    final Histogram histogram = new Histogram4(30);
    final int perThread = range / threadCount;
    Thread[] threads = new Thread[threadCount];
    for (int t=0; t<threadCount; t++) {
      final int from = perThread * t, to = (t+1==threadCount) ? range : perThread * (t+1); 
      threads[t] = new Thread(new Runnable() { public void run() {
      for (int i=from; i<to; i++){
        int result = countFactors(i);
        histogram.increment(result);
      }    
    }});

    }

    for (int t=0; t<threadCount; t++) 
      threads[t].start();
    try {
      for (int t=0; t<threadCount; t++) 
      threads[t].join();
      } 
    catch (InterruptedException exn) { }
    SimpleHistogram.dump(histogram);
    System.out.println("Hej");
    int[] result = histogram.getBucketCounts();
    for(int i = 0; i < histogram.getSpan(); i++){
      System.out.println("Histogram " + i + " Factors: " + histogram.getCount(i));
      System.out.println("Result: " + result[i]);
    }


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
