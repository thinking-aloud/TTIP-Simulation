package slick2d;

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
    private static final int FPS = 1;
    private final ArrayList<Car> cars = new ArrayList();
    private final XapHelper xapHelper;

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
        
        
        for (int row = 0; row < mapHeight; row++) {
            if (row % 3 == 1) {
                String startingPosition = xapHelper.getRoxelByCoordinates(0, row).getId();
                cars.add(Car.createHorizontalCar(startingPosition));
            }
        }
    }

    @Override
    public void update(GameContainer gc, int delta) throws SlickException {
        for (Car car : cars) {
            car.move();
        }
    }

    @Override
    public void render(GameContainer gc, Graphics g) throws SlickException {
        tiledMap.render(0, 0);

        for (Car car : cars) {
            car.getImage().draw(car.getX(), car.getY());
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
