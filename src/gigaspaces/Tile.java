package gigaspaces;

import com.gigaspaces.annotation.pojo.SpaceId;
import com.gigaspaces.annotation.pojo.SpaceIndex;
import com.gigaspaces.metadata.index.SpaceIndexType;
import java.awt.Point;

public class Tile {

    private String id;
    private Boolean occupied;
    private int x, y;

    // Default constructor (required by XAP)
    public Tile() {
    }

    public Tile(Point position, Boolean occupied) {
        this.x = position.x;
        this.y = position.y;
        this.occupied = occupied;
    }
    
    @SpaceId(autoGenerate = true)
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "id:" + id + " x:" + x + " y:" + y + " " + isOccupied();
    }

    @SpaceIndex(type = SpaceIndexType.BASIC)
    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    @SpaceIndex(type = SpaceIndexType.BASIC)
    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    @SpaceIndex(type = SpaceIndexType.BASIC)
    public Boolean isOccupied() {
        return occupied;
    }

    public void setOccupied(Boolean occupied) {
        this.occupied = occupied;
    }
}
