package raster;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Optional;

public class RasterBufferedImage implements Raster<Color> {

    private BufferedImage image;

    public RasterBufferedImage(int width, int height) {
        image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    }

    @Override
    public void setValue(int x, int y, Color color) {

        if(x < 0 || x > getWidth() - 1|| y < 0 || y > getHeight() - 1) {
            return;
        }else{
            image.setRGB(x, y, color.getRGB());
        }

    }

    @Override
    public Optional<Color> getValue(int x, int y) {

        if(x < 0 || x > getWidth() - 1|| y < 0 || y > getHeight() - 1) {
            return Optional.empty();
        }
        return Optional.of(image.getRGB(x, y));
    }

    @Override
    public int getWidth() {
        return image.getWidth();
    }

    @Override
    public int getHeight() {
        return image.getHeight();
    }

    @Override
    public void clear() {
        Graphics g = image.getGraphics();
        g.clearRect(0, 0, image.getWidth(), image.getHeight());
    }

    public BufferedImage getImage() {
        return image;
    }
}
