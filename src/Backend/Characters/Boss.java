package Backend.Characters;
import Backend.Items.*;
import Backend.Weapons.*;

public class Boss{
    Stats stats;
    Weapon weapon;
    Item item;

    public Boss(Stats stats, Weapon weapon, Item item) {
        this.stats = stats;
        this.item = item;
        this.weapon = weapon;
    }
}

