package helper;

import domain.Car;
import domain.Roxel;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Point;

public class CarContainer implements Runnable {

    private final Car car;
    private final Image image;

    private static final int horizontalOffsetX = 17;
    private static final int horizontalOffsetY = 32;
    private static final int verticalOffsetX = 10;
    private static final int verticalOffsetY = 23;
    private static final int tileSize = 64;
    private static final boolean trafficControl = true;

    private final XapHelper xapHelper;

    public CarContainer(Car car) throws SlickException {
        this.car = car;
        this.xapHelper = new XapHelper();

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

    //
    // public methods
    //
    public void move() {
//        Roxel template = new Roxel((int) car.getX(), (int) car.getY());
//        template.setOccupied(true);
//        Roxel currentRoxel = xapHelper.takeRoxel(template);
//
//        if (currentRoxel != null) {
//            Roxel nextRoxel = takeNextRoxel(currentRoxel);
//
//            if (nextRoxel != null) {
//                if (!nextRoxel.isOccupied()
//                        && xapHelper.isGreen(nextRoxel, car.getDrivingDirection())) {
//
//                    // set next roxel occupied
//                    nextRoxel.setOccupied(true);
//
//                    // move car
//                    car.setX(nextRoxel.getX());
//                    car.setY(nextRoxel.getY());
//                    xapHelper.writeToTupelspace(car);
//
//                    // pass old roxel to trafficLight or TS
//                    currentRoxel.setOccupied(false);
//                }
//                xapHelper.writeToTupelspace(nextRoxel);
//            }
//
//            // passes the Roxel to the traffic process
//            if (trafficControl && currentRoxel.isJunction()) {
////                xapHelper.passRoxelToTrafficLight(currentRoxel);
//                currentRoxel.setOpenDirection(Car.DrivingDirection.TODECIDE);
//            }
//            xapHelper.writeToTupelspace(currentRoxel);
//        }
    }

    public Image getImage() {
        return image;
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

    private Roxel takeNextRoxel(Roxel currentRoxel) {
        switch (car.getDrivingDirection()) {
            case EAST:
                Roxel template = new Roxel((int) currentRoxel.getEast().getX(), (int) currentRoxel.getEast().getY());
                return xapHelper.takeRoxel(template);
            case SOUTH:
                Roxel template2 = new Roxel((int) currentRoxel.getSouth().getX(), (int) currentRoxel.getSouth().getY());
                return xapHelper.takeRoxel(template2);
        }
        return null;
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
