/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logic;

import domain.Car;
import domain.CarAllowance;
import domain.Roxel;
import domain.RoxelRegistration;
import gui.Main;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.openspaces.core.GigaSpace;
import org.openspaces.core.context.GigaSpaceContext;
import org.openspaces.events.EventDriven;
import org.openspaces.events.EventTemplate;
import org.openspaces.events.adapter.SpaceDataEvent;
import org.openspaces.events.notify.Notify;
import org.openspaces.events.notify.NotifyType;

/**
 *
 * @author jo
 */
@EventDriven
@Notify(performTakeOnNotify = true, ignoreEventOnNullTake = true)
@NotifyType(write = true, update = true)
public class CarProcess {

    private final Car car;

    @GigaSpaceContext
    GigaSpace gs;

    public CarProcess(Car car, GigaSpace gs) {
        this.car = car;
        this.gs = gs;
        registerForNextRoxel();
    }

    @EventTemplate
    public CarAllowance allowance() {
        return new CarAllowance(this.car.getId());
    }

    /*@EventTemplate
     public Roxel desiredRoxel() {
     System.out.println("CarProcess.desiredRoxel()");
     Roxel template;
     switch (this.car.getDrivingDirection()) {
     case EAST:
     //                if (this.car.getX() < Main.mapWidth - 1) {
     //                    template = new Roxel(this.car.getX() + 1,
     //                            this.car.getY(), Car.Direction.EAST);
     //                } else {
     //                    template = new Roxel(0, this.car.getY(), Car.Direction.EAST);
     //                }
     template = new Roxel(Car.Direction.EAST);
     break;
     case SOUTH:
     //                if (this.car.getY() < Main.mapHeight - 1) {
     //                    template = new Roxel(this.car.getX(),
     //                            this.car.getY() + 1, Car.Direction.SOUTH);
     //                } else {
     //                    template = new Roxel(this.car.getX(),
     //                            0, Car.Direction.SOUTH);
     //                }
     template = new Roxel(Car.Direction.SOUTH);
     break;
     default:
     template = null;
     break;
     }
     /*if (template != null) {
     System.out.println("Car " + this.car.getId() + " wants to move to "
     + template.getX() + ", " + template.getY());
     }/
     return template;
     }*/
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
//                System.out.println("CarProcess.move(" + nextRoxel.getX() + ", " + nextRoxel.getY() + ")");

                if (currentRoxel != null) {
                    if (!nextRoxel.isOccupied()
                            && (nextRoxel.getOpenDirection() == this.car.getDrivingDirection())) {
                        /*System.out.println("Car " + this.car.getId() + " is about to move to "
                         + rox.getX() + ", " + rox.getY());*/

                        // set next roxel occupied
                        nextRoxel.setOccupied(true);

                        // move car
                        car.setX(nextRoxel.getX());
                        car.setY(nextRoxel.getY());
                        gs.write(this.car);

                        // pass old roxel to trafficLight or TS
                        currentRoxel.setOccupied(false);
                        if (currentRoxel.isJunction() != null && currentRoxel.isJunction()) {
                            currentRoxel.setOpenDirection(Car.Direction.TODECIDE);
                        }
                        /*System.out.println("Car " + this.car.getId() + " moved to "
                         + this.car.getX() + ", " + this.car.getY());*/
                    }
                    gs.write(currentRoxel);
                }
            }
            gs.write(nextRoxel);
        }
        registerForNextRoxel();
    }

    private Roxel readNextRoxel() {
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

        return gs.read(template);
    }

    private void registerForNextRoxel() {
        Integer time = 30;
        Roxel nextRoxel = readNextRoxel();
        RoxelRegistration reg;

        if (nextRoxel != null) {
            reg = new RoxelRegistration(nextRoxel.getId(), this.car.getId(), time);

        } else {

            System.out.println("CarProcess.readNextRoxel(): " + car.getDrivingDirection() + ", "
                    + car.getX() + ", " + car.getY());

            System.out.println("CarProcess.registerForNextRoxel(): Car " + this.car.getX()
                    + ", " + this.car.getY() + ", " + this.car.getDrivingDirection()
                    + " couldn't register for next roxel.");

            reg = new RoxelRegistration(null, this.car.getId(), time);
        }
        gs.write(reg);
    }
}
