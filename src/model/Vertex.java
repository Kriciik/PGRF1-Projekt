package model;

import transforms.*;

public class Vertex implements Vectorizable<Vertex> {
    private final Point3D position;
    private final Point3D positionWorldSpace;
    private final Col color;
    private final Vec2D uv;
    private final Vec3D normal;

    public Vertex(Point3D position, Point3D positionWorldSpace, Col color, Vec2D uv, Vec3D normal) {
        this.position = position;
        this.positionWorldSpace = positionWorldSpace;
        this.color = color;
        this.uv = uv;
        this.normal = normal;
    }

    public Vertex(Point3D position, Col color, Vec2D uv) {
        this.position = position;
        this.positionWorldSpace = position;
        this.color = color;
        this.uv = uv;
        this.normal = new Vec3D(0, 0, 1);
    }

    public Vertex(Point3D position, Col color, Vec2D uv, Vec3D normal) {
        this.position = position;
        this.positionWorldSpace = position;
        this.color = color;
        this.uv = uv;
        this.normal = normal;
    }

    public Vertex(double x, double y, double z) {
        this.position = new Point3D(x, y, z);
        this.positionWorldSpace = this.position;
        this.color = new Col(0xffffff);
        this.uv = new Vec2D(0, 0);
        this.normal = new Vec3D(0, 0, 1);
    }

    public Vertex(double x, double y, double z, Col color) {
        this.position = new Point3D(x, y, z);
        this.positionWorldSpace = this.position;
        this.color = color;
        this.uv = new Vec2D(0, 0);
        this.normal = new Vec3D(0, 0, 1);
    }

    public Vertex(Point3D position, Col color) {
        this.position = position;
        this.positionWorldSpace = position; // OPRAVENO
        this.color = color;
        this.uv = new Vec2D(0, 0);
        this.normal = new Vec3D(0, 0, 1);
    }

    public Point3D getPosition() {
        return position;
    }

    public Point3D getPositionWorldSpace() {
        return positionWorldSpace;
    }

    public Col getColor() {
        return color;
    }

    public Vec2D getUv() {
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
                positionWorldSpace.mul(d),
                color.mul(d),
                uv.mul(d),
                normal.mul(d)
        );
    }

    @Override
    public Vertex add(Vertex v) {
        return new Vertex(
                position.add(v.getPosition()),
                positionWorldSpace.add(v.getPositionWorldSpace()),
                color.add(v.getColor()),
                uv.add(v.getUv()),
                normal.add(v.getNormal())
        );
    }
}