package logic;

import domain.Car;
import domain.CarAllowance;
import domain.Roxel;
import domain.RoxelRegistration;
import gui.Main;
import org.openspaces.core.GigaSpace;
import org.openspaces.events.EventDriven;
import org.openspaces.events.EventTemplate;
import org.openspaces.events.adapter.SpaceDataEvent;
import org.openspaces.events.notify.Notify;
import org.openspaces.events.notify.NotifyType;

@EventDriven
@Notify(performTakeOnNotify = true, ignoreEventOnNullTake = true)
@NotifyType(write = true, update = true)
public class CarProcess {

    private final Car car;
    private final GigaSpace gs;

    public CarProcess(Car car, GigaSpace gs) {
        this.car = car;
        this.gs = gs;
        registerForNextRoxel();
    }

    @EventTemplate
    public CarAllowance allowance() {
        return new CarAllowance(this.car.getId());
    }

    @SpaceDataEvent
    public void move(CarAllowance allowance) {
        // takes the allowance out of the TS. Prevends instance flooding
        gs.take(allowance);

        if (allowance.getRoxelId() != null) {
            Roxel template = new Roxel(car.getX(), car.getY());
            template.setOccupied(true);
            Roxel currentRoxel = gs.take(template);
            Roxel nextRoxel = gs.readById(Roxel.class, allowance.getRoxelId());
            if (nextRoxel != null) {
                if (!nextRoxel.isOccupied()) {
                    nextRoxel.setCarWaiting(Boolean.FALSE);
                }

                if (currentRoxel != null) {
                    if (!nextRoxel.isOccupied()
                            && (nextRoxel.getOpenDirection() == this.car.getDrivingDirection())) {

                        // set next roxel occupied
                        nextRoxel.setOccupied(true);

                        // move car
                        car.setX(nextRoxel.getX());
                        car.setY(nextRoxel.getY());
                        gs.write(car);

                        // pass old roxel to trafficLight or TS
                        currentRoxel.setOccupied(false);

                        if (currentRoxel.isJunction() != null && currentRoxel.isJunction()) {
                            currentRoxel.setOpenDirection(Car.Direction.TODECIDE);
                        }
                    }
                    gs.write(currentRoxel);
                }
                gs.write(nextRoxel);
            }
        }
        registerForNextRoxel();
    }

    private Roxel takeNextRoxel() {
        Roxel template = null;

        switch (car.getDrivingDirection()) {
            case EAST:
                if (this.car.getX() < Main.mapWidth - 1) {
                    template = new Roxel(this.car.getX() + 1, this.car.getY());
                } else {
                    template = new Roxel(0, this.car.getY());
                }
                break;
            case SOUTH:
                if (this.car.getY() < Main.mapHeight - 1) {
                    template = new Roxel(this.car.getX(), this.car.getY() + 1);
                } else {
                    template = new Roxel(this.car.getX(), 0);
                }
        }
        return gs.take(template);
    }

    private void registerForNextRoxel() {
        Integer time = 30;
        Roxel nextRoxel = takeNextRoxel();
        RoxelRegistration reg;

        if (nextRoxel != null) {
            reg = new RoxelRegistration(nextRoxel.getId(), this.car.getId(), time);

            nextRoxel.setCarWaiting(Boolean.TRUE);
            gs.write(nextRoxel);
        } else {
            System.out.println("CarProcess.registerForNextRoxel(): Car " + this.car.getX()
                    + ", " + this.car.getY() + ", " + this.car.getDrivingDirection()
                    + " couldn't register for next roxel.");

            reg = new RoxelRegistration(null, this.car.getId(), time);
        }
        gs.write(reg);
    }
}
