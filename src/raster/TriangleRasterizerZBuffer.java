package raster;

import model.Vertex;
import shader.Shader;
import utils.Lerp;

public class TriangleRasterizerZBuffer extends TriangleRasterizer {

    public TriangleRasterizerZBuffer(ZBuffer zBuffer) {
        super(zBuffer);
    }

    @Override
    public void rasterize(Vertex a, Vertex b, Vertex c, Shader shader) {

        if (a.getY() > b.getY()) { Vertex temp = a; a = b; b = temp; }
        if (a.getY() > c.getY()) { Vertex temp = a; a = c; c = temp; }
        if (b.getY() > c.getY()) { Vertex temp = b; b = c; c = temp; }

        Lerp<Vertex> lerp = new Lerp<>();

        int ay = (int) Math.round(a.getY());
        int by = (int) Math.round(b.getY());
        int cy = (int) Math.round(c.getY());

        int yMin = Math.max(0, ay);
        int yMax1 = Math.min(zBuffer.getHeight() - 1, by);
        int yMax2 = Math.min(zBuffer.getHeight() - 1, cy);
        int width = zBuffer.getWidth();

        // 1. část trojuhelníku
        for(int y = yMin; y  < yMax1; y++ ) {
            // Hrana AB
            double tAB = (by == ay) ? 0 : (y - ay) / (double) (by - ay);
            Vertex ab = lerp.lerp(a, b, tAB);

            // Hrana AC
            double tAC = (cy == ay) ? 0 : (y - ay) / (double) (cy - ay);
            Vertex ac = lerp.lerp(a, c, tAC);

            int xAB = (int) Math.round(ab.getX());
            int xAC = (int) Math.round(ac.getX());

            // check, if zprava doleva
            if(xAB > xAC) {
                Vertex temp = ab;
                ab = ac;
                ac = temp;

                xAB = (int) Math.round(ab.getX());
                xAC = (int) Math.round(ac.getX());
            }

            int xStart = Math.max(0, xAB);
            int xEnd = Math.min(width - 1, xAC);
            for(int x = xStart; x <= xEnd; x++) {
                double t = (xAC == xAB) ? 0 : (x - xAB) / (double) (xAC - xAB); // dělení 0 nn

                Vertex pixel = lerp.lerp(ab, ac, t);
                zBuffer.setPixelWithZTest(x, y, pixel.getZ(), shader.getColor(pixel));

            }
        }

        // 2. část trojuhelniku
        int yStart2 = Math.max(0, by);
        for(int y = yStart2; y  < yMax2; y++ ) {

            // Hrana BC
            double tBC = (cy == by) ? 0 : (double) (y - by) / (cy - by);
            Vertex bc = lerp.lerp(b, c, tBC);

            // Hraba AC
            double tAC = (cy == ay) ? 0 : (double) (y - ay) / (cy - ay);
            Vertex ac = lerp.lerp(a, c, tAC);

            // Vytažení X souřadnic
            int xBC = (int) Math.round(bc.getX());
            int xAC = (int) Math.round(ac.getX());

            // check, if zprava doleva
            if(xBC > xAC) {
                Vertex temp = bc;
                bc = ac;
                ac = temp;

                xBC = (int) Math.round(bc.getX());
                xAC = (int) Math.round(ac.getX());
            }

            int xStart = Math.max(0, xBC);
            int xEnd = Math.min(width - 1, xAC);

            for(int x = xStart; x <= xEnd; x++) {
                double t = (xAC == xBC) ? 0 : (x - xBC) / (double) (xAC - xBC); // dělení 0 nn

                Vertex pixel = lerp.lerp(bc, ac, t);
                zBuffer.setPixelWithZTest(x, y, pixel.getZ(), shader.getColor(pixel));
            }
        }


    }
}
