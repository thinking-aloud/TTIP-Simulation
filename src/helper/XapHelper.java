package helper;

import domain.Car;
import domain.Roxel;
import java.util.Arrays;
import org.newdawn.slick.SlickException;
import org.openspaces.core.GigaSpace;
import org.openspaces.core.GigaSpaceConfigurer;
import org.openspaces.core.space.SpaceProxyConfigurer;

public class XapHelper {

    private final SpaceProxyConfigurer configurer;
    private final GigaSpace gigaSpace;

    public XapHelper() {
        configurer = new SpaceProxyConfigurer("myGrid");
        configurer.lookupGroups("gigaspaces-10.1.0-XAPPremium-ga");
        gigaSpace = new GigaSpaceConfigurer(configurer).create();
    }

    public void initRoxels(int mapWidth, int mapHeight) {
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
        Roxel roxes[] = gigaSpace.readMultiple(template);
        System.out.println("Roxes: " + roxes.length + " " + Arrays.toString(roxes));
        for (Roxel rox : roxes) {
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
                rox.setNorth(north.getId());
            }
            if (east != null) {
                rox.setEast(east.getId());
            }
            if (south != null) {
                rox.setSouth(south.getId());
            }
            if (west != null) {
                rox.setWest(west.getId());
            }
            if (rox.isJunction()) {
                rox.setOpenDirection(Car.DrivingDirection.EAST);
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
            Roxel rox = getRoxelByCoordinates(0, row);
            if (rox != null) {
                Car car = new Car(Car.DrivingDirection.EAST, speed);
                car.setOccupiedRoxel(rox);
                gigaSpace.write(car);
            }
            // MORE CARS!!!
            rox = getRoxelByCoordinates(2, row);
            if (rox != null) {
                Car car = new Car(Car.DrivingDirection.EAST, speed);
                car.setOccupiedRoxel(rox);
                gigaSpace.write(car);
            }
        }
        // Vertical
        for (int column = 0; column < mapWidth; column++) {
            Roxel rox = getRoxelByCoordinates(column, 0);
            if (rox != null) {
                Car car = new Car(Car.DrivingDirection.SOUTH, speed);
                car.setOccupiedRoxel(rox);
                gigaSpace.write(car);
            }
            // MORE CARS!!!
            rox = getRoxelByCoordinates(column, 2);
            if (rox != null) {
                Car car = new Car(Car.DrivingDirection.SOUTH, speed);
                car.setOccupiedRoxel(rox);
                gigaSpace.write(car);
            }
        }
    }

    public void releaseRoxel(Roxel rox) {
        if (rox != null) {
            gigaSpace.write(rox);
        }
    }

    public Roxel getRoxelByCoordinates(int x, int y) {
        Roxel template = new Roxel();
        template.setX(x);
        template.setY(y);
        return gigaSpace.read(template);
    }

    public Roxel getRoxelById(String id) {
        return gigaSpace.read(new Roxel(id));
    }

    public Roxel takeRoxelById(String id) {
        return gigaSpace.take(new Roxel(id));
    }

    public Car[] getCars() throws SlickException {
        return gigaSpace.readMultiple(new Car());
    }

    public void updateCar(Car car) {
        gigaSpace.write(car);
    }

    public boolean isGreen(Roxel rox, Car.DrivingDirection dir) {
        return (rox.getOpenDirection() == dir);
    }

    public void passRoxelToTrafficLight(Roxel oldRoxel) {
        Thread tl = new Thread(new TrafficLight(oldRoxel));
        tl.start();
    }
}
