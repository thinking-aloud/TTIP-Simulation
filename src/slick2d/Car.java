package slick2d;

import com.gigaspaces.annotation.pojo.SpaceId;
import gigaspaces.Roxel;
import gigaspaces.XapHelper;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class Car implements Runnable{

    private String id;
    private char direction;
    private Roxel position;
    private final Image image;

    
    private static final int horizontalOffsetX = 17;
    private static final int horizontalOffsetY = 32;
    private static final int verticalOffsetX = 10;
    private static final int verticalOffsetY = 23;
    private static final int tileSize = 64;
    
    private final XapHelper xapHelper;

    // private, can't be called from outside
    private Car(Roxel rox, char dir) throws SlickException {
        this.direction = dir;
        position = rox;
        
        xapHelper = new XapHelper();

        xapHelper.setOccupied(position, this);


        if (direction == 'e') {
            image = new Image("res/car_red.png");
            image.rotate(90);
        } else {
            image = new Image("res/car_blue.png");
        }
    }

    //
    // static factory methods
    //
    public static Car createHorizontalCar(Roxel rox) throws SlickException {
        return new Car(rox, 'e');
    }

    public static Car createVerticalCar(Roxel rox) throws SlickException {
        return new Car(rox, 's');
    }

    //
    // public methods
    //
    public void move() {
        if (!xapHelper.isOccupied(getNextRoxel())) {
            xapHelper.setOccupied(getNextRoxel(), this);
            xapHelper.setOccupied(position, this);

            position = getNextRoxel();
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
        int x = position.getGridPosition().x * tileSize;
        if (direction == 'e') {
            x += horizontalOffsetX;
        } else {
            x += verticalOffsetX;
        }
        return x;
    }

    public int getY() {
        int y = position.getGridPosition().y * tileSize;
        if (direction == 'e') {
            y += horizontalOffsetY;
        } else {
            y += verticalOffsetY;
        }
        return y;
    }

    //
    // private methods
    //
    private Roxel getNextRoxel() {
        Roxel next = null;
        switch (direction) {
            case 'e':
                next = position.getEast();
                break;
            case 's':
                next = position.getSouth();
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
