package domain;

import com.gigaspaces.annotation.pojo.SpaceId;

public class Car {

    public enum Direction {

        EAST,
        SOUTH,
        TODECIDE
    }

    private String id;
    private Direction drivingDirection;
    private Integer x, y;

    /**
     * empty constructor used by TS
     */
    public Car() {
    }

    public Car(Direction dir) {
        this.drivingDirection = dir;
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

    /**
     * @return the drivingDirection
     */
    public Direction getDrivingDirection() {
        return drivingDirection;
    }

    /**
     * @param drivingDirection the drivingDirection to set
     */
    public void setDrivingDirection(Direction drivingDirection) {
        this.drivingDirection = drivingDirection;
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
}
