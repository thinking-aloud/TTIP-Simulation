package helper;

import domain.Car;
import domain.Roxel;
import org.newdawn.slick.geom.Point;

public class TrafficLight implements Runnable {

    private final Integer x, y;
    private final XapHelper xapHelper;
    private Car.DrivingDirection direction;
    private Long lastLightSwitch;
    private static final Integer timeBetweenSwitch = 5; // in seconds

    public TrafficLight(Integer x, Integer y) {
        this.x = x;
        this.y = y;
        this.xapHelper = new XapHelper();
        this.direction = Car.DrivingDirection.EAST;
        lastLightSwitch = System.currentTimeMillis();
    }

//    private boolean carOnPreviousRoxel() {
//        if (rox.getOpenDirection() == Car.DrivingDirection.EAST && xapHelper.readRoxel(new Roxel(rox.getWest())) == null) {
//            return true;
//        }
//        return rox.getOpenDirection() == Car.DrivingDirection.SOUTH && xapHelper.readRoxel(new Roxel(rox.getNorth())) == null;
//    }
    private void handleRoxel() {
//        if (!carOnPreviousRoxel()) {
//            if (rox.getOpenDirection() == Car.DrivingDirection.EAST) {
//                rox.setOpenDirection(Car.DrivingDirection.SOUTH);
//            } else {
//                rox.setOpenDirection(Car.DrivingDirection.EAST);
//            }
//        }
        Roxel template = new Roxel(x, y, Car.DrivingDirection.TODECIDE);
        Roxel rox = xapHelper.takeRoxel(template);

        if (rox != null) {
            // switch traffic light
            long currentTime = System.currentTimeMillis();
            if (lastLightSwitch + (1000 * timeBetweenSwitch) > currentTime) {
                lastLightSwitch = currentTime;
                if (direction.equals(Car.DrivingDirection.EAST)) {
                    direction = Car.DrivingDirection.SOUTH;
                    rox.setOpenDirection(direction);
                } else {
                    direction = Car.DrivingDirection.EAST;
                    rox.setOpenDirection(direction);
                }
            }
            xapHelper.writeToTupelspace(rox);
        }

    }

    public Car.DrivingDirection getDirection() {
        return direction;
    }

    public void setDirection(Car.DrivingDirection direction) {
        this.direction = direction;
    }

    public Long getLastLightSwitch() {
        return lastLightSwitch;
    }

    public void setLastLightSwitch(Long lastLightSwitch) {
        this.lastLightSwitch = lastLightSwitch;
    }

    @Override
    public void run() {
        handleRoxel();
    }
}
