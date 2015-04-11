import java.util.*;

public class MinimumAverageLatency {
  public static void main(String args[]) {
    //int nCycles = Integer.parseInt(args[0]);
    //int nStages = Integer.parseInt(args[1]);
    int reservationTable[][] = {
      {0, 0, 1, 0, 0 ,0, 1},
      {0, 1, 0, 1, 0 ,0, 1},
      {0, 0, 0, 0, 0 ,1, 0},
      {0, 1, 0, 0, 0 ,0, 0},
      {0, 0, 0, 0, 0 ,0, 0},
      {0, 0, 0, 1, 1 ,0, 0}
    };
 
    // Computing forbidden latencies
    // For each stage, look for difference between all 1s.
    //   Naive way :
    //    Foreach stage
    //      Foreach 1
    //        take diff between all subsequent 1s' index and current index, save in a set 
    //        
    
    
  }
}
