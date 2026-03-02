package model;

import transforms.Mat4Identity;

import java.util.List;

public class Arrow extends Solid{

    // TODO: udělat šipku ib = 0,1,2,3,4 List<partBuffer> = ["LINE_STRIP","TRIANGLE_něco"]
    // Line = 0,1; Triangle = 2,3,4

    public Arrow() {
        super(List.of(new Vertex(200, 300, 0.5),// Index 0: Začátek těla šipky (vlevo)
                        new Vertex(400, 300, 0.5), // Index 1: Konec těla šipky (vpravo, před špičkou)
                        new Vertex(400, 250, 0.5), // Index 2: Spodní roh trojúhelníku (špičky)
                        new Vertex(400, 350, 0.5), // Index 3: Horní roh trojúhelníku (špičky)
                        new Vertex(500, 300, 0.5)
                 ),
                List.of(0,1,2,3,4),
                List.of(new SolidPart(Topology.LINE_LIST, 1, 0),
                        new SolidPart(Topology.TRIANGLE_LIST, 1, 2)),
                new Mat4Identity());
    }
}
