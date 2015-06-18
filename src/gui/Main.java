package gui;

import domain.Car;
import domain.Roxel;
import helper.XapHelper;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Point;
import org.newdawn.slick.tiled.TiledMap;

public class Main extends BasicGame {

    private TiledMap tiledMap;
    static final int FPS = 60;
    private final XapHelper xapHelper;
    private static Image arrow, questionMark;
    public static int mapHeight;
    public static int mapWidth;
    private Image red, blue;

    private static final int horizontalOffsetX = 17;
    private static final int horizontalOffsetY = 32;
    private static final int verticalOffsetX = 10;
    private static final int verticalOffsetY = 23;
    private static final int tileSize = 64;

    public Main(String gamename) {
        super(gamename);
        xapHelper = new XapHelper();
    }

    @Override
    public void init(GameContainer gc) throws SlickException {
        tiledMap = new TiledMap("res/streetgrid.tmx");
        mapHeight = tiledMap.getHeight();
        mapWidth = tiledMap.getWidth();
        xapHelper.initRoxels(mapWidth, mapHeight);
        xapHelper.initCars(mapWidth, mapHeight);

        Car cars[] = xapHelper.readAllCars();
        arrow = new Image("res/arrow.png");
        questionMark = new Image("res/question-mark.png");

        red = new Image("res/car_red.png");

        Image img = new Image("res/car_blue.png");
        img.rotate(90);
        blue = img;

        for (Car car : cars) {
            xapHelper.createCarProcess(car);
        }

        xapHelper.startAllowanceManager();
    }

    @Override
    public void update(GameContainer gc, int delta) {
        // not used due to thread usage
    }

    @Override
    public void render(GameContainer gc, Graphics g) {
        tiledMap.render(0, 0);

        for (Roxel roxel : xapHelper.readAllRoxels()) {
            if (roxel.isJunction() != null && roxel.isJunction()) {
                if (roxel.getOpenDirection() == Car.Direction.SOUTH) {
                    arrow.rotate(90);
                    arrow.draw(roxel.getX() * 64 + 16, roxel.getY() * 64 + 16);
                    arrow.rotate(-90);
                } else if (roxel.getOpenDirection() == Car.Direction.EAST) {
                    arrow.draw(roxel.getX() * 64 + 16, roxel.getY() * 64 + 16);
                } else {
                    questionMark.draw(roxel.getX() * 64 + 22, roxel.getY() * 64 + 16);
                }
            }
        }

        for (Car car : xapHelper.readAllCars()) {

            float x = getPosition(car).getX();
            float y = getPosition(car).getY();

            if (car.getDrivingDirection() == Car.Direction.EAST) {
                red.draw(x, y);
            } else {
                blue.draw(x, y);
            }
        }

    }

    // returns the position of the car in pixels
    public Point getPosition(Car car) {
        float x = car.getX() * tileSize;
        float y = car.getY() * tileSize;
        switch (car.getDrivingDirection()) {
            case EAST:
                x += horizontalOffsetX;
                y += horizontalOffsetY;
                break;
            case SOUTH:
                x += verticalOffsetX;
                y += verticalOffsetY;
        }

        return new Point(x, y);
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
