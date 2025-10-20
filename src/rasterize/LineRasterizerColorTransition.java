package rasterize;

import raster.RasterBufferedImage;

import java.awt.*;

public class LineRasterizerColorTransition  extends LineRasterizer{
    public LineRasterizerColorTransition(RasterBufferedImage raster) {
        super(raster);
    }

    @Override
    public void rasterize(int x1, int y1, int x2, int y2) {
        int dx = x2 - x1;
        int dy = y2 - y1;

        float[] col1 = c1.getColorComponents(null);
        float[] col2 = c2.getColorComponents(null);

        if (Math.abs(dy) > Math.abs(dx)) {
            // Strmější čára - iteruju podle Y
            if (y1 > y2) {

                int tmp = y1; y1 = y2; y2 = tmp;
               // int tmpx = x1; x1 = x2; x2 = tmpx;

            }

            float k = (x2 - x1) / (float)(y2 - y1);

            for (int y = y1; y <= y2; y++) {
                int x = Math.round(x1 + (y - y1) * k);
                float t = (y - y1) / (float)(y2 - y1);

                float r = (1 - t) * col1[0] + t * col2[0];
                float g = (1 - t) * col1[1] + t * col2[1];
                float b = (1 - t) * col1[2] + t * col2[2];
                Color color = new Color(r, g, b);

                raster.setPixel(x, y, color);
            }
        } else {
            // Plochá čára - iteruju podle X
            if (x1 > x2) {
                int tmp = x1; x1 = x2; x2 = tmp;
               // int tmpy = y1; y1 = y2; y2 = tmpy;
            }
            float k = (y2 - y1) / (float)(x2 - x1);
            for (int x = x1; x <= x2; x++) {
                int y = Math.round(y1 + (x - x1) * k);
                float t = (x - x1) / (float)(x2 - x1);

                float r = (1 - t) * col1[0] + t * col2[0];
                float g = (1 - t) * col1[1] + t * col2[1];
                float b = (1 - t) * col1[2] + t * col2[2];
                Color color = new Color(r, g, b);

                raster.setPixel(x, y, color);
            }
        }
    }
}
