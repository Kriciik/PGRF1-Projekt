package model;

import transforms.Col;
import transforms.Mat4Identity;
import transforms.Point3D;
import java.util.ArrayList;

public class Axes extends Solid {

    public Axes() {
        super(new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new Mat4Identity());

        Col red = new Col(255, 0, 0);
        Col green = new Col(0, 255, 0);
        Col blue = new Col(0, 0, 255);

        // Osa X
        getVertexBuffer().add(new Vertex(new Point3D(0, 0, 0), red));      // 0: Počátek
        getVertexBuffer().add(new Vertex(new Point3D(1, 0, 0), red));      // 1: Špička
        getVertexBuffer().add(new Vertex(new Point3D(0.8, -0.1, 0), red)); // 2: Základna šipky 1
        getVertexBuffer().add(new Vertex(new Point3D(0.8, 0.1, 0), red));  // 3: Základna šipky 2

        // Osa Y
        getVertexBuffer().add(new Vertex(new Point3D(0, 0, 0), green));      // 4: Počátek
        getVertexBuffer().add(new Vertex(new Point3D(0, 1, 0), green));      // 5: Špička
        getVertexBuffer().add(new Vertex(new Point3D(-0.1, 0.8, 0), green)); // 6: Základna šipky 1
        getVertexBuffer().add(new Vertex(new Point3D(0.1, 0.8, 0), green));  // 7: Základna šipky 2

        // Osa Z
        getVertexBuffer().add(new Vertex(new Point3D(0, 0, 0), blue));       // 8: Počátek
        getVertexBuffer().add(new Vertex(new Point3D(0, 0, 1), blue));       // 9: Špička
        getVertexBuffer().add(new Vertex(new Point3D(0, -0.1, 0.8), blue));  // 10: Základna šipky 1
        getVertexBuffer().add(new Vertex(new Point3D(0, 0.1, 0.8), blue));   // 11: Základna šipky 2

        //3 úsečky
        addIndices(
                0, 1,  // Osa X
                4, 5,  // Osa Y
                8, 9   // Osa Z
        );

        // 3 šipky
        addIndices(
                1, 2, 3,   // Šipka X
                5, 6, 7,   // Šipka Y
                9, 10, 11  // Šipka Z
        );

        getPartBuffer().add(new SolidPart(Topology.LINES, 3, 0));
        getPartBuffer().add(new SolidPart(Topology.TRIANGLES, 3, 6));
    }
}