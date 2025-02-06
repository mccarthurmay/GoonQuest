package Backend.Characters;
import java.util.ArrayList;
import java.util.Arrays;

import Backend.Items.*;
import Backend.Weapons.*;

public class ItemTesting {
    public static void main(String[] args) {

        // NOTE -- must have a weapon or error
        Axe test = new Axe("test", 2,"what",2,3);
        ArrayList<Weapon> weapons = new ArrayList<>();
        weapons.add(test);


        // Create HP Item
        HpItem milk = new HpItem("milk", 10);

        // Create deBuff item - NOTE, due to item deleting from list, the logic only allows a duration of 1 or else it's forever
        StatusEffect poisonarrow = new StatusEffect("Poison Arrow", "health", false, 20, 1);

        // Add items
        ArrayList<Item> items = new ArrayList<>();
        items.add(milk);
        items.add(poisonarrow);

        // Create stats
        Stats stat = new Stats(2,15,100,5,100);

        // Create hero
        Hero Christian = new Hero("Christian", 100.0, weapons, items, stat);

        // Show how to use item
        System.out.println(Christian);
        Christian.useItem(milk);
        System.out.println(Christian);

        // Show how to use buff
        Christian.useItem(poisonarrow);
        System.out.println(Christian);

        // Oh Christian attacked!!! Monster defended.




    }
}
