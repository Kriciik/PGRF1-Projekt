package model;

import transforms.Mat4Identity;

import java.util.List;

public class Arrow extends Solid{

    // TODO: udělat šipku ib = 0,1,2,3,4 List<partBuffer> = ["LINE_STRIP","TRIANGLE_něco"]
    // Line = 0,1; Triangle = 2,3,4

    public Arrow() {
        super(List.of(new Vertex(200, 300, 0.5) // ...
                 ),
                List.of(0,1,2,3,4),
                List.of(new SolidPart("TRIANGLE"), new SolidPart("",)),
                new Mat4Identity());
    }
}
