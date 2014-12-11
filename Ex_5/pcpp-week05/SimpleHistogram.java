// For week 3
// sestoft@itu.dk * 2014-09-04

import java.util.*;
import java.util.concurrent.atomic.*;

class SimpleHistogram {
  public static void main(String[] args) {
    final Histogram histogram = new Histogram1(30);
    histogram.increment(7);
    histogram.increment(13);
    histogram.increment(7);
    dump(histogram);
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
  public int[] getBucketCounts();
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

  public int[] getBucketCounts(){
    return counts;
  }
}

class Histogram2 implements Histogram {
  private final int[] counts;
  public Histogram2(int span) {
    this.counts = new int[span];
  }
  public synchronized void increment(int item) {
    counts[item] = counts[item] + 1;
  }

  public synchronized int getCount(int item) {
    return counts[item];
  }
  public int getSpan() {
    return counts.length;
  }
  public int[] getBucketCounts(){
    return counts; 
  }
}

class Histogram3 implements Histogram {
  private final AtomicInteger[] counts;
  public Histogram3(int span) {
    this.counts = new AtomicInteger[span];
    for(int i = 0; i < span; i++){
      this.counts[i] = new AtomicInteger(0);
    }
  }
  public void increment(int item) {
    counts[item].getAndIncrement();
  }

  public int getCount(int item) {
    return counts[item].intValue();
  }
  public int getSpan() {
    return counts.length;
  }
  public int[] getBucketCounts(){
    int[] tmp = new int[counts.length];
    for(int i = 0; i < getSpan() ; i++){
      tmp[i] = getCount(i);
    }
    return tmp;
  }
}

class Histogram4 implements Histogram {
  private final AtomicIntegerArray counts;
  public Histogram4(int span) {
    this.counts = new AtomicIntegerArray(span);
  }
  public void increment(int item) {
    counts.getAndIncrement(item);
  }

  public int getCount(int item) {
    return counts.get(item);
  }
  public int getSpan() {
    return counts.length();
  }
  public int[] getBucketCounts(){
    int[] tmp = new int[counts.length()];
    for(int i = 0; i < getSpan() ; i++){
      tmp[i] = getCount(i);
    }
    return tmp;
  }
}