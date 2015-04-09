package slick2d;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class Car {
    private final boolean horizontal;
    private int x;
    private int y;
    private Image image;

    public Car(boolean horizontal) throws SlickException {
        this.horizontal = horizontal;
        
        image = new Image("res/car.png");
        
        if (horizontal) {
            x = 0;
            y = 96;
        } else {
            x = 73;
            y = 0;
            image.rotate(90);
        }
    }
    
    public void move () {
        if (horizontal) {
            x++;
            if (x++ > 1152) {
                x = 0;
            }
        } else {
            y++;
            if (y++ > 768) {
                y = 0;
            }
        }
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Image getImage() {
        return image;
    }
    
}
