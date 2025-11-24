package render;

import rasterize.LineRasterizer;
import solids.Solid;
import transforms.Point3D;

import java.util.List;

public class Renderer {

    private LineRasterizer rasterizer;

    public Renderer(LineRasterizer rasterizer) {
        this.rasterizer = rasterizer;
    }
    public void render(Solid solid){
        // TODO: DODĚLAT
        // - mám vb a ib
        // - procházím ib, podle indexu si vezmu 2 vrcholy z vb
        // - spojím je usečkou

        List<Point3D> vertexBuffer = solid.getVertexBuffer();
        List<Integer> indexBuffer = solid.getIndexBuffer();

        if (vertexBuffer == null || indexBuffer == null || indexBuffer.isEmpty()) {
            return;
        }

        for (int i = 0; i < indexBuffer.size() - 1; i += 2) {

            // dvojice hran
            int indexA = indexBuffer.get(i);
            int indexB = indexBuffer.get(i + 1);

            Point3D pointA = vertexBuffer.get(indexA);
            Point3D pointB = vertexBuffer.get(indexB);

            rasterizer.rasterize(
                    (int)pointA.getX(), (int)pointA.getY(),
                    (int)pointB.getX(), (int)pointB.getY()
            );
        }


    }

}
