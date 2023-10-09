/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;

public class Board {
    private final int n;
    private final int[][] tiles;

    private final int emptyX;
    private final int emptyY;

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        // initialize the board
        if (tiles == null) throw new IllegalArgumentException("tiles is null");
        this.n = tiles.length;
        this.tiles = new int[tiles.length][tiles[0].length];
        for (int i = 0; i < tiles.length; i++) {
            this.tiles[i] = Arrays.copyOf(tiles[i], tiles.length);
        }
        int x = 0, y = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (tiles[i][j] == 0) {
                    x = i;
                    y = j;
                    break;
                }
            }
        }
        emptyX = x;
        emptyY = y;
    }

    // string representation of this board
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(n + "\n");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                s.append(String.format("%2d ", tiles[i][j]));
            }
            s.append("\n");
        }
        return s.toString();
    }

    // board dimension n
    public int dimension() {
        return this.n;
    }

    // number of tiles out of place
    public int hamming() {
        int hamDistance = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (tiles[i][j] == 0) continue;
                if (tiles[i][j] != (i * n + (j + 1))) hamDistance++;
            }
        }
        return hamDistance;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        int manDistance = 0;
        int deltaX, deltaY;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (tiles[i][j] == 0) continue;
                deltaX = Math.abs((i - (tiles[i][j] - 1) / n));
                deltaY = Math.abs((j - (tiles[i][j] - 1) % n));
                manDistance += (deltaX + deltaY);
            }
        }
        return manDistance;
    }

    // is this board the goal board?
    public boolean isGoal() {
        return manhattan() == 0;
    }

    // does this board equal y?
    public boolean equals(Object y) {
        if (y == null) return false;
        if (y.getClass() != this.getClass()) return false;
        return Arrays.deepEquals(this.tiles, ((Board) y).tiles);
    }

    private int[][] copyTiles() {
        int[][] ret = new int[n][n];
        for (int i = 0; i < n; i++) {
            ret[i] = Arrays.copyOf(tiles[i], n);
        }
        return ret;
    }

    private void swap(int[][] t, int x1, int y1, int x2, int y2) {
        int tmp = t[x1][y1];
        t[x1][y1] = t[x2][y2];
        t[x2][y2] = tmp;
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        ArrayList<Board> nb = new ArrayList<>();
        int[][] neighbor;
        if (emptyX - 1 >= 0) {
            neighbor = copyTiles();
            swap(neighbor, emptyX, emptyY, emptyX - 1, emptyY);
            nb.add(new Board(neighbor));
        }
        if (emptyX + 1 <= n - 1) {
            neighbor = copyTiles();
            swap(neighbor, emptyX, emptyY, emptyX + 1, emptyY);
            nb.add(new Board(neighbor));
        }
        if (emptyY - 1 >= 0) {
            neighbor = copyTiles();
            swap(neighbor, emptyX, emptyY, emptyX, emptyY - 1);
            nb.add(new Board(neighbor));
        }
        if (emptyY + 1 <= n - 1) {
            neighbor = copyTiles();
            swap(neighbor, emptyX, emptyY, emptyX, emptyY + 1);
            nb.add(new Board(neighbor));
        }
        return nb;
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        int[][] newTiles = copyTiles();
        int[] indices = new int[4];
        int k = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i != emptyX || j != emptyY) {
                    indices[k++] = i;
                    indices[k++] = j;
                    if (k == indices.length) break;
                }
            }
            if (k == indices.length) break;
        }
        int t = newTiles[indices[0]][indices[1]];
        newTiles[indices[0]][indices[1]] = newTiles[indices[2]][indices[3]];
        newTiles[indices[2]][indices[3]] = t;
        return new Board(newTiles);
    }

    // unit testing (not graded)
    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);
        Board inital2 = new Board(tiles);
        StdOut.println(initial.equals(inital2));
        StdOut.println(initial.equals(initial.twin()));

        StdOut.println(initial);
        StdOut.println("hanmming dis: " + initial.hamming());
        StdOut.println("manhattan dis: " + initial.manhattan());
        StdOut.println("Twin: ");
        StdOut.println(initial.twin());
        StdOut.println("Neighbors: ");
        ArrayList<Board> ns = (ArrayList<Board>) initial.neighbors();
        for (Board b : ns) StdOut.println(b);
    }

}
