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

public class BruteCollinearPoints {
    private ArrayList<LineSegment> lines = new ArrayList<>();

    // finds all line segments containing 4 points
    public BruteCollinearPoints(Point[] points) {
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

        Point[] tmp = Arrays.copyOf(points, len);
        Arrays.sort(tmp);
        // brute find collinear points for 4 points
        for (int i = 0; i < len; i++) {
            for (int j = i + 1; j < len; j++) {
                for (int k = j + 1; k < len; k++) {
                    for (int h = k + 1; h < len; h++) {
                        double slope1 = tmp[i].slopeTo(tmp[j]);
                        double slope2 = tmp[i].slopeTo(tmp[k]);
                        double slope3 = tmp[i].slopeTo(tmp[h]);
                        if (slope1 == slope2 && slope1 == slope3) {
                            lines.add(new LineSegment(tmp[i], tmp[h]));
                        }
                    }
                }
            }
        }
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
