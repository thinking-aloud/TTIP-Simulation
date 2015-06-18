package helper;

import domain.Car;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Point;

public class CarContainer {
    
    private final Car car;
    private final Image image;

    private static final int horizontalOffsetX = 17;
    private static final int horizontalOffsetY = 32;
    private static final int verticalOffsetX = 10;
    private static final int verticalOffsetY = 23;
    private static final int tileSize = 64;

    public CarContainer(Car car) throws SlickException {
        this.car = car;

        switch (car.getDrivingDirection()) {
            case EAST:
                image = new Image("res/car_red.png");
                break;
            case SOUTH:
                image = new Image("res/car_blue.png");
                image.rotate(90);
                break;
            default:
                image = new Image("res/car_red.png");
        }
    }

    // returns the position of the car in pixels
    public Point getPosition() {
        float x = car.getX() * tileSize;
        float y = car.getY() * tileSize;
        switch (car.getDrivingDirection()) {
            case EAST:
                x += horizontalOffsetX;
                y += horizontalOffsetY;
                break;
            case SOUTH:
                x += verticalOffsetX;
                y += verticalOffsetY;
        }

        return new Point(x, y);
    }
    
       public Image getImage() {
        return image;
    }

}
