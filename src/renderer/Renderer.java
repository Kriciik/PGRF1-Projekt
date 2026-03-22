package renderer;

import model.Solid;
import transforms.Mat4;

public interface Renderer {
    // Hlavní metoda pro vykreslení
    void render(Solid solid);

    // Nastavení matic pro kameru a zobrazení
    void setView(Mat4 view);
    void setProj(Mat4 proj);
}