import java.awt.geom.Ellipse2D;
import java.awt.Rectangle;

class Bush {
    Ellipse2D.Double bush;

    public Bush(int x, int y, int size) {
        bush = new Ellipse2D.Double(x, y, size, size);
    }

    public boolean intersects(Rectangle player) {
        return bush.intersects(player);
    }
}