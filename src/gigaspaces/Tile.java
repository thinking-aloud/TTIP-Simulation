package gigaspaces;

import com.gigaspaces.annotation.pojo.SpaceId;
import com.gigaspaces.annotation.pojo.SpaceIndex;
import com.gigaspaces.metadata.index.SpaceIndexType;

public class Tile {

    private Integer x, y, ssn;
    private Boolean occupied;

    // Default constructor (required by XAP)
    public Tile() {}

    public Tile(Integer ssn, Integer x, Integer y, Boolean occ) {
        this.ssn = ssn;
        this.x = x;
        this.y = y;
        this.occupied = occ;
    }

    @SpaceId(autoGenerate=false)
    public Integer getSsn() {
        return ssn;
    }
    public void setSsn(Integer ssn) {
        this.ssn = ssn;
    }

    @Override
    public String toString() {
        return "Tile #" + ssn + ": " + getX() + " " + getY() + " " + isOccupied();
    }

    @SpaceIndex(type=SpaceIndexType.BASIC)
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

    public Boolean isOccupied() {
        return occupied;
    }

    public void setOccupied(Boolean occupied) {
        this.occupied = occupied;
    }
}