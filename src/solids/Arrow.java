package solids;

import transforms.Point3D;

public class Arrow extends Solid {
    public Arrow() {
        // TODO: naplním VB - v souřadnicích rasteru
        // TODO: naplním IB

        //špička šipky
        vertexBuffer.add(new Point3D(200, 100, 0));

        //konec tyčky
        vertexBuffer.add(new Point3D(200, 300, 0));

        //levé rameno
        vertexBuffer.add(new Point3D(150, 150, 0));

        //pravé rameno
        vertexBuffer.add(new Point3D(250, 150, 0));

        // tyčka
        indexBuffer.add(1);
        indexBuffer.add(0);

        // levé rameno
        indexBuffer.add(0);
        indexBuffer.add(2);

        // pravé rameno
        indexBuffer.add(0);
        indexBuffer.add(3);
    }

}
