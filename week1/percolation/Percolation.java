/* *****************************************************************************
 *  Name:              Alan Turing
 *  Coursera User ID:  123456
 *  Last modified:     1/1/2019
 **************************************************************************** */

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private WeightedQuickUnionUF ufforfull;
    private WeightedQuickUnionUF ufforperc;
    private boolean[] opened;
    private int openedSiteNumbers;
    private int width;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (n <= 0) throw new IllegalArgumentException("width must > 1");
        width = n;
        ufforperc = new WeightedQuickUnionUF(n * n + 2);
        ufforfull = new WeightedQuickUnionUF(n * n + 1);
        opened = new boolean[n * n + 2];
        openedSiteNumbers = 0;
        for (int i = 0; i < n * n + 2; i++) {
            opened[i] = false;
        }
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        if (!inGrid(row, col)) throw new IllegalArgumentException("index out of bounds");
        if (isOpen(row, col)) return;
        opened[calcIndex(row, col)] = true;
        connectAdjacent(row, col);
        openedSiteNumbers++;
        if (row == 1) {
            ufforfull.union(0, calcIndex(row, col));
            ufforperc.union(0, calcIndex(row, col));
        }
        if (row == width) {
            ufforperc.union(calcIndex(row, col), width * width + 1);
        }
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        if (!inGrid(row, col)) throw new IllegalArgumentException("index out of bounds");
        return opened[calcIndex(row, col)];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        if (!inGrid(row, col)) throw new IllegalArgumentException("index out of bounds");
        return (ufforfull.find(calcIndex(row, col)) == ufforfull.find(0));
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return openedSiteNumbers;
    }

    // does the system percolate?
    public boolean percolates() {
        return ufforperc.find(0) == ufforperc.find(width * width + 1);
    }

    // return if the site in grid
    private boolean inGrid(int row, int col) {
        if (row <= 0 || row > width || col <= 0 || col > width) return false;
        return true;
    }

    // return the index of given site (row, col)
    private int calcIndex(int row, int col) {
        if (!inGrid(row, col)) throw new IllegalArgumentException("site not in grid");
        return (row - 1) * width + col;
    }

    // connect adjacent
    private void connectAdjacent(int row, int col) {
        if (inGrid(row - 1, col) && isOpen(row - 1, col)) {
            ufforperc.union(calcIndex(row, col), calcIndex(row - 1, col));
            ufforfull.union(calcIndex(row, col), calcIndex(row - 1, col));
        }

        if (inGrid(row + 1, col) && isOpen(row + 1, col)) {
            ufforfull.union(calcIndex(row, col), calcIndex(row + 1, col));
            ufforperc.union(calcIndex(row, col), calcIndex(row + 1, col));
        }

        if (inGrid(row, col - 1) && isOpen(row, col - 1)) {
            ufforfull.union(calcIndex(row, col), calcIndex(row, col - 1));
            ufforperc.union(calcIndex(row, col), calcIndex(row, col - 1));
        }

        if (inGrid(row, col + 1) && isOpen(row, col + 1)) {
            ufforfull.union(calcIndex(row, col), calcIndex(row, col + 1));
            ufforperc.union(calcIndex(row, col), calcIndex(row, col + 1));
        }

    }

    public static void main(String[] args) {
    }
}
