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

        if (a.getY() > b.getY()) {
            Vertex temp = a;
            a = b;
            b = temp;
        }
        if (a.getY() > c.getY()) {
            Vertex temp = a;
            a = c;
            c = temp;
        }
        if (b.getY() > c.getY()) {
            Vertex temp = b;
            b = c;
            c = temp;
        }

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
            int xAB = (int) Math.round((1 - tAB) * ax + tAB * bx);
            double zAB = (1 - tAB) * az + (tAB * bz);
            // TODO: spočítat barva (DONE)
            Col colAB = a.getColor().mul(1 - tAB).add(b.getColor().mul(tAB));
            Vertex ab = a.mul(1 - tAB).add(b.mul(tAB));
            // TODO: spočítat normála
            // TODO: spočítat UV


            // Hrana AC
            double tAC = (y - ay) / (double) (cy - ay);
            int xAC = (int) Math.round((1 - tAC) * ax + tAC * cx);
            double zAC = (1 - tAB) * cz + (tAB * cz);

            // finta TODO:
            Col colAC = a.getColor().mul(1 - tAC).add(b.getColor().mul(tAC));
            Vertex ac = a.mul(1 - tAC).add(b.mul(tAC));

            if(xAB > xAC) {
                int temp  = xAB;
                double tempZ = zAB;
                xAB = xAC;
                xAC = temp;
                zAB = zAC;
                zAC = tempZ;
            }

            for(int x = (int) Math.round(ab.getX()); x <= (int) Math.round(ac.getX()); x++) {
                double t = (x-xAB) / (double) (xAC - xAB);
                double z = (1 - t) * zAC + t * zAB;
                Col col = colAB.mul(1 - tAB).add(colAB.mul(tAB));
                zBuffer.setPixelWithZTest(x, y, z,new Col(65535));

            }
        }

        // 2. část trojuhelniku
        for(int y = by; y  < cy; y++ ) {

            // Hrana BC
            double tBC = (double) (y - by) / (cy - by);
            int xBC = (int) Math.round((1 - tBC) * bx + tBC * cx);
            double zBC = (1 - tBC) * cz + (tBC * bz);

            // Hrana AC
            double tAC = (double) (y - ay) / (cy - ay);
            int xAC = (int) Math.round((1 - tAC) * ax + (tAC * cx));
            double zAC = (1 - tAC) * az + tAC * cz;

            if(xBC > xAC) {
                int temp  = xBC;
                double tempZ = zBC;
                xBC = xAC;
                xAC = temp;
                zBC = zAC;
                zAC = tempZ;
            }
            for(int x = xBC; x <= xAC; x++) {
                double t = (x-xBC) / (double) (xAC - xBC);
                double z = (1 - t) * zBC + t * zAC;
                zBuffer.setPixelWithZTest(x, y, z,new Col(65535));
            }
        }


    }
}
