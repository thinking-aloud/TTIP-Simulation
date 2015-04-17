package slick2d;

import gigaspaces.XapHelper;
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
    private static final int FPS = 5;
    private final Car[] cars = new Car[60];
    private final XapHelper xapHelper;

    public Main(String gamename) {
        super(gamename);
        xapHelper = new XapHelper();
    }

    @Override
    public void init(GameContainer gc) throws SlickException {
        xapHelper.initRoadTiles();
        
        tiledMap = new TiledMap("res/streetgrid.tmx");

        // THIS IS DONE SHITTY! NEEDS IMPROVEMENT
        // horizontal cars
        for (int row = 0; row < 4; row++) {
            cars[row] = Car.createHorizontalCar(0, row);
            cars[row + 4] = Car.createHorizontalCar(2, row);
            cars[row + 8] = Car.createHorizontalCar(3, row);
            cars[row + 12] = Car.createHorizontalCar(5, row);
            cars[row + 16] = Car.createHorizontalCar(6, row);
            cars[row + 20] = Car.createHorizontalCar(8, row);
        }

        // vartical cars
        for (int column = 0; column < 6; column++) {
            cars[column + 24] = Car.createVerticalCar(column, 0);
            cars[column + 30] = Car.createVerticalCar(column, 1);
            cars[column + 36] = Car.createVerticalCar(column, 2);
            cars[column + 42] = Car.createVerticalCar(column, 3);
            cars[column + 48] = Car.createVerticalCar(column, 4);
            cars[column + 54] = Car.createVerticalCar(column, 5);
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
