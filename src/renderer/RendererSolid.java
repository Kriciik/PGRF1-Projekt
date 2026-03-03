package renderer;

import model.Solid;
import model.SolidPart;
import model.Vertex;
import raster.TriangleRasterizerTest;
import rasterize.LineRasterizer;
import transforms.Mat4;

public class RendererSolid {
    private LineRasterizer lineRasterizer;
    private TriangleRasterizerTest triangleRasterizerTest;

    public RendererSolid(LineRasterizer lineRasterizer, TriangleRasterizerTest triangleRasterizerTest) {
        this.lineRasterizer = lineRasterizer;
        this.triangleRasterizerTest = triangleRasterizerTest;
    }

    public void render(Solid solid){
        for(SolidPart part : solid.getPartBuffer()){
            switch(part.getType()){
                case POINTS:
                    // TODO: points
                case LINES:
                    // TODO: lines
                    int index = part.getStartIndex();

                    for(int i = 0; i < part.getCount(); i++){
                        int indexA = solid.getIndexBuffer().get(index++);
                        int indexB = solid.getIndexBuffer().get(index++);

                        Vertex a = solid.getVertexBuffer().get(indexA);
                        Vertex b = solid.getVertexBuffer().get(indexB);

                        // TODO: Pronásobit MVP
                        // getModel().mul(view).mul(proj);

                        // TODO: ořezání

                        // TODO: Dehomogenizace

                        // TODO: transformace do okna

                        // TODO: rasterizace + z-buffer
                        lineRasterizer.rasterize(
                                (int) Math.round(a.getX()),
                                (int) Math.round(a.getY()),
                                (int) Math.round(b.getX()),
                                (int) Math.round(b.getY())
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

                        // --|--

                        triangleRasterizerTest.rasterize(a,b,c);
                    }

                    break;
            }
        }
    }

    public void setLineRasterizer(LineRasterizer lineRasterizer) {
        this.lineRasterizer = lineRasterizer;
    }

    public void setTriangleRasterizerTest(TriangleRasterizerTest triangleRasterizerTest) {
        this.triangleRasterizerTest = triangleRasterizerTest;
    }
}
