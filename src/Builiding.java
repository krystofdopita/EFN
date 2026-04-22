import java.awt.*;

class Building {
    Rectangle body;
    Rectangle door;

    public Building(int x, int y, int w, int h, int dx, int dy, int dw, int dh) {
        body = new Rectangle(x, y, w, h);
        door = new Rectangle(dx, dy, dw, dh);
    }
}