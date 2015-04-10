package gigaspaces;

import com.j_spaces.core.client.SQLQuery;
import org.openspaces.core.GigaSpace;
import org.openspaces.core.GigaSpaceConfigurer;
import org.openspaces.core.space.SpaceProxyConfigurer;

public class Program {

    public static void main(String[] args) {

        System.out.println("Connecting to data grid");
        SpaceProxyConfigurer configurer = new SpaceProxyConfigurer("myGrid");
        configurer.lookupGroups("gigaspaces-10.1.0-XAPPremium-ga");
        GigaSpace gigaSpace = new GigaSpaceConfigurer(configurer).create();

        System.out.println("Write (store) a couple of entries in the data grid:");
        gigaSpace.write(new Tile(1, 2, 1, true));
        gigaSpace.write(new Tile(2, 2, 2, false));

        System.out.println("Read (retrieve) an entry from the grid by its id:");
        Tile result1 = gigaSpace.readById(Tile.class, 1);
        System.out.println("Result: " + result1);

        System.out.println("Read an entry from the grid using a SQL-like query:");
        Tile result2 = gigaSpace.read(new SQLQuery<Tile>(Tile.class, "x=?", "2"));
        System.out.println("Result: " + result2);

        System.out.println("Read all entries of type Person from the grid:");
        Tile[] results = gigaSpace.readMultiple(new Tile());
        System.out.println("Result: " + java.util.Arrays.toString(results));

        configurer.close();
    }
}