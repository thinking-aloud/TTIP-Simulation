/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logic;

import domain.Car;
import domain.Roxel;
import gui.Main;
import org.openspaces.core.GigaSpace;
import org.openspaces.core.context.GigaSpaceContext;
import org.openspaces.events.DynamicEventTemplate;
import org.openspaces.events.EventDriven;
import org.openspaces.events.EventTemplate;
import org.openspaces.events.adapter.SpaceDataEvent;
import org.openspaces.events.notify.NotifyType;
import org.openspaces.events.polling.Polling;

/**
 *
 * @author jo
 */
@EventDriven
@Polling()
@NotifyType(write = true, update = true)
public class CarProcess {

    private Car car;

    @GigaSpaceContext
    GigaSpace gs;

    public CarProcess(Car car, GigaSpace gs) {
        this.car = car;
        this.gs = gs;
    }

    @EventTemplate
    public Roxel desiredRoxel() {
        Roxel template;
        switch (this.car.getDrivingDirection()) {
            case EAST:
                if (this.car.getX() < Main.mapWidth - 1) {
                    template = new Roxel(this.car.getX() + 1,
                            this.car.getY(), Car.Direction.EAST);
                } else {
                    template = new Roxel(0, this.car.getY(), Car.Direction.EAST);
                }
                break;
            case SOUTH:
                if (this.car.getY() < Main.mapHeight - 1) {
                    template = new Roxel(this.car.getX(),
                            this.car.getY() + 1, Car.Direction.SOUTH);
                } else {
                    template = new Roxel(this.car.getX(),
                            0, Car.Direction.SOUTH);
                }
                break;
            default:
                template = null;
                break;
        }
        if (template != null) {
            System.out.println("Car " + this.car.getId() + " wants to move to "
                    + template.getX() + ", " + template.getY());
        }
        return template;
    }

    @SpaceDataEvent
    public Roxel move(Roxel rox) {
        Roxel template = new Roxel((int) car.getX(), (int) car.getY());
        template.setOccupied(true);
        Roxel currentRoxel = gs.take(template);

        if (currentRoxel != null && rox != null) {
            if (!rox.isOccupied()
                    && (rox.getOpenDirection() == this.car.getDrivingDirection())) {
                System.out.println("Car " + this.car.getId() + " is about to move to "
                    + rox.getX() + ", " + rox.getY());

                // set next roxel occupied
                rox.setOccupied(true);

                // move car
                car.setX(rox.getX());
                car.setY(rox.getY());
                gs.write(this.car);

                // pass old roxel to trafficLight or TS
                currentRoxel.setOccupied(false);
                if (currentRoxel.isJunction()) {
                    currentRoxel.setOpenDirection(Car.Direction.TODECIDE);
                }
                gs.write(currentRoxel);
                System.out.println("Car " + this.car.getId() + " moved to "
                    + this.car.getX() + ", " + this.car.getY());
            }
        }
        return rox;
    }
}
