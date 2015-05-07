package gui;

import helper.CarContainer;
import domain.Car;
import helper.XapHelper;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.tiled.TiledMap;

public class Main extends BasicGame {

    private TiledMap tiledMap;
    static final int FPS = 100;
    private final XapHelper xapHelper;
    private final ArrayList<CarContainer> carContainers;
    private final ArrayList<Thread> carContainerThreads;
    private static AppGameContainer appgc;

    public Main(String gamename) {
        super(gamename);
        xapHelper = new XapHelper();
        this.carContainers = new ArrayList();
        this.carContainerThreads = new ArrayList();
    }

    @Override
    public void init(GameContainer gc) throws SlickException {
        tiledMap = new TiledMap("res/streetgrid.tmx");
        int mapHeight = tiledMap.getHeight();
        int mapWidth = tiledMap.getWidth();
        xapHelper.initRoxels(mapWidth, mapHeight);
        xapHelper.initCars(mapWidth, mapHeight, FPS);
        Car cars[] = xapHelper.getCars();

        for (Car car : cars) {
            CarContainer cc = new CarContainer(car);
            carContainers.add(cc);
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

        for (CarContainer container : carContainers) {
            Integer x = container.getX();
            Integer y = container.getY();
            if(x != null && y != null) {
                container.getImage().draw(x, y);
            }
        }

        // should be done like this, but the application is not thread save
//        for (Car cars : xapHelper.getCars()) {
//            CarContainer cc = new CarContainer(cars);
//            Integer x = cc.getX();
//            Integer y = cc.getY();
//            
//            if(x != null && y != null) {
//                cc.getImage().draw(x, y);
//            } else {
//                System.out.println("x: " + x);
//                System.out.println("y: " + y);
//            }
//        }
    }

    public static void main(String[] args) {
        try {
            appgc = new AppGameContainer(new Main("TTIP Simulation 2000"));
            appgc.setTargetFrameRate(FPS);
            appgc.setDisplayMode(1152, 768, false);
            appgc.start();
        } catch (SlickException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static int getFPS() {
        int fps = appgc.getFPS();

        if (fps > 0) {
            return fps;
        }

        return 1;
    }

}
