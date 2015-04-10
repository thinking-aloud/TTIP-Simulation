package slick2d;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class Car {

    private final boolean horizontal;
    private float x;
    private float y;
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
        int x = 17;
        int y = 96 + (row * 192);
        return new Car(x, y, true);
    }

    public static Car createVerticalCar(int column) throws SlickException {
        int x = 74 + (column * 192);
        int y = 23;
        return new Car(x, y, false);
    }

    public void move() {
        int step = 64;
        
        if (horizontal) {
            if (x + step < 1152) {
                x = x + step;
            } else {
                x = 17;
            }
        } else {
            if (y + step < 768) {
                y = y + step;
            } else {
                y = 17;
            }
        }
    }
    
    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public Image getImage() {
        return image;
    }

}
