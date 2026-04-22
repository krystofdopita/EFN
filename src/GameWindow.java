import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class GameWindow extends JFrame {

    public GameWindow() {
        setTitle("Hra");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);

        GamePanel panel = new GamePanel();
        add(panel);

        setVisible(true);
        panel.start();
    }
    //budovy
    class Building {
        Rectangle body;
        Rectangle door;

        public Building(int x, int y, int w, int h, int dx, int dy, int dw, int dh) {
            body = new Rectangle(x, y, w, h);
            door = new Rectangle(dx, dy, dw, dh);
        }
    }


    class GamePanel extends JPanel implements KeyListener {

        int x = 100, y = 100;
        int speed = 20;

        int cameraX, cameraY;

        final int WORLD_W = 5000;
        final int WORLD_H = 5000;

        boolean up, down, left, right;
        boolean hasKey = false;

        Building[] walls = {
                // velka vila
                new Building(
                        2000, 500,
                        1000, 800,
                        2400, 1300,
                        80, 10
                ),

                // masazni chatka
                new Building(
                        2400, 2400,
                        600, 500,
                        2650, 2900,
                        80, 10
                ),

                // mala vila
                new Building(
                        3200, 4100,
                        800, 600,
                        4000, 4350,   // pravý okraj budovy
                        10, 80
                ),

                // kumbal
                new Building(
                        400, 2500,
                        450, 350,
                        400-10, 2650,
                        10, 80
                )};

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
                        Thread.sleep(16);
                    } catch (Exception ignored) {}
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

            // hranice sveta
            newX = Math.max(0, Math.min(newX, WORLD_W - 30));
            newY = Math.max(0, Math.min(newY, WORLD_H - 30));

            Rectangle player = new Rectangle(newX, newY, 30, 30);

            for (Building b : walls) {

                if (player.intersects(b.body)) {
                    return;
                }

                if (player.intersects(b.door)) {
                    if (!hasKey) {
                        x -= 10;
                    } else {
                        System.out.println("Opustil jsi budovu");
                    }
                }
            }

            x = newX;
            y = newY;

            // klic
            if (x > 300 && x < 330 && y > 300 && y < 330) {
                hasKey = true;
            }

            // kamera
            cameraX = x - getWidth() / 2 + 15;
            cameraY = y - getHeight() / 2 + 15;

            cameraX = Math.max(0, Math.min(cameraX, WORLD_W - getWidth()));
            cameraY = Math.max(0, Math.min(cameraY, WORLD_H - getHeight()));
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            Graphics2D g2 = (Graphics2D) g;

            g2.translate(-cameraX, -cameraY);

            // OSTROV

            g.setColor(new Color(40, 100, 40));
            g.fillRect(0, 0, WORLD_W, WORLD_H);

            // voda
            g.setColor(new Color(0, 164, 207));
            g.fillRect(0, 0, 50, WORLD_H);
            g.fillRect(0, 0, WORLD_W, 50);
            g.fillRect(WORLD_W - 50, 0, 50, WORLD_H);
            g.fillRect(0, WORLD_H - 50, WORLD_W, 50);

            // pisek 1
            g.setColor(new Color(255, 209, 117));
            g.fillRect(50, 50, 100, WORLD_H - 100);
            g.fillRect(50, 50, WORLD_W - 100, 100);
            g.fillRect(250, 250, 250, 5);
            g.fillRect(WORLD_W - 150, 50, 100, WORLD_H - 100);
            g.fillRect(50, WORLD_H - 150, WORLD_W - 100, 100);

            // pisek 2
            g.setColor(new Color(177, 121, 0));
            g.fillRect(150, 150, 150, WORLD_H - 300);
            g.fillRect(150, 150, WORLD_W - 300, 150);
            g.fillRect(WORLD_W - 300, 150, 150, WORLD_H - 300);
            g.fillRect(150, WORLD_H - 300, WORLD_W - 300, 150);

            // budovy
            for (Building b : walls) {

                g.setColor(Color.GRAY);
                g.fillRect(b.body.x, b.body.y, b.body.width, b.body.height);

                g.setColor(hasKey ? Color.GREEN : Color.RED);
                g.fillRect(b.door.x, b.door.y, b.door.width, b.door.height);
            }

            // klic
            if (!hasKey) {
                g.setColor(Color.YELLOW);
                g.fillOval(310, 310, 10, 10);
            }

            // hrac
            g.setColor(Color.CYAN);
            g.fillRect(x, y, 30, 30);
        }

        // input pohyb
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