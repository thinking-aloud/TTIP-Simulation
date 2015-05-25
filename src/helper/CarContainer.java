package helper;

import domain.Car;
import domain.Roxel;
import gui.Main;
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
        Roxel template = new Roxel((int) car.getX(), (int) car.getY());
        template.setOccupied(true);
        Roxel currentRoxel = xapHelper.takeRoxel(template);

        if (currentRoxel != null) {
            Roxel nextRoxel = takeNextRoxel(currentRoxel);

            if (nextRoxel != null) {
                if (!nextRoxel.isOccupied()
                        && xapHelper.isGreen(nextRoxel, car.getDrivingDirection())) {

                    // set next roxel occupied
                    nextRoxel.setOccupied(true);

                    // move car
                    car.setX(nextRoxel.getX());
                    car.setY(nextRoxel.getY());
                    xapHelper.writeToTupelspace(car);

                    // pass old roxel to trafficLight or TS
                    currentRoxel.setOccupied(false);
                }
                xapHelper.writeToTupelspace(nextRoxel);
            }

            // passes the Roxel to the traffic process
            if (trafficControl && currentRoxel.isJunction()) {
                currentRoxel.setOpenDirection(Car.Direction.TODECIDE);
            }
            xapHelper.writeToTupelspace(currentRoxel);
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

    private Roxel takeNextRoxel(Roxel currentRoxel) {
        switch (car.getDrivingDirection()) {
            case EAST:
                if(currentRoxel.getX() < Main.mapWidth -1) {
                    Roxel template = new Roxel(currentRoxel.getX() + 1, 
                            currentRoxel.getY(), Car.Direction.EAST);
                    return xapHelper.takeRoxel(template);
                } else {
                    Roxel template = new Roxel(0, 
                            currentRoxel.getY(), Car.Direction.EAST);
                    return xapHelper.takeRoxel(template);
                }
            case SOUTH:
                if(currentRoxel.getY() < Main.mapHeight - 1) {
                    Roxel template = new Roxel(currentRoxel.getX(), 
                            currentRoxel.getY() + 1, Car.Direction.SOUTH);
                    return xapHelper.takeRoxel(template);
                } else {
                    Roxel template = new Roxel(currentRoxel.getX(), 
                            0, Car.Direction.SOUTH);
                    return xapHelper.takeRoxel(template);
                }
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
    
       public Image getImage() {
        return image;
    }

}
