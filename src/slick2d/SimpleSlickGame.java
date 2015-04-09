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
//    private Image carHorizontal;
//    private Image carVerical;
//    private int carWidth;
    
    private Car car1;
//    private Car car2;
    private Car car3;
//    private Car car4;
    
    public SimpleSlickGame(String gamename) {
        super(gamename);
    }

    @Override
    public void init(GameContainer gc) throws SlickException {
        tiledMap = new TiledMap("res/streetgrid.tmx");
        
        car1 = new Car(true);
        car3 = new Car(false);
        
//        car2 = new Car(true);
//        car4 = new Car(false);
    }

    @Override
    public void update(GameContainer gc, int i) throws SlickException {
        car1.move();
//        car2.move();
        car3.move();
//        car4.move();
    }

    @Override
    public void render(GameContainer gc, Graphics g) throws SlickException {
        tiledMap.render(0, 0);
        
        car1.getImage().draw(car1.getX(), car1.getY());
//        car2.getImage().draw(car2.getX(), car2.getY());
        car3.getImage().draw(car3.getX(), car3.getY());
//        car4.getImage().draw(car4.getX(), car4.getY());
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