package raster;

import java.awt.*;

public interface Raster {
    void setPixel(int x, int y, Color color);
    int getPixel(int x, int y);
    int getWidth();
    int getHeight();
    void clear();
}
