package model;

public class Rectangle extends Polygon {
    public Rectangle(Point a, Point b, Point c){
        generateRectangle(a, b, c);
    }

    private void generateRectangle(Point a, Point b, Point c){


        // vektor A a B
        double dx = b.getX() - a.getX();
        double dy = b.getY() - a.getY();

        double len = Math.sqrt(dx*dx + dy*dy);
        double nx = -dy / len;
        double ny = dx / len;

        double dist = (c.getX() - a.getX()) * nx + (c.getY() - a.getY()) * ny;

        // body obdélníku
        Point d = new Point(
                (int)(a.getX() + nx * dist),
                (int)(a.getY() + ny * dist)
        );

        Point e = new Point(
                (int)(b.getX() + nx * dist),
                (int)(b.getY() + ny * dist)
        );

        // body do Polygonu
        addPoint(a);
        addPoint(b);
        addPoint(e);
        addPoint(d);
    }
}
