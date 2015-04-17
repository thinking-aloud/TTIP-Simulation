package gigaspaces;

import java.awt.Point;
import slick2d.Car;

/**
 *
 * @author jo
 */
public class Roxel {
    private Point gridPosition;
    private Roxel north, east, south, west;
    private Car car;
    
    public Roxel(Point pos) {
        gridPosition = pos;
    }

    /**
     * @return the gridPosition
     */
    public Point getGridPosition() {
        return gridPosition;
    }

    /**
     * @param gridPosition the gridPosition to set
     */
    public void setGridPosition(Point gridPosition) {
        this.gridPosition = gridPosition;
    }

    /**
     * @return the north
     */
    public Roxel getNorth() {
        return north;
    }

    /**
     * @param north the north to set
     */
    public void setNorth(Roxel north) {
        this.north = north;
    }

    /**
     * @return the east
     */
    public Roxel getEast() {
        return east;
    }

    /**
     * @param east the east to set
     */
    public void setEast(Roxel east) {
        this.east = east;
    }

    /**
     * @return the south
     */
    public Roxel getSouth() {
        return south;
    }

    /**
     * @param south the south to set
     */
    public void setSouth(Roxel south) {
        this.south = south;
    }

    /**
     * @return the west
     */
    public Roxel getWest() {
        return west;
    }

    /**
     * @param west the west to set
     */
    public void setWest(Roxel west) {
        this.west = west;
    }

    /**
     * @return the car
     */
    public Car getCar() {
        return car;
    }

    /**
     * @param car the car to set
     */
    public void setCar(Car car) {
        this.car = car;
    }
}
