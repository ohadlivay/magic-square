import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.TreeMap;

public class AStar {
    String goalType;
    State magicSquare;   
    HashMap<State, Integer> expectedCosts = new HashMap<>();
    
    static boolean UCS_flag = false; //to check uniform cost search

    // Generate successors of a given state by swapping blank (0) with neighbors
    public static List<State> Successor(State currState) {
        List<State> successors = new ArrayList<>();
        int[][] board = currState.getBoard();

        // Find the blank
        int blankX = -1, blankY = -1;
        outerLoop:
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (board[i][j] == 0) {
                    blankX = i;
                    blankY = j;
                    break outerLoop;
                }
            }
        }

        // Up
        if (blankX > 0) {
            State copyState = currState.copy();
            copyState.setTile(blankX, blankY, copyState.getTile(blankX - 1, blankY));
            copyState.setTile(blankX - 1, blankY, 0);
            successors.add(copyState);
        }

        // Down
        if (blankX < board.length - 1) {
            State copyState = currState.copy();
            copyState.setTile(blankX, blankY, copyState.getTile(blankX + 1, blankY));
            copyState.setTile(blankX + 1, blankY, 0);
            successors.add(copyState);
        }

        // Left
        if (blankY > 0) {
            State copyState = currState.copy();
            copyState.setTile(blankX, blankY, copyState.getTile(blankX, blankY - 1));
            copyState.setTile(blankX, blankY - 1, 0);
            successors.add(copyState);
        }

        // Right
        if (blankY < board[0].length - 1) {
            State copyState = currState.copy();
            copyState.setTile(blankX, blankY, copyState.getTile(blankX, blankY + 1));
            copyState.setTile(blankX, blankY + 1, 0);
            successors.add(copyState);
        }

        return successors;
    }

    public static int EvaluationFunction(State s, String goalType) {
        int h;
        
        if (UCS_flag)
        	return s.getRealCost();
        
        switch (goalType) {
            case "SUM":
                h = SumHeuristic(s);
                break;
            case "DEF":
                h = DefHeuristic(s);
                break;
            case "TOP":
                h = TopHeuristic(s);
                break;
            default:
                throw new IllegalArgumentException("Invalid goal type: " + goalType);
        }

        int sum = s.getRealCost() + h;
        return sum;
    }

// sum of manhatten distances from each cell in state to goal
    private static int DefHeuristic(State s) {
        int[][] goalBoard = {
            {1, 2, 3},
            {4, 5, 6},
            {7, 8, 0}
        };

        int[][] board = s.getBoard();
        int cost = 0;

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (board[i][j] != 0) { // Ignore the blank tile
                    int value = board[i][j];
                    int goalX = (value - 1) / 3;
                    int goalY = (value - 1) % 3;
                    cost += Math.abs(i - goalX) + Math.abs(j - goalY);
                }
            }
        }
        return cost;
    }


    
    

    // use sum of all manhatten distances of 1/2/3 from their spot ((0,0),(1,0),(2,0))
    private static int TopHeuristic(State s) {
        int sum = 0;
        int[][] board = s.getBoard();
        
        for(int i =0; i<3; i++) {
        	for(int j = 0; j<3; j++) {
        		if(board[i][j] == 1) {
        			sum+=Math.abs(i-0) + Math.abs(j-0);
        		}
        		if(board[i][j] == 2) {
        			sum+=Math.abs(i-0) + Math.abs(j-1);
        		}
        		if(board[i][j] == 3) {
        			sum+=Math.abs(i-0) + Math.abs(j-2);
        		}
        	}
        }
        return sum;
    }

    
    private static int SumHeuristic(State s) {
        int min = Integer.MAX_VALUE;

        // Try all triplets that sum to 9
        for (int t = 0; t < 9; t++) {
            for (int g = t + 1; g < 9; g++) {
                for (int m = g + 1; m < 9; m++) {
                    if (t + g + m != 9) {
                        continue; // Not a valid triplet
                    }

                    // Check all permutations of the triplet
                    int[][] permutations = {
                        {t, g, m},
                        {t, m, g},
                        {g, t, m},
                        {g, m, t},
                        {m, t, g},
                        {m, g, t}
                    };

                    for (int[] perm : permutations) {
                        int dist = MD(s, perm[0], perm[1], perm[2]);
                        min = Math.min(min, dist);
                    }
                }
            }
        }

        if (min == Integer.MAX_VALUE) {
            System.out.println("Something bad happened in the heuristic computation");
        }

        return min;
    }
   


    private static int MD(State s, int t, int g, int m) {
        // Manhattan distance for each tile to target top-row location
        int sum = 0;
        sum += Math.abs(getX(s, t) - 0) + Math.abs(getY(s, t) - 0); // t -> (0,0)
        sum += Math.abs(getX(s, g) - 0) + Math.abs(getY(s, g) - 1); // g -> (0,1)
        sum += Math.abs(getX(s, m) - 0) + Math.abs(getY(s, m) - 2); // m -> (0,2)
        return sum;
    }

    private static int getX(State s, int val) {
        int[][] board = s.getBoard();
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (board[i][j] == val) return i;
            }
        }
        return -1;
    }

    private static int getY(State s, int val) {
        int[][] board = s.getBoard();
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (board[i][j] == val) return j;
            }
        }
        return -1;
    }
    
    
    public static State solve(State startState, String goalType) {
        // PriorityQueue ordered by EvaluationFunction (realCost + heuristic)
        PriorityQueue<State> openSet = new PriorityQueue<>(Comparator.comparingInt(s -> EvaluationFunction(s, goalType)));
        Set<State> closedSet = new HashSet<>();

        // Initialize the start state
        startState.setRealCost(0); // g(n) = 0 for the start state
        startState.setParent(null); // Start state has no parent
        openSet.add(startState);

        while (!openSet.isEmpty()) {
            // Get the state with the lowest evaluation function
            State current = openSet.poll();

            // Check if we have reached the goal
            if (isGoal(current, goalType)) {
                System.out.println("SUCCESS");
                // Print the size of the closed set == number of nodes visited
                System.out.println("Closed set size: " + closedSet.size());
                return reconstructPath(current); // Goal found, return the path
            }

            // Add the current state to the closed set
            closedSet.add(current);

            // Process successors of the current state
            for (State successor : Successor(current)) {
                // Ignore successors that are already fully explored
                if (closedSet.contains(successor)) continue;

                // Calculate the tentative cost to reach this successor
                int tentativeRealCost = current.getRealCost() + 1;

                // Check if the successor is in the open set
                boolean inOpenSet = openSet.contains(successor);

                // Update the successor's cost and parent if a better path is found
                if (!inOpenSet || tentativeRealCost < successor.getRealCost()) {
                    successor.setRealCost(tentativeRealCost); // Update g(n)
                    successor.setParent(current); // Set the parent for path reconstruction

                    // Refresh the entry in the open set if it already exists
                    if (inOpenSet) {
                        openSet.remove(successor); // Remove the stale entry
                    }
                    openSet.add(successor); // Add the updated successor
                }
            }

            
            // Debugging: Print the size of the open set at each step
            System.out.println("Open set size: " + openSet.size());
        }
        

        // If we exit the loop, no solution was found
        System.out.println("FAILURE");
        
        System.out.println("Closed set size: " + closedSet.size());
        
        return null;
    }


    private static State reconstructPath(State goalState) {
        State current = goalState;
        List<State> path = new ArrayList<>();

        while (current != null) {
            path.add(0, current); // Add each state to the front of the list
            current = current.getParent();
        }

        System.out.println("Path length: " + path.size());
        for (State state : path) {
            System.out.println(state); // Print each state in the path (requires a toString implementation in State)
        }

        return goalState; // Return the goal state for additional handling, if needed
    }
