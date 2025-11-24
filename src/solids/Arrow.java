package solids;

import transforms.Point3D;

public class Arrow extends Solid {
    public Arrow() {
        // TODO: naplním VB - v souřadnicích rasteru
        // TODO: naplním IB

        //konec tyčky
        vertexBuffer.add(new Point3D(0.6f,0 , 0)); // v3

        //špička šipky
        vertexBuffer.add(new Point3D(-0.5f, 0, 0)); // v0

        //levé rameno
        vertexBuffer.add(new Point3D(0.5, 0.1, 0)); // v4
        //pravé rameno
        vertexBuffer.add(new Point3D(0.5,-0.1, 0)); // v2

        addIndices(0,1,0,2,0,3);
    }



}
