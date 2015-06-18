package domain;

import com.gigaspaces.annotation.pojo.SpaceId;

public class CarAllowance {
    private String id;
    private String carId;
    private String roxelId;
    
    public CarAllowance() {}
    
    public CarAllowance(String carId) {
        this.carId = carId;
    }
    
    public CarAllowance(String carId, String roxelId) {
        this.carId = carId;
        this.roxelId = roxelId;
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
    
    
}
