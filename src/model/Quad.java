package model;


import transforms.Col;
import transforms.Mat4Identity;

import java.util.List;

public class Quad extends Solid{
    public Quad() {
        // TODO: až bude celý řetězec, nechci screen space souřadnice
        super(List.of(
                        new Vertex(-1, -1, 0, new Col(0xff0000)), // Levý dolní
                        new Vertex( 1, -1, 0, new Col(0x00ff00)), // Pravý dolní
                        new Vertex( 1,  1, 0, new Col(0x0000ff)), // Pravý horní
                        new Vertex(-1,  1, 0, new Col(0xffff00))  // Levý horní
                ),
                List.of(
                        0, 1, 2,
                        0, 2, 3
                ),
                List.of(
                        new SolidPart(Topology.TRIANGLES, 2, 0)
                ),
                new Mat4Identity()
        );
    }
}
