package slick2d;

import gigaspaces.XapHelper;
import java.awt.Point;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class Car {

    private static final int horizontalOffsetX = 17;
    private static final int horizontalOffsetY = 32;
    private static final int verticalOffsetX = 10;
    private static final int verticalOffsetY = 23;
    private static final int tileSize = 64;
    private final boolean horizontal;
    private final Image image;
    private Point position; // messured in tiles
    private final XapHelper xapHelper;

    // private, can't be called from outside
    private Car(Point point, boolean horizontal) throws SlickException {
        this.horizontal = horizontal;
        position = point;
        
        xapHelper = new XapHelper();

        xapHelper.setOccupied(position, true);


        if (!horizontal) {
            image = new Image("res/car_red.png");
            image.rotate(90);
        } else {
            image = new Image("res/car_blue.png");
        }
    }

    //
    // static factory methods
    //
    public static Car createHorizontalCar(int column, int row) throws SlickException {
        int x = column;
        int y = (3 * row) + 1; // 3 because every 3rd tile has a street and one offset
        return new Car(new Point(x, y), true);
    }

    public static Car createVerticalCar(int column, int row) throws SlickException {
        int x = (3 * column) + 1;
        int y = row;
        return new Car(new Point(x, y), false);
    }

    //
    // public methods
    //
    public void move() {
        if (!xapHelper.isOccupied(getNextTile())) {
            xapHelper.setOccupied(getNextTile(), true);
            xapHelper.setOccupied(position, false);

            position = getNextTile();
        }
    }

    public Image getImage() {
        return image;
    }

    // returns the position of the car in pixels
    public int getX() {
        int x = position.x * tileSize;
        if (horizontal) {
            x += horizontalOffsetX;
        } else {
            x += verticalOffsetX;
        }
        return x;
    }

    public int getY() {
        int y = position.y * tileSize;
        if (horizontal) {
            y += horizontalOffsetY;
        } else {
            y += verticalOffsetY;
        }
        return y;
    }

    //
    // private methods
    //
    private Point getNextTile() {
        int x = position.x;
        int y = position.y;

        if (horizontal) {
            if (x < 17) {
                x++;
            } else {
                x = 0;
            }
        } else {
            if (y < 11) {
                y++;
            } else {
                y = 0;
            }
        }

        return new Point(x, y);
    }

}
