package Backend.Characters;
import Backend.Items.*;
import Backend.Weapons.*;

public class Enemy {
    Stats stats;
    Weapon weapon;
    Item item;


    public Enemy(Stats stats, Weapon weapon, Item item){
        this.stats = stats;
        this.item = item;
        this.weapon = weapon;
    }

}
