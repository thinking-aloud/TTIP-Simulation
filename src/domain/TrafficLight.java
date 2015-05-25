package domain;

import com.gigaspaces.annotation.pojo.SpaceId;
import gui.Main;
import helper.XapHelper;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TrafficLight implements Runnable {

    private final Integer x, y;
    private final XapHelper xapHelper;
    private Car.Direction direction;
    private Long lastLightSwitch;
    private static final Integer timeBetweenSwitch = 5; // in seconds
    private String id;
    private final String previousNorth;
    private final String previousWest;

    private Integer index = 0;

    public TrafficLight(Integer x, Integer y) {
        this.x = x;
        this.y = y;
        this.xapHelper = new XapHelper();
        this.direction = Car.Direction.EAST;
        lastLightSwitch = System.currentTimeMillis();

        // determin previous roxel
        if (x > 0) {
            previousWest = xapHelper.readRoxel(new Roxel(x - 1, y)).getId();
        } else {
            previousWest = xapHelper.readRoxel(new Roxel(Main.mapWidth - 1, y)).getId();
        }
        
        if (y > 0) {
            previousNorth = xapHelper.readRoxel(new Roxel(x, y - 1)).getId();
        } else {
            previousNorth = xapHelper.readRoxel(new Roxel(x, Main.mapHeight - 1)).getId();
        }
    }

    private void handleRoxel() {
        // switch traffic light
        long currentTime = System.currentTimeMillis();
        if (lastLightSwitch + (1000 * timeBetweenSwitch) < currentTime) {
            lastLightSwitch = currentTime;
            if (direction.equals(Car.Direction.EAST)) {
                direction = Car.Direction.SOUTH;
            }
        }

        // smart traffic handling
        Roxel template = new Roxel(x, y, Car.Direction.TODECIDE);
        Roxel rox = xapHelper.takeRoxel(template);

        if (rox != null) {
            Roxel prevNorth = xapHelper.readRoxelById(previousNorth);
            Roxel prevWest = xapHelper.readRoxelById(previousWest);

            if (prevNorth != null && prevWest != null) {
                if (prevNorth.isOccupied() && !prevWest.isOccupied()) {
                    rox.setOpenDirection(Car.Direction.SOUTH);
                } else if (!prevNorth.isOccupied() && prevWest.isOccupied()) {
                    rox.setOpenDirection(Car.Direction.EAST);
                } else if (prevNorth.isOccupied() && prevWest.isOccupied()) {
                    rox.setOpenDirection(direction);
                }
            }
            xapHelper.writeToTupelspace(rox);
        }

    }

    public Car.Direction getDirection() {
        return direction;
    }

    public void setDirection(Car.Direction direction) {
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
        while (true) {
            try {
                Thread.sleep(1000);
                handleRoxel();
            } catch (InterruptedException ex) {
                Logger.getLogger(TrafficLight.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * @return the id
     */
    @SpaceId(autoGenerate = true)
    public String getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

}
