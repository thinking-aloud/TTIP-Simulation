package helper;

import domain.Car;
import domain.Roxel;

/**
 *
 * @author jo
 */
public class TrafficLight implements Runnable {

    private final Roxel rox;
    private final XapHelper xapHelper;

    public TrafficLight(Roxel rox) {
        this.rox = rox;
        this.xapHelper = new XapHelper();
    }

    private boolean carOnPreviousRoxel() {
        if (rox.getOpenDirection() == Car.DrivingDirection.EAST && xapHelper.readRoxel(new Roxel(rox.getWest())) == null) {
            return true;
        }
        return rox.getOpenDirection() == Car.DrivingDirection.SOUTH && xapHelper.readRoxel(new Roxel(rox.getNorth())) == null;
    }

    public void handleRoxel() {
        if (!carOnPreviousRoxel()) {
            if (rox.getOpenDirection() == Car.DrivingDirection.EAST) {
                rox.setOpenDirection(Car.DrivingDirection.SOUTH);
            } else {
                rox.setOpenDirection(Car.DrivingDirection.EAST);
            }
        }
        xapHelper.writeToTupelspace(rox);
    }

    @Override
    public void run() {
        handleRoxel();
    }
}
