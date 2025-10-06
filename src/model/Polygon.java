package model;

import java.util.ArrayList;

public class Polygon {
private final ArrayList<Point> points;

public Polygon() {
        this.points = new ArrayList<>();
    }
    public void addPoint(Point p) {
     points.add(p);
    }

    public Point getPoint(int i) {
        return points.get(i);
    }

    public ArrayList<Point> getPoints() {
    return points;
    }
}
