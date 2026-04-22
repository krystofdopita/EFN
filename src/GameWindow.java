import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class GameWindow extends JFrame {
    boolean inBush = false;
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

    class GamePanel extends JPanel implements KeyListener {
        //spawn
        Player player = new Player(710, 350);
        //rychlost hrace
        int speed = 20;


        boolean inventoryOpen = false;
        int cameraX, cameraY;

        final int WORLD_W = 5000;
        final int WORLD_H = 5000;

        boolean up, down, left, right;
        boolean[] keys = new boolean[10];
        Bush[] bushes = {
                // 67 kvadrant
                new Bush(1500, 100, 90), new Bush(3200, 200, 300), new Bush(4200, 600, 150), new Bush(4500, 1200, 400),

                // ii kvadrant
                new Bush(200, 200, 80), new Bush(700, 600, 120), new Bush(900, 1200, 200), new Bush(1000, 1300, 100),

                // iii kvadrant
                new Bush(600, 3500, 280), new Bush(1200, 3200, 90), new Bush(1800, 3000, 350), new Bush(1500, 4300, 500), new Bush(1700, 4100, 180),

                // iv kvadrant
                new Bush(1800, 1800, 70), new Bush(3200, 1800, 250), new Bush(3000, 3500, 160), new Bush(3600, 3000, 450), new Bush(4400, 2500, 110), new Bush(4200, 4200, 220), new Bush(4700, 3600, 130)
        };

        Rectangle[] water = {
                new Rectangle(0, 0, 50, WORLD_H),
                new Rectangle(0, 0, WORLD_W, 50),
                new Rectangle(WORLD_W - 50, 0, 50, WORLD_H),
                new Rectangle(0, WORLD_H - 50, WORLD_W, 50)
        };

        Building[] walls = {
                // velka vila
                new Building(2000, 500, 1000, 800, 2400, 1300, 80, 10,0),
                // masazni chatka
                new Building(2400, 2400, 500, 500, 2625, 2900, 80, 10,1),
                // mala vila
                new Building(3200, 4100, 800, 600, 4000, 4350, 10, 80,2),
                // kumbal
                new Building(400, 2500, 450, 350, 400-10, 2650, 10, 80,3),
                //pristav
                new Building(600, 0, 250, 300, 600 + 250/2 - 80/2, 300, 80, 10,4)};


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
            keys[3] = true;
            player.inventory.add("67");

        }

        void update() {

            int newX = player.x;
            int newY = player.y;

            if (up) newY -= speed;
            if (down) newY += speed;
            if (left) newX -= speed;
            if (right) newX += speed;

            // hranice sveta
            newX = Math.max(0, Math.min(newX, WORLD_W - 30));
            newY = Math.max(0, Math.min(newY, WORLD_H - 30));

            Rectangle p = player.getBounds(newX, newY);
            if (inventoryOpen) return;

            // vytlaceni z vody
            for (Rectangle w : water) {
                if (p.intersects(w)) {

                    // tlaci hrace na predchozi pozici
                    if (up) newY += speed;
                    if (down) newY -= speed;
                    if (left) newX += speed;
                    if (right) newX -= speed;


                    newX = player.x;
                    newY = player.y;
                }
            }

            //schovani v keri zabarveni
            inBush = false;
            for (Bush b : bushes) {
                if (b.intersects(p)) {
                    inBush = true;
                    break;
                }
            }

            for (Building b : walls) {

                if (p.intersects(b.body)) {
                    return;
                }

                if (p.intersects(b.door)) {
                    if (!keys[b.keyId]) {
                        p.x -= 10;
                    }
                }
            }

            player.move(newX, newY);



            // kamera
            cameraX = player.x - getWidth() / 2 + 15;
            cameraY = player.y - getHeight() / 2 + 15;

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
            g.setColor(new Color(145, 158, 0));
            g.fillRect(150, 150, 150, WORLD_H - 300);
            g.fillRect(150, 150, WORLD_W - 300, 150);
            g.fillRect(WORLD_W - 300, 150, 150, WORLD_H - 300);
            g.fillRect(150, WORLD_H - 300, WORLD_W - 300, 150);



            // zbarveni dveri pri zamknuti a odemceni
            for (Building b : walls) {

                g.setColor(Color.GRAY);
                g.fillRect(b.body.x, b.body.y, b.body.width, b.body.height);

                boolean unlocked = keys[b.keyId];

                g.setColor(unlocked ? Color.GREEN : Color.RED);
                g.fillRect(b.door.x, b.door.y, b.door.width, b.door.height);
            }

            //pristav
            g.setColor(new Color(124, 81, 0));
            g.fillRect(200*3,0,250,300);
            g.setColor(new Color(80, 68, 0));
            g.fillRect(200*3,0,5*3,300);
            g.fillRect(600+250-15,0,15,300);

            //vybarveni masazni chatky
            g.setColor(new Color(0, 126, 152));
            g.fillRect(2400,2400,50,500);
            g.fillRect(2500,2400,50,500);
            g.fillRect(2600,2400,50,500);
            g.fillRect(2700,2400,50,500);
            g.fillRect(2800,2400,50,500);
            g.fillRect(2900,2400,50,500);
            g.setColor(new Color(207, 207, 207));
            g.fillRect(2450,2400,50,500);
            g.fillRect(2550,2400,50,500);
            g.fillRect(2650,2400,50,500);
            g.fillRect(2750,2400,50,500);
            g.fillRect(2850,2400,50,500);

            //zlaty kruh
            g.setColor(Color.ORANGE);
            g.fillOval(2400+250/4+15, 2400+250/4-10, 400, 400);


            // kere
            g.setColor(new Color(0, 120, 0));
            for (Bush b : bushes) {
                g2.fill(b.bush);
            }

            // klic


            // hrac
            if (inBush) {
                // schovany hrac
                g.setColor(new Color(0, 72, 255));
            } else {
                g.setColor(Color.CYAN);
            }

            g.fillRect(player.x, player.y, 30, 30);
            if (inventoryOpen) {
                g.setColor(new Color(0, 0, 0, 180));
                g.fillRect(cameraX + 100, cameraY + 100, 300, 400);

                g.setColor(Color.WHITE);
                g.drawString("INVENTÁŘ:", cameraX + 120, cameraY + 130);

                int i = 0;
                for (String item : player.inventory) {
                    g.drawString("- " + item, cameraX + 120, cameraY + 160 + (i * 20));
                    i++;
                }
            }
        }

        // input pohyb
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_W -> up = true;
                case KeyEvent.VK_S -> down = true;
                case KeyEvent.VK_A -> left = true;
                case KeyEvent.VK_D -> right = true;
                case KeyEvent.VK_I -> inventoryOpen = !inventoryOpen;
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