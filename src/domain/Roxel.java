package domain;

import com.gigaspaces.annotation.pojo.SpaceId;
import com.gigaspaces.annotation.pojo.SpaceRouting;
import java.io.Serializable;

public class Roxel implements Serializable {

    private Integer tileNumber;
    private String id;
    private Integer x, y;
    private Car.Direction openDirection;
    private Boolean occupied, junction, carWaiting;

    // constructor for Gigaspaces querying
    public Roxel() {
        this.occupied = Boolean.FALSE;
    }

    public Roxel(Integer x, Integer y) {
        this();
        this.x = x;
        this.y = y;
        this.junction = this.junction = x % 3 == 1 && y % 3 == 1;
    }

    public Roxel(Integer x, Integer y, Car.Direction direction) {
        this(x, y);
        this.openDirection = direction;
    }
    
    public Roxel(Integer x, Integer y, Car.Direction direction, boolean carWaiting) {
        this(x, y, direction);
        this.carWaiting = carWaiting;
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
    public Car.Direction getOpenDirection() {
        return openDirection;
    }

    /**
     * @param openDirection the openDirection to set
     */
    public void setOpenDirection(Car.Direction openDirection) {
        this.openDirection = openDirection;
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

    public Boolean isJunction() {
        return junction;
    }

    public void setJunction(Boolean junction) {
        this.junction = junction;
    }

    public Boolean getCarWaiting() {
        return carWaiting;
    }

    public void setCarWaiting(Boolean carWaiting) {
        this.carWaiting = carWaiting;
    }

    /**
     * @return the tileNumber
     */
    @SpaceRouting
    public Integer getTileNumber() {
        return tileNumber;
    }

    /**
     * @param tileNumber the tileNumber to set
     */
    public void setTileNumber(Integer tileNumber) {
        this.tileNumber = tileNumber;
    }

}
