package domain;

import com.gigaspaces.annotation.pojo.SpaceId;
import gui.Main;
import java.io.Serializable;
import org.newdawn.slick.geom.Point;

public class Roxel implements Serializable {

    private String id;
    private Integer x, y;
    private Point north, east, south, west;
    private Car.DrivingDirection openDirection;
    private Boolean occupied;

    // constructor for Gigaspaces querying
    public Roxel() {
        this.occupied = Boolean.FALSE;
        this.openDirection = Car.DrivingDirection.EAST;
    }

    public Roxel(Integer x, Integer y) {
        this();
        this.x = x;
        this.y = y;
    }

    public Roxel(Integer x, Integer y, Car.DrivingDirection direction) {
        this(x, y);
        this.openDirection = direction;
    }

    /**
     *
     * @return the id
     */
    @SpaceId(autoGenerate = true)
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
    public Point getNorth() {
        return north;
    }

    /**
     * @param north the north to set
     */
    public void setNorth(Point north) {
        this.north = north;
    }

    /**
     * @return the east
     */
    public Point getEast() {
        return east;
    }

    /**
     * @param east the east to set
     */
    public void setEast(Point east) {
        this.east = east;
    }

    /**
     * @return the south
     */
    public Point getSouth() {
        return south;
    }

    /**
     * @param south the south to set
     */
    public void setSouth(Point south) {
        this.south = south;
    }

    /**
     * @return the west
     */
    public Point getWest() {
        return west;
    }

    /**
     * @param west the west to set
     */
    public void setWest(Point west) {
        this.west = west;
    }

    /**
     *
     * @return a nicely formated string
     */
    @Override
    public String toString() {
        return "Roxel #" + id + ", Position: " + x + ", " + y;
    }

    public Integer getX() {
        return x;
    }

    public void setX(Integer x) {
        this.x = x;
    }

    public Integer getY() {
        return y;
    }

    public void setY(Integer y) {
        this.y = y;
    }

    /**
     * @return the openDirection
     */
    public Car.DrivingDirection getOpenDirection() {
        return openDirection;
    }

    /**
     * @param openDirection the openDirection to set
     */
    public void setOpenDirection(Car.DrivingDirection openDirection) {
        this.openDirection = openDirection;
    }

    public boolean isJunction() {
//        return (this.north != null && this.east != null && this.south != null && this.west != null);
        return(Main.mapWidth % 3 == 1 && Main.mapHeight % 3 == 1);
    }

    public Boolean isOccupied() {
        return occupied;
    }

    public void setOccupied(Boolean occupied) {
        this.occupied = occupied;
    }

    public Boolean getOccupied() {
        return occupied;
    }

}
