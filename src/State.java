import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class State {
    private int[][] board;
    private State parent;
    private int realCost;

    /**
     * Constructs a State by deep-copying the given 2D array.
     */
    public State(int[][] board) {
        this.board = new int[board.length][board[0].length];
        for (int i = 0; i < board.length; i++) {
            System.arraycopy(board[i], 0, this.board[i], 0, board[i].length);
        }
        this.parent = null;
        realCost = Integer.MAX_VALUE;
    }

    public int getRealCost() {
		return realCost;
	}

	public void setRealCost(int cost) {
		this.realCost = cost;
	}

	public void setBoard(int[][] board) {
		this.board = board;
	}

	/**
     * Copy constructor that creates a new State based on another State's board.
     */
    public State(State other) {
        this(other.board);
    }
    
    

    /**
     * Returns the tile at row i, column j.
     */
    public int getTile(int i, int j) {
        return board[i][j];
    }

    /**
     * Sets the tile at row i, column j to val.
     */
    public void setTile(int i, int j, int val) {
        board[i][j] = val;
    }

    /**
     * Returns the (deep-copied) underlying board.
     */
    public int[][] getBoard() {
        // If you need to return a copy, do so here. 
        // Or return a reference if that's acceptable for your scenario.
        int[][] copy = new int[board.length][board[0].length];
        for (int i = 0; i < board.length; i++) {
            System.arraycopy(board[i], 0, copy[i], 0, board[i].length);
        }
        return copy;
    }

    /**
     * Generates a new State that is a deep copy of this State.
     */
    public State copy() {
        return new State(this);
    }

    @Override
    public boolean equals(Object obj) {
        State other = (State) obj;

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (this.board[i][j] != other.board[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }


    @Override
    public int hashCode() {
        return Arrays.deepHashCode(this.board);
    }


    /**
     * For debugging/printing purposes.
     */
    @Override
    public String toString() {
        return Arrays.deepToString(board);
    }
    
    public List<State> getPath() {
        List<State> path = new ArrayList<>();
        State current = this;
        while (current != null) {
            path.add(0, current);
            current = current.parent;
        }
        return path;
    }
    

    public State getParent() {
        return parent;
    }

    public void setParent(State parent) {
        this.parent = parent;
    }
}
