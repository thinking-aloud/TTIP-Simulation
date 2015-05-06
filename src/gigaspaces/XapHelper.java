package gigaspaces;

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

    public void initRoadTiles(int mapWidth, int mapHeight) {
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
        // Roxel roxes[] = gigaSpace.readMultiple(new SQLQuery<Roxel>(Roxel.class, "", ""));
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
            gigaSpace.write(rox);
        }
    }

    public void initCars(int mapWidth, int mapHeight, int speed) throws SlickException {
        // Horizontal
        for (int row = 0; row < mapHeight; row++) {
            Roxel rox = getRoxelByCoordinates(0, row);
            if (rox != null) {
                String startingPosition = rox.getId();
                Car car = new Car(startingPosition, Car.DrivingDirection.EAST, speed/10+1);
                // register Car to generate ID
                gigaSpace.write(car);
                Car gs_car = gigaSpace.read(car);
                occupyRoxel(gs_car.getPositionRoxel(), gs_car.getId());
            }
        }
        // Vertical
        for (int column = 0; column < mapWidth; column++) {
            Roxel rox = getRoxelByCoordinates(column, 0);
            if (rox != null) {
                String startingPosition = rox.getId();
                Car car = new Car(startingPosition, Car.DrivingDirection.SOUTH, speed/10);
                // register Car to generate ID
                gigaSpace.write(car);
                Car gs_car = gigaSpace.read(car);
                occupyRoxel(gs_car.getPositionRoxel(), gs_car.getId());
            }
        }
    }

    public boolean isOccupied(String rox) {
        Roxel qry = new Roxel();
        qry.setId(rox);
        Roxel result = gigaSpace.read(qry);

        if (result != null) {
            return result.getCar() != null;
        }
        return false;
    }

    public boolean occupyRoxel(String rox, String car) {
        Roxel qry = new Roxel();
        qry.setId(rox);
        Roxel result = gigaSpace.take(qry);

        if (result != null && result.getCar() == null) {
            result.setCar(car);
            gigaSpace.write(result);
            return true;
        }
        return false;
    }

    public void releaseRoxel(String rox) {
        Roxel qry = new Roxel();
        qry.setId(rox);
        Roxel result = gigaSpace.read(qry);

        if (result != null) {
            result.setCar(null);
            gigaSpace.write(result);
        }
    }

    public Roxel getRoxelByCoordinates(int x, int y) {
        Roxel template = new Roxel();
        template.setX(x);
        template.setY(y);
        return gigaSpace.read(template);
    }

    public Roxel getRoxelById(String id) {
        Roxel rox = new Roxel();
        rox.setId(id);
        return gigaSpace.read(rox);
    }

    public Car[] getCars() throws SlickException {
        Car cars[] = gigaSpace.readMultiple(new Car());
        /*ArrayList<CarContainer> conts = new ArrayList();
         for (Car car: cars) {
         conts.add(CarContainer.createContainerWithExistingCar(car));
         }*/
        return cars;
    }

    public void updateCar(Car car) {
        gigaSpace.write(car);
    }
}
