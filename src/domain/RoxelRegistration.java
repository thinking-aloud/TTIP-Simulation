/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domain;

import com.gigaspaces.annotation.pojo.SpaceId;

/**
 *
 * @author jo
 */
public class RoxelRegistration {
    private String id;
    private String roxelId;
    private String carId;
    private Integer timer;
    
    public RoxelRegistration() {}
    
    
    public RoxelRegistration(String roxelId, String carId, Integer timer) {
        this.roxelId = roxelId;
        this.carId = carId;
        this.timer = timer;
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
     * @return the roxelId
     */
    public String getRoxelId() {
        return roxelId;
    }

    /**
     * @param roxelId the roxelId to set
     */
    public void setRoxelId(String roxelId) {
        this.roxelId = roxelId;
    }

    /**
     * @return the carId
     */
    public String getCarId() {
        return carId;
    }

    /**
     * @param carId the carId to set
     */
    public void setCarId(String carId) {
        this.carId = carId;
    }

    /**
     * @return the timer
     */
    public Integer getTimer() {
        return timer;
    }

    /**
     * @param timer the timer to set
     */
    public void setTimer(Integer timer) {
        this.timer = timer;
    }
    
    
}
