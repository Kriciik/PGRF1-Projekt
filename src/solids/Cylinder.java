package solids;

import transforms.Point3D;

public class Cylinder extends Solid {
    public Cylinder() {

        int segments = 20;
        double radius = 0.5;
        double height = 1.0;

        // generování bodů
        for (int i = 0; i < segments; i++) {

            double angle = 2.0 * Math.PI * i / segments;
            double x = radius * Math.cos(angle);
            double y = radius * Math.sin(angle);

            vertexBuffer.add(new Point3D(x, y, 0));
            vertexBuffer.add(new Point3D(x, y, height));
            addIndices(2 * i, 2 * i + 1);

            if (i > 0) {
                // spodek
                addIndices(2 * (i - 1), 2 * i);
                // vršek
                addIndices(2 * (i - 1) + 1, 2 * i + 1);
            }
        }

        //uzavření kruhu
        addIndices(2 * (segments - 1), 0);         // spodek
        addIndices(2 * (segments - 1) + 1, 1);     // vršek
    }


}
