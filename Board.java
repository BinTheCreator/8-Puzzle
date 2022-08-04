import dsa.Inversions;
import dsa.LinkedQueue;
import stdlib.In;
import stdlib.StdOut;


// A data type to represent a board in the 8-puzzle game or its generalizations.
public class Board {
    private int [][] tiles; // Tiles in the board
    private int n; // Board size
    private int hamming; // Hamming distance to the goal board
    private int manhattan; // Manhattan distance to the goal board
    private int blankPos; // Position of the blank tile in row-major order

    // Constructs a board from an n x n array; tiles[i][j] is the tile at row i and column j, with 0
    // denoting the blank tile.
    public Board(int[][] tiles) {
        this.tiles = tiles;
        this.n = tiles.length;
        this.manhattan = 0;
        this.hamming = 0;
        this.blankPos = 0;
        int count = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                int c = tileAt(i, j);
                count = (count + 1) % (n * n);
                if (c != count && count != 0) {
                    hamming++; // Compute Hamming Distances
                }
                if (c != 0) {
                    // Compute Manhattan Distances
                    int row = Math.abs((c - 1) / n - i);
                    int col = Math.abs((c - 1) % n - j);
                    manhattan += row + col;
                }
                // Compute blankPos in row-major order
                if (c == 0) {
                    blankPos = i * n + j + 1;
                }
            }
        }

    }

    // Returns the size of this board.
    public int size() {
        return n;

    }

    // Returns the tile at row i and column j of this board.
    public int tileAt(int i, int j) {
        return tiles[i][j];
    }

    // Returns Hamming distance between this board and the goal board.
    public int hamming() {
        return this.hamming;
    }

    // Returns the Manhattan distance between this board and the goal board.
    public int manhattan() {
        return this.manhattan;
    }

        // Returns true if this board is the goal board, and false otherwise.
    public boolean isGoal() {
        return this.hamming == 0;
    }

        // Returns true if this board is solvable, and false otherwise.
    public boolean isSolvable() {
        int [] count = new int[n * n - 1];
        int index = 0;
        int h = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (tiles[i][j] != 0) {
                    count[index] = tiles[i][j];
                    index++;
                } else {
                    h = i;
                }
            }
        }
        long k = Inversions.count(count);
        if (n % 2 == 1) {
            return (k % 2 == 0);
        } else {
            return ((k + h) % 2 == 1);
        }
    }

    // Returns an iterable object containing the neighboring boards of this board.
    public Iterable<Board> neighbors() {
        LinkedQueue<Board> q = new LinkedQueue<Board>();
        int i = (blankPos - 1) / n;
        int j = (blankPos - 1) % n;
        if (i + 1 < n) {
            int[][] temp = cloneTiles();
            int k = temp[i + 1][j];
            temp[i + 1][j] = temp[i][j];
            temp[i][j] = k;
            Board board = new Board(temp);
            q.enqueue(board);
        }
        if (i - 1 >= 0) {
            int[][] temp = cloneTiles();
            int k = temp[i - 1][j];
            temp[i - 1][j] = temp[i][j];
            temp[i][j] = k;
            Board board = new Board(temp);
            q.enqueue(board);
        }
        if (j - 1 >= 0) {
            int[][] temp = cloneTiles();
            int k = temp[i][j - 1];
            temp[i][j - 1] = temp[i][j];
            temp[i][j] = k;
            Board board = new Board(temp);
            q.enqueue(board);
        }
        if (j + 1 < n) {
            int[][] temp = cloneTiles();
            int k = temp[i][j + 1];
            temp[i][j + 1] = temp[i][j];
            temp[i][j] = k;
            Board board = new Board(temp);
            q.enqueue(board);
        }
        return q;
    }

    // Returns true if this board is the same as other, and false otherwise.
    public boolean equals(Object other) {
        if (other == null) {
            return false;
        }
        if (other == this) {
            return true;
        }
        if (other.getClass() != this.getClass()) {
            return false;
        }
        return (this.toString().equals(other.toString()));
    }

    // Returns a string representation of this board.
    public String toString() {
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                s.append(String.format("%2s", tiles[i][j] == 0 ? " " : tiles[i][j]));
                if (j < n - 1) {
                    s.append(" ");
                }
            }
            if (i < n - 1) {
                s.append("\n");
            }
        }
        return s.toString();
    }

    // Returns a defensive copy of tiles[][].
    private int[][] cloneTiles() {
        int[][] clone = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                clone[i][j] = tiles[i][j];
            }
        }
        return clone;
    }

    // Unit tests the data type. [DO NOT EDIT]
    public static void main(String[] args) {
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                tiles[i][j] = in.readInt();
            }
        }
        Board board = new Board(tiles);
        StdOut.printf("The board (%d-puzzle):\n%s\n", n, board);
        String f = "Hamming = %d, Manhattan = %d, Goal? %s, Solvable? %s\n";
        StdOut.printf(f, board.hamming(), board.manhattan(), board.isGoal(), board.isSolvable());
        StdOut.println("Neighboring boards:");
        for (Board neighbor : board.neighbors()) {
            StdOut.println(neighbor);
            StdOut.println("----------");
        }
    }
}
