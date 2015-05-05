package gigaspaces;

import com.gigaspaces.annotation.pojo.SpaceId;

/**
 *
 * @author jo
 */
public class Roxel {
    private String id, north, east, south, west, car;
    private Integer x, y;
    
    public Roxel(Integer x, Integer y) {
        this.x = x;
        this.y = y;
        this.id = String.format("%03d-%03d", x, y);
    }

    // constructor for Gigaspaces querying
    public Roxel() {
    }
    
    @SpaceId(autoGenerate = false)
    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return the north
     */
    public String getNorth() {
        return this.north;
    }

    /**
     * @param north the north to set
     */
    public void setNorth(String north) {
        this.north = north;
    }

    /**
     * @return the east
     */
    public String getEast() {
        return this.east;
    }

    /**
     * @param east the east to set
     */
    public void setEast(String east) {
        this.east = east;
    }

    /**
     * @return the south
     */
    public String getSouth() {
        return this.south;
    }

    /**
     * @param south the south to set
     */
    public void setSouth(String south) {
        this.south = south;
    }

    /**
     * @return the west
     */
    public String getWest() {
        return this.west;
    }

    /**
     * @param west the west to set
     */
    public void setWest(String west) {
        this.west = west;
    }

    /**
     * @return the car
     */
    public String getCar() {
        return this.car;
    }

    /**
     * @param car the car to set
     */
    public void setCar(String car) {
        this.car = car;
    }
    
    @Override
    public String toString() {
        return "Roxel #" + getId() + ", Position: " + getX() + ", " + getY();
    }

    /**
     * @return the x
     */
    public Integer getX() {
        return this.x;
    }

    /**
     * @return the y
     */
    public Integer getY() {
        return this.y;
    }

    /**
     * @param x the x to set
     */
    public void setX(Integer x) {
        this.x = x;
    }

    /**
     * @param y the y to set
     */
    public void setY(Integer y) {
        this.y = y;
    }
}
