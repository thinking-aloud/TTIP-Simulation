package slick2d;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.tiled.TiledMap;

public class SimpleSlickGame extends BasicGame {
    
    private TiledMap tiledMap;
    
    private final Car[] cars = new Car[10];
    
    public SimpleSlickGame(String gamename) {
        super(gamename);
    }

    @Override
    public void init(GameContainer gc) throws SlickException {
        tiledMap = new TiledMap("res/streetgrid.tmx");
        
        // horizontal cars
        for (int i = 0; i < 4; i++) {
            cars[i] = Car.createHorizontalCar(i);
        }
        
        // vartical cars
         for (int i = 0; i < 6; i++) {
            cars[i+4] = Car.createVerticalCar(i);
        }
        
    }

    @Override
    public void update(GameContainer gc, int i) throws SlickException {
        for(Car car : cars) {
            car.move();
        }
    }

    @Override
    public void render(GameContainer gc, Graphics g) throws SlickException {
        tiledMap.render(0, 0);
        
        for(Car car : cars) {
            car.getImage().draw(car.getX(), car.getY());
        }
    }

    public static void main(String[] args) {
        try {
            AppGameContainer appgc;
            appgc = new AppGameContainer(new SimpleSlickGame("Simple Slick Game"));
            appgc.setDisplayMode(1152, 768, false);
            appgc.start();
        } catch (SlickException ex) {
            Logger.getLogger(SimpleSlickGame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}