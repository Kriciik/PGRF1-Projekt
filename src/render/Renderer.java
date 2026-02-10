package render;

import rasterize.LineRasterizer;
import solids.Solid;
import transforms.Mat4;
import transforms.Point3D;
import transforms.Vec3D;

import java.util.List;

public class Renderer {

    private LineRasterizer lineRasterizer;
    private int width, height;
    private Mat4 view, proj;
    public Renderer(LineRasterizer rasterizer,  int width, int height, Mat4 view, Mat4 proj) {
        this.lineRasterizer = rasterizer;
        this.width = width;
        this.height = height;
        this.view = view;
        this.proj = proj;

    }


    public void render(Solid solid) {

        List<Point3D> vertexBuffer = solid.getVertexBuffer();
        List<Integer> indexBuffer = solid.getIndexBuffer();

        if (vertexBuffer == null || indexBuffer == null || indexBuffer.isEmpty()) {
            return;
        }

        // optimalizace nasobení
        Mat4 mvp = solid.getModel().mul(view).mul(proj);

        for (int i = 0; i < indexBuffer.size() - 1; i += 2) {

            // dvojice hran
            int indexA = indexBuffer.get(i);
            int indexB = indexBuffer.get(i + 1);

            // model space
            Point3D pointA = vertexBuffer.get(indexA);
            Point3D pointB = vertexBuffer.get(indexB);

            pointA = pointA.mul(mvp);
            pointB = pointB.mul(mvp);

            if (pointA.getW() < 0.1 || pointB.getW() < 0.1) {
                continue;
            }

            pointA = pointA.mul(1/ pointA.getW());
            pointB = pointB.mul(1/ pointB.getW());

            //transformace do okna obrazovky
            Vec3D vecA = transformToWindow(pointA);
            Vec3D vecB = transformToWindow(pointB);

            lineRasterizer.rasterize(
                    (int)vecA.getX(), (int)vecA.getY(),
                    (int)vecB.getX(), (int)vecB.getY(),
                    solid.getColor()
            );
        }


    }


    public void setView(Mat4 view) {
        this.view = view;
    }


    public void setProj(Mat4 proj) {
        this.proj = proj;
    }


    private Vec3D transformToWindow(Point3D p){
        return new Vec3D(p)
                .mul(new Vec3D(1,-1, 1))
                .add(new Vec3D(1,1,0))
                .mul(new Vec3D((double) (width - 1) / 2, (double) (height - 1) /2, 1));

    }
}
