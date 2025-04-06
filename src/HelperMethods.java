
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class HelperMethods {

    public static int[][] generateMagicSquare() {
        ArrayList<Integer> numbers = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            numbers.add(i);
        }
        Collections.shuffle(numbers);

        int[][] magicSquare = new int[3][3];
        int index = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                magicSquare[i][j] = numbers.get(index++);
            }
        }

        return magicSquare;
    }
    
    public static int[][] getUserInput() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter 9 numbers (ex: 235 468 170):");
        String input = scanner.nextLine();
        String[] parts = input.split(" ");

        if (parts.length != 3) {
            throw new IllegalArgumentException("Invalid input. Please enter exactly 9 numbers.");
        }

        String numbersString = parts[0] + parts[1] + parts[2];
        if (numbersString.length() != 9 || !numbersString.matches("\\d{9}")) {
            throw new IllegalArgumentException("Invalid numbers. Please enter exactly 9 digits.");
        }

        int[][] magicSquare = new int[3][3];
        int index = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                magicSquare[i][j] = Character.getNumericValue(numbersString.charAt(index++));
            }
        }

        return magicSquare;
    }
	public static void InputScreen() {
		System.out.println("Welcome");
		
		
	}
	
    public static void printMagicSquare(int[][] magicSquare) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                System.out.print(magicSquare[i][j] + " ");
            }
            System.out.println();
        }
    }

    public static String InputGoal() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Choose:");
        System.out.println("1. TOP");
        System.out.println("2. DEF");
        System.out.println("3. SUM");

        int choice = scanner.nextInt();
        switch (choice) {
            case 1:
                return "TOP";
            case 2:
                return "DEF";
            case 3:
                return "SUM";
            default:
                throw new IllegalArgumentException("Invalid choice. Please choose 1, 2, or 3.");
        }
    }
}

