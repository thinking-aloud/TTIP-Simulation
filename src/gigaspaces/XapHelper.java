package gigaspaces;

import java.awt.Point;
import org.openspaces.core.GigaSpace;
import org.openspaces.core.GigaSpaceConfigurer;
import org.openspaces.core.space.SpaceProxyConfigurer;
import slick2d.Car;

public class XapHelper {

    private final SpaceProxyConfigurer configurer;
    private final GigaSpace gigaSpace;

    public XapHelper() {
        configurer = new SpaceProxyConfigurer("myGrid");
        configurer.lookupGroups("gigaspaces-10.1.0-XAPPremium-ga");
        gigaSpace = new GigaSpaceConfigurer(configurer).create();
    }
    
    public void initRoadTiles() {
        gigaSpace.clear(null);

        
        // initialize street fields
        for (int i = 0; i < 18; i++) {
            for (int j = 0; j < 12; j++) {
                if (j % 3 == 1 || i % 3 == 1) {
                    gigaSpace.write(new Roxel(new Point(i, j)));
                }
            }
        }
        
        // include connection between neighbours
        Roxel roxes[] = gigaSpace.readMultiple(new Roxel());
        for (Roxel rox: roxes) {
            Roxel tmp = gigaSpace.read(new Roxel(new Point(rox.getGridPosition().x, rox.getGridPosition().y-1)));
            if (tmp != null) {
                rox.setNorth(tmp);
            }
            tmp = gigaSpace.read(new Roxel(new Point(rox.getGridPosition().x+1, rox.getGridPosition().y)));
            if (tmp != null) {
                rox.setEast(tmp);
            }
            rox.setSouth(gigaSpace.read(new Roxel(new Point(rox.getGridPosition().x, rox.getGridPosition().y+1))));
            rox.setWest(gigaSpace.read(new Roxel(new Point(rox.getGridPosition().x-1, rox.getGridPosition().y))));
            gigaSpace.write(rox);
        }
    }

    public boolean isOccupied(Roxel rox) {
        Roxel result = gigaSpace.read(rox);
        
        if(result != null) {
            return result.getCar() != null;
        }
        return false;
    }

    public void setOccupied(Roxel rox, Car car) {
        Roxel result = gigaSpace.read(rox);
        
        if(result != null) {
            result.setCar(car);
            gigaSpace.write(result);
        }
    }
    
    public Roxel getRoxel(Point pos) {
        return gigaSpace.read(new Roxel(pos));
    }
}
