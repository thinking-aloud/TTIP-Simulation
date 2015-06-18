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
    private Car.Direction direction;
    private long lastLightSwitch;
    private static final Integer timeBetweenSwitch = 5; // in seconds

    public TrafficLightProcess(GigaSpace gs, int x, int y) {
        this.gs = gs;
        this.x = x;
        this.y = y;
        this.direction = Car.Direction.EAST;
        lastLightSwitch = System.currentTimeMillis();

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
        Roxel template = new Roxel(x, y, Car.Direction.TODECIDE, true);
        return template;
    }

    @SpaceDataEvent
    public Roxel eventListener(Roxel rox) {
        System.out.println("TrafficLightProcess.eventListener(" + rox.getX() + ", " + rox.getY() + ")");

        // switch traffic light
        long currentTime = System.currentTimeMillis();
        if (lastLightSwitch + (1000 * timeBetweenSwitch) < currentTime) {
            lastLightSwitch = currentTime;
            if (direction.equals(Car.Direction.EAST)) {
                direction = Car.Direction.SOUTH;
            }
        }
        Roxel prevNorth = gs.readById(Roxel.class, previousNorth);
        Roxel prevWest = gs.readById(Roxel.class, previousWest);

        try {
            if (prevNorth.isOccupied()/* && !prevWest.isOccupied()*/) {
                rox.setOpenDirection(Car.Direction.SOUTH);
            } else if (prevWest.isOccupied()) {
                rox.setOpenDirection(Car.Direction.EAST);
            } else {
                rox.setOpenDirection(direction);
            }
        } catch (NullPointerException ex) {
            System.out.println("TrafficLightProcess.eventListener(): Kreuzung "
                    + rox.getX() + ", " + rox.getY()
                    + ": Fehler beim lesen der benachbarten Felder.");
        }
        System.out.println("TrafficLightProcess.eventListener(): Kreuzung "
                + rox.getX() + ", " + rox.getY() + " wurde in Richtung "
                + rox.getOpenDirection() + " gesetzt.");

        return rox;
    }

}
