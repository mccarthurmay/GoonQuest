package Backend.Characters;

import Backend.Weapons.*;
import Backend.Items.*;
import Display.GamePanel;
import Display.KeyHandler;

import java.io.File;
import java.io.FileNotFoundException;
import java.security.Key;
import java.sql.Array;
import java.util.ArrayList;
import java.util.Scanner;

public class HeroFactory {

    /**
     * Create full default hero, saves some time
     */
    public static Hero createDefaultHero(GamePanel gp, KeyHandler keyH) {
        // Create default weapon
        Weapon defaultWeapon = new Weapon("Stick", 2, "Basic weapon", 2, 3, "A simple stick", "");
        Weapon battleAxe = new Weapon("Battle Axe", 25, "wind", 8, 85, "Man, it's windy", "./src/Backend/Images/Weapons/Axe/Sprite.png");
        Weapon normalAxe = new Weapon("Basic Axe", 8, "wind", 5, 85, "Man, it's windy", "./src/Backend/Images/Weapons/AxeTool/Sprite.png");
        Weapon bigSword = new Weapon("Big sword", 28, "wind", 7, 85, "Man, it's windy", "./src/Backend/Images/Weapons/BigSword/Sprite.png");
        Weapon club = new Weapon("Club", 18, "wind", 15, 99, "Man, it's windy", "./src/Backend/Images/Weapons/Club/Sprite.png");
        Weapon Lance = new Weapon("Knight's lance", 22, "wind", 11, 90, "Man, it's windy", "./src/Backend/Images/Weapons/Lance/Sprite.png");
        Weapon katana = new Weapon("Kusanagi-no-tsurugi", 30, "wind", 10, 80, "Man, it's windy", "./src/Backend/Images/Weapons/Katana/Sprite.png");
        Weapon hammer = new Weapon("Sledgehammer.", 14, "wind", 12, 99, "Man, it's wind1y", "./src/Backend/Images/Weapons/Hammer/Sprite.png");
        Weapon sword = new Weapon("Dawnbreaker", 20, "wind", 20, 100, "Man, it's windy", "./src/Backend/Images/Weapons/Sword/Sprite.png");
        ArrayList<Weapon> weapons = new ArrayList<>();
        weapons.add(defaultWeapon);
        weapons.add(battleAxe);
        weapons.add(normalAxe);
        weapons.add(bigSword);
        weapons.add(club);
        weapons.add(Lance);
        weapons.add(katana);
        weapons.add(hammer);
        weapons.add(sword);


        // Create default items
        ArrayList<Item> items = new ArrayList<>();
        items.add(new HpItem("Health Potion", 10, ""));
        items.add(new StatusEffect("Strength Boost", "dmg", true, 5, 3, ""));
        items.add(new HpItem("Health 1", 10, ""));
        items.add(new HpItem("Health3 Potion", 10, ""));
        items.add(new HpItem("Health P4236otion", 10, ""));
        items.add(new HpItem("Health Potion", 10, ""));
        items.add(new HpItem("Heal56th 234Potion", 10, ""));
        items.add(new HpItem("Health62 Potion", 10, ""));
        items.add(new HpItem("Health Potion", 10, ""));
        items.add(new HpItem("Healt4235h Potion", 10, ""));
        items.add(new HpItem("Health Potion", 10, ""));
        items.add(new HpItem("Health Potion", 10, ""));
        items.add(new HpItem("Health P6623otion", 10, ""));

        // Create default stats
        Stats stats = new Stats(100.0, 10.0, 200.0, 0.15, 0.85);

        // Create and return the hero
        return new Hero("Player", weapons, items, stats, gp, keyH);
    }

    /**
     * CUSTOM FULL HERO CREATION!!!
     */
    public static Hero createCustomHero(String name, ArrayList<Weapon> weapons,
                                        ArrayList<Item> items, Stats stats,
                                        GamePanel gp, KeyHandler keyH) {
        return new Hero(name, weapons, items, stats, gp, keyH);
    }

}