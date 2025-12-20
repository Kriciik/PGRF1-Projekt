package solids;

import transforms.Col;
import transforms.Point3D;

public class PointMarker extends Solid {

    public PointMarker() {

        double s = 0.05; // Velikost křížku

        // Střed
        vertexBuffer.add(new Point3D(0, 0, 0)); // index 0

        // Ramena kříže ve směru os
        vertexBuffer.add(new Point3D(s, 0, 0));  // 1
        vertexBuffer.add(new Point3D(-s, 0, 0)); // 2
        vertexBuffer.add(new Point3D(0, s, 0));  // 3
        vertexBuffer.add(new Point3D(0, -s, 0)); // 4
        vertexBuffer.add(new Point3D(0, 0, s));  // 5
        vertexBuffer.add(new Point3D(0, 0, -s)); // 6

        // Čáry (všechny jdou ze středu)
        addIndices(0, 1, 0, 2); // Osa X
        addIndices(0, 3, 0, 4); // Osa Y
        addIndices(0, 5, 0, 6); // Osa Z
    }
}