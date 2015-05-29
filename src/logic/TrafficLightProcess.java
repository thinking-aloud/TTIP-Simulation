/*
 */
package logic;

import domain.Car;
import domain.Roxel;
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
public class TrafficLightProcess {
    
    @GigaSpaceContext
    GigaSpace gs;
    
    public TrafficLightProcess(GigaSpace gs) {
        this.gs = gs;
    }
    
    @EventTemplate
    public Roxel undecidedJunction() {
        Roxel template = new Roxel();
        template.setOpenDirection(Car.Direction.TODECIDE);
        return template;
    }
    
    @SpaceDataEvent
    public Roxel eventListener(Roxel rox) {
        // switch traffic light
        /*long currentTime = System.currentTimeMillis();
        if (lastLightSwitch + (1000 * timeBetweenSwitch) < currentTime) {
            lastLightSwitch = currentTime;
            if (direction.equals(Car.Direction.EAST)) {
                direction = Car.Direction.SOUTH;
            }
        }

        // smart traffic handling
        Roxel template = new Roxel(getX(), getY(), Car.Direction.TODECIDE);
        Roxel rox = xapHelper.takeRoxel(template);*/

        if (rox != null) {
            System.out.println("Kreuzung " + rox.getX() + ", " + rox.getY() + " wurde in Richtung verlassen.");

            //Roxel prevNorth = xapHelper.readRoxelById(previousNorth);
            Roxel prevNorth = gs.read(new Roxel(rox.getX(), rox.getY()-1));
            //Roxel prevWest = xapHelper.readRoxelById(previousWest);
            Roxel prevWest = gs.read(new Roxel(rox.getX()-1, rox.getY()));
            
            if (prevNorth != null && prevWest != null) {
                if (prevNorth.isOccupied() && !prevWest.isOccupied()) {
                    rox.setOpenDirection(Car.Direction.SOUTH);
                } else if (!prevNorth.isOccupied() && prevWest.isOccupied()) {
                    rox.setOpenDirection(Car.Direction.EAST);
                } else if (prevNorth.isOccupied() && prevWest.isOccupied()) {
                    rox.setOpenDirection(Car.Direction.EAST);
                }
            }
            System.out.println("Kreuzung " + rox.getX() + ", " + rox.getY() + " wurde in Richtung " + rox.getOpenDirection() + " gesetzt.");

        }
        return rox;
    }
}
