/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

import java.util.ArrayList;

public class KdTree {
    private Node root;
    private double dist;
    private Point2D nearestPoint;

    public KdTree() {

    }

    private class Node {
        private boolean isOdd;  // odd level's node is vertical
        private Point2D point;
        private int size;
        private Node left, right;
        private RectHV rect;

        public Node(boolean isOdd, Point2D point, int size, RectHV rect) {
            this.isOdd = isOdd;
            this.point = point;
            this.size = size;
            this.rect = rect;
        }

        public boolean isVertical() {
            return isOdd;
        }

        public RectHV leftRect() {
            if (isVertical()) {
                return new RectHV(rect.xmin(), rect.ymin(), point.x(), rect.ymax());
            }
            else {
                return new RectHV(rect.xmin(), rect.ymin(), rect.xmax(), point.y());
            }
        }

        public RectHV rightRect() {
            if (isVertical()) {
                return new RectHV(point.x(), rect.ymin(), rect.xmax(), rect.ymax());
            }
            else {
                return new RectHV(rect.xmin(), point.y(), rect.xmax(), rect.ymax());
            }
        }

    }

    public boolean isEmpty() {
        return size() == 0;
    }                     // is the set empty?

    private int size(Node node) {
        if (node == null) return 0;
        else return node.size;
    }                      // number of points in the set

    public int size() {
        return size(root);
    }

    private Node insert(Point2D p, Node node, Node parent, boolean isleft) {
        if (node == null) {
            if (parent == null) {
                return new Node(true, p, 1, new RectHV(0, 0, 1, 1));
            }
            else {
                if (isleft) {
                    return new Node(!parent.isOdd, p, 1, parent.leftRect());
                }
                else {
                    return new Node(!parent.isOdd, p, 1, parent.rightRect());
                }
            }
        }
        else if (node.point.equals(p)) {
            return node;
        }
        else if (node.isVertical()) {
            if (p.x() < node.point.x()) {
                node.left = insert(p, node.left, node, true);
            }
            else {
                node.right = insert(p, node.right, node, false);
            }
        }
        else {
            if (p.y() < node.point.y()) {
                node.left = insert(p, node.left, node, true);
            }
            else {
                node.right = insert(p, node.right, node, false);
            }
        }
        node.size = 1 + size(node.left) + size(node.right);
        return node;
    }

    public void insert(Point2D p) {
        if (p == null) throw new IllegalArgumentException("p is null");
        root = insert(p, root, null, true);
    }        // add the point to the set (if it is not already in the set)

    private boolean contains(Point2D p, Node node) {
        if (node == null) {
            return false;
        }
        if (p.equals(node.point)) return true;
        if (node.isVertical()) {
            if (p.x() < node.point.x()) {
                return contains(p, node.left);
            }
            else {
                return contains(p, node.right);
            }
        }
        else {
            if (p.y() < node.point.y()) {
                return contains(p, node.left);
            }
            else {
                return contains(p, node.right);
            }
        }
    }

    public boolean contains(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException("p is null");
        }
        return contains(p, root);
    }         // does the set contain point p?

    private void draw(Node node, Node parent) {
        if (node == null) return;
        if (parent == null) {
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.setPenRadius();
            StdDraw.line(node.point.x(), 0, node.point.x(), 1);
        }
        else if (node.isVertical()) {
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.setPenRadius();
            StdDraw.line(node.point.x(), node.rect.ymin(), node.point.x(), node.rect.ymax());
        }
        else {
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.setPenRadius();
            StdDraw.line(node.rect.xmin(), node.point.y(), node.rect.xmax(), node.point.y());
        }
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);
        StdDraw.point(node.point.x(), node.point.y());
        draw(node.left, node);
        draw(node.right, node);
    }

    public void draw() {
        draw(root, null);
    }

    private void range(RectHV rect, ArrayList<Point2D> points, Node node) {
        if (node == null) return;
        if (rect.contains(node.point)) points.add(node.point);
        if (node.isVertical()) {   // some optimization
            if (node.point.x() > rect.xmax()) {
                range(rect, points, node.left);
            }
            else if (node.point.x() < rect.xmin()) {
                range(rect, points, node.right);
            }
            else {
                range(rect, points, node.left);
                range(rect, points, node.right);
            }
        }
        else {
            if (node.point.y() > rect.ymax()) {
                range(rect, points, node.left);
            }
            else if (node.point.y() < rect.ymin()) {
                range(rect, points, node.right);
            }
            else {
                range(rect, points, node.left);
                range(rect, points, node.right);
            }
        }
    }

    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new IllegalArgumentException("rect is null");
        ArrayList<Point2D> points = new ArrayList<>();
        range(rect, points, root);
        return points;
    }           // all points that are inside the rectangle (or on the boundary)

    private void nearest(Point2D p, Node node) {
        if (node == null) return;
        if (p.distanceSquaredTo(node.point) < dist) {
            nearestPoint = node.point;
            dist = p.distanceSquaredTo(node.point);
        }
        boolean searchLeft = false, searchRight = false;
        if (node.left != null && node.left.rect.distanceSquaredTo(p) < dist) {
            searchLeft = true;
        }
        if (node.right != null && node.right.rect.distanceSquaredTo(p) < dist) {
            searchRight = true;
        }
        if (searchLeft && searchRight) {
            if (node.isVertical()) {
                if (p.x() < node.point.x()) {
                    nearest(p, node.left);
                    if (node.right.rect.distanceSquaredTo(p) < dist) {
                        nearest(p, node.right);
                    }
                }
                else {
                    nearest(p, node.right);
                    if (node.left.rect.distanceSquaredTo(p) < dist) {
                        nearest(p, node.left);
                    }
                }
            }
            else {
                if (p.y() < node.point.y()) {
                    nearest(p, node.left);
                    if (node.right.rect.distanceSquaredTo(p) < dist) {
                        nearest(p, node.right);
                    }
                }
                else {
                    nearest(p, node.right);
                    if (node.left.rect.distanceSquaredTo(p) < dist) {
                        nearest(p, node.left);
                    }
                }
            }
        }
        else {
            if (searchLeft) {
                nearest(p, node.left);
            }
            if (searchRight) {
                nearest(p, node.right);
            }
        }
    }

    public Point2D nearest(Point2D p) {
        if (p == null) throw new IllegalArgumentException("p is null");
        dist = Double.MAX_VALUE;
        nearestPoint = null;
        nearest(p, root);
        return nearestPoint;
    }         // a nearest neighbor in the set to point p; null if the set is empty

    public static void main(String[] args) {

    }
}




