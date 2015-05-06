package slick2d;

import gigaspaces.Car;
import gigaspaces.XapHelper;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class CarContainer implements Runnable {

    private Car car;
    private final Image image;

    private static final int horizontalOffsetX = 17;
    private static final int horizontalOffsetY = 32;
    private static final int verticalOffsetX = 10;
    private static final int verticalOffsetY = 23;
    private static final int tileSize = 64;

    private final XapHelper xapHelper;

    // private, can't be called from outside
    private CarContainer(Car car) throws SlickException {
        this.car = car;
        this.xapHelper = new XapHelper();

        if (this.car.getDrivingDirection() == Car.DrivingDirection.EAST) {
            image = new Image("res/car_red.png");
        } else {
            image = new Image("res/car_blue.png");
            image.rotate(90);
        }
    }

    //
    // static factory methods
    //
    /*public static CarContainer createHorizontalCar(String rox) throws SlickException {
     Car car = new Car(rox, Car.DrivingDirection.EAST);
     return new CarContainer(car);
     }

     public static CarContainer createVerticalCar(String rox) throws SlickException {
     Car car = new Car(rox, Car.DrivingDirection.SOUTH);
     return new CarContainer(car);
     }*/
    public static CarContainer createContainerWithExistingCar(Car car) throws SlickException {
        return new CarContainer(car);
    }

    //
    // public methods
    //
    public void move() {
        String next = getNextRoxel();
        if (!xapHelper.isOccupied(next)) {
            String oldPosition = getPositionRoxel();
            if (xapHelper.occupyRoxel(next, this.getCar().getId())) {
                this.car.setPositionRoxel(next);
                xapHelper.updateCar(this.car);
                xapHelper.releaseRoxel(oldPosition);
            }

        }
    }

    public String getId() {
        return this.getCar().getId();
    }

    public void setId(String id) {
        this.getCar().setId(id);
    }

    public Image getImage() {
        return image;
    }

    // returns the position of the car in pixels
    public Integer getX() {
        int x = xapHelper.getRoxelById(getPositionRoxel()).getX() * tileSize;
        if (this.getCar().getDrivingDirection() == Car.DrivingDirection.EAST) {
            x += horizontalOffsetX;
        } else {
            x += verticalOffsetX;
        }
        return x;
    }

    public Integer getY() {
        int y = xapHelper.getRoxelById(getPositionRoxel()).getY() * tileSize;
        if (this.getCar().getDrivingDirection() == Car.DrivingDirection.EAST) {
            y += horizontalOffsetY;
        } else {
            y += verticalOffsetY;
        }
        return y;
    }

    //
    // private methods
    //
    private String getNextRoxel() {
        String next;
        switch (this.getCar().getDrivingDirection()) {
            case EAST:
                next = xapHelper.getRoxelById(getPositionRoxel()).getEast();
                break;
            case SOUTH:
                next = xapHelper.getRoxelById(getPositionRoxel()).getSouth();
                break;
            case NORTH:
                next = xapHelper.getRoxelById(getPositionRoxel()).getNorth();
                break;
            case WEST:
                next = xapHelper.getRoxelById(getPositionRoxel()).getWest();
                break;
            default:
                next = getPositionRoxel();
                break;
        }
        return next;
    }

    /**
     * @return the positionRoxel
     */
    public String getPositionRoxel() {
        return this.getCar().getPositionRoxel();
    }

    /**
     * @return the car
     */
    public Car getCar() {
        return car;
    }

    @Override
    public void run() {
        while (true) {
            this.move();
            try {
                long pause = 1000 / this.car.getSpeed();
                Thread.sleep(pause);
            } catch (InterruptedException ex) {
                Logger.getLogger(CarContainer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
