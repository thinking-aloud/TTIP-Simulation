package domain;

import com.gigaspaces.annotation.pojo.SpaceId;
import com.gigaspaces.annotation.pojo.SpaceIndex;
import com.gigaspaces.metadata.index.SpaceIndexType;

public class Roxel {
    private String id, north, east, south, west, car;
    private Integer x, y;
    
    public Roxel(Integer x, Integer y) {
        this.x = x;
        this.y = y;
        this.id = String.format("%03d-%03d", x, y);
        this.car = null;
    }

    // constructor for Gigaspaces querying
    public Roxel() {
    }
    
    /**
     * 
     * @return the id
     */
    @SpaceId(autoGenerate = false)
    public String getId() {
        return id;
    }

    /**
     * 
     * @param id sets the id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return the north
     */
    public String getNorth() {
        return north;
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
        return east;
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
        return south;
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
        return west;
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
        return car;
    }

    /**
     * @param car the car to set
     */
    public void setCar(String car) {
        this.car = car;
    }
    
    /**
     * 
     * @return a nicely formated string
     */
    @Override
    public String toString() {
        return "Roxel #" + getId() + ", Position: " + getX() + ", " + getY();
    }

    /**
     * @return the x
     */
    @SpaceIndex(type=SpaceIndexType.EXTENDED)
    public Integer getX() {
        return x;
    }

    /**
     * @return the y
     */
    @SpaceIndex(type=SpaceIndexType.EXTENDED)
    public Integer getY() {
        return y;
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
