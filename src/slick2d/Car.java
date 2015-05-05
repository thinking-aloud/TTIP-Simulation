package slick2d;

import com.gigaspaces.annotation.pojo.SpaceId;
import gigaspaces.XapHelper;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class Car implements Runnable {

    public enum DrivingDirection {
        WEST,
        EAST,
        NORTH,
        SOUTH
    }
    
    private String id, positionRoxel;
    private final Image image;
    private DrivingDirection drivingDirection;
    
    private static final int horizontalOffsetX = 17;
    private static final int horizontalOffsetY = 32;
    private static final int verticalOffsetX = 10;
    private static final int verticalOffsetY = 23;
    private static final int tileSize = 64;
    
    private final XapHelper xapHelper;

    // private, can't be called from outside
    private Car(String rox, DrivingDirection carFacing) throws SlickException {
        this.drivingDirection = carFacing;
        positionRoxel = rox;

        xapHelper = new XapHelper();

        xapHelper.setOccupied(positionRoxel, this.id);

        if (carFacing == DrivingDirection.EAST) {
            image = new Image("res/car_red.png");
        } else {
            image = new Image("res/car_blue.png");
            image.rotate(90);
        }
    }

    //
    // static factory methods
    //
    public static Car createHorizontalCar(String rox) throws SlickException {
        return new Car(rox, DrivingDirection.EAST);
    }

    public static Car createVerticalCar(String rox) throws SlickException {
        return new Car(rox, DrivingDirection.SOUTH);
    }

    //
    // public methods
    //
    public void move() {
        if (!xapHelper.isOccupied(getNextRoxel())) {
            xapHelper.setOccupied(getNextRoxel(), this.id);
            xapHelper.setOccupied(positionRoxel, this.id);

            positionRoxel = getNextRoxel();
        }
    }
    
    @SpaceId(autoGenerate = true)
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Image getImage() {
        return image;
    }

    // returns the position of the car in pixels
    public int getX() {
        int x = xapHelper.getRoxelById(positionRoxel).getX() * tileSize;
        if (this.drivingDirection == DrivingDirection.EAST) {
            x += horizontalOffsetX;
        } else {
            x += verticalOffsetX;
        }
        return x;
    }

    public int getY() {
        int y = xapHelper.getRoxelById(positionRoxel).getY() * tileSize;
        if (this.drivingDirection == DrivingDirection.EAST) {
            y += horizontalOffsetY;
        } else {
            y += verticalOffsetY;
        }
        return y;
    }

    //
    // private methods
    //
    private String getNextRoxel() {
        String next = null;
        switch (drivingDirection) {
            case EAST:
                next = xapHelper.getRoxelById(positionRoxel).getEast();
                break;
            case SOUTH:
                next = xapHelper.getRoxelById(positionRoxel).getSouth();
                break;
            default:
                break;
        }
        return next;
    }

    @Override
    public void run() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
