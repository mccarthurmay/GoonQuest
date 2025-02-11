package Backend.Characters;
import java.io.IOException;
import java.util.ArrayList;

import Backend.Items.*;
import Backend.Weapons.*;
import Backend.Characters.*;

public class ItemUsageExample {
    public static void main(String[] args) throws IOException {

        // NOTE -- must have a weapon or error
        Weapon test = new Weapon("test", 2,"what",2,3,"testing","");
        ArrayList<Weapon> weapons = new ArrayList<>();
        weapons.add(test);


        // Create HP Item
        HpItem milk = new HpItem("milk", 10, "");

        // Create deBuff item
        StatusEffect poisonarrow = new StatusEffect("Poison Arrow", "hp", false, 20, 1,"");

        // Create buff item
        StatusEffect SlurpJuice = new StatusEffect("Slurp Juice", "dmg", true, 10000, 2,"");

        // Add items
        ArrayList<Item> items = new ArrayList<>();
        items.add(milk);
        items.add(poisonarrow);
        items.add(SlurpJuice);

        // Create stats
        Stats stat = new Stats(2,15,100,5,100);

        // Create hero
        Hero Christian = HeroFactory.createCharacterTestHero();

        // Christian uses an HP item
        System.out.println(Christian);
        Christian.useItem(milk);
        // + 10 health
        System.out.println(Christian);

        // Christian gets a buff and debuff
        Christian.useItem(SlurpJuice);
        Christian.useItem(poisonarrow);
        // + 10000 damage, - 20 health
        System.out.println(Christian);


        System.out.println("Oh Christian attacked!!! (1 move) Monster defended. (1 move) round end");
        System.out.println("// So one round happened... at the end of every round we will run this command: \n");
        Christian.reduceDebuff(poisonarrow);
        Christian.reduceBuff(SlurpJuice);
        System.out.println("// Then we will see the outcome of:");
        System.out.println(Christian);
        System.out.println("// The debuff is gone!!!!");
        System.out.println("// But wait... the buff is still there??? OH!!! It's there for two rounds...");

        System.out.println("// The monster swings his big burly stick!!! Christian defends. (round end... but Christian didn't attack?)\n");
        System.out.println(Christian);
        Christian.reduceDebuff(poisonarrow); // duration at 0... doesn't throw error
        System.out.println("// He still has buff? Oh right! he didn't attack");

        System.out.println("// Christian throws a pebble. Monster dies.\n");
        Christian.reduceDebuff(poisonarrow);
        Christian.reduceBuff(SlurpJuice);
        System.out.println(Christian);

        System.out.println("// Buff reduced since Christian attacked. Done ditto.");


    }
}
