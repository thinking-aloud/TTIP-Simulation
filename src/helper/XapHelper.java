package helper;

import domain.Car;
import domain.Roxel;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Point;
import org.openspaces.core.GigaSpace;
import org.openspaces.core.GigaSpaceConfigurer;
import org.openspaces.core.space.SpaceProxyConfigurer;

public class XapHelper {

    private final SpaceProxyConfigurer configurer;
    private final GigaSpace gigaSpace;

    // you get 2/3 (x mod 3 != 1) cars. No cars on intersections
    // 0 = no cars, 1-2 = one car, 3 = 2 cars, 4 = 3 cars
    private final int horizontalCarRows = 1;
    private final int verticalCarRows = 1;

    public XapHelper() {
        configurer = new SpaceProxyConfigurer("myGrid");
        configurer.lookupGroups("gigaspaces-10.1.0-XAPPremium-ga");
        gigaSpace = new GigaSpaceConfigurer(configurer).create();
    }

    public void initRoxels(int mapWidth, int mapHeight) {
        // clears gigaspaces on startup
        gigaSpace.clear(null);

        // initialize street fields
        for (int i = 0; i < mapWidth; i++) {
            for (int j = 0; j < mapHeight; j++) {
                if (j % 3 == 1 || i % 3 == 1) {
                    gigaSpace.write(new Roxel(i, j));
                }
            }
        }

        // include connection between neighbours
        Roxel template = new Roxel();
        Roxel roxels[] = gigaSpace.readMultiple(template);
        for (Roxel rox : roxels) {
            // Default
            Roxel north = gigaSpace.read(new Roxel(rox.getX(), rox.getY() - 1));
            Roxel east = gigaSpace.read(new Roxel(rox.getX() + 1, rox.getY()));
            Roxel south = gigaSpace.read(new Roxel(rox.getX(), rox.getY() + 1));
            Roxel west = gigaSpace.read(new Roxel(rox.getX() - 1, rox.getY()));
            // Edges
            if (north == null && rox.getY() == 0) {
                north = gigaSpace.read(new Roxel(rox.getX(), mapHeight - 1));
            }
            if (east == null && rox.getX() == mapWidth - 1) {
                east = gigaSpace.read(new Roxel(0, rox.getY()));
            }
            if (south == null && rox.getY() == mapHeight - 1) {
                south = gigaSpace.read(new Roxel(rox.getX(), 0));
            }
            if (west == null && rox.getX() == 0) {
                west = gigaSpace.read(new Roxel(mapWidth - 1, rox.getY()));
            }
            if (north != null) {
                rox.setNorth(new Point(north.getX(), north.getY()));
            }
            if (east != null) {
                rox.setEast(new Point(east.getX(), east.getY()));
            }
            if (south != null) {
                rox.setSouth(new Point(south.getX(), south.getY()));
            }
            if (west != null) {
                rox.setWest(new Point(west.getX(), west.getY()));
            }
            if (rox.isJunction()) {
                rox.setOpenDirection(Car.DrivingDirection.TODECIDE);

                // creates a traffic light thread
                Thread tl = new Thread(new TrafficLight(rox.getX(), rox.getY()));
                tl.start();

            } else if (rox.getNorth() != null && rox.getSouth() != null) {
                rox.setOpenDirection(Car.DrivingDirection.SOUTH);
            } else if (rox.getEast() != null && rox.getWest() != null) {
                rox.setOpenDirection(Car.DrivingDirection.EAST);
            }
            gigaSpace.write(rox);
        }
    }

    public void initCars(int mapWidth, int mapHeight, int speed) throws SlickException {

        // Horizontal
        for (int row = 0; row < mapHeight; row++) {
            for (int column = 0; column < horizontalCarRows; column++) {
                
                if (row % 3 == 1 && column % 3 != 1) {
                    Roxel rox = readRoxel(new Roxel(column, row));

                    if (rox != null) {
                        // occupy roxel
                        Roxel roxel = takeRoxel(new Roxel(rox.getX(), rox.getY()));

                        if (roxel != null) {
                            roxel.setOccupied(true);
                            gigaSpace.write(roxel);

                            // place car
                            Car car = new Car(Car.DrivingDirection.EAST, speed);
                            car.setX(rox.getX());
                            car.setY(rox.getY());
                            gigaSpace.write(car);
                        }
                    }
                }
            }
        }

        // Vertical
        for (int column = 0; column < mapWidth; column++) {
            for (int row = 0; row < verticalCarRows; row++) {

                if (column % 3 == 1 && row % 3 != 1) {
                    Roxel rox = readRoxel(new Roxel(column, row));

                    if (rox != null) {
                        // occupy roxel
                        Roxel roxel = takeRoxel(new Roxel(rox.getX(), rox.getY()));

                        if (roxel != null) {
                            roxel.setOccupied(true);
                            gigaSpace.write(roxel);

                            // place car
                            Car car = new Car(Car.DrivingDirection.SOUTH, speed);
                            car.setX(rox.getX());
                            car.setY(rox.getY());
                            gigaSpace.write(car);
                        }
                    } else {
                        System.out.println("rox " + column + ", " + row + " is null!");
                    }
                }
            }
        }
        System.out.println("Number of cars: " + gigaSpace.count(new Car()));
    }

    public void initIntersections() {

    }

    public Roxel takeRoxel(Roxel template) {
        return gigaSpace.take(template);
    }

    public Roxel readRoxel(Roxel template) {
        return gigaSpace.read(template);
    }

    public Roxel[] readAllRoxels() {
        return gigaSpace.readMultiple(new Roxel());
    }

    public Car[] readAllCars() {
        return gigaSpace.readMultiple(new Car());
    }

    public <T> void writeToTupelspace(T object) {
        gigaSpace.write(object);
    }

    public boolean isGreen(Roxel roxel, Car.DrivingDirection direction) {
        return (!roxel.getOpenDirection().equals(Car.DrivingDirection.TODECIDE) && roxel.getOpenDirection() == direction);
    }

//    public void passRoxelToTrafficLight(Roxel roxel) {
//        Thread tl = new Thread(new TrafficLight(roxel));
//        tl.start();
//    }
}
