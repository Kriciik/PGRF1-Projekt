package model;

import transforms.Col;
import transforms.Point3D;

public class Vertex implements Vectorizable<Vertex> {
    private final Point3D position;
    private final Col color;

    public Vertex(double x, double y, double z) {
        this.position = new Point3D(x,y,z);
        this.color = new Col(0xfffffff);
    }

    public Vertex(double x, double y, double z, Col color) {
        this.position = new Point3D(x,y,z);
        this.color = color;
    }

    public Vertex(Point3D position, Col color) {
        this.position = position;
        this.color = color;
    }

    public Point3D getPosition() {
        return position;
    }

    public Col getColor() {
        return color;
    }

    public double getX() {
        return position.getX();
    }
    public double getY() {
        return position.getY();
    }
    public double getZ() {
        return position.getZ();
    }

    @Override
    public Vertex mul(double d) {
        // TODO: implementovat
        return new Vertex(position.mul(d), color.mul(d));
    }

    @Override
    public Vertex add(Vertex v) {
        // TODO: implementovat
        return new Vertex(position.add(v.getPosition()), color.add(v.getColor()));
    }

    //TODO: dehomo, transformToWindow -> vratí vertex, atd. (možný todo)
}
