import java.awt.*;

class Building {
    Rectangle body;
    Rectangle door;
    int keyId;

    public Building(int x, int y, int w, int h, int dx, int dy, int dw, int dh, int keyId) {
        body = new Rectangle(x, y, w, h);
        door = new Rectangle(dx, dy, dw, dh);
        this.keyId = keyId;
    }
}