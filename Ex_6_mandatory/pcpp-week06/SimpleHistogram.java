// For week 3
// sestoft@itu.dk * 2014-09-04

import javax.annotation.concurrent.GuardedBy;

class SimpleHistogram {
  public static void main(String[] args) {
    //final Histogram histogram = new Histogram2(30);
    final HistogramMort histogram = new HistogramMort(30);

    histogram.increment(7);
    histogram.increment(13);
    histogram.increment(7);
    //dump(histogram);
  }

  public static void dump(Histogram histogram) {
    int totalCount = 0;
    for (int item=0; item<histogram.getSpan(); item++) {
      System.out.printf("%4d: %9d%n", item, histogram.getCount(item));
      totalCount += histogram.getCount(item);
    }
    System.out.printf("      %9d%n", totalCount);
  }
}

interface Histogram {
  public void increment(int item);
  public int getCount(int item);
  public int getSpan();
  public void addAll(Histogram hist);
}

class Histogram1 implements Histogram {
  private int[] counts;
  public Histogram1(int span) {
    this.counts = new int[span];
  }
  public void increment(int item) {
    counts[item] = counts[item] + 1;
  }

  public int getCount(int item) {
    return counts[item];
  }
  public int getSpan() {
    return counts.length;
  }

  public void addAll(Histogram hist){
    //Not implemented
  }

}

class Histogram2 implements Histogram {
  @GuardedBy("this")
  private int[] counts;
  public Histogram2(int span) {
    this.counts = new int[span];
  }
  public synchronized void increment(int item) {
    counts[item] = counts[item] + 1;
  }

  public synchronized int getCount(int item) {
    return counts[item];
  }
  public synchronized int getSpan() {
    return counts.length;
  }

  public void addAll(Histogram hist){
    synchronized(this){
      synchronized(hist){
        if(this.getSpan() == hist.getSpan()){
          Histogram combinedHist = new Histogram2(this.getSpan() + hist.getSpan());
          int counter = 0;
          for(int i = 0; i < this.getSpan(); i++){
            this.counts[i] += hist.getCount(i);
          }
        } 
        else{
          throw new RuntimeException();
        }
      }
    }
  }
}



class HistogramMort {
  @GuardedBy("this")
  public int[] counts;
  public HistogramMort(int span) {
    this.counts = new int[span];
  }
  public synchronized void increment(int item) {
    counts[item] = counts[item] + 1;
  }

  public synchronized int getCount(int item) {
    return counts[item];
  }
  public synchronized int getSpan() {
    return counts.length;
  }

  public void addAll(HistogramMort hist){
    synchronized(this){
      synchronized(hist){
        if(this.getSpan() == hist.getSpan()){
          HistogramMort combinedHist = new HistogramMort(this.getSpan() + hist.getSpan());
          int counter = 0;
          for(int i = 0; i < this.getSpan(); i++){
            this.counts[i] += hist.counts[i];
          }
        } 
        else{
          throw new RuntimeException();
        }
      }
    }
  }
}
