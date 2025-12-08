package solids;

import transforms.Cubic;
import transforms.Point3D;

public class Curve extends Solid {
    public Curve(){

        Cubic cubic = new Cubic(Cubic.BEZIER,
                new Point3D(0,0,0),
                new Point3D(0.25,0,0.5),
                new Point3D(0.75,0,0.75),
                new Point3D(1,0,0));
        int n = 100;
        for(int i = 0; i < n; i++){
            float step = i/(float) n;
            //vertexBuffer.add(new Point3D (step, 0, Math.cos(step)));
            vertexBuffer.add(cubic.compute(step));

        }

        for(int i = 0; i < vertexBuffer.size() - 1; i += 1){
            indexBuffer.add(i);
            indexBuffer.add(i+1);
        }

    }
}
