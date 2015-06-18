package logic;

import domain.Car;
import domain.Roxel;
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
public class TrafficLightProcess {

    private final GigaSpace gs;
    int x, y;
    private final String previousNorth;
    private final String previousWest;
    private final Car.Direction lastDirection;
    //private static final Integer timeBetweenSwitch = 5; // in seconds

    public TrafficLightProcess(GigaSpace gs, int x, int y) {
        this.gs = gs;
        this.x = x;
        this.y = y;
        this.lastDirection = Car.Direction.TODECIDE;

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
        Roxel template = new Roxel(x, y, Car.Direction.TODECIDE);
        return template;
    }

    @SpaceDataEvent
    public Roxel eventListener(Roxel rox) {
//        System.out.println("TrafficLightProcess.eventListener(" + rox.getX() + ", " + rox.getY() + ")");

        Roxel prevNorth = gs.readById(Roxel.class, previousNorth);
        Roxel prevWest = gs.readById(Roxel.class, previousWest);

        if (prevNorth != null && prevWest != null) {
            switch (lastDirection) {
                case EAST:
                    if (prevWest.isOccupied()) {
                        rox.setOpenDirection(Car.Direction.EAST);
                    } else if (prevNorth.isOccupied()) {
                        rox.setOpenDirection(Car.Direction.SOUTH);
                    } else {
                        rox.setOpenDirection(Car.Direction.TODECIDE);
                    }
                    break;
                case SOUTH:
                    if (prevNorth.isOccupied()) {
                        rox.setOpenDirection(Car.Direction.SOUTH);
                    } else if (prevWest.isOccupied()) {
                        rox.setOpenDirection(Car.Direction.EAST);
                    } else {
                        rox.setOpenDirection(Car.Direction.TODECIDE);
                    }
                    break;
                default:
                    if (prevWest.isOccupied()) {
                        rox.setOpenDirection(Car.Direction.EAST);
                    } else if (prevNorth.isOccupied()) {
                        rox.setOpenDirection(Car.Direction.SOUTH);
                    } else {
                        rox.setOpenDirection(Car.Direction.TODECIDE);
                    }
                    break;
            }
        } else if(prevNorth == null) {
            System.out.println("TrafficLightProcess.eventListener(): Kreuzung "
                    + rox.getX() + ", " + rox.getY() + ": North is null.");
        } else {
            System.out.println("TrafficLightProcess.eventListener(): Kreuzung "
                    + rox.getX() + ", " + rox.getY() + ": West is null.");
            
        }
        return rox;
    }

}
