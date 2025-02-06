package Backend.Characters;
import java.util.ArrayList;
import java.util.Arrays;

import Backend.Items.*;
import Backend.Weapons.*;

public class CharTest2 {
    public static void main(String[] args) {
        Axe axe = new Axe("Big Axe", 10,"fire", 1, 1);
        Sword sword = new Sword("Sword", 10,"fire",1,1);
        ArrayList<Weapon> weapons = new ArrayList<>();
        weapons.add(sword);
        weapons.add(axe);

        HpItem milk = new HpItem("milk", 10);
        ArrayList<Item> items = new ArrayList<>();
        items.add(milk);

        Stats stat = new Stats(1,1,1,1,1);

        Hero Christian = new Hero("Christian", 100.0, weapons, items,stat);
        System.out.println(Christian);


        Christian.useItem(milk);
        System.out.println(Christian);

//        Axe greatAxe = new Axe("Awesome Axe", 100,"fire", 0.1, 1);
//        Sword greatSword = new Sword("Tubular Sword", 100,"fire",0.1,0.95);
//        ArrayList<Weapon> greatWeapons = new ArrayList<>(Arrays.asList(greatSword, greatAxe));
//        Stats scaryStats = new Stats(3,8,100,0.1,0.7);
//        Stats holyStats = new Stats(7, 10, 200, 1, 1);
//        Enemy goblar = new Enemy(scaryStats, greatAxe, "Goblar the Horrible");
//        Hero thor = new Hero("Thor the Wonderful", 100.9, greatWeapons, items, holyStats);
//        System.out.println(goblar.getStats());
//        thor.attack(goblar);
//        goblar.attack(thor);

    }
}
