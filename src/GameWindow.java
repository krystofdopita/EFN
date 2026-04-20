import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class GameWindow extends JFrame {

    public GameWindow() {
        setTitle("Hra - místnost");
        setSize(800, 900);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);

        GamePanel panel = new GamePanel();
        add(panel);

        setVisible(true);
        panel.start();
    }

    // ----------------- HERNÍ PANEL -----------------
    class GamePanel extends JPanel implements KeyListener {

        int x = 100, y = 100;
        int speed = 4;

        boolean up, down, left, right;

        boolean hasKey = false;

        // místnost (zdi)
        Rectangle walls[] = {
                new Rectangle(0, 0, 800, 20),     // nahoře
                new Rectangle(0, 0, 20, 600),     // vlevo
                new Rectangle(0, 590, 800, 20),    // dole
                new Rectangle(780, 0, 20, 600)    // vpravo
        };

        // dveře (výstup)
        Rectangle door = new Rectangle(760, 250, 40, 100);

        public GamePanel() {
            setFocusable(true);
            addKeyListener(this);
        }

        public void start() {
            Thread t = new Thread(() -> {
                while (true) {
                    update();
                    repaint();

                    try {
                        Thread.sleep(16); // ~60 FPS
                    } catch (Exception e) {}
                }
            });
            t.start();
        }

        void update() {

            int newX = x;
            int newY = y;

            if (up) newY -= speed;
            if (down) newY += speed;
            if (left) newX -= speed;
            if (right) newX += speed;

            Rectangle player = new Rectangle(newX, newY, 30, 30);

            // kolize se zdmi
            for (Rectangle w : walls) {
                if (player.intersects(w)) {
                    return; // nepovol pohyb
                }
            }

            x = newX;
            y = newY;

            // klíč (pro demo ho vezmeme automaticky na místě)
            if (x > 300 && x < 330 && y > 300 && y < 330) {
                hasKey = true;
            }

            // dveře
            if (player.intersects(door)) {
                if (hasKey) {
                    System.out.println("Opustil jsi místnost");
                    // tady později přepneš mapu
                } else {
                    x -= 10; // blokace
                }
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            // pozadí
            g.setColor(new Color(40, 40, 40));
            g.fillRect(0, 0, getWidth(), getHeight());

            // zdi
            g.setColor(Color.GRAY);
            for (Rectangle w : walls) {
                g.fillRect(w.x, w.y, w.width, w.height);
            }

            // dveře
            g.setColor(hasKey ? Color.GREEN : Color.RED);
            g.fillRect(door.x, door.y, door.width, door.height);

            // klíč (vizuálně)
            if (!hasKey) {
                g.setColor(Color.YELLOW);
                g.fillOval(310, 310, 10, 10);
            }

            // hráč
            g.setColor(Color.CYAN);
            g.fillRect(x, y, 30, 30);
        }

        // -------- klávesy --------
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_W -> up = true;
                case KeyEvent.VK_S -> down = true;
                case KeyEvent.VK_A -> left = true;
                case KeyEvent.VK_D -> right = true;
            }
        }

        public void keyReleased(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_W -> up = false;
                case KeyEvent.VK_S -> down = false;
                case KeyEvent.VK_A -> left = false;
                case KeyEvent.VK_D -> right = false;
            }
        }

        public void keyTyped(KeyEvent e) {}
    }
}
