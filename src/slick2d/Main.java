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
    private static final int FPS = 30;
    private final XapHelper xapHelper;
    private ArrayList<CarContainer> carContainers = new ArrayList();
    private ArrayList<Thread> carContainerThreads = new ArrayList();

    public Main(String gamename) {
        super(gamename);
        xapHelper = new XapHelper();
    }

    @Override
    public void init(GameContainer gc) throws SlickException {
        tiledMap = new TiledMap("res/streetgrid.tmx");
        int mapHeight = tiledMap.getHeight();
        int mapWidth = tiledMap.getWidth();
        xapHelper.initRoadTiles(mapWidth, mapHeight);
        xapHelper.initCars(mapWidth, mapHeight, FPS);
        Car cars[] = xapHelper.getCars();
        for (Car car: cars) {
            CarContainer cc = CarContainer.createContainerWithExistingCar(car);
            carContainers.add(cc);
            carContainerThreads.add(new Thread(cc));
        }
        for (Thread t : carContainerThreads) {
            t.start();
        }
    }

    @Override
    public void update(GameContainer gc, int delta) throws SlickException {
        /*for (CarContainer cont : carContainers) {
            cont.move();
        }*/
    }

    @Override
    public void render(GameContainer gc, Graphics g) throws SlickException {
        tiledMap.render(0, 0);
        for (CarContainer cont : carContainers) {
            cont.getImage().draw(cont.getX(), cont.getY());
        }
    }

    public static void main(String[] args) {
        try {
            AppGameContainer appgc;
            appgc = new AppGameContainer(new Main("Simple Slick Game"));
            appgc.setTargetFrameRate(FPS);
            appgc.setDisplayMode(1152, 768, false);
            appgc.start();
        } catch (SlickException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
