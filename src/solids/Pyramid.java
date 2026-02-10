package solids;

import transforms.Point3D;

public class Pyramid extends Solid {
    public Pyramid() {
        // Podstava
        vertexBuffer.add(new Point3D(-0.5, -0.5, 0));
        vertexBuffer.add(new Point3D(0.5, -0.5, 0));
        vertexBuffer.add(new Point3D(0.5, 0.5, 0));
        vertexBuffer.add(new Point3D(-0.5, 0.5, 0));

        // Vrchol
        vertexBuffer.add(new Point3D(0, 0, 1));

        // Obvod podstavy
        addIndices(0, 1, 1, 2, 2, 3, 3, 0);
        // Hrany ke špičce
        addIndices(0, 4, 1, 4, 2, 4, 3, 4);
    }



}
