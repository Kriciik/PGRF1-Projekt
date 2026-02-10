package solids;

import transforms.Cubic; // Import té knihovní třídy
import transforms.Mat4;
import transforms.Point3D;

public class SmartCubic extends Solid {

    public SmartCubic(Mat4 type, Point3D p1, Point3D p2, Point3D p3, Point3D p4) {
        Cubic cubicMath = new Cubic(type, p1, p2, p3, p4);
        // kroky
        int steps = 100;

        // generace bodů
        for (int i = 0; i <= steps; i++) {
            double t = (double) i / steps;
            Point3D p = cubicMath.compute(t);

            vertexBuffer.add(p);
            if (i > 0) {
                addIndices(i - 1, i);
            }
        }
    }
}