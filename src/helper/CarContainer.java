package helper;

import domain.Car;
import domain.Roxel;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class CarContainer implements Runnable {

    private final Car car;
    private final Image image;

    private static final int horizontalOffsetX = 17;
    private static final int horizontalOffsetY = 32;
    private static final int verticalOffsetX = 10;
    private static final int verticalOffsetY = 23;
    private static final int tileSize = 64;

    private final XapHelper xapHelper;

    public CarContainer(Car car) throws SlickException {
        this.car = car;
        this.xapHelper = new XapHelper();

        if (car.getDrivingDirection() == Car.DrivingDirection.EAST) {
            image = new Image("res/car_red.png");
        } else {
            image = new Image("res/car_blue.png");
            image.rotate(90);
        }
    }

    //
    // public methods
    //
    public void move() {
        Roxel next = getNextRoxel();
        Roxel oldRoxel = car.getOccupiedRoxel();

        car.setOccupiedRoxel(next);
        xapHelper.updateCar(car);
        xapHelper.releaseRoxel(oldRoxel);
    }

    public Image getImage() {
        return image;
    }

    // returns the position of the car in pixels
    public Integer getX() {
        Roxel roxel = car.getOccupiedRoxel();

        if (roxel == null) {
            return null;
        }

        int x = roxel.getX() * tileSize;
        if (car.getDrivingDirection() == Car.DrivingDirection.EAST) {
            x += horizontalOffsetX;
        } else {
            x += verticalOffsetX;
        }
        return x;
    }

    public Integer getY() {
        Roxel roxel = car.getOccupiedRoxel();

        if (roxel == null) {
            return null;
        }

        int y = roxel.getY() * tileSize;
        if (car.getDrivingDirection() == Car.DrivingDirection.EAST) {
            y += horizontalOffsetY;
        } else {
            y += verticalOffsetY;
        }
        return y;
    }

    //
    // private methods
    //
    private Roxel getNextRoxel() {
        Roxel next;
        Roxel roxel = car.getOccupiedRoxel();

        switch (car.getDrivingDirection()) {
            case EAST:
                next = xapHelper.takeRoxelById(roxel.getEast());
                break;
            case SOUTH:
                next = xapHelper.takeRoxelById(roxel.getSouth());
                break;
            case NORTH:
                next = xapHelper.takeRoxelById(roxel.getNorth());
                break;
            case WEST:
                next = xapHelper.takeRoxelById(roxel.getWest());
                break;
            default:
                next = roxel;
                break;
        }
        if (next != null) {
            return next;
        }

        return roxel;
    }

    @Override
    public void run() {
        while (true) {
            move();
            try {
                Thread.sleep(car.getSpeed());
            } catch (InterruptedException ex) {
                Logger.getLogger(CarContainer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}
