package raster;

import java.awt.*;
import java.util.OptionalInt;

public interface Raster {
    void setPixel(int x, int y, Color color);
    OptionalInt getPixel(int x, int y);
    int getWidth();
    int getHeight();
    void clear();
}
