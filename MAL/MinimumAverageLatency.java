import java.util.*;

public class MinimumAverageLatency {
  public static void main(String args[]) {
    //int nCycles = Integer.parseInt(args[0]);
    //int nStages = Integer.parseInt(args[1]);
    int reservationTable[][] = {
      {0, 0, 1, 0, 1, 0, 0},
      {1, 0, 0, 0, 0, 0, 1},
      {0, 0, 0, 1, 0, 0, 1}
    };

    Set<Integer> forbiddenCycles = new HashSet<Integer>();

    for(int i = 0; i < reservationTable.length; i++) {
      for(int j = 0; j < reservationTable[0].length; j++) {
        if(reservationTable[i][j] == 1) {
          for(int k = 0; k < reservationTable[0].length; k++) {
            if(j != k && reservationTable[i][k] == 1) forbiddenCycles.add(Math.abs(j - k));
          } 
        }
      }
    }
    System.out.println("Forbidden Latency Cycle #s : " + forbiddenCycles.toString());

    int initialStateVector = 0;
    for(int cycle : forbiddenCycles) {
      initialStateVector += Math.pow(2, cycle - 1);
    }
    System.out.println("Initial State Vector : " + 
        initialStateVector + "d i.e. " + 
        toBinary(initialStateVector) + "b");  

    LinkedList<State> states = new LinkedList<State>();
    states.add(new State(initialStateVector, initialStateVector, reservationTable[0].length));

    State parentState = null;
    while((parentState = getUnvisited(states)) != null) {
      int currentStateVector = parentState.number;

      System.out.println("\n  Finding states from " + parentState.number + " ( " + toBinary(parentState.number) + " )");

      for(int i = 0; i < reservationTable[0].length; i++) {   
        boolean isEven = currentStateVector%2 == 0;
        currentStateVector = currentStateVector >> 1;

        if(isEven) { 
          currentStateVector = initialStateVector | currentStateVector;
          System.out.println("   Potential state : " + currentStateVector + " ( " + toBinary(currentStateVector) + " )");
          boolean found = false;
          for(State st : states) {  
            if(st.number == currentStateVector) {
              found = true;
              break;
            }
          }

          if(!found) {    
            State temp = new State(currentStateVector, currentStateVector, reservationTable[0].length); 
            System.out.println("   Confirmed new state");
            states.add(temp);
          }

          int indexOfParentState = states.indexOf(parentState);
          parentState.visited = true;

          if(parentState.cyclesToReach > (i + 1)) {
            parentState.nextGreedyState = currentStateVector;
            parentState.cyclesToReach = i + 1; 
          }     
          states.set(indexOfParentState, parentState);

        }
      }
    }
    System.out.println("\n\nStates : " + states + "\n");

    // Finding MAL

    State minState = null;
    int mal = Integer.MAX_VALUE;
    for(State validState : states) { 
      State iteratingState = validState;

      System.out.println("\n Cycles from : " + validState.number + " ( " + toBinary(validState.number)+ " )");
      int cyclesTraversed = 0;
      int statesTraversed = 0;
      if(iteratingState.nextGreedyState == validState.number) {
        cyclesTraversed = validState.cyclesToReach;
      } else {
        while(iteratingState.nextGreedyState != validState.number) {
          System.out.print("  " + iteratingState.number + " ( " + toBinary(iteratingState.number) + " ) [ " + iteratingState.cyclesToReach + " ] -> ");
          cyclesTraversed += iteratingState.cyclesToReach; 
          statesTraversed++;
          iteratingState = getGreedyState(states, iteratingState.nextGreedyState);
        } 
      } 
      System.out.println(iteratingState.number + " ( " + toBinary(iteratingState.number) + " ) [ " + iteratingState.cyclesToReach + " ] -> " + 
                         validState.number + " ( " + toBinary(validState.number) + " ) ");

      cyclesTraversed += iteratingState.cyclesToReach;
      statesTraversed++;

      if(mal > cyclesTraversed/statesTraversed) {
        mal = cyclesTraversed/statesTraversed;
        minState = validState;
      }
    }

    System.out.println("\n MAL from : "+ minState + " , MAL = " + mal);
  }
  public static State getGreedyState(LinkedList<State> l, int value) {
    for(State s : l) {
      if(s.number == value) return s;
    }
    return null;
  }
  public static State getUnvisited(LinkedList<State> l) { 
    int visited = 0;
    for(State s : l) {
      if(!s.visited) return s;
    }
    return null;
  }
  public static String toBinary(int num) {
    StringBuilder sb = new StringBuilder(""); 
    while(num != 0) {
      sb.append(num%2);
      num = num >> 1; 
    }
    return sb.reverse().toString();
  }
}
class State {
  int number;
  int nextGreedyState;
  int cyclesToReach;
  boolean visited = false;

  State(int a, int b, int c) {
    number = a;
    nextGreedyState = b;
    cyclesToReach = c;
  }

  public String toString() {
    return "State : " + number + (visited?" V":" NV") + " (" + MinimumAverageLatency.toBinary(number) + ")" +
      "->" + nextGreedyState + "( " + MinimumAverageLatency.toBinary(nextGreedyState)+ " )" + 
      "[ " + cyclesToReach+ " ]";
  }

}
