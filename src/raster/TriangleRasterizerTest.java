package raster;

import model.Vertex;
import transforms.Col;
import transforms.Point3D;
import utils.Lerp;

public class TriangleRasterizerTest {
    private final ZBuffer zBuffer;
    public TriangleRasterizerTest(ZBuffer zBuffer) {
        this.zBuffer = zBuffer;
    }

    public void rasterize(Vertex a, Vertex b, Vertex c) {

        if (a.getY() > b.getY()) { Vertex temp = a; a = b; b = temp; }
        if (a.getY() > c.getY()) { Vertex temp = a; a = c; c = temp; }
        if (b.getY() > c.getY()) { Vertex temp = b; b = c; c = temp; }

        Lerp<Vertex> lerp = new Lerp<>();

        int ax = (int) Math.round(a.getX());
        int ay = (int) Math.round(a.getY());
        double az = a.getZ();

        int bx = (int) Math.round(b.getX());
        int by = (int) Math.round(b.getY());
        double bz = b.getZ();

        int cx = (int) Math.round(c.getX());
        int cy = (int) Math.round(c.getY());
        double cz = c.getZ();




        // 1. část trojuhelníku
        for(int y = ay; y  < by; y++ ) {
            // Hrana AB
            double tAB = (y - ay) / (double) (by - ay);
            Vertex ab = lerp.lerp(a, b, tAB);

            // Hrana AC
            double tAC = (y - ay) / (double) (cy - ay);
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

            for(int x = xAB; x <= xAC; x++) {
                double t = (xAC == xAB) ? 0 : (x - xAB) / (double) (xAC - xAB); // dělení 0 nn

                Vertex pixel = lerp.lerp(ab, ac, t);
                zBuffer.setPixelWithZTest(x, y, pixel.getZ(), pixel.getColor());

            }
        }

        // 2. část trojuhelniku
        for(int y = by; y  < cy; y++ ) {

            // Hrana BC
            double tBC = (double) (y - by) / (cy - by);
            Vertex bc = lerp.lerp(b, c, tBC);

            // Hrana AC
            double tAC = (double) (y - ay) / (cy - ay);
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

            for(int x = xBC; x <= xAC; x++) {
                double t = (xAC == xBC) ? 0 : (x - xBC) / (double) (xAC - xBC); // dělení 0 nn

                Vertex pixel = lerp.lerp(bc, ac, t);
                zBuffer.setPixelWithZTest(x, y, pixel.getZ(), pixel.getColor());
            }
        }


    }
}
