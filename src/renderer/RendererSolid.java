package renderer;

import model.Solid;
import model.SolidPart;
import model.Vertex;
import raster.TriangleRasterizerTest;
import rasterize.LineRasterizer;
import transforms.Mat4;
import transforms.Mat4Identity;
import transforms.Point3D;
import utils.Lerp;

public class RendererSolid {
    private LineRasterizer lineRasterizer;
    private TriangleRasterizerTest triangleRasterizerTest;

    // Přidané proměnné pro zobrazovací řetězec
    private Mat4 view = new Mat4Identity();
    private Mat4 proj = new Mat4Identity();
    private int width;
    private int height;

    public RendererSolid(LineRasterizer lineRasterizer, TriangleRasterizerTest triangleRasterizerTest, int width, int height) {
        this.lineRasterizer = lineRasterizer;
        this.triangleRasterizerTest = triangleRasterizerTest;
        this.width = width;
        this.height = height;
    }

    public void render(Solid solid){
        Mat4 mvp = solid.getModel().mul(view).mul(proj);
        Lerp<Vertex> lerp = new Lerp<>();
        for(SolidPart part : solid.getPartBuffer()){
            switch(part.getType()){
                case POINTS:
                    // TODO: points
                case LINES:
                    int index = part.getStartIndex();

                    for(int i = 0; i < part.getCount(); i++){
                        int indexA = solid.getIndexBuffer().get(index++);
                        int indexB = solid.getIndexBuffer().get(index++);

                        Vertex a = solid.getVertexBuffer().get(indexA);
                        Vertex b = solid.getVertexBuffer().get(indexB);

                        Vertex aT = transformVertex(a, mvp);
                        Vertex bT = transformVertex(b, mvp);

                        // if bod za kamerou, nekreslím, usečku nekreslím
                        if (aT == null || bT == null) continue;

                        lineRasterizer.rasterize(
                                (int) Math.round(aT.getX()),
                                (int) Math.round(aT.getY()),
                                (int) Math.round(bT.getX()),
                                (int) Math.round(bT.getY())
                        );
                    }
                    break;

                case TRIANGLES:
                    // TODO: triangles
                    index = part.getStartIndex();

                    for(int i = 0; i < part.getCount(); i++){
                        int indexA = solid.getIndexBuffer().get(index++);
                        int indexB = solid.getIndexBuffer().get(index++);
                        int indexC = solid.getIndexBuffer().get(index++);

                        Vertex a = solid.getVertexBuffer().get(indexA);
                        Vertex b = solid.getVertexBuffer().get(indexB);
                        Vertex c = solid.getVertexBuffer().get(indexC);


//                        // transformace vrcholů
//                        Vertex aT = transformVertex(a, mvp);
//                        Vertex bT = transformVertex(b, mvp);
//                        Vertex cT = transformVertex(c, mvp);

                        if (a.getZ() > b.getZ()) { Vertex temp = a; a = b; b = temp; }
                        if (a.getZ() > c.getZ()) { Vertex temp = a; a = c; c = temp; }
                        if (b.getZ() > c.getZ()) { Vertex temp = b; b = c; c = temp; }

                        double zMin = 0;
                        // procházet vrcholy podle z, od max po min

                        if(a.getZ() < zMin){
                            continue;
                        }
                        if(b.getZ() < zMin){
                            // TODO: spočítat nový trojuhelník
                                // TODO: hledám vrchol ab, ac
                                // TODO: spočítat interpolační koeficienty a najít vrcholy

                            double ab =(zMin - a.getZ()) / (b.getZ() - a.getZ());
                            double ac =(zMin - a.getZ()) / (c.getZ() - a.getZ());

                            double tAB = (zMin - a.getZ()) / ( b.getZ() - a.getZ());


                            return;

                        }
                        if(c.getZ() < zMin)
                        {
                            // TODO: spočítat 2 nový trojuhelník
                            // TODO: rasterizovat

                        }

                        // TODO: dehomog

                        // TODO: transformace do okna
                        // TODO: Rasterizace

                        // if bod za kamerou, usečku nekreslím
                        if (aT == null || bT == null || cT == null) continue;

                        triangleRasterizerTest.rasterize(aT, bT, cT, solid.getShader());
                    }
                    break;
            }
        }
    }

    private Vertex transformVertex(Vertex v, Mat4 mvp) {
        Point3D p = v.getPosition().mul(mvp);
        // TODO: Fast clip
        // TODO: Ořezaní podle z
        // TODO:

        // if W <= 0, nevykreslím
        if (p.getW() <= 0) return null;

        // NDC
        double x = p.getX() / p.getW();
        double y = p.getY() / p.getW();
        double z = p.getZ() / p.getW();

        // test, jestli je mimo obrazovku
        if (x < -1 || x > 1 || y < -1 || y > 1 || z < 0 || z > 1) {
            return null;
        }
        double winX = (x + 1) / 2.0 * (width - 1);
        double winY = (1 - y) / 2.0 * (height - 1);

        return new Vertex(winX, winY, z, v.getColor());
    }

    public void setView(Mat4 view) { this.view = view; }
    public void setProj(Mat4 proj) { this.proj = proj; }

    public void setLineRasterizer(LineRasterizer lineRasterizer) {
        this.lineRasterizer = lineRasterizer;
    }
    public void setTriangleRasterizerTest(TriangleRasterizerTest triangleRasterizerTest) {
        this.triangleRasterizerTest = triangleRasterizerTest;
    }
}
