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
    private static final boolean trafficControl = false;

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
        Roxel template = new Roxel((int) car.getPosition().getX(), (int) car.getPosition().getY());
        template.setOccupied(true);
        Roxel currentRoxel = xapHelper.takeRoxel(template);
        
        if (currentRoxel != null) {
            Roxel nextRoxel = takeNextRoxel(currentRoxel);

            if (nextRoxel != null 
                    && !nextRoxel.isOccupied() 
                    && xapHelper.isGreen(nextRoxel, this.car.getDrivingDirection())) {
                
                // set next roxel occupied
                nextRoxel.setOccupied(true);
                xapHelper.writeToTupelspace(nextRoxel);

                // move car
                car.setPosition(new Point((int) nextRoxel.getX(), (int) nextRoxel.getY()));
                xapHelper.writeToTupelspace(car);

                // pass old roxel to trafficLight or TS
                currentRoxel.setOccupied(false);
            }

            // traffic is not working correctly. Cars go through the map just once.
            if (trafficControl) {
                if (currentRoxel.isJunction()) {
                    xapHelper.passRoxelToTrafficLight(currentRoxel);
                } else {
                    xapHelper.writeToTupelspace(currentRoxel);
                }
            } else {
                xapHelper.writeToTupelspace(currentRoxel);
            }

        }
    }

    public Image getImage() {
        return image;
    }

    // returns the position of the car in pixels
    public Point getPosition() {
        Point position = car.getPosition();

        if (position == null) {
            return null;
        }

        float x = position.getX() * tileSize;
        if (car.getDrivingDirection() == Car.DrivingDirection.EAST) {
            x += horizontalOffsetX;
        } else {
            x += verticalOffsetX;
        }

        float y = position.getY() * tileSize;
        if (car.getDrivingDirection() == Car.DrivingDirection.EAST) {
            y += horizontalOffsetY;
        } else {
            y += verticalOffsetY;
        }

        return new Point(x, y);
    }

    private Roxel takeNextRoxel(Roxel currentRoxel) {
        switch (car.getDrivingDirection()) {
            case EAST:
                return xapHelper.takeRoxel(new Roxel(currentRoxel.getEast()));
            case SOUTH:
                return xapHelper.takeRoxel(new Roxel(currentRoxel.getSouth()));
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
