import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class TestAStar {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter puzzle input in one of the following ways:");
        System.out.println("1) 123 456 780 DEF => manual 3x3 board and goal");
        System.out.println("2) TOP/SUM/DEF => generate random board for the specified goal");
        System.out.print("Your input: ");

        String inputLine = scanner.nextLine().trim();

        // If input matches one of the predefined goals, generate random state
        if (inputLine.equalsIgnoreCase("TOP") || inputLine.equalsIgnoreCase("SUM") || inputLine.equalsIgnoreCase("DEF")) {
            String goal = inputLine.toUpperCase();
            State randomState = generateRandomState();

            System.out.println("Generated Start State:\n" + randomState);
            System.out.println("Selected Goal: " + goal);

            randomState.setRealCost(0);
            State finalState = AStar.solve(randomState, goal);
            if (finalState != null) {
                printPath(finalState.getPath());
            } else {
                System.out.println("No solution found.");
            }

        } else {
            // Attempt to parse input of the form "xxx xxx xxx GOAL"
            String[] tokens = inputLine.split("\\s+");
            if (tokens.length != 4) {
                System.out.println("Invalid input format. Expected something like \"123 456 780 DEF\".");
                return;
            }
            String row1 = tokens[0];
            String row2 = tokens[1];
            String row3 = tokens[2];
            String goal = tokens[3].toUpperCase();

            // Validate that row1, row2, row3 each have length 3 and contain digits 0..8 unique
            if (!isValidRow(row1) || !isValidRow(row2) || !isValidRow(row3)) {
                System.out.println("Each row must be exactly 3 digits (0..8) with no repeats among all digits!");
                return;
            }

            // Build the 3x3 array
            int[][] board = new int[3][3];
            boolean[] used = new boolean[9];

            for (int i = 0; i < 3; i++) {
                int digit = row1.charAt(i) - '0';
                if (used[digit]) {
                    System.out.println("Duplicate digit found: " + digit);
                    return;
                }
                used[digit] = true;
                board[0][i] = digit;
            }
            for (int i = 0; i < 3; i++) {
                int digit = row2.charAt(i) - '0';
                if (used[digit]) {
                    System.out.println("Duplicate digit found: " + digit);
                    return;
                }
                used[digit] = true;
                board[1][i] = digit;
            }
            for (int i = 0; i < 3; i++) {
                int digit = row3.charAt(i) - '0';
                if (used[digit]) {
                    System.out.println("Duplicate digit found: " + digit);
                    return;
                }
                used[digit] = true;
                board[2][i] = digit;
            }

            State userState = new State(board);
            userState.setRealCost(0);
            System.out.println("Parsed Start State:\n" + userState);
            System.out.println("Goal type: " + goal);

            State finalState = AStar.solve(userState, goal);
            if (finalState != null) {
                printPath(finalState.getPath());
            } else {
                System.out.println("No solution found.");
            }
        }

        scanner.close();
    }

    private static State generateRandomState() {
        List<Integer> nums = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            nums.add(i);
        }
        Collections.shuffle(nums);

        int[][] board = new int[3][3];
        int idx = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                board[i][j] = nums.get(idx++);
            }
        }
        return new State(board);
    }

    private static boolean isValidRow(String row) {
        if (row.length() != 3) return false;
        for (char c : row.toCharArray()) {
            if (c < '0' || c > '8') {
                return false;
            }
        }
        return true;
    }

    private static void printPath(List<State> path) {
        if (path.isEmpty()) {
            System.out.println("No path found.");
            return;
        }

        System.out.println("\nSolution path (" + (path.size() - 1) + " moves):");
        for (int i = 0; i < path.size(); i++) {
            System.out.println("\nStep " + i + ":");
            int[][] board = path.get(i).getBoard();
            for (int row = 0; row < board.length; row++) {
                System.out.print("  ");
                for (int col = 0; col < board[row].length; col++) {
                	if(board[row][col] == 0) {
                        System.out.print("X" + " ");
                	}
                	else{
                		System.out.print(board[row][col] + " ");
                	}
                }
                System.out.println();
            }
        }
    }
}
