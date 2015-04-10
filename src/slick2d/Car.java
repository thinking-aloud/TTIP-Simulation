package slick2d;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class Car {

    private final boolean horizontal;
    private int x;
    private int y;
    private final Image image;

    // private, can't be called from outside
    private Car(int x, int y, boolean horizontal) throws SlickException {
        this.x = x;
        this.y = y;
        this.horizontal = horizontal;

        image = new Image("res/car.png");

        if (!horizontal) {
            image.rotate(90);
        }
    }

    // static factory methods
    public static Car createHorizontalCar(int row) throws SlickException {
        int x = 0;
        int y = 96 + (row * 192);
        return new Car(x, y, true);
    }

    public static Car createVerticalCar(int column) throws SlickException {
        int x = 74 + (column * 192);
        int y = 0;
        return new Car(x, y, false);
    }

    public void move() {
        if (horizontal) {
            x++;
            if (x++ > 1152) {
                x = 0;
            }
        } else {
            y++;
            if (y++ > 768) {
                y = 0;
            }
        }
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

}
