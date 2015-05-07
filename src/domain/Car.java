package domain;

import com.gigaspaces.annotation.pojo.SpaceId;

public class Car {

    public enum DrivingDirection {

        WEST,
        EAST,
        NORTH,
        SOUTH
    }

    private String id;
    private DrivingDirection drivingDirection;
    private Roxel occupiedRoxel;
    private Integer speed;

    public Car(DrivingDirection dir, Integer speed) {
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
    public DrivingDirection getDrivingDirection() {
        return drivingDirection;
    }

    /**
     * @param drivingDirection the drivingDirection to set
     */
    public void setDrivingDirection(DrivingDirection drivingDirection) {
        this.drivingDirection = drivingDirection;
    }

    public Roxel getOccupiedRoxel() {
        return occupiedRoxel;
    }

    public void setOccupiedRoxel(Roxel occupiedRoxel) {
        this.occupiedRoxel = occupiedRoxel;
    }

    public Integer getSpeed() {
        return speed;
    }

    public void setSpeed(Integer speed) {
        this.speed = speed;
    }
    
}
