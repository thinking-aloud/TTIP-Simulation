package gui;

import helper.CarContainer;
import domain.Car;
import domain.Roxel;
import helper.XapHelper;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.tiled.TiledMap;

public class Main extends BasicGame {

    private TiledMap tiledMap;
    static final int FPS = 60;
    static final int SPEED = 1000; // sleep between updates (0=fastest)
    private final XapHelper xapHelper;
    private final ArrayList<Thread> carContainerThreads;
    private static Image arrow;

    public Main(String gamename) {
        super(gamename);
        xapHelper = new XapHelper();
        this.carContainerThreads = new ArrayList();
    }

    @Override
    public void init(GameContainer gc) throws SlickException {
        tiledMap = new TiledMap("res/streetgrid.tmx");
        int mapHeight = tiledMap.getHeight();
        int mapWidth = tiledMap.getWidth();
        xapHelper.initRoxels(mapWidth, mapHeight);
        xapHelper.initCars(mapWidth, mapHeight, SPEED);
        Car cars[] = xapHelper.getCars();
        arrow = new Image("res/arrow.png");

        for (Car car : cars) {
            CarContainer cc = new CarContainer(car);
            carContainerThreads.add(new Thread(cc));
        }

        for (Thread t : carContainerThreads) {
            t.start();
        }
    }

    @Override
    public void update(GameContainer gc, int delta) throws SlickException {
    }

    @Override
    public void render(GameContainer gc, Graphics g) throws SlickException {
        tiledMap.render(0, 0);
        
        for(Roxel roxel : xapHelper.getAllRoxels()) {
            if(roxel.isJunction()) {
                if(roxel.getOpenDirection() == Car.DrivingDirection.SOUTH) {
                    arrow.rotate(90);
                    arrow.draw(roxel.getX() * 64 + 16, roxel.getY() * 64 + 16);
                    arrow.rotate(-90);
                } else {
                    arrow.draw(roxel.getX() * 64 + 16, roxel.getY() * 64 + 16);
                }
            }
        }

        for (Car car : xapHelper.getCars()) {
            CarContainer cc = new CarContainer(car);
            Integer x = cc.getX();
            Integer y = cc.getY();

            if (x != null && y != null) {
                cc.getImage().draw(x, y);
            }
        }
        
    }

    public static void main(String[] args) {
        try {
            AppGameContainer appgc = new AppGameContainer(new Main("TTIP Simulation 2000"));
            appgc.setTargetFrameRate(FPS);
            appgc.setDisplayMode(1152, 768, false);
            appgc.start();
        } catch (SlickException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
