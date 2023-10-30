/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;

import java.util.ArrayList;

public class PointSET {
    private SET<Point2D> points;

    public PointSET() {   // construct an empty set of points
        points = new SET<>();
    }

    public boolean isEmpty() {   // is the set empty?
        return points.isEmpty();
    }

    public int size() {    // number of points in the set
        return points.size();
    }

    public void insert(Point2D p) {   // add the point to the set (if it is not already in the set)
        if (p == null) throw new IllegalArgumentException("p is null");
        points.add(p);
    }

    public boolean contains(Point2D p) {  // does the set contain point p?
        if (p == null) throw new IllegalArgumentException("p is null");
        return points.contains(p);
    }

    public void draw() {   // draw all points to standard draw
        for (Point2D p : points) {
            p.draw();
        }
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new IllegalArgumentException("rect is null");
        ArrayList<Point2D> pointInRect = new ArrayList<>();
        for (Point2D p : points) {
            if (rect.contains(p)) {
                pointInRect.add(p);
            }
        }
        return pointInRect;
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null) throw new IllegalArgumentException("p is null");
        Point2D minp = null;
        double mindistance = Double.MAX_VALUE;
        for (Point2D point : points) {
            if (p.distanceSquaredTo(point) < mindistance) {
                mindistance = p.distanceSquaredTo(point);
                minp = point;
            }
        }
        return minp;
    }

    public static void main(String[] args) {

    }      // unit testing of the methods (optional)
}
