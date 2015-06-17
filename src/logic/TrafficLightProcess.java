/*
 */
package logic;

import domain.Car;
import domain.Roxel;
import gui.Main;
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

    int junctionX, junctionY;
    private final String previousNorth;
    private final String previousWest;

    public TrafficLightProcess(GigaSpace gs, int x, int y) {
        this.gs = gs;
        this.junctionX = x;
        this.junctionY = y;
        if (x > 0) {
            previousWest = gs.read(new Roxel(x - 1, y)).getId();
        } else {
            previousWest = gs.read(new Roxel(Main.mapWidth - 1, y)).getId();
        }
        
        if (y > 0) {
            previousNorth = gs.read(new Roxel(x, y - 1)).getId();
        } else {
            previousNorth = gs.read(new Roxel(x, Main.mapHeight - 1)).getId();
        }
    }

    @EventTemplate
    public Roxel undecidedJunction() {
        Roxel template = new Roxel(this.junctionX, this.junctionY, Car.Direction.TODECIDE);
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
            //System.out.println("Kreuzung " + rox.getX() + ", " + rox.getY() + " wurde verlassen.");

            //Roxel prevNorth = xapHelper.readRoxelById(previousNorth);
            Roxel prevNorth = gs.readById(Roxel.class, previousNorth);
            //Roxel prevWest = xapHelper.readRoxelById(previousWest);
            Roxel prevWest = gs.readById(Roxel.class, previousWest);

            try {
            //if (prevNorth != null && prevWest != null) {
                if (prevNorth.isOccupied()/* && !prevWest.isOccupied()*/) {
                    rox.setOpenDirection(Car.Direction.SOUTH);
                } /*else if (!prevNorth.isOccupied() && prevWest.isOccupied()) {
                    rox.setOpenDirection(Car.Direction.EAST);
                } else if (prevNorth.isOccupied() && prevWest.isOccupied()) {
                    rox.setOpenDirection(Car.Direction.EAST);
                }*/ else {
                    rox.setOpenDirection(Car.Direction.EAST);
                }
            //}
            } catch (NullPointerException ex) {
                System.out.println("Kreuzung " + rox.getX() + ", " + rox.getY() + ": Fehler beim lesen der benachbarten Felder.");
            }
            System.out.println("Kreuzung " + rox.getX() + ", " + rox.getY() + " wurde in Richtung " + rox.getOpenDirection() + " gesetzt.");

        }
        return rox;
    }
}
