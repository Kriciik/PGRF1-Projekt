package model;

import transforms.Col;
import transforms.Mat4Identity;

import java.util.List;

public class Arrow extends Solid{
    // Line = 0,1; Triangle = 2,3,4

    public Arrow() {
        // TODO: až bude celý řetězec, nechci screen space souřadnice
        super(List.of(
                new Vertex(200, 300, 0.5), // v0
                new Vertex(250, 300, 0.5), // v1
                new Vertex(250, 320, 0.5, new Col(0xff0000)), // v2
                new Vertex(270, 300, 0.5, new Col(0xff0000)), // v3
                new Vertex(250, 280, 0.5, new Col(0xff0000)) // v4
             ),
            List.of(0,1,2,3,4),
            List.of(new SolidPart(Topology.LINES, 1, 0),
                    new SolidPart(Topology.TRIANGLES, 1, 2)),
            new Mat4Identity());
    }
}
