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
public class CarAllowance {
    private String id;
    private String carId;
    
    public CarAllowance() {}
    
    public CarAllowance(String carId) {
        this.carId = carId;
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
    
    
}
