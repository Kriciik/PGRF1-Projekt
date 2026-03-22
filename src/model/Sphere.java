package model;

import transforms.Col;
import transforms.Mat4Identity;
import transforms.Point3D;
import transforms.Vec2D;

import java.util.ArrayList;

public class Sphere extends Solid {

    public Sphere(Point3D center, double radius, int rings, int sectors, Col color) {
        super(new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new Mat4Identity());
        // Generování vrcholů
        for (int r = 0; r <= rings; r++) {
            double v = (double) r / rings;

            double theta = r * Math.PI / rings; // 0-1PI
            double sinTheta = Math.sin(theta);
            double cosTheta = Math.cos(theta);

            for (int s = 0; s <= sectors; s++) {
                double u = (double) s / sectors;

                double phi = s * 2 * Math.PI / sectors; // 0-2PI
                double sinPhi = Math.sin(phi);
                double cosPhi = Math.cos(phi);

                double x = cosPhi * sinTheta;
                double y = sinPhi * sinTheta;

                getVertexBuffer().add(new Vertex(
                        new Point3D(
                                x * radius + center.getX(),
                                y * radius + center.getY(),
                                cosTheta * radius + center.getZ()
                        ),
                        color,
                        new Vec2D(u, v)
                ));
            }
        }
        // Generování indexů
        for (int r = 0; r < rings; r++) {
            for (int s = 0; s < sectors; s++) {
                int a = (r * (sectors + 1)) + s;
                int b = a + sectors + 1;
                int c = a + 1;
                int d = b + 1;

                addIndices(a, b, c);
                addIndices(c, b, d);
            }
        }
        getPartBuffer().add(new SolidPart(Topology.TRIANGLES,getIndexBuffer().size() / 3, 0));
    }
}