// For week 6
// sestoft@itu.dk * 2014-09-29

// The Dining Philosophers problem, due to E.W. Dijkstra 1965.  Five
// philosophers (threads) sit at a round table on which there are five
// forks (shared resources), placed between the philosophers.  A
// philosopher alternatingly thinks and eats spaghetti.  To eat, the
// philosopher needs exclusive use of the two forks placed to his left
// and right, so he tries to lock them.  

// Both the places and the forks are numbered 0 to 5.  The fork to the
// left of place p has number p, and the fork to the right has number
// (p+1)%5.

// This solution is wrong; it will deadlock after a while.

import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.atomic.AtomicInteger;

public class TestPhilosophers {
  public static void main(String[] args) {
    Fork[] forks = { new Fork(), new Fork(), new Fork(), new Fork(), new Fork() };
    Philosopher[] phils = new Philosopher[forks.length];
    for (int place=0; place<forks.length; place++) {
      Philosopher p = new Philosopher(forks, place);
      phils[place] = p;
      Thread phil = new Thread(p);
      phil.start();
    }
    while(true){
      int i = 0;
      try{
        Thread.sleep(10000);
        for(Philosopher p : phils){
          System.out.println("phil: " + i++ + " has eaten" + p.ai);
        }
      }
      catch (Exception e) {
         // 
      }
    }
  }
}

class Philosopher implements Runnable {
  private final Fork[] forks;
  private final int place;
  public AtomicInteger ai = new AtomicInteger();


  public Philosopher(Fork[] forks, int place) {
    this.forks = forks;
    this.place = place;
  }

  //Ex 6.2.4
  public void run2() {
    while (true) {
      // Take the two forks to the left and the right
      int left = place, right = (place+1) % forks.length;
      if(left<right){
        synchronized(forks[left]){
          synchronized(forks[right]){
            // Eat
            System.out.print(place + " ");
            // ai.getAndIncrement();
          }
        }
      }

      else if(left>right){
        synchronized(forks[right]){
          synchronized(forks[left]){
            // Eat
            System.out.print(place + " ");
            // ai.getAndIncrement();
            
          }
        }
      }
      // Think
      try { Thread.sleep(10); }
      catch (InterruptedException exn) { }
    }
  }

  //Ex 6.2.5
  public void run() {
    while (true) {
      // Take the two forks to the left and the right
      int left = place, right = (place+1) % forks.length;
          if(forks[left].tryLock()){
            try{
              if(forks[right].tryLock()){
                try{
                  // Eat
                  // System.out.print(place + " ");
                  ai.getAndIncrement();

                }
                finally{
                  forks[right].unlock();
                }
              }
            }
            finally{
              forks[left].unlock();
            }
          }

      // Think
      try { Thread.sleep(10); }
      catch (InterruptedException exn) { }
    }
  }
}
class Fork extends ReentrantLock { }

