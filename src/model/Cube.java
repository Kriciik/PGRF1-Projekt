package model;

import transforms.Col;
import transforms.Mat4Identity;
import transforms.Point3D;
import transforms.Vec2D;

import java.util.ArrayList;

public class Cube extends Solid {

    public Cube() {
        super(new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new Mat4Identity());

        Col cLBF = new Col(255, 0, 0);     // (Červená)
        Col cRBF = new Col(255, 165, 0);   // (Oranžová)
        Col cRTF = new Col(255, 255, 0);   // (Žlutá)
        Col cLTF = new Col(0, 255, 0);     // (Zelená)

        Col cLBB = new Col(0, 0, 255);     // (Modrá)
        Col cRBB = new Col(75, 0, 130);    // (Indigo)
        Col cRTB = new Col(238, 130, 238); // (Fialová)
        Col cLTB = new Col(255, 255, 255); // (Bílá)

        Vec2D uv00 = new Vec2D(0, 0); // Vlevo nahoře
        Vec2D uv10 = new Vec2D(1, 0); // Vpravo nahoře
        Vec2D uv11 = new Vec2D(1, 1); // Vpravo dole
        Vec2D uv01 = new Vec2D(0, 1); // Vlevo dole

        // Přední stěna
        getVertexBuffer().add(new Vertex(new Point3D(-1, -1, 1), cLBF, uv01)); // 0
        getVertexBuffer().add(new Vertex(new Point3D( 1, -1, 1), cRBF, uv11)); // 1
        getVertexBuffer().add(new Vertex(new Point3D( 1,  1, 1), cRTF, uv10)); // 2
        getVertexBuffer().add(new Vertex(new Point3D(-1,  1, 1), cLTF, uv00)); // 3
        addIndices(0, 1, 2, 0, 2, 3);

        // Zadní stěna
        getVertexBuffer().add(new Vertex(new Point3D( 1, -1, -1), cRBB, uv01)); // 4
        getVertexBuffer().add(new Vertex(new Point3D(-1, -1, -1), cLBB, uv11)); // 5
        getVertexBuffer().add(new Vertex(new Point3D(-1,  1, -1), cLTB, uv10)); // 6
        getVertexBuffer().add(new Vertex(new Point3D( 1,  1, -1), cRTB, uv00)); // 7
        addIndices(4, 5, 6, 4, 6, 7);

        // Horní stěna
        getVertexBuffer().add(new Vertex(new Point3D(-1, 1,  1), cLTF, uv01)); // 8
        getVertexBuffer().add(new Vertex(new Point3D( 1, 1,  1), cRTF, uv11)); // 9
        getVertexBuffer().add(new Vertex(new Point3D( 1, 1, -1), cRTB, uv10)); // 10
        getVertexBuffer().add(new Vertex(new Point3D(-1, 1, -1), cLTB, uv00)); // 11
        addIndices(8, 9, 10, 8, 10, 11);

        // Spodní stěna
        getVertexBuffer().add(new Vertex(new Point3D(-1, -1, -1), cLBB, uv01)); // 12
        getVertexBuffer().add(new Vertex(new Point3D( 1, -1, -1), cRBB, uv11)); // 13
        getVertexBuffer().add(new Vertex(new Point3D( 1, -1,  1), cRBF, uv10)); // 14
        getVertexBuffer().add(new Vertex(new Point3D(-1, -1,  1), cLBF, uv00)); // 15
        addIndices(12, 13, 14, 12, 14, 15);

        // Pravá stěna
        getVertexBuffer().add(new Vertex(new Point3D(1, -1,  1), cRBF, uv01)); // 16
        getVertexBuffer().add(new Vertex(new Point3D(1, -1, -1), cRBB, uv11)); // 17
        getVertexBuffer().add(new Vertex(new Point3D(1,  1, -1), cRTB, uv10)); // 18
        getVertexBuffer().add(new Vertex(new Point3D(1,  1,  1), cRTF, uv00)); // 19
        addIndices(16, 17, 18, 16, 18, 19);

        // Levá stěna
        getVertexBuffer().add(new Vertex(new Point3D(-1, -1, -1), cLBB, uv01)); // 20
        getVertexBuffer().add(new Vertex(new Point3D(-1, -1,  1), cLBF, uv11)); // 21
        getVertexBuffer().add(new Vertex(new Point3D(-1,  1,  1), cLTF, uv10)); // 22
        getVertexBuffer().add(new Vertex(new Point3D(-1,  1, -1), cLTB, uv00)); // 23
        addIndices(20, 21, 22, 20, 22, 23);

        this.getPartBuffer().add(new SolidPart(Topology.TRIANGLES, getIndexBuffer().size() / 3, 0));
    }
}