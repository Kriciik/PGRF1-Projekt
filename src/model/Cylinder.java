package model;

import transforms.Col;
import transforms.Mat4Identity;
import transforms.Point3D;
import transforms.Vec2D;

import java.util.ArrayList;

public class Cylinder extends Solid {

    public Cylinder(double radius, double height, int sectors, Col color) {
        super(new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new Mat4Identity());

        double halfHeight = height / 2.0;

        getVertexBuffer().add(new Vertex(new Point3D(0, 0, -halfHeight), color, new Vec2D(0.5, 0.5))); // 0
        getVertexBuffer().add(new Vertex(new Point3D(0, 0, halfHeight), color, new Vec2D(0.5, 0.5)));  // 1

        // Generování vrcholů
        for (int s = 0; s <= sectors; s++) {
            double u = (double) s / sectors;
            double phi = u * 2 * Math.PI;
            double x = Math.cos(phi) * radius;
            double y = Math.sin(phi) * radius;

            getVertexBuffer().add(new Vertex(new Point3D(x, y, -halfHeight), color, new Vec2D(u, 1))); // Spodní okraj
            getVertexBuffer().add(new Vertex(new Point3D(x, y, halfHeight), color, new Vec2D(u, 0)));  // Horní okraj
        }

        for (int s = 0; s < sectors; s++) {
            //zjištění sousedních bodů na obvodu
            int btm1 = 2 + (s * 2);
            int top1 = 3 + (s * 2);
            int btm2 = 2 + ((s + 1) * 2);
            int top2 = 3 + ((s + 1) * 2);
            // Plášť válce
            addIndices(btm1, btm2, top1);
            addIndices(top1, btm2, top2);
            // Spodní podstava
            addIndices(0, btm1, btm2);
            // Horní podstava
            addIndices(1, top2, top1);
        }
        getPartBuffer().add(new SolidPart(Topology.TRIANGLES, getIndexBuffer().size() / 3,0));
    }
}