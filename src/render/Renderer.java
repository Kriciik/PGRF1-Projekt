package render;

import rasterize.LineRasterizer;
import solids.Solid;
import transforms.Point3D;
import transforms.Vec3D;

import java.util.List;

public class Renderer {

    private LineRasterizer lineRasterizer;
    private int width, height;
    public Renderer(LineRasterizer rasterizer,  int width, int height) {
        this.lineRasterizer = rasterizer;
        this.width = width;
        this.height = height;
    }
    // TODO: DODĚLAT
    // - mám vb a ib
    // - procházím ib, podle indexu si vezmu 2 vrcholy z vb
    // - spojím je usečkou


    public void render(Solid solid) {



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

            //transformace do okna obrazovky
            Vec3D vecA = new Vec3D(pointA.getX(), pointA.getY(), pointA.getZ());
            Vec3D vecB = new Vec3D(pointB.getX(), pointB.getY(), pointB.getZ());

            // FIXME: Pro tohle vytvoříme metodu příště
            vecA = vecA
                    .mul(new Vec3D(1,-1, 1))
                    .add(new Vec3D(1,1,0))
                    .mul(new Vec3D((double) (width - 1) / 2, (double) (height - 1) /2, 1));
            vecB = vecB
                    .mul(new Vec3D(1,-1, 1))
                    .add(new Vec3D(1,1,0))
                    .mul(new Vec3D((double) (width - 1) / 2, (double) (height - 1) /2, 1));

            lineRasterizer.rasterize(
                    (int)vecA.getX(), (int)vecA.getY(),
                    (int)vecB.getX(), (int)vecB.getY()
            );
        }


    }

}
