package slick2d;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class Car {

    private static final int horizontalOffsetX = 17;
    private static final int verticalOffsetY = 23;
    private static final int tileSize = 64;
    private final boolean horizontal;
    private final Image image;
    private int x;
    private int y;

    // private, can't be called from outside
    private Car(int x, int y, boolean horizontal) throws SlickException {
        this.horizontal = horizontal;
        this.x = x;
        this.y = y;

        image = new Image("res/car.png");

        if (!horizontal) {
            image.rotate(90);
        }
    }

    // static factory methods
    public static Car createHorizontalCar(int row) throws SlickException {
        int x = horizontalOffsetX;
        int y = (tileSize + 32) + (row * 3 * tileSize);
        return new Car(x, y, true);
    }

    public static Car createVerticalCar(int column) throws SlickException {
        int x = (tileSize + 10) + (column * 3 * tileSize);
        int y = verticalOffsetY;
        return new Car(x, y, false);
    }

    public void move() {

        if (horizontal) {
            if (!isOccupied(x + tileSize, y)) {
                if (x + tileSize < 1152) {
                    x += tileSize;
                } else {
                    x = horizontalOffsetX;
                }
            }
        } else {
            if (!isOccupied(x, y + tileSize)) {
                if (y + tileSize < 768) {
                    y += tileSize;
                } else {
                    y = verticalOffsetY;
                }
            }
        }
    }

    private boolean isOccupied(int x, int y) {
        return false;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Image getImage() {
        return image;
    }

    public int getCurrentXTile() {
        System.out.println("x: " + x);
        return x / tileSize;
    }

    public int getCurrentYTile() {
        System.out.println("y: " + y);
        return y / tileSize;
    }

}