//    public static State solve(State startState, String goalType) {
//        HashMap<State,Integer> visitedStates = new HashMap<State,Integer>(); //state and EVALUATED cost
//
//        startState.setRealCost(0);
//        visitedStates.put(startState, 0);
//
//        boolean haveSolution = false;
//        
//		while (!haveSolution) {
//			State cheapestState = findStateWithSmallestEvaluation(visitedStates);
//            List<State> successors = Successor(cheapestState);
//            for(State s : successors) {
//            	if(isGoal(s,goalType)) {
//            		System.out.println("SUCCESS");
//            		return s;
//            	}
//            	if(visitedStates.containsKey(s)) {
//            		//update the minimum of either previous realcost or current real cost
//            		s.setRealCost(Math.min(s.getRealCost(),cheapestState.getRealCost()+1));
//            	}
//            	
//            	int eval = EvaluationFunction(s,goalType);
//            	if(visitedStates.containsKey(s)) {
//                	int prevEval = visitedStates.get(s);
//                	visitedStates.put(s, Math.min(eval, prevEval));
//            	}else {
//                	visitedStates.put(s, eval);
//            	}
//            }
//		}
//		System.out.println("FAILURE");
//		return null;
//    }

    private static State findStateWithSmallestEvaluation(HashMap<State, Integer> states) {
        State minState = null;
        int minEval = Integer.MAX_VALUE;

        for (Map.Entry<State, Integer> entry : states.entrySet()) {
            if (entry.getValue() < minEval) {
                minEval = entry.getValue();
                minState = entry.getKey();
            }
        }

        return minState;
    }


    private static boolean isGoal(State s, String goalType) {
        int[][] board = s.getBoard();
        switch (goalType) {
            case "SUM":
                // top row sums to 9
                return board[0][0] + board[0][1] + board[0][2] == 9;
            case "DEF":
                // standard 8-puzzle goal layout
                return (board[0][0] == 1 && board[0][1] == 2 && board[0][2] == 3 &&
                        board[1][0] == 4 && board[1][1] == 5 && board[1][2] == 6 &&
                        board[2][0] == 7 && board[2][1] == 8 && board[2][2] == 0);
            case "TOP":
                // top row is [1,2,3]
                return (board[0][0] == 1 && board[0][1] == 2 && board[0][2] == 3);
            default:
                System.out.println("Invalid goal type: " + goalType);
                return false;
        }
    }
}
