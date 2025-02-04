package Backend.Characters;
import Backend.Weapons.*;
import Backend.Items.*;
import java.util.ArrayList;





public class Hero {
    ArrayList<Weapon> ownedWeapons = new ArrayList<>();
    ArrayList<Item> ownedItems = new ArrayList<>();
    Double position;
    String name;
    Stats stats;

    public Hero(String name, Double position, ArrayList<Weapon> ownedWeapons, ArrayList<Item> ownedItems, Stats stats) {
        this.name = name;
        this.position = position;
        this.ownedWeapons = ownedWeapons;
        this.ownedItems = ownedItems;
        this.stats = stats;
    }

    @Override
    public String toString() {
        return "Hero{" +
                "ownedWeapons=" + ownedWeapons +
                ", ownedItems=" + ownedItems +
                ", position=" + position +
                ", name='" + name + '\'' +
                ", stats=" + stats +
                '}';
    }

    // obtain weapon

    // obtain items

    // read stats

    // select weapon  (should this just be done locally in battle?)i

    //

}
