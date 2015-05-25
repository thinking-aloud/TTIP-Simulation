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
//    private Roxel occupiedRoxel;
    private Integer speed, x, y;

    public Car(Direction dir, Integer speed) {
        this.drivingDirection = dir;
        this.speed = speed;
    }

    /**
     * empty constructor used by TS
     */
    public Car() {
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

//    public Roxel getOccupiedRoxel() {
//        return occupiedRoxel;
//    }
//
//    public void setOccupiedRoxel(Roxel occupiedRoxel) {
//        this.occupiedRoxel = occupiedRoxel;
//    }
    public Integer getSpeed() {
        return speed;
    }

    public void setSpeed(Integer speed) {
        this.speed = speed;
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
