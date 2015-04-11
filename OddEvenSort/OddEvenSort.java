import java.util.*;

class Sorters implements Runnable {
  private Thread t;
  private String threadName;
  private boolean isEven;
  private int workOn;

  Sorters( String name, boolean isE, int w){
    threadName = name;
    isEven = isE;
    workOn = w;
  }
  public void run() {
    try {
      int min= Math.min(OddEvenSort.nums[workOn],OddEvenSort.nums[workOn+1]);
      int max= Math.max(OddEvenSort.nums[workOn],OddEvenSort.nums[workOn+1]);
      OddEvenSort.nums[workOn] = min;
      OddEvenSort.nums[workOn+1]=max;

      System.out.println(threadName + " : " + Arrays.toString(OddEvenSort.nums));
      Thread.sleep(1000);

    } catch (InterruptedException e) {
      System.out.println("Thread " +  threadName + " interrupted.");
    }
  }
  public void join () { 
    if(t != null) { 
      try {
        t.join(); 
      } catch(Exception e)  { 
        System.out.println("Exception occured : " + e.getMessage());
      }
    }
  }
  public void start () {
    t = new Thread (this, threadName);
    t.start();
  }
}

public class OddEvenSort {
  public static int n, even;
  public static int odd;
  public static int nums[];
  public static Sorters oddThreads[];
  public static Sorters evenThreads[];

  public static void waitForAll(boolean isEven) {
    int i = 0;
    if(!isEven){
      for(i=0;i<odd;i++) {
        oddThreads[i].join();  
      }
    } else {
      for(i=0;i<even;i++) {
        evenThreads[i].join(); 
      }
    }
  }
  public static void startAll(boolean isEven){
    int i = 0;
    if(!isEven){
      for(i=0;i<odd;i++) {
        oddThreads[i].start();  
      }
    } else {
      for(i=0;i<even;i++) {
        evenThreads[i].start(); 
      }
    }
  }
  public static void main(String args[]) {
    n = Integer.parseInt(args[0]);
    int i = 0;
    odd = n/2;
    even = n/2;
    if(n%2==0){
      even--;
    }

    nums = new int[n];
    for(i = 0; i < n; i++){
      nums[i] = Integer.parseInt(args[i+1]);
    }

    System.out.println("\n\nInitial Array : " + Arrays.toString(OddEvenSort.nums));  

    System.out.println("\n========================================");
    oddThreads = new Sorters[odd]; 
    evenThreads = new Sorters[even];

    for(i = 0 ;i < odd; i++) {
      oddThreads[i] = new Sorters("OddThread " + (i + 1), false, 2*i);  
    }
    for(i = 0; i < even; i++) {
      evenThreads[i] = new Sorters("EvenThread " + (i + 1), true, 2*i + 1); 
    }

    boolean currentThreadType = false;

    for(i = 0; i < n; i++) {
      startAll(currentThreadType); 

      waitForAll(currentThreadType);

      currentThreadType = !currentThreadType;

      System.out.println("========================================");
    }
    
    System.out.println("\n\nSorted Array : " + Arrays.toString(OddEvenSort.nums));

    System.out.println("========================================");
  }   
}
