package raster;

import transforms.Col;

import java.awt.*;

public class ZBuffer {
    private final Raster<Col> imageBuffer;
    private final Raster<Double> depthBuffer;

    public ZBuffer(Raster imageBuffer) {
        this.imageBuffer = imageBuffer;
        this.depthBuffer = new DepthBuffer(imageBuffer.getWidth(), imageBuffer.getHeight());
    }

    public void setPixelWithZTest(int x, int y, double z, Color color) {
        // TODO: načtu hodnotu z depth bufferu (nemusí existovat)
        // TODO: porovnám hodnotu s hodnotou Z, která přišla do metody
        // TODO: podle podmínky
            //TODO: 1. nedělám nic
            //TODO: 2. obarvím pixel, updatuju depth buffer

        for(int i = 0; i < this.depthBuffer.getWidth(); i++) {
            for(int j = 0; j < this.depthBuffer.getHeight(); j++) {

            }
        }
    }
}
