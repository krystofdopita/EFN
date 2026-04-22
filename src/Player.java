import java.awt.Rectangle;
import java.util.ArrayList;

class Player {

    // pozice
    int x, y;
    int size = 30;
    int speed = 20;

    // pohyb
    boolean up, down, left, right;

    // stav
    boolean inBush = false;

    // inventar
    ArrayList<String> inventory = new ArrayList<>();

    public Player(int startX, int startY) {
        this.x = startX;
        this.y = startY;
    }

    // pohyb
    public void move(int newX, int newY) {
        this.x = newX;
        this.y = newY;
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, size, size);
    }

    public Rectangle getBounds(int nextX, int nextY) {
        return new Rectangle(nextX, nextY, size, size);
    }

    // inventář
    public void addItem(String item) {
        if (!inventory.contains(item)) {
            inventory.add(item);
            System.out.println("Získal jsi: " + item);
        }
    }

    public boolean hasItem(String item) {
        return inventory.contains(item);
    }

    // vypsani inventare
    public void printInventory() {
        System.out.println("=== INVENTORY ===");
        if (inventory.isEmpty()) {
            System.out.println("EMPTY");
            return;
        }

        for (String item : inventory) {
            System.out.println("- " + item);
        }
    }
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    //
    public void stopMovement() {
        up = down = left = right = false;
    }
}