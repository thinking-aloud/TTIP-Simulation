package helper;

import domain.Car;
import domain.Roxel;
import domain.TrafficLight;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class CarContainer implements Runnable {

    private final Car car;
    private final Image image;
    private final long speed = 1000; // sleep between car moves

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
        if (next != null && xapHelper.isGreen(next, this.car.getDrivingDirection())) {
            next = xapHelper.takeRoxelById(next.getId());
            Roxel oldRoxel = car.getOccupiedRoxel();
            car.setOccupiedRoxel(next);
            xapHelper.updateCar(car);
            if (oldRoxel.isJunction()) {
                xapHelper.passRoxelToTrafficLight(oldRoxel);
            } else {
                xapHelper.releaseRoxel(oldRoxel);
            }
        }
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
                next = xapHelper.getRoxelById(roxel.getEast());
                break;
            case SOUTH:
                next = xapHelper.getRoxelById(roxel.getSouth());
                break;
            case NORTH:
                next = xapHelper.getRoxelById(roxel.getNorth());
                break;
            case WEST:
                next = xapHelper.getRoxelById(roxel.getWest());
                break;
            default:
                next = null;
                break;
        }
        return next;
    }

    @Override
    public void run() {
        while (true) {
            move();
            try {
                Thread.sleep(speed);
            } catch (InterruptedException ex) {
                Logger.getLogger(CarContainer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}
