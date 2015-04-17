package gigaspaces;

import com.j_spaces.core.client.SQLQuery;
import java.awt.Point;
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
                    gigaSpace.write(new Tile(new Point(i, j), false));
                }
            }
        }
    }

    public boolean isOccupied(Point point) {
        Tile result = gigaSpace.read(new SQLQuery<Tile>(
                Tile.class,
                "x=? AND y=? AND occupied=?",
                point.x, point.y, true)
        );
        
        return result != null;
    }

    public void setOccupied(Point point, boolean occupied) {
        Tile result = gigaSpace.read(new SQLQuery<Tile>(
                Tile.class,
                "x=? AND y=?",
                point.x, point.y)
        );
        
        if(result != null) {
            result.setOccupied(occupied);
            gigaSpace.write(result);
        }

    }
}
