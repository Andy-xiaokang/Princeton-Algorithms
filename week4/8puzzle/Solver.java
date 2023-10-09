/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

public class Solver {
    private final boolean solvable;
    private final int moves;
    private final Node goal;

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) throw new IllegalArgumentException("intial is null");
        MinPQ<Node> minPQ = new MinPQ<>();
        minPQ.insert(new Node(initial, 0, null));
        MinPQ<Node> minPQ1 = new MinPQ<>();
        minPQ1.insert(new Node(initial.twin(), 0, null));
        MinPQ<Node> selector = minPQ;
        Node node = null;
        while (!selector.isEmpty()) {
            node = selector.delMin();
            if (node.board.isGoal()) break;
            Iterable<Board> neighbors = node.board.neighbors();
            for (Board b : neighbors) {
                if (node.prev == null || !b.equals(node.prev.board)) {
                    selector.insert(new Node(b, node.moves + 1, node));
                }
            }
            selector = selector == minPQ ? minPQ1 : minPQ;
        }
        solvable = selector == minPQ;
        assert node != null;
        moves = node.moves;
        goal = node;
    }
    

    private class Node implements Comparable<Node> {
        private final Board board;
        private final int moves;
        private final Node prev;
        private final int priority;

        private Node(Board board, int moves, Node prev) {
            this.board = board;
            this.moves = moves;
            this.prev = prev;
            this.priority = board.manhattan() + moves;
        }

        public int compareTo(Node that) {
            return Integer.compare(this.priority, that.priority);
        }
    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        return this.solvable;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        if (!solvable) return -1;
        return this.moves;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        if (!this.solvable) return null;
        Stack<Board> sol = new Stack<>();
        Node n = goal;
        while (n != null) {
            sol.push(n.board);
            n = n.prev;
        }
        return sol;
    }

    // test client (see below)
    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
}
