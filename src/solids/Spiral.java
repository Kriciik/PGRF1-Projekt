package solids;

import transforms.Point3D;

public class Spiral extends Solid {

    public Spiral() {
        // parametry spirály
        int points = 100;
        double turns = 10.0;
        double height = 2.0;
        double radius = 0.5;

        for (int i = 0; i < points; i++) {
            // od 0 do 1
            double t = (double) i / (points - 1);

            double angle = turns * 2.0 * Math.PI * t;

            double x = radius * Math.cos(angle);
            double y = radius * Math.sin(angle);
            double z = height * t;

            vertexBuffer.add(new Point3D(x, y, z));
            if (i > 0) {
                addIndices(i - 1, i);
            }
        }
    }
}