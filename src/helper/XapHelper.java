package helper;

import domain.Car;
import domain.Roxel;
import domain.TrafficLight;
import logic.CarProcess;
import org.newdawn.slick.SlickException;
import org.openspaces.core.GigaSpace;
import org.openspaces.core.GigaSpaceConfigurer;
import org.openspaces.core.space.SpaceProxyConfigurer;
import org.openspaces.events.notify.SimpleNotifyContainerConfigurer;
import org.openspaces.events.notify.SimpleNotifyEventListenerContainer;
import logic.TrafficLightProcess;
import org.openspaces.events.polling.SimplePollingContainerConfigurer;
import org.openspaces.events.polling.SimplePollingEventListenerContainer;

public class XapHelper {

    private final SpaceProxyConfigurer configurer;
    private final GigaSpace gigaSpace;

    // you get 2/3 (x mod 3 != 1) cars. No cars on intersections
    // 0 = no cars, 1-2 = one car, 3 = 2 cars, 4 = 3 cars
    private final int horizontalCarRows = 3;
    private final int verticalCarRows = 3;

    public XapHelper() {
        configurer = new SpaceProxyConfigurer("eventSpace");
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

                    Roxel roxel = new Roxel(i, j);

                    if (roxel.isJunction()) {
                        // Set random Direction for Junction
                        /*int dir = new Random().nextInt(2);
                         if (dir == 1) {
                         roxel.setOpenDirection(Car.Direction.EAST);
                         } else {
                         roxel.setOpenDirection(Car.Direction.SOUTH);
                         }*/
                        roxel.setOpenDirection(Car.Direction.TODECIDE);

                        // creates a traffic light thread
                        Thread tl = new Thread(new TrafficLight(i, j));
                        tl.start();
                    } else if (i % 3 != 1 && j % 3 == 1) {
                        roxel.setOpenDirection(Car.Direction.EAST);
                    } else if (i % 3 == 1 && j % 3 != 1) {
                        roxel.setOpenDirection(Car.Direction.SOUTH);
                    }
                    gigaSpace.write(roxel);
                }
            }
        }
    }

    public void initTrafficLights() {
        SimpleNotifyEventListenerContainer nelc = new SimpleNotifyContainerConfigurer(gigaSpace)
                .eventListenerAnnotation(new TrafficLightProcess(gigaSpace))
                .notifyContainer();
        nelc.start();
    }

    public void createCarProcess(Car car) {
        SimplePollingEventListenerContainer pelc = new SimplePollingContainerConfigurer(
                gigaSpace).eventListenerAnnotation(new CarProcess(car, gigaSpace))
                .pollingContainer();
        pelc.start();
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
                            Car car = new Car(Car.Direction.EAST, speed);
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
                            Car car = new Car(Car.Direction.SOUTH, speed);
                            car.setX(rox.getX());
                            car.setY(rox.getY());
                            gigaSpace.write(car);
                        }
                    }
                }
            }
        }
    }

    public Roxel takeRoxel(Roxel template) {
        return gigaSpace.take(template);
    }

    public Roxel readRoxelById(String id) {
        return gigaSpace.readById(Roxel.class, id);
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

    public boolean isGreen(Roxel roxel, Car.Direction direction) {
        return (!roxel.getOpenDirection().equals(Car.Direction.TODECIDE) && roxel.getOpenDirection() == direction);
    }

}
