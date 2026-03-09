package model;

import transforms.Col;
import transforms.Point2D;
import transforms.Point3D;
import transforms.Vec3D;

public class Vertex implements Vectorizable<Vertex> {
    private final Point3D position;
    private final Col color;
    private final Point2D uv;
    private final Vec3D normal;

    public Vertex(Point3D position, Col color, Point2D uv, Vec3D normal) {
        this.position = position;
        this.color = color;
        this.uv = uv;
        this.normal = normal;
    }

    public Vertex(double x, double y, double z) {
        this.position = new Point3D(x, y, z);
        this.color = new Col(0xffffff);
        this.uv = new Point2D(0, 0);
        this.normal = new Vec3D(0, 0, 1);
    }

    public Vertex(double x, double y, double z, Col color) {
        this.position = new Point3D(x, y, z);
        this.color = color;
        this.uv = new Point2D(0, 0);
        this.normal = new Vec3D(0, 0, 1);
    }

    public Vertex(Point3D position, Col color) {
        this.position = position;
        this.color = color;
        this.uv = new Point2D(0, 0);
        this.normal = new Vec3D(0, 0, 1);
    }

    public Point3D getPosition() {
        return position;
    }

    public Col getColor() {
        return color;
    }

    public Point2D getUv() {
        return uv;
    }

    public Vec3D getNormal() {
        return normal;
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
        return new Vertex(
                position.mul(d),
                color.mul(d),
                uv.mul(d),
                normal.mul(d)
        );
    }

    @Override
    public Vertex add(Vertex v) {
        return new Vertex(
                position.add(v.getPosition()),
                color.add(v.getColor()),
                uv.add(v.getUv()),
                normal.add(v.getNormal())
        );
    }

    //TODO: dehomo, transformToWindow -> vratí vertex, atd. (možný todo)
}
