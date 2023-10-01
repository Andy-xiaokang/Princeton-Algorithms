/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;

public class FastCollinearPoints {
    private ArrayList<LineSegment> lines = new ArrayList<>();

    // finds all line segments containing 4 or more points
    public FastCollinearPoints(Point[] points) {
        if (points == null) throw new IllegalArgumentException("argument is null");
        for (Point p : points) {
            if (p == null) throw new IllegalArgumentException("one point is null");
        }
        int len = points.length;
        for (int i = 0; i < len; i++) {
            for (int j = i + 1; j < len; j++) {
                if (points[i].compareTo(points[j]) == 0) {
                    throw new IllegalArgumentException("has repeated point");
                }
            }
        }
        if (points.length < 4) {
            return;
        }
        points = Arrays.copyOf(points, len);
        Arrays.sort(points);
        Point[] tmp = Arrays.copyOf(points, len);
        for (Point p : points) {
            Arrays.sort(tmp, p.slopeOrder());
            for (int i = 1; i < len; ) {
                int j = i + 1;
                while (j < len && p.slopeTo(tmp[i]) == p.slopeTo(tmp[j])) {
                    j++;
                }  // avoid count the same line duplicate
                if (j - i >= 3 && p.compareTo(min(tmp, i, j - 1)) < 0) {
                    lines.add(new LineSegment(p, max(tmp, i, j - 1)));
                }
                if (j == len) break;
                i = j;
            }
        }
    }

    private Point min(Point[] points, int lo, int hi) {
        if (lo > hi) throw new IllegalArgumentException("low is less than high");
        Point minp = points[lo];
        for (int i = lo + 1; i <= hi; i++) {
            if (points[i].compareTo(minp) < 0) minp = points[i];
        }
        return minp;
    }

    private Point max(Point[] points, int lo, int hi) {
        if (lo > hi) throw new IllegalArgumentException("low is less than high");
        Point maxp = points[lo];
        for (int i = lo + 1; i <= hi; i++) {
            if (points[i].compareTo(maxp) > 0) maxp = points[i];
        }
        return maxp;
    }

    // the number of line segments
    public int numberOfSegments() {
        return lines.size();
    }

    // the line segments
    public LineSegment[] segments() {
        int len = numberOfSegments();
        LineSegment[] linesArray = new LineSegment[len];
        for (int i = 0; i < len; i++) {
            linesArray[i] = lines.get(i);
        }
        return linesArray;
    }

    public static void main(String[] args) {
        // client test refer to flying pig
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
