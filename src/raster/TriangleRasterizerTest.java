package raster;

import model.Vertex;
import transforms.Col;
import transforms.Point3D;

public class TriangleRasterizerTest {
    private final ZBuffer zBuffer;
    public TriangleRasterizerTest(ZBuffer zBuffer) {
        this.zBuffer = zBuffer;
    }

    public void rasterize(Vertex a, Vertex b, Vertex c) {

        int ax = (int) Math.round(a.getX());
        int ay = (int) Math.round(a.getY());
        double az = a.getZ();

        int bx = (int) Math.round(b.getX());
        int by = (int) Math.round(b.getY());
        double bz = b.getZ();

        int cx = (int) Math.round(c.getX());
        int cy = (int) Math.round(c.getY());
        double cz = c.getZ();

        // TODO: setřídit podle y => Ay <=By <= Cy prohazovat všechny souřadnice bodů (doma)

        // 1. část trojuhelníku
        for(int y = ay; y  < by; y++ ) {
            // Hrana AB
            double tAB = (double) (y - ax) / (bx - ax);
            int xAB = (int) Math.round((1 - tAB) * ax + (tAB * bx));
            double zAB = (int) Math.round((1 - tAB) * az + (tAB * bz));

            // Hrana AC
            double tAC = (double) (y - ax) / (cx - ax);
            int xAC = (int) Math.round((1 - tAC) * ax + (tAC * cx));
            double zAC = (int) Math.round((1 - tAB) * cz + (tAB * cz));

            // TODO: KOntrola: xAB < xAC (asi hotovo?)
            if(xAB > xAC) {
                int temp  = xAB;
                xAB = xAC;
                xAC = temp;
            }

            for(int x = xAB; x <= xAC; x++) {
                double t = (x-xAB) / (double) (xAC - xAB);
                double z = (1 - t) * zAC + t * zAB;
                zBuffer.setPixelWithZTest(x, y, z,new Col(0xFF0000));

            }
        }

        // TODO: 2. část trojuhelniku
        for(int y = by; y  < cy; y++ ) {

            // Hrana BC
            double tBC = (double) (y - bx) / (cx - bx);
            int xBC = (int) Math.round((1 - tBC) * ax + (tBC * bx));
            double zBC = (int) Math.round((1 - tBC) * az + (tBC * bz));

            // Hrana AC
            double tAC = (double) (y - ax) / (cx - ax);
            int xAC = (int) Math.round((1 - tAC) * ax + (tAC * cx));
            double zAC = (int) Math.round((1 - tBC) * cz + (tBC * cz));

            if(xBC > xAC) {
                int temp  = xBC;
                xBC = xAC;
                xAC = temp;
            }
            for(int x = xBC; x <= xAC; x++) {
                double t = (x-xBC) / (double) (xAC - xBC);
                double z = (1 - t) * zAC + t * zBC;
                zBuffer.setPixelWithZTest(x, y, z,new Col(0xFF0000));
            }
        }


    }
}
