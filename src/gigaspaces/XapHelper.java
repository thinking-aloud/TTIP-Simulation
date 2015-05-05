package gigaspaces;

import java.util.Arrays;
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
    
    public void initRoadTiles() {
        gigaSpace.clear(null);

        
        // initialize street fields
        for (int i = 0; i < 18; i++) {
            for (int j = 0; j < 12; j++) {
                if (j % 3 == 1 || i % 3 == 1) {
                    gigaSpace.write(new Roxel(i, j));
                }
            }
        }
        
        // include connection between neighbours
        Roxel template = new Roxel();
        Roxel roxes[] = gigaSpace.readMultiple(template);
        System.out.println("Roxes: " + roxes.length + " " + Arrays.toString(roxes));
        for (Roxel rox: roxes) {
            Roxel tmp = gigaSpace.read(new Roxel(rox.getX(), rox.getY()-1));
            if (tmp != null) {
                rox.setNorth(tmp.getId());
            }
            tmp = gigaSpace.read(new Roxel(rox.getX()+1, rox.getY()));
            if (tmp != null) {
                rox.setEast(tmp.getId());
            }
            tmp = gigaSpace.read(new Roxel(rox.getX(), rox.getY()+1));
            if (tmp != null) {
                rox.setSouth(tmp.getId());
            }
            tmp = gigaSpace.read(new Roxel(rox.getX()-1, rox.getY()));
            if (tmp != null) {
                rox.setWest(tmp.getId());
            }
            gigaSpace.write(rox);
        }
    }

    public boolean isOccupied(String rox) {
        Roxel qry = new Roxel();
        qry.setId(rox);
        Roxel result = gigaSpace.read(qry);
        
        if(result != null) {
            return result.getCar() != null;
        }
        return false;
    }

    public void setOccupied(String rox, String car) {
        Roxel qry = new Roxel();
        qry.setId(rox);
        Roxel result = gigaSpace.read(qry);
        
        if(result != null) {
            result.setCar(car);
            gigaSpace.write(result);
        }
    }
    
    public Roxel getRoxelByCoordinates(int x, int y) {
        return gigaSpace.read(new Roxel(x, y));
    }
    
    public Roxel getRoxelById(String id) {
        Roxel rox = new Roxel();
        rox.setId(id);
        return gigaSpace.read(rox);
    }
}
