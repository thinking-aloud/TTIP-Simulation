package slick2d;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.tiled.TiledMap;

public class SimpleSlickGame extends BasicGame {
    
    private TiledMap tiledMap;
    private Image carHorizontal;
    private Image carVerical;
    private int carWidth;
    
    private final int initHorizontalXPos = 0;
    private final int initHorizontalYPos = 96;
    private final int initVerticalXPos = 73;
    private final int initVerticalYPos = 0;

    public SimpleSlickGame(String gamename) {
        super(gamename);
    }

    @Override
    public void init(GameContainer gc) throws SlickException {
        tiledMap = new TiledMap("res/streetgrid.tmx");
        carHorizontal = new Image("res/car.png");
        carVerical = new Image("res/car.png");
        carVerical.rotate(90);      
        carWidth = carHorizontal.getWidth();
    }

    @Override
    public void update(GameContainer gc, int i) throws SlickException {
    }

    @Override
    public void render(GameContainer gc, Graphics g) throws SlickException {
        tiledMap.render(0, 0);
        
        // horizontal cars
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 4; j++) {
                carHorizontal.draw(initHorizontalXPos + (i * 2 * carWidth), initHorizontalYPos + j * 3 * tiledMap.getTileHeight());
            }
        }
        
        // vertical cars
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 10; j++) {
                carVerical.draw( initVerticalXPos + i * 3 * tiledMap.getTileHeight(), initVerticalYPos + (j * 2 * carWidth));
            }
        }
//        g.drawString("Howdy!", 100, 100);
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