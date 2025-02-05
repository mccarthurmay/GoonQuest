package Backend.Characters;
import Backend.Items.*;
import Backend.Weapons.*;

public class Enemy extends CharacterManager{
    Stats stats;
    Weapon weapon;
    Item item;


    public Enemy(Stats stats, Weapon weapon, String name){
        super(stats, weapon, name);
        this.stats = stats;
        this.weapon = weapon;
        this.name = name;
    }

    public Stats getStats() {
        return stats;
    }
}
