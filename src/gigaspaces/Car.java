/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gigaspaces;

import com.gigaspaces.annotation.pojo.SpaceId;

/**
 *
 * @author jo
 */
public class Car {

    public enum DrivingDirection {
        WEST,
        EAST,
        NORTH,
        SOUTH
    }
    
    private String id, positionRoxel;
    private DrivingDirection drivingDirection;
    
    public Car(String pos, DrivingDirection dir, Integer speed) {
        this.positionRoxel = pos;
        this.drivingDirection = dir;
    }
    
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
     * @return the positionRoxel
     */
    public String getPositionRoxel() {
        return positionRoxel;
    }

    /**
     * @param positionRoxel the positionRoxel to set
     */
    public void setPositionRoxel(String positionRoxel) {
        this.positionRoxel = positionRoxel;
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

}
