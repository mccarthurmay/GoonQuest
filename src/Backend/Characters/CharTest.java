package Backend.Characters;
import java.util.ArrayList;
import java.util.Arrays;

import Backend.Items.*;
import Backend.Weapons.*;

public class CharTest {
    public static void main(String[] args) {
        Axe axe = new Axe("Big Axe", 10,"fire", 1, 1);
        Sword sword = new Sword("Sword", 10,"fire",1,1);
        ArrayList<Weapon> weapons = new ArrayList<>();
        weapons.add(sword);
        weapons.add(axe);



        StatusEffect milk = new StatusEffect("milk", "health", true);
        StatusEffect milk2 = new StatusEffect("milk2", "health", true);
        ArrayList<Item> items = new ArrayList<>();
        items.add(milk);
        items.add(milk2);

        Stats stat = new Stats(1,1,1,1,1);

        Hero Christian = new Hero("Christian", 100.0, weapons, items,stat);
        System.out.println(Christian);

        Axe greatAxe = new Axe("Super Strong Axe", 100,"fire", .5, 1);
        Sword greatSword = new Sword("SuperSword", 50,"fire",1,1);
        ArrayList<Weapon> greatWeapons = new ArrayList<>(Arrays.asList(greatAxe, greatSword));
        ArrayList<Item> moreItems = new ArrayList<>();
        Stats greatStats = new Stats(5, 200, 200, 0.1, 0.1);
        Hero GodMode = new Hero("Thor the Mighty", 100.0, weapons, moreItems, stat);
        System.out.println(GodMode.stats);

    }
}
