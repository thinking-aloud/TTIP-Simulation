package domain;

import com.gigaspaces.annotation.pojo.SpaceId;
import java.io.Serializable;

public class Roxel implements Serializable {

    private String id;
    private Integer x, y;
    private Car.Direction openDirection;
    private Boolean occupied, junction;

    // constructor for Gigaspaces querying
    public Roxel() {
        this.occupied = Boolean.FALSE;
        if (x != null && y != null) {
            this.junction = this.junction = x % 3 == 1 && y % 3 == 1;
        }
    }

    public Roxel(Car.Direction direction) {
        this();
        this.openDirection = direction;
    }

    public Roxel(Integer x, Integer y) {
        this();
        this.x = x;
        this.y = y;
    }

    public Roxel(Integer x, Integer y, Car.Direction direction) {
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

}
