package rasterize;

public class LineRasterizerTrivial extends LineRasterizer {

    public LineRasterizerTrivial(RasterBufferedImage raster) {
        super(raster);
    }

    @Override
    public void rasterize(int x1, int y1, int x2, int y2, int color) {

        int dx = x2 - x1;

        if (dx == 0) {
            if (y1 > y2) {
                int temp = y1;
                y1 = y2;
                y2 = temp;
            }
            for (int y = y1; y <= y2; y++) {
                raster.setPixel(x1, y, color);
            }
            return;
        }
        float k = (y2 - y1) / (float) (x2 - x1);
        float q = y1 - k * x1;

        if (Math.abs(k) > 1) {
            if (y1 > y2) {
                int tmpy = y1;
                y1 = y2;
                y2 = tmpy;
            }
            for (int y = y1; y <= y2; y++) {
                int x = Math.round((y - q) / k);
                raster.setPixel(x, y, color);
            }

        } else {

            if (x1 > x2) {
                int tmpx = x1;
                x1 = x2;
                x2 = tmpx;
            }
            for (int x = x1; x <= x2; x++) {
                int y = Math.round(k * x + q);
                raster.setPixel(x, y, color);
            }

            // TODO: dokončit

        }
    }
}
