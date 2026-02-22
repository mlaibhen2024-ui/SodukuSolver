import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Stack;

public class Solver {

    private int[][] board;

    public Solver(int[][] board) {
        this.board = board;
    }

    /**
     * This method check each row, colum, and 3x3 box to ensure that a placing a number at that
     * specific spot is valid .
     *
     * @param current The current 9x9 soduku board
     * @param  row the row index (0-8) where the number should be place
     * @param col the col index(0-8) where the number should be place
     * @param num the numer (1-9) to check for validity
     * @return {@code true} if the number is valid to place, {@code false} otherwise.
     */
    // Check if placing num at (row,col) is valid
    private boolean isValid(int[][] current, int row, int col, int num) {

        // Check row
        for (int i = 0; i < 9; i++) {
            if (current[row][i] == num) {
                return false;
            }
        }

        // Check column
        for (int i = 0; i < 9; i++) {
            if (current[i][col] == num) {
                return false;
            }
        }

        // Check 3x3 box
        int startRow = row - row % 3;
        int startCol = col - col % 3;

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (current[startRow + i][startCol + j] == num) {
                    return false;
                }
            }
        }

        return true;
    }
    /**
     * This method try to solve a Sudoku puzzle using a Depth-First Search (DFS) approach with a stack.
     * The method uses helper methods {@code copyBoard()}, {@code findEmptyCell()}, and {@code isValid()}
     * to manage the board state and check the validity of number placements.
     *
     * @return true if the puzzle is solved successfully (the board is updated in place with the solution),
     *         false if the puzzle is unsolvable.
     */
    public boolean solve() {

        Stack<int[][]> stack = new Stack<>();
        stack.push(copyBoard(board));

        while (!stack.isEmpty()) {

            int[][] current = stack.pop();

            int[] empty = findEmptyCell(current);

            // If no empty cell → solved
            if (empty == null) {
                board = current;
                return true;
            }

            int row = empty[0];
            int col = empty[1];

            // Try 1–9
            for (int num = 9; num >= 1; num--) { // reverse order for proper DFS
                if (isValid(current, row, col, num)) {

                    int[][] newBoard = copyBoard(current);
                    newBoard[row][col] = num;
                    stack.push(newBoard);
                }
            }
        }

        return false;
    }
    /**
     * This method check each cell to find an empty cell .
     * An empty cell is represented by 0
     *
     * @param current The current 9x9 soduku board
     *  @return an array of two integers {row, col} representing the position of the
     *  *first empty cell found, or {@code null} if no empty cells remain
     */
    private int[] findEmptyCell(int[][] current) {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (current[i][j] == 0) {
                    return new int[]{i, j};
                }
            }
        }
        return null;
    }
    /**
     * Creates and returns a deep copy of the given 9x9 two-dimensional integer array.
     * This is useful for algorithms like backtracking (common in games like Sudoku)
     * where the original state of the board must be preserved while exploring
     * potential moves or solutions without side effects.
     *
     * @param original The 9x9 2D integer array to be copied. It is assumed to be non-null
     *                 and have dimensions of exactly 9x9.
     * @return A new 9x9 2D integer array that is a deep copy of the original array.
     */
    private int[][] copyBoard(int[][] original) {
        int[][] copy = new int[9][9];
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                copy[i][j] = original[i][j];
            }
        }
        return copy;
    }

    /**
     * Reads a 9x9 Sudoku board from the provided Scanner object.
     * Expects 9 lines of input, where each line contains 9 numerical characters.
     * Empty lines are skipped.
     *
     * @param scanner The Scanner object to read input from.
     * @return A 9x9 2D array representing the Sudoku board.
     */
    public static int[][] readSingleBoard(Scanner scanner) {
        int[][] board = new int[9][9];
        int row = 0;

        while (scanner.hasNextLine() && row < 9) {
            String line = scanner.nextLine().trim();
            if (line.isEmpty()) continue;

            String[] nums = line.split("");
            for (int col = 0; col < 9; col++) {
                board[row][col] = nums[col].charAt(0) - '0';
            }
            row++;
        }

        return board;
    }
    /**
     * The main entry point for the Sudoku solver application.
     * This method reads puzzles from a file named "sudokus.txt",
     * attempts to solve each one, and prints the sum of the top-left
     * three digits (board[0][0]*100 + board[0][1]*10 + board[0][2])
     * of all successfully solved puzzles.
     *
     * @param args Command line arguments (not used in this application).
     */

    public static void main(String[] args) {

        List<int[][]> allBoards = new ArrayList<>();

        try (Scanner scanner = new Scanner(new File("sudokus.txt"))) {

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line.startsWith("Grid")) {
                    allBoards.add(readSingleBoard(scanner));
                }
            }

        } catch (FileNotFoundException e) {
            System.out.println("File not found.");
            return;
        }

        int totalSum = 0;

        for (int[][] puzzle : allBoards) {

            Solver solver = new Solver(puzzle);

            if (solver.solve()) {

                int topLeft =
                        solver.board[0][0] * 100 +
                                solver.board[0][1] * 10 +
                                solver.board[0][2];

                totalSum += topLeft;
            }
        }

        // Final required output
        System.out.println(totalSum);
    }
}