package slick2d;

import gigaspaces.Car;
import gigaspaces.XapHelper;
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
    static final int FPS = 1;
    private final XapHelper xapHelper;
    private final ArrayList<CarContainer> carContainers;
    private final ArrayList<Thread> carContainerThreads;

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
        
        for (Car car: cars) {
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
            container.getImage().draw(container.getX(), container.getY());
        }
        
        // should be done like this, but the application is not thread save
//        for (Car cars : xapHelper.getCars()) {
//            CarContainer cc = new CarContainer(cars);
//            cc.getImage().draw(cc.getX(), cc.getY());
//        }
    }

    public static void main(String[] args) {
        try {
            AppGameContainer appgc;
            appgc = new AppGameContainer(new Main("TTIP Simulation 2000"));
            appgc.setTargetFrameRate(FPS);
            appgc.setDisplayMode(1152, 768, false);
            appgc.start();
        } catch (SlickException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
